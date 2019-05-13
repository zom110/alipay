package com.sdhoo.pdloan.payctr.busi.oflinepay.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.common.base.util.TimeCalcUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctOutNotifyRcd;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayExtraService;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayRcdService;
import com.sdhoo.pdloan.payctr.PaycenterConstants;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.dto.UserAccRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserPayDftCfgInf;
import com.sdhoo.pdloan.payctr.dto.UserPayRecordInf;
import com.sdhoo.pdloan.payctr.enums.UserPayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayStepStatusEnum;
import com.sdhoo.pdloan.payctr.service.IfcChnlCfgService;
import com.sdhoo.pdloan.payctr.service.PayctrBeanProcService;
import com.sdhoo.pdloan.payctr.service.PayctrOptlmtService;
import com.sdhoo.pdloan.payctr.service.PayctrOutNotifyService;
import com.sdhoo.pdloan.payctr.service.UserPayChnlService;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccResendVerifyReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccVerifyCheckReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrPayReq;
import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;

/**
 * 线下支付渠道支付实现
 * 
 * @author SDPC_LIU
 *
 */
@Service(value = (PaycenterConstants.USR_PAYCHNL_SERVICENAME_PRE + PaycenterConstants.USERPAY_CHNL_OFLINE))
public class UsrPayChnlServiceOflineImpl implements UserPayChnlService {

    private static final Logger logger = LoggerFactory.getLogger(UsrPayChnlServiceOflineImpl.class);

    /**
     * 当前支付渠道(线下支付).
     */
    private UserPayIfcChnlEnum payIfcChnlEnum = UserPayIfcChnlEnum.USRPAY_OFL;

    @Autowired
    private PctIdGenService sequenceGenService;

    @Autowired
    private DtPctUserPayRcdService pctUserPayRcdService;

    @Autowired
    private DtPctUserPayExtraService pctUserPayExtraService;
    
    @Autowired
    private PayctrBeanProcService paycenterBeanProcService ;

    /**
     * 支付中心操作限制服务.
     */
    @Autowired
    private PayctrOptlmtService paycenterOptlmtService;

    @Autowired
    private IfcChnlCfgService ifcChnlCfgService ;
    
    @Autowired
    private PayctrOutNotifyService payctrOutNotifyService ;
    
    /**
     * 过期时间格式化样本
     */
	SimpleDateFormat expTimeFmt = TimeCalcUtils.getThreadLocalDateFormat("yyyy-MM-dd HH:mm");
	
	/**
	 * 金额格式化
	 */
	DecimalFormat moneyYuanFmt = new DecimalFormat("#0.00");
    

    @Override
	public UserPayIfcChnlEnum getPayIfcChnlEnum() {
		return payIfcChnlEnum;
	}

	@Override
	public DtPctIfcChnlCfg getDefaultChnlCfg() {
		UserPayDftCfgInf userPayDftCfgInf = ifcChnlCfgService.getUserPayDftCfgInf();
		Long dftCfgId = userPayDftCfgInf.getOflineChnlCfgId();
		return ifcChnlCfgService.getCachedPctIfcChnlCfg(dftCfgId); 
	}

    @Override
    public String getChnlUserId(Long userId) {
        if (userId == null) {
            return null;
        } else {
            return userId.toString();
        }
    }

    @Override
    public String checkUsrCanUseChnl(Long usrId, DtPctIfcChnlCfg ifcCfgInf, Map<String, Object> extraInf) {
        // 线下支付默认验证通过
        return "";
    }

