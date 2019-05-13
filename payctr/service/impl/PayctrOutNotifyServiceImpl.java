package com.sdhoo.pdloan.payctr.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.common.base.util.WebOptUtils;
import com.sdhoo.common.base.util.WebOptUtils.WebRequest;
import com.sdhoo.common.base.util.WebOptUtils.WebResp;
import com.sdhoo.pdloan.bcrud.model.DtPctOutNotifyRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctOutNotifyRcdService;
import com.sdhoo.pdloan.payctr.PaycenterThreadPools;
import com.sdhoo.pdloan.payctr.enums.PctOutNotifyStepEnum;
import com.sdhoo.pdloan.payctr.service.PayctrOptlmtService;
import com.sdhoo.pdloan.payctr.service.PayctrOutNotifyService;


/**
 * 支付中心通知服务实现类
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-20 12:44:35
 *
 */
@Service
public class PayctrOutNotifyServiceImpl implements PayctrOutNotifyService {

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(PayctrOutNotifyServiceImpl.class); 
	
	@Resource
	private DtPctOutNotifyRcdService dtPctOutNotifyRcdService ;
	
	@Resource
	private PayctrOptlmtService payctrOptlmtService ;


	@Override
	public DtPctOutNotifyRcd createAndStartNotifyPlan(DtPctOutNotifyRcd ntfRcd) {

		Integer ifcChnlId = ntfRcd.getIfcChnlId();
		Long ifcChnlCfgId = ntfRcd.getIfcChnlCfgId();
		String tradePayCode = ntfRcd.getTradePayCode();
		String notifyUrl = ntfRcd.getNotifyUrl();
		String tradeStepStatus = ntfRcd.getTradeStepStatus();
		if(ifcChnlId == null || ifcChnlCfgId == null || StringUtils.isEmpty(tradePayCode) || StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(tradeStepStatus) ) {
			throw new IllegalArgumentException("通知发起-->入数参数不足,数据未创建");
		}
		// 通知地址验证 
		if(notifyUrl.indexOf("http") < 0 ) {
			throw new IllegalArgumentException("通知发起-->地址参数不正确,数据未创建");
		}

		// 查找数据是否已经存在,已存在,则关闭原来通知进程并重新发起最新状态通知
		
		Map<String,Object> existQmap = new HashMap<>(3);
		existQmap.put("ifcChnlId", ifcChnlId);
		existQmap.put("ifcChnlCfgId", ifcChnlCfgId);
		existQmap.put("tradePayCode", tradePayCode);
		List<DtPctOutNotifyRcd> existRcdList = dtPctOutNotifyRcdService.selectByCriteria(existQmap, 0, 2);
		int exist_size = existRcdList.size();
		DtPctOutNotifyRcd existRcd = null ;
		Long existId = null ;
		if(exist_size > 0 ) {
			existRcd = existRcdList.get(0);
		}
		if(existRcd != null ) { // 已存在,删除已有对象并关闭已存在的通知线程并
			existId = existRcd.getPonrId();
			dtPctOutNotifyRcdService.deleteByPrimKey(existId); 
		}
		
		// 通知入库 
		Date curTime = new Date(); // 当前时间 
		Date nextPlanTime = ntfRcd.getNextPlanTime(); // 下一次通知时间
		if(nextPlanTime == null ) { // 默认通知时间为当前时间
			nextPlanTime = curTime ;
		}
		PctOutNotifyStepEnum newStepEnum = PctOutNotifyStepEnum.UN_NOTIFY ;
		ntfRcd.setPonrId(existId); // 替换原有ID 
		ntfRcd.setStepStatus(newStepEnum.getCode());
		ntfRcd.setNextPlanTime(nextPlanTime); 
		ntfRcd.setPonrCtime(curTime);
		ntfRcd.setPonrMtime(curTime);
		dtPctOutNotifyRcdService.insertSelective(ntfRcd); 

		// 判断并异步发起通知
		asynvCheckAndStartNotify(ntfRcd); 

		return ntfRcd;
	}


