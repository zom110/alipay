package com.sdhoo.pdloan.payctr.busi.oflinedf.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctOutNotifyRcd;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctSysPayExtraService;
import com.sdhoo.pdloan.bcrud.service.DtPctSysPayRcdService;
import com.sdhoo.pdloan.payctr.PaycenterConstants;
import com.sdhoo.pdloan.payctr.PaycenterThreadPools;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.dto.SyspayDftCfgInf;
import com.sdhoo.pdloan.payctr.dto.SyspayRecordInf;
import com.sdhoo.pdloan.payctr.enums.PayeeUnitTypeEnum;
import com.sdhoo.pdloan.payctr.enums.SysPayStepStatusEnum;
import com.sdhoo.pdloan.payctr.enums.SyspayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.service.IfcChnlCfgService;
import com.sdhoo.pdloan.payctr.service.PayctrBeanProcService;
import com.sdhoo.pdloan.payctr.service.PayctrOptlmtService;
import com.sdhoo.pdloan.payctr.service.PayctrOutNotifyService;
import com.sdhoo.pdloan.payctr.service.SysPayChnlService;
import com.sdhoo.pdloan.payctr.service.req.DoCreateSysPayReq;
import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;
import com.sdhoo.pdloan.payctr.util.PayctUtils;

/**
 * 系统支付宝付代付的实现.
 * 
 * @author SDPC_LIU
 *
 */
@Service(value = (PaycenterConstants.SYSPAY_CHNLSERVICENAME_PRE + PaycenterConstants.SYSPAY_CHNL_OFLINE))
public class SysPayChnlServiceOflineImpl implements SysPayChnlService {

    private static final Logger logger = LoggerFactory.getLogger(SysPayChnlServiceOflineImpl.class);

    /**
     * 当前渠道枚举(线下放款)
     */
    private SyspayIfcChnlEnum currIfcChnlEnum = SyspayIfcChnlEnum.OFLINE;

    @Autowired
    private DtPctSysPayRcdService dtPctSysPayRcdService;
    
    @Autowired
    private DtPctSysPayExtraService dtSysPayExtraService; 

    @Autowired
    private PctIdGenService pctIdGenService;

    @Autowired
    private PayctrOptlmtService paycenterOptlmtService;

    @Autowired
    private PayctrBeanProcService paycenterBeanProcService ;
    
    @Autowired
    private IfcChnlCfgService ifcChnlCfgService ;

    @Resource
    private PayctrOutNotifyService payctrOutNotifyService ;


	@Override
	public DtPctIfcChnlCfg getDefaultChnlCfg() {
		SyspayDftCfgInf syspayDftCfgInf = ifcChnlCfgService.getSyspayDftCfgInf();
		Long syspayCfgId = syspayDftCfgInf.getSysOflinePayChnlCfgId();
		return ifcChnlCfgService.getCachedPctIfcChnlCfg(syspayCfgId);
	}