    @Override
    public UserPayRecordInf doCreatePayRecord(DoCreateUsrPayReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {

        Long userId = req.getUserId(); 
        String payCode = req.getPayCode(); 
        String title = req.getTitle(); 
        String relOrderbusiInfs = req.getBusiRelInf(); 
        Integer payType = req.getPayType(); 
        // 交易金额
        Double payamtYuan = req.getPayamtYuan(); 

        Integer initUserType = req.getInitUserType();
        String initUserId = req.getInitUserId();
        String initUserName = req.getInitUserName();
        String initMemo = req.getInitMemo(); 
        Long ifcChnlCfgId = ifcCfgInf.getIccId();
        String chnlName = ifcCfgInf.getMemberName();
        
        String logPre = "用户支付[线下支付渠道]-->发起支付创建-->";

        // 创建支付单,
        // 后台通知地址
        String statusNotifyUrl = req.getNotifyUrl();
        if (title == null) {
            title = payIfcChnlEnum.getDesc() + "支付";
        }

        // 生成ID
        PctIdData nextRcdId = sequenceGenService.genAndCachePayctIdData();
        Date currTime = nextRcdId.getIdDate();

        // ID prop , 自增主键
        Long uprId;
        // 用户ID
        Long uprUid = userId;
        // 支付码
        String uprCode = payCode;
        // 支付备注,注明
        String uprMemo = title;
        // 支付类型,1付款,2收入
        Integer uprPayType = payType;
        // 实际交易金额
        Double uprPayRmbamt = new BigDecimal(payamtYuan).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); // 交易金额  ;
        // 接口渠道ID
        Integer uprPaychanlId = payIfcChnlEnum.getCode();
        // 接口渠道名称(例:线下支付即时到账,微信公众号支付等)
        String uprPaychanlName = chnlName ; // currPayChanelEnum.getDesc();
        // 渠道支付状态,0创建,1交易成功,2交易失败,
        Integer uprPayStepStatus = UserPayStepStatusEnum.CREATE.getCode();
        // 渠道后台通知的url
        String uprNotifyUrl = statusNotifyUrl;
        // 关联的业务信息
        String busiRelInf = relOrderbusiInfs;
        // 支付发起人类型,0未定,1用户,2管理员
        Integer uprInitUtype = initUserType;

        // 支付发起人id
        Long uprInitUid;

        if (initUserId != null && initUserId.matches(PaycenterConstants.REGEXSTR_LONG)) {
            uprInitUid = Long.valueOf(initUserId);
        } else {
            uprInitUid = 0L;
        }
        uprId = nextRcdId.getIdVal();
        // 支付订单创建并入库
        DtPctUserPayRcd upr = new DtPctUserPayRcd();
        DtPctUserPayExtra upe = new DtPctUserPayExtra();
        upr.setUprId(uprId); 
        upr.setUserId(uprUid); 
        upr.setIcuaId(0L); // 不属于开户性质  
        upr.setUprCode(uprCode); 
        upr.setPayTitle(title); 
        upr.setPayType(uprPayType); 
        upr.setPayRmbamt(uprPayRmbamt); 
        upr.setIfcChnlId(uprPaychanlId); 
        upr.setIfcChnlCfgId(ifcChnlCfgId); 
        upr.setIfcChnlName(uprPaychanlName); 
        upr.setStepStatus(uprPayStepStatus);
        Integer transactionType = req.getTransactionType();
        if (transactionType != null) {
            upr.setTransactionType(transactionType);
        }
        upr.setUprCtime(currTime); 
        upr.setUprMtime(currTime); 

        upe.setUprId(uprId);
        upe.setBusiRelInfo(busiRelInf);
        upe.setUprNotifyUrl(uprNotifyUrl);
        // 发起用户类型
        upe.setInitUtype(uprInitUtype);
        // 发起用户id
        upe.setInitUid(uprInitUid);
        upe.setInitUname(initUserName); 
        // 备注.
        upe.setInitMemo(initMemo);
        upe.setUprMemo(uprMemo);
        upe.setUpeCtime(currTime);
        upe.setUpeMtime(currTime);

        // 创建支付
        pctUserPayRcdService.insertSelective(upr);
        pctUserPayExtraService.insertSelective(upe);

        logger.debug(logPre + "支付信息创建成功");
        
        // 更新支付状态为由创建状态变更为指定的发起结果的状态(创建状态的数据才作更新处理)
        // 新的交易状态 
        UserPayStepStatusEnum new_userPayStepEnum = null ;
        // 渠道返回编号
        String channelRstCode = null;
        // 渠道响应信息
        String channelRstMsg = null;
        // 渠道反馈JSON内容
        String channelRstInfo = null;
        // 接口交易序列号
        String chnlTransNo = "";
        // 渠道响应时间
        Date chnlFebTime = null ;
        
        // 支付准备信息
        String prepayJsn = null ;
        // 发起渠道请求.
        Date ifcChnlExpiretime = null ;
       
        new_userPayStepEnum = UserPayStepStatusEnum.SUCCESS; // 线下支付默认成功 
        
        channelRstCode = "0";
        channelRstMsg = "支付成功";
        Map<String,Object> chnlRstInf = new HashMap<>(2);
        chnlRstInf.put("rspCode", "0");
        chnlRstInf.put("rspMsg", "线下还款默认成功");
        chnlRstInf.put("initMemo", initMemo );
        channelRstInfo = "";
        chnlTransNo=uprCode;
        chnlFebTime = currTime; 
        prepayJsn = "";
        Calendar chnlExptimeCld = Calendar.getInstance();
        chnlExptimeCld.add(Calendar.MINUTE, 2); 
        ifcChnlExpiretime = chnlExptimeCld.getTime();

        // 数据更新
        DtPctUserPayRcd updateUpr = new DtPctUserPayRcd();
        // 支付ID
        updateUpr.setUprId(uprId);
        updateUpr.setStepStatus(new_userPayStepEnum.getCode());
        updateUpr.setIfcChnlFebtime(chnlFebTime);
		updateUpr.setIfcChnlExpiretime(ifcChnlExpiretime);
        updateUpr.setUprMtime(currTime);
        Integer updateCnt = pctUserPayRcdService.updateByPrimKeySelective(updateUpr);

        DtPctUserPayExtra updateUpe = new DtPctUserPayExtra();
        updateUpe.setUprId(uprId);
        updateUpe.setIfcChnlRspcode(channelRstCode);
        updateUpe.setIfcChnlRspmsg(channelRstMsg);
        updateUpe.setIfcChnlSerial(chnlTransNo);
		updateUpe.setPrepayJsn(prepayJsn);
        if (channelRstInfo != null) {
            updateUpe.setIfcFebInf(channelRstInfo);
        }
        updateUpe.setUpeMtime(currTime);
        pctUserPayExtraService.updateByPrimKeySelective(updateUpe);
        if (updateCnt != 1) {
            logger.warn(logPre + "接口调用后更新支付状态时更新条数不等于1.");
        }

        if(UserPayStepStatusEnum.FAIL.equals(new_userPayStepEnum) || UserPayStepStatusEnum.SUCCESS.equals(new_userPayStepEnum)) {
        	 // 发送状态更新通知 
            try {
            	if(StringUtils.isNotEmpty(uprNotifyUrl)) {
            		DtPctOutNotifyRcd ntfRcd = new DtPctOutNotifyRcd(); 
            		ntfRcd.setIfcChnlId(upr.getIfcChnlId());
            		ntfRcd.setIfcChnlCfgId(upr.getIfcChnlCfgId());
            		ntfRcd.setTradePayCode(upr.getUprCode());
            		ntfRcd.setTradeStepStatus(""+new_userPayStepEnum.getCode());
            		ntfRcd.setNotifyUrl(uprNotifyUrl);
            		Calendar nextPlanTime_cld = Calendar.getInstance();
            		nextPlanTime_cld.add(Calendar.SECOND, 3); // 3秒后发起通知 
            		ntfRcd.setNextPlanTime(nextPlanTime_cld.getTime()); 
            		payctrOutNotifyService.createAndStartNotifyPlan(ntfRcd );
            	}
            } catch (Exception e) {
              logger.error(logPre + "支付状态变更后通知出现异常了.此处未处理直接跳过",e); 
            }
        }

        // 发起不成功,则抛出业务异常
        if (UserPayStepStatusEnum.FAIL.equals(new_userPayStepEnum) || UserPayStepStatusEnum.EXCEPTION.equals(new_userPayStepEnum)) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, channelRstMsg);
        }
        upr.setStepStatus(updateUpr.getStepStatus());
        upe.setIfcChnlRspcode(updateUpe.getIfcChnlRspcode());
        upe.setIfcChnlRspmsg(updateUpe.getIfcChnlRspmsg());
        upe.setIfcChnlSerial(updateUpe.getIfcChnlSerial());
        upe.setPrepayJsn(prepayJsn);
        // 支付信息.
        UserPayRecordInf userPayInfo = paycenterBeanProcService.genUsrPayRecordInfByModel(upr, upe);
        // 异步监听(线下支付不需要)
        // 数据返回
        return userPayInfo;
    }

    /**
     * 线下支付渠道没有被动通知支持.
     */
    @Override
    public UserPayRecordInf verifyAndParseNotifyParams(Map<String, ? extends Object> formParam) throws BaseServiceException {
        logger.warn("线下支付代扣渠道没有被动通知支持,但是方法被调用了..");
        return null;
    }

    /**
     * 主动核对并更新支付状态
     * 
     * @param upr
     * @return
     * @throws BaseServiceException
     */
    @Override
    public UserPayRecordInf checkAndUpdatePayStatus(DtPctUserPayRcd upr, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {

        // 商户订单号
        String payCode = upr.getUprCode();
        // 支付信息ID
        Long uprId = upr.getUprId();
        String logPre = "线下支付代扣渠道代扣渠道核对并更新支付(uprId="+uprId+",payCode=" + payCode + ")状态-->";
        // 当前支付记录
        DtPctUserPayRcd currUpr = null;

        Map<String, Object> payrcdQmap = new HashMap<>(2);
        payrcdQmap.put("ifcChnlId", payIfcChnlEnum.getCode());
        if (uprId != null) {
            payrcdQmap.put("uprId", uprId);
        }
        payrcdQmap.put("uprCode", payCode);
        List<DtPctUserPayRcd> qrst = pctUserPayRcdService.selectByCriteria(payrcdQmap, 0, 2);
        if (qrst.size() != 1) {
            // 支付记录不存在
            logger.warn(logPre + "支付记录不存在,返回null记录.");
            return null;
        }
        // 当前数据最新状态
        currUpr = qrst.get(0);

        uprId = currUpr.getUprId();
        Integer payChnlId = currUpr.getIfcChnlId();
        UserPayIfcChnlEnum curUserpayChnlEnum = UserPayIfcChnlEnum.getByCode(payChnlId);
        if (!payIfcChnlEnum.equals(curUserpayChnlEnum)) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付记录渠道不是本渠道的");
        }
        Integer currStepStatus = currUpr.getStepStatus();
        UserPayStepStatusEnum currPayStepStatus = UserPayStepStatusEnum.getByCode(currStepStatus);
        boolean isUpdate = false;

        String optCode = "checkAndUpdatePayStatus";
        // 操作密码
        String optPwd = StringUtils.createRandomStr(5);

        try {
            // 操作限制判断
            boolean checkRst = paycenterOptlmtService.checkAndExpUsrpayLmtById(uprId, optCode, optPwd, true); 

            if (UserPayStepStatusEnum.WAITE4NTF.equals(currPayStepStatus) && checkRst ) {
                UserPayRecordInf ifcChnlLstPayRcdInf = null;
                try {
                    ifcChnlLstPayRcdInf = getChnlIfcPayRecordInfByPayCode(currUpr.getUprCode(), ifcCfgInf);
                } catch (BaseServiceException e) {
                    logger.error(logPre + "渠道接口获取数据异常", e);
                }
                if (ifcChnlLstPayRcdInf != null) {
                    // 更新支付记录
                    Integer payStepStatus = ifcChnlLstPayRcdInf.getPayStepStatus();
                    if ( payStepStatus != null && !currStepStatus.equals(payStepStatus)) {
                        Date mdfTime = new Date();
                        // 更新状态
                        String payChnlFebinfo = ifcChnlLstPayRcdInf.getPayChnlFebinfo();
                        String payChnlRstcode = ifcChnlLstPayRcdInf.getPayChnlRstcode();
                        String payChnlRstMsg = ifcChnlLstPayRcdInf.getPayChnlRstMsg();
                        Date payChnlFebtime = ifcChnlLstPayRcdInf.getPayChnlFebtime();

                        DtPctUserPayRcd payRcd = new DtPctUserPayRcd();
                        payRcd.setUprId(uprId);
                        payRcd.setIfcChnlFebtime(payChnlFebtime);
                        payRcd.setUprMtime(mdfTime);
                        payRcd.setStepStatus(payStepStatus);
                        pctUserPayRcdService.updateByPrimKeySelective(payRcd);

                        DtPctUserPayExtra updateExtra = new DtPctUserPayExtra();
                        updateExtra.setUprId(uprId);
                        updateExtra.setIfcChnlRspcode(payChnlRstcode);
                        updateExtra.setIfcChnlRspmsg(payChnlRstMsg);
                        updateExtra.setIfcFebInf(payChnlFebinfo);
                        updateExtra.setUpeMtime(mdfTime);
                        pctUserPayExtraService.updateByPrimKeySelective(updateExtra);

                        // 数据有更新
                        isUpdate = true;

                        // 发送状态更新通知 
                        try {
                        	DtPctUserPayExtra uprInf = pctUserPayExtraService.getByPrimKey(currUpr.getUprId(), null);
                        	String uprNotifyUrl = uprInf.getUprNotifyUrl();
                        	if(StringUtils.isNotEmpty(uprNotifyUrl)) {
                        		DtPctOutNotifyRcd ntfRcd = new DtPctOutNotifyRcd(); 
                        		ntfRcd.setIfcChnlId(currUpr.getIfcChnlId());
                        		ntfRcd.setIfcChnlCfgId(currUpr.getIfcChnlCfgId());
                        		ntfRcd.setTradePayCode(currUpr.getUprCode());
                        		ntfRcd.setTradeStepStatus(""+payStepStatus);
                        		ntfRcd.setNotifyUrl(uprNotifyUrl);
                        		payctrOutNotifyService.createAndStartNotifyPlan(ntfRcd );
                        	}
                        } catch (Exception e) {
                          logger.error(logPre + "支付状态变更后通知出现异常了.此处未处理直接跳过",e); 
                        }

                    }
                } else {
                    logger.error(logPre + "渠道接口获取状态结果为空");
                }
            }
            if (isUpdate) {
                currUpr = pctUserPayRcdService.getByPrimKey(uprId, null);
            }
            DtPctUserPayExtra extra = pctUserPayExtraService.getByPrimKey(uprId, null);
            UserPayRecordInf rtVal = paycenterBeanProcService.genUsrPayRecordInfByModel(currUpr, extra);
            return rtVal;
        } finally {
            paycenterOptlmtService.releaseUsrpayLmtById(uprId, optCode, optPwd);
        }
    }

    /**
     * 通过商户订单号从渠道接口获取最新交易情况.
     * 
     * @param chnlOutTradeNo
     * @return
     * @throws BaseServiceException
     */
    private UserPayRecordInf getChnlIfcPayRecordInfByPayCode(String chnlOutTradeNo, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {

        String logPre = "线下支付通过渠道商户订单号(" + chnlOutTradeNo + ")提取最新支付情况 ";

        // 新的交易状态 
        UserPayStepStatusEnum new_userPayStepEnum = null; 
        // 渠道返回编号
        String channelRstCode = null;
        // 渠道响应信息
        String channelRstMsg = null;
        // 渠道反馈JSON内容
        String channelRstInfo = null;
        // 渠道交易号
        String transNo = null ;
        // 渠道响应时间
        Date chnlFebTime = null ;

        Date currTime = new Date();
        // 请求序列号
        try {
            
            
            new_userPayStepEnum = UserPayStepStatusEnum.SUCCESS; // 线下支付默认成功 
            
            channelRstCode = "0";
            channelRstMsg = "支付成功";
            Map<String,Object> chnlRstInf = new HashMap<>(2);
            chnlRstInf.put("rspCode", "0");
            chnlRstInf.put("rspMsg", "线下还款默认成功");
            chnlRstInf.put("initMemo", "" );
            channelRstInfo = "";
            chnlFebTime = currTime; 
        
        } catch (Exception e) {
            logger.error(logPre + "数据获取异常", e);
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "调用线下支付代扣渠道查询交易记录异常");
        }
        
        UserPayRecordInf newUprRecord = new UserPayRecordInf();
        newUprRecord.setPayCode(chnlOutTradeNo);

        // 最新支付记录状态
        if (new_userPayStepEnum != null) {
            newUprRecord.setPayStepStatus(new_userPayStepEnum.getCode());
        }
        if (channelRstCode != null) {
            newUprRecord.setPayChnlRstcode(channelRstCode);
        }
        if (channelRstMsg != null) {
            newUprRecord.setPayChnlRstMsg(channelRstMsg);
        }
        if (channelRstInfo != null) {
            newUprRecord.setPayChnlFebinfo(channelRstInfo);
        }
        if (StringUtils.isNotEmpty(transNo)) {
            // 渠道序列号
            newUprRecord.setPayChnlSerial(transNo);
        }
        newUprRecord.setPayChnlFebtime(chnlFebTime);

        return newUprRecord;
    }

    /**
     * 被动通知处理,
     */
    @Override
    public UserPayRecordInf payfedbackCheckAndDoBusi(DoPayFedbackBusiReq fedbackInf, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
    	throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "线下支付暂不支持被动回调");
    }

    /**
     * 渠道用户开户
     */
    @Override
    public UserAccRecordInf openAccount(DoCreateUsrAccReq usrAccInf, DtPctIfcChnlCfg ifcCfgInf ) throws BaseServiceException {
    	
    	throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "线下支付暂不支持开户绑定");
//        return null;
    }

    /**
     * 绑卡短验确认
     */
    @Override
    public UserAccRecordInf toBindCheck(DoCreateUsrAccVerifyCheckReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
    	throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "线下支付暂不支持开户绑定");
//      return null;
    }

    /**
     * 绑卡短验重发
     */
    @Override
    public UserAccRecordInf toCreateUserAccResendVerify(DoCreateUsrAccResendVerifyReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
    	throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "线下支付暂不支持开户绑定");
//      return null;
    }

    
    
    /* ************************ */
    

}