	@Override
	public Runnable asynvCheckAndStartNotify(final DtPctOutNotifyRcd ntfRcd) {
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					doStartNotify(ntfRcd);
				} catch (BaseServiceException e) {
					e.printStackTrace();
				}
			}
		};
		// 异步发起通知
		PaycenterThreadPools.outNotifyThreadPool.submit(runnable);
		return runnable ;
	}
	
	/**
	 * 发起通知请求
	 * @param rcdInf
	 * @return
	 * @throws BaseServiceException
	 */
	private DtPctOutNotifyRcd doStartNotify(DtPctOutNotifyRcd inRcd ) throws BaseServiceException {

		Long ponrId = inRcd.getPonrId();
		
		String optCode = "";
		String optPwd = StringUtils.createRandomStr(6);
		
		DtPctOutNotifyRcd rcdInf ;
		try {
			payctrOptlmtService.checkAndExpNotifyLmtById(ponrId, optCode, optPwd, true); 
			
			rcdInf = dtPctOutNotifyRcdService.getByPrimKey(ponrId, null);

			String notifyUrl = rcdInf.getNotifyUrl();
			Integer ifcChnlId = rcdInf.getIfcChnlId();
			Long ifcChnlCfgId = rcdInf.getIfcChnlCfgId();
			String tradeStepStatus = rcdInf.getTradeStepStatus();
			String tradePayCode = rcdInf.getTradePayCode();
			Integer stepStatus = rcdInf.getStepStatus();
			Integer curstepNotifyCnt = rcdInf.getCurstepNotifyCnt();
			PctOutNotifyStepEnum stepEnum = PctOutNotifyStepEnum.getByCode(stepStatus);
			
			if(!(PctOutNotifyStepEnum.UN_NOTIFY.equals(stepEnum) || PctOutNotifyStepEnum.NOTIFY_FAIL.equals(stepEnum))) {
				throw new BaseServiceException("未通知或失败的通知才能发起通知请求");
			}
			String needSuccRspStr = "success"; // 需要返回的信息
			Integer maxNotifyCnt = 5 ; // 最大通知次数 
			
			String logPre = "通知记录("+ponrId+")发起通知处理-->";
			Date curTime = new Date();
			Map<String, String> paramsMap = new HashMap<>();
			paramsMap.put("ifcChnlId", ifcChnlId.toString());
			paramsMap.put("ifcChnlCfgId", ifcChnlCfgId.toString());
			paramsMap.put("tradeStepStatus", tradeStepStatus);
			paramsMap.put("tradePayCode", tradePayCode); 

			String notifyFefinf = null ; // 通知返回信息
			PctOutNotifyStepEnum newStepEnum ; // 新的状态 
			Date nextPlanTime = null; // 下一次计划通知时间

			WebRequest request = new WebRequest();
			request.setUrl(notifyUrl);
			request.setParamsMap(paramsMap);
			int timeOutMillSec = 30000; // 超时时间 
			request.setConnTimeout(timeOutMillSec);
			request.setReadTimeout(timeOutMillSec);
			try {
				WebResp doGetRsp = WebOptUtils.doGet(request );
				notifyFefinf = doGetRsp.getResponseStr();
				if(notifyFefinf != null ) {
					if(notifyFefinf.length() > 60) {
						notifyFefinf = notifyFefinf.substring(0, 58) + "..." ; 
					}
					if(needSuccRspStr.equalsIgnoreCase(notifyFefinf)) { // 比对结果,忽略大小写 
						newStepEnum = PctOutNotifyStepEnum.SUCCESS; 
					}else {
						newStepEnum = 	PctOutNotifyStepEnum.NOTIFY_FAIL; // 通知失败,则下次再通知 
					}
				}else {
					newStepEnum = PctOutNotifyStepEnum.NOTIFY_FAIL ;
				}
			} catch (Exception e) {
				logger.warn(logPre+"调用请求出现异常了",e);
				newStepEnum = PctOutNotifyStepEnum.NOTIFY_FAIL ;
			}

			Integer new_curStepNotifyCnt = ++curstepNotifyCnt ;
			
			if( PctOutNotifyStepEnum.NOTIFY_FAIL.equals(newStepEnum)) {
				if(new_curStepNotifyCnt >= maxNotifyCnt ) {
					newStepEnum = PctOutNotifyStepEnum.CANCLED; // 超过次数,则置为通知取消
				}else {
					// 未超过次数,计算下一次通知时间
					nextPlanTime = calcNextPlanTime(new_curStepNotifyCnt , curTime); 
					if(nextPlanTime == null ) { // 计算返回空,则认为不继续通知
						newStepEnum = PctOutNotifyStepEnum.CANCLED; // 超过次数,则置为通知取消
					}
				}
			}
			DtPctOutNotifyRcd mdfRcd = new DtPctOutNotifyRcd();
			mdfRcd.setPonrId(ponrId);
			mdfRcd.setCurstepNotifyCnt(new_curStepNotifyCnt);
			mdfRcd.setStepStatus(newStepEnum.getCode());
			mdfRcd.setNextPlanTime(nextPlanTime );
			mdfRcd.setLastNotifyTime(curTime); 
			mdfRcd.setLastNotifyFebinf(notifyFefinf);
			mdfRcd.setPonrMtime(curTime);
			dtPctOutNotifyRcdService.updateByPrimKeySelective(mdfRcd);
			
			rcdInf.setStepStatus(newStepEnum.getCode()); 
		}finally {
			payctrOptlmtService.releaseNotifyLmtById(ponrId, optCode, optPwd);
		}
		return rcdInf ;
	}

	private Date calcNextPlanTime(Integer new_curStepNotifyCnt, Date curTime) {
		
		Calendar nextTimeCld = Calendar.getInstance();
		if(curTime != null ) {
			nextTimeCld.setTime(curTime);
		}
		
		if(new_curStepNotifyCnt < 1 ) { // 调用次数小于1次的,下次为10秒后发
			nextTimeCld.add(Calendar.SECOND, 10); 
		}else if (new_curStepNotifyCnt < 2) { // 调用次数小于2次的,下次为30秒后发
			nextTimeCld.add(Calendar.SECOND, 30); 
		}else if (new_curStepNotifyCnt < 3) { // 调用次数小于3次的,下次为60秒后发
			nextTimeCld.add(Calendar.SECOND, 60); 
		}else if (new_curStepNotifyCnt < 4) { // 调用次数小于4次的,下次为90秒后发
			nextTimeCld.add(Calendar.SECOND, 90); 
		}else if (new_curStepNotifyCnt < 5) { // 调用次数小于5次的,下次为120秒后发
			nextTimeCld.add(Calendar.SECOND, 120); 
		}else { // 否则不计算计划推送时间
			return null ;
		}
		return nextTimeCld.getTime();
	}

}