    public DtPctSysPayRcd generateSysPayRcdByReq(DoCreateSysPayReq req) {
        String sprCode = req.getSprCode();
        String payTitle = req.getPayTitle();
        // 交易金额,精确小数点后两位
        Double payamtYuan = new BigDecimal(req.getPayamtYuan()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        Integer payeeChnlType = req.getPayeeChnlType();
        // 收款渠道ID,暂定0未指定,
        Integer payeeChnlId = 0;
        String payeeChnlName = req.getPayeeChnlName();
        String payeeChnlFullname = req.getPayeeChnlFullname();
        // 收款人账户单位类型
        Integer payeeAccUnitType = req.getPayeeAccUnitType();
        // 户号
        String payeeAccNo = req.getPayeeAccNo();
        // 收款人姓名
        String payeeAccFullName = req.getPayeeAccFullName();

        String payeeAccIdcard = req.getPayeeAccIdcard();
        String payeeAccMobile = req.getPayeeAccMobile();
        Integer stepStatus = SysPayStepStatusEnum.CREATE.getCode();
        String payeeChnlProvname = req.getPayeeChnlProvname();
        String payeeChnlCityname = req.getPayeeChnlCityname();

        // 调用接口渠道ID
        int ifcChnlId = currIfcChnlEnum.getCode(); 
        Long ifcChnlCfgId = req.getIfcChnlCfgId(); 
        Long userId = req.getUserId();

        DtPctSysPayRcd sdtSysPayRcd = new DtPctSysPayRcd();
        sdtSysPayRcd.setSprCode(sprCode);
        sdtSysPayRcd.setPayTitle(payTitle);
        sdtSysPayRcd.setIfcChnlId(ifcChnlId);
        sdtSysPayRcd.setIfcChnlCfgId(ifcChnlCfgId); 
        sdtSysPayRcd.setUserId(userId); 
        sdtSysPayRcd.setPayRmbamt(payamtYuan);
        sdtSysPayRcd.setPayeeChnlId(payeeChnlId);
        sdtSysPayRcd.setPayeeChnlType(payeeChnlType);
        sdtSysPayRcd.setPayeeChnlName(payeeChnlName);
        sdtSysPayRcd.setPayeeChnlFullname(payeeChnlFullname);
        sdtSysPayRcd.setPayeeChnlProvname(payeeChnlProvname);
        sdtSysPayRcd.setPayeeChnlCityname(payeeChnlCityname);
        sdtSysPayRcd.setPayeeAccUnitType(payeeAccUnitType);
        sdtSysPayRcd.setPayeeAccNo(payeeAccNo);
        sdtSysPayRcd.setPayeeAccFullname(payeeAccFullName);
        sdtSysPayRcd.setPayeeAccIdcard(payeeAccIdcard);
        sdtSysPayRcd.setPayeeAccMobile(payeeAccMobile);
        sdtSysPayRcd.setStepStatus(stepStatus);

        return sdtSysPayRcd;
    }

    /**
     * 创建扩展信息
     * 
     * @param req
     * @return
     */
    public DtPctSysPayExtra generateExtraByReq(DoCreateSysPayReq req) {

        // 业务关联信息
        String busiRelInf = req.getBusiRelInf();
        Integer initUserType = req.getInitUserType();
        String initUserId = req.getInitUserId();
        String initUserName = req.getInitUserName();
        String initMemo = req.getInitMemo();

        DtPctSysPayExtra extra = new DtPctSysPayExtra();
        extra.setBusiRelInf(busiRelInf);
        extra.setInitUtype(initUserType);
        extra.setInitUid(initUserId);
        extra.setInitUname(initUserName); 
        extra.setInitMemo(initMemo);

        return extra;

    }

    /**
     * 支付信息验证
     * 
     * @param model
     */
    public void validateDtPctSysPayRcd4Create(DtPctSysPayRcd model) throws BaseServiceException {

        // 基本数据验证
        if (model == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付信息不能为空");
        }
        if (currIfcChnlEnum.getCode() != model.getIfcChnlId()) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "接口渠道不匹配");
        }
        if (model.getPayRmbamt() <= 0) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付金额不能小于0");
        }
        if (StringUtils.isEmpty(model.getPayeeChnlName())) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "银行名称不能为空");
        }
        Integer payeeAccUnitType = model.getPayeeAccUnitType();
        PayeeUnitTypeEnum payeeUnitTypeEnum = PayeeUnitTypeEnum.getByCode(payeeAccUnitType);
        if (payeeUnitTypeEnum == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款单位类型无效");
        }
        if (StringUtils.isEmpty(model.getPayeeAccNo())) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款账号不能为空");
        }
        if (StringUtils.isEmpty(model.getPayeeAccFullname())) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款户名不能为空");
        }

        // 个人需要验证身份证号及手机号
        if (PayeeUnitTypeEnum.PERSON.equals(payeeUnitTypeEnum)) {

            if (StringUtils.isEmpty(model.getPayeeAccIdcard())) {
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款人身份证号不能为空");
            }
            if (StringUtils.isEmpty(model.getPayeeAccMobile())) {
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款人手机号不能为空");
            }
        }

        // 企业支付额外验证省份,地市,分行名称
        if (PayeeUnitTypeEnum.COMPANY.equals(payeeUnitTypeEnum)) {
            if (StringUtils.isEmpty(model.getPayeeChnlFullname())) {
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "分行名称不能为空");
            }
            if (StringUtils.isEmpty(model.getPayeeChnlProvname())) {
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "省份名称不能为空");
            }
            if (StringUtils.isEmpty(model.getPayeeChnlCityname())) {
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "地市名称不能为空");
            }
        }
    }

    @Override
    public SyspayRecordInf doCreateSysPayRecord(DoCreateSysPayReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {

        // 创建支付请求Bean
        DtPctSysPayRcd dtPctSysPayRcd = generateSysPayRcdByReq(req);
        DtPctSysPayExtra sysPayExtra = generateExtraByReq(req);

        // 基本信息验证
        validateDtPctSysPayRcd4Create(dtPctSysPayRcd);
        
        String logPre = "系统代付实现类[线下放款]发起代付请求-->";

        // 生成ID
        PctIdData nextRcdId = pctIdGenService.genAndCachePayctIdData();
        Date currTime = nextRcdId.getIdDate();

        String sprCode = req.getSprCode();
        String batchNo = PayctUtils.getNumberFromStr(sprCode); 
        
        dtPctSysPayRcd.setIfcChnlName(ifcCfgInf.getMemberName());
        dtPctSysPayRcd.setIfcChnlBatchNo(batchNo);

        Long sprId = nextRcdId.getIdVal();
        dtPctSysPayRcd.setSprId(sprId);

        // 当前时间
        dtPctSysPayRcd.setSprCtime(currTime);
        dtPctSysPayRcd.setSprMtime(currTime);

        // 信息入库
        dtPctSysPayRcdService.insertSelective(dtPctSysPayRcd);
        

        // 新的数据及状态
        SysPayStepStatusEnum newStepStatus;
        // 渠道响应信息Jsn
        String ifcChnlFebInfJsn ;
        String chnlRspCode ;
        String chnlRspMsg ;
        
       // 调用接口:
        newStepStatus = SysPayStepStatusEnum.SUCCESS; // 线下放款默认成功 
        Map<String,Object> oflRspData = new HashMap<>();
        oflRspData.put("rspCode", "0");
        oflRspData.put("rspMsg", "线下放款,默认成功");
        ifcChnlFebInfJsn = JSONObject.toJSONString(oflRspData) ;
        chnlRspCode = "0";
        chnlRspMsg = "线下放款成功";

        DtPctSysPayRcd mdfPayRcd = new DtPctSysPayRcd();
        mdfPayRcd.setSprId(sprId); 
        mdfPayRcd.setStepStatus(newStepStatus.getCode());
        mdfPayRcd.setStepMemo(chnlRspMsg);
        mdfPayRcd.setSprMtime(currTime);
        dtPctSysPayRcdService.updateByPrimKeySelective(mdfPayRcd);

        sysPayExtra.setSprId(sprId);
        sysPayExtra.setLastFebInf(ifcChnlFebInfJsn);
        sysPayExtra.setLastRspCode(chnlRspCode);
        sysPayExtra.setLastRspMsg(chnlRspMsg);
        sysPayExtra.setSpeCtime(currTime);
        sysPayExtra.setSpeMtime(currTime);
        String statusNotifyUrl = req.getStatusNotifyUrl();
		sysPayExtra.setStatusNotifyUrl(statusNotifyUrl);
        // 调用成功,更新响应数据
        dtSysPayExtraService.insertSelective(sysPayExtra);

        // 支付结果核对并返回,
        dtPctSysPayRcd.setStepStatus(newStepStatus.getCode());
        dtPctSysPayRcd.setStepMemo(chnlRspMsg);

        if(SysPayStepStatusEnum.WAITE4NTF.equals(newStepStatus)) {
            // 需要异步处理的,则发起异步监听
            checkAndAddMonitorThread(dtPctSysPayRcd, ifcCfgInf, 0, 900);
        }else if (  SysPayStepStatusEnum.SUCCESS.equals(newStepStatus) || SysPayStepStatusEnum.FAIL.equals(newStepStatus) ) {
        	
        	DtPctSysPayRcd mdfRcd = new DtPctSysPayRcd();
        	mdfRcd.setSprId(sprId);
        	mdfRcd.setIfcChnlEndtime(currTime);
        	mdfRcd.setSprMtime(currTime); 
        	dtPctSysPayRcdService.updateByPrimKeySelective(mdfRcd); 

        	// 成功或失败, 发送通知 
        	if(StringUtils.isNotEmpty(statusNotifyUrl)) {
        		DtPctOutNotifyRcd ntfRcd = new DtPctOutNotifyRcd(); 
        		ntfRcd.setIfcChnlId(ifcCfgInf.getIfcChnlId());
        		ntfRcd.setIfcChnlCfgId(ifcCfgInf.getIccId());
        		ntfRcd.setTradePayCode(sprCode);
        		ntfRcd.setTradeStepStatus(""+newStepStatus.getCode());
        		ntfRcd.setNotifyUrl(statusNotifyUrl);
        		payctrOutNotifyService.createAndStartNotifyPlan(ntfRcd );
        	}
        }
        SyspayRecordInf payRcdInf = paycenterBeanProcService.genSysPayRecordInfByModel(dtPctSysPayRcd, sysPayExtra);
        return payRcdInf;
    }


    /**
     * 添加监控
     * 
     * @param payRecordInf 支付记录 
     * @param ifcCfgInf 配置信息 
     * @param fstWaiteSecs 首次等待时间秒数
     * @param maxSecs 过程最大时间秒数
     */
    private void checkAndAddMonitorThread(final DtPctSysPayRcd payRecordInf, final DtPctIfcChnlCfg ifcCfgInf, final Integer fstWaiteSecs, final Integer maxSecs) {
        if (payRecordInf == null) {
            logger.warn("添加监控时由于支付记录入参信息为空导致监控失败");
            return;
        }
        Integer chanlIdVal = payRecordInf.getIfcChnlId();
        SyspayIfcChnlEnum repayChanelEnum = SyspayIfcChnlEnum.getByCode(chanlIdVal);
        Integer payStepStatus = payRecordInf.getStepStatus();
        if (!(SysPayStepStatusEnum.WAITE4NTF.getCode() == payStepStatus)) {
            // 不需要监听
            return;
        }
        final String logPre = "执行对支付记录(uprId=" + payRecordInf.getSprId() + ")指定渠道(" + repayChanelEnum + ")核对渠道并通知结果-->";
        // 宝付需要主动监听
        Runnable mntThread = new Runnable() {
            @Override
            public void run() {

                Integer fstWaiteSec = fstWaiteSecs ;
                Integer maxSec = maxSecs;
                // 每次休眠5秒
                int perSlpSec = 5 ;
                
                if(fstWaiteSec == null ) {
                    fstWaiteSec = 0;
                }
                if( maxSec == null || maxSec <= 0 ) {
                    maxSec = 1 ;
                }

                if(fstWaiteSec > 0 ) {
                    try {
                        Thread.sleep(fstWaiteSec);
                    } catch (InterruptedException e) {
                        logger.warn(logPre + "第一次休眠异常..");
                    } 
                }

                Integer currSec = 0 ;
                Boolean isContinue = true;
                while (isContinue && (currSec < maxSec )) {
                    
                    try {
                        
                        SyspayRecordInf checkRst = checkAndUpdatePayStatus(payRecordInf, ifcCfgInf);
                        SysPayStepStatusEnum stepStatus = SysPayStepStatusEnum.getByCode(checkRst.getStepStatus());
                        logger.info(logPre + "执行结果:" + stepStatus);
                        if (!SysPayStepStatusEnum.WAITE4NTF.equals(stepStatus)) {
                            // 不是待监听状态,则不需要监听了
                            isContinue = false;
                        }
                    } catch (Exception e) {
                        logger.error(logPre + "线程执行异常", e);
                    }

                    // 休眠.
                    try {
                        currSec += perSlpSec ; 
                        if(isContinue) {
                            Thread.sleep((1000 * perSlpSec));
                        }
                    } catch (InterruptedException e1) {
                        logger.error(logPre + "休眠异常", e1);
                        e1.printStackTrace();
                    }
                }
            }
        };
        // 加入线程池
        PaycenterThreadPools.syspayMntThreadPool.submit(mntThread);
        logger.debug(logPre + "宝付代付线程池添加了一个线程.");
    }


    @Override
    public SyspayRecordInf checkAndUpdatePayStatus(DtPctSysPayRcd sysPayRcd, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {

        if (sysPayRcd == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "代付记录不能为空");
        }
        // 返回值,默认为入参值.
        DtPctSysPayRcd rtRcd = sysPayRcd;
        Long curSprId = sysPayRcd.getSprId();

//        String logPre = "核对代付记录(sprId=" + curSprId + ")交易状态-->";
        // 需要监听状态的数据才要发起监听
        String optCode = "checkPayStatus";
        // 操作密码
        String optPwd = StringUtils.createRandomStr(5);
        String logPre = "线下放款代付核对代付记录(sprId="+curSprId+")-->";
        try {
            // 核对可操作.
            paycenterOptlmtService.checkAndExpSyspayLmtById(curSprId, optCode, optPwd, true);

            Integer stepStatus = sysPayRcd.getStepStatus();
            SysPayStepStatusEnum currStepEnum = SysPayStepStatusEnum.getByCode(stepStatus);

            Integer ifcChnlId = sysPayRcd.getIfcChnlId();
            SyspayIfcChnlEnum ifcChnlEnum = SyspayIfcChnlEnum.getByCode(ifcChnlId);
            if(!currIfcChnlEnum.equals(ifcChnlEnum)) {
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付记录对应的渠道不是本渠道的");
            }

            Date currTime = new Date();

            SysPayStepStatusEnum newStepEnum ;

            if (SysPayStepStatusEnum.WAITE4NTF.equals(currStepEnum) ) {

                String febJsn ;
                String newStateMemo ;
                Date finishDate = null  ; // 交易结束时间 

                newStepEnum = SysPayStepStatusEnum.SUCCESS; 
                newStateMemo = newStepEnum.getDesc(); 
                finishDate = currTime ; 

                Map<String,Object> oflRspData = new HashMap<>();
                oflRspData.put("rspCode", "0");
                oflRspData.put("rspMsg", "线下放款,默认成功");
                febJsn = JSONObject.toJSONString(oflRspData) ;
              
                // 状态有变化,则更新内容.
                if ( newStepEnum != null && !currStepEnum.equals(newStepEnum)) {
                    // 状态有变化,则更新状态及内容
                    Long sprId = curSprId;
                    DtPctSysPayRcd mdfRcd = new DtPctSysPayRcd();
                    mdfRcd.setSprId(sprId);
                    mdfRcd.setStepStatus(newStepEnum.getCode());
                    mdfRcd.setStepMemo(newStateMemo);
                    mdfRcd.setSprMtime(currTime);
                    if(finishDate != null ) {
                    	mdfRcd.setIfcChnlEndtime(finishDate);
                    }
                    dtPctSysPayRcdService.updateByPrimKeySelective(mdfRcd);
                    DtPctSysPayExtra mdfIfcRcd = new DtPctSysPayExtra();
                    mdfIfcRcd.setSprId(sprId);
                    mdfIfcRcd.setLastFebInf(febJsn);
                    mdfIfcRcd.setLastRspMsg(newStateMemo);
                    dtSysPayExtraService.updateByPrimKeySelective(mdfIfcRcd);
                    rtRcd = dtPctSysPayRcdService.getByPrimKey(sprId, null);
                }

                // 交易成功或失败,则发送异步通知 
                if ( SysPayStepStatusEnum.SUCCESS.equals(newStepEnum) || SysPayStepStatusEnum.FAIL.equals(newStepEnum)) {
                    
                	try {
                		DtPctSysPayExtra spreInf = dtSysPayExtraService.getByPrimKey(rtRcd.getSprId(), null);
                		String statusNotifyUrl = spreInf.getStatusNotifyUrl();
                		if(StringUtils.isNotEmpty(statusNotifyUrl)) {
                			// 发送通知
                			DtPctOutNotifyRcd ntfRcd = new DtPctOutNotifyRcd(); 
                			ntfRcd.setIfcChnlId(rtRcd.getIfcChnlId());
                			ntfRcd.setIfcChnlCfgId(rtRcd.getIfcChnlCfgId());
                			ntfRcd.setNotifyUrl(statusNotifyUrl);
                			ntfRcd.setTradePayCode(rtRcd.getSprCode());
                			ntfRcd.setTradeStepStatus(""+newStepEnum.getCode());
                			payctrOutNotifyService.createAndStartNotifyPlan(ntfRcd );
                		}
					} catch (Exception e) {
						logger.warn(logPre+"发起通知出现异常",e); 
					}
                }
            }
            DtPctSysPayExtra extraInf = dtSysPayExtraService.getByPrimKey(curSprId, null);
            return paycenterBeanProcService.genSysPayRecordInfByModel(rtRcd, extraInf);
        } finally {
            paycenterOptlmtService.releaseSyspayLmtById(curSprId, optCode, optPwd);
        }
    }


    /**
     * 被动处理
     */
    @Override
    public SyspayRecordInf fedbackCheckAndDoBusi(DoPayFedbackBusiReq fedbackInf, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
        throw new BaseServiceException("线下放款代付未支持被动通知处理");
        // return null;
    }

}
