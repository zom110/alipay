package com.sdhoo.pdloan.payctr.busi.yibaodk.service.impl;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.RegexConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.BeanProcUtils;
import com.sdhoo.common.base.util.RegexUtils;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.common.base.util.TimeCalcUtils;
import com.sdhoo.common.web.util.WebDataProcUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctBankInfo;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlUserAcc;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlUserExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctOutNotifyRcd;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctBankInfoService;
import com.sdhoo.pdloan.bcrud.service.DtPctIfcChnlUserAccService;
import com.sdhoo.pdloan.bcrud.service.DtPctIfcChnlUserExtraService;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayExtraService;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayRcdService;
import com.sdhoo.pdloan.payctr.PaycenterConstants;
import com.sdhoo.pdloan.payctr.PaycenterThreadPools;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.YibaodkCfgInf;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req.BkPayRecordReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req.BkPayReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req.JqBkReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req.JqBkSmsValiAgainReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req.JqBkSmsValiReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.BkPayRecordRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.BkPayRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.JqBkRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.JqBkSmsValiAgainRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.JqBkSmsValiRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodk.enums.BindcardAuthtypeEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodk.enums.BindcardIdCardTypeEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodk.enums.BindcardIdentityTypeEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodk.enums.BindcardStatusEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodk.enums.YibaoPayErrorCodeEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodk.enums.YibaoPayStatusEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodk.service.YibaodkCfgService;
import com.sdhoo.pdloan.payctr.busi.yibaodk.service.YibaodkClientService;
import com.sdhoo.pdloan.payctr.dto.UserAccRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserPayDftCfgInf;
import com.sdhoo.pdloan.payctr.dto.UserPayRecordInf;
import com.sdhoo.pdloan.payctr.enums.UserAccMainStatusEnum;
import com.sdhoo.pdloan.payctr.enums.UserAccStepStatusEnum;
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
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;

/**
 * 易宝代扣渠道代扣实现.
 * 
 * @author SDPC_LIU
 *
 */
@Service(value = (PaycenterConstants.USR_PAYCHNL_SERVICENAME_PRE + PaycenterConstants.USERPAY_CHNL_YIBAOBINDDK))
public class UsrPayChnlServiceYibaodkImpl implements UserPayChnlService {

    private static final Logger logger = LoggerFactory.getLogger(UsrPayChnlServiceYibaodkImpl.class);

    /**
     * 当前支付渠道.
     */
    private UserPayIfcChnlEnum payIfcChnlEnum = UserPayIfcChnlEnum.YIBAOPAY_BINDDK;

    @Autowired
    private PctIdGenService sequenceGenService;

    @Autowired
    private DtPctUserPayRcdService pctUserPayRcdService;

    @Autowired
    private DtPctUserPayExtraService pctUserPayExtraService;

    @Autowired
    private YibaodkCfgService yibaodkCfgService;
    
    @Autowired
    private DtPctIfcChnlUserAccService pctIfcChnlUserAccService;
    
    @Autowired
    private DtPctIfcChnlUserExtraService pctIfcChnlUserExtraService;
    
    @Autowired
    private DtPctBankInfoService pctBankInfoService;
    
    @Autowired
    private PayctrBeanProcService paycenterBeanProcService ;

    /**
     * 易宝代扣服务.
     */
    @Autowired
    private YibaodkClientService yibaoClientService ;
    
    /**
     * 易宝支付配置服务
     */
    @Autowired
    private YibaodkCfgService yibaoCfgService ;

    /**
     * 支付中心操作限制服务.
     */
    @Autowired
    private PayctrOptlmtService paycenterOptlmtService;

    @Autowired
    private IfcChnlCfgService ifcChnlCfgService ;
    
    @Resource
    private PayctrOutNotifyService payctrOutNotifyService ;

//    /**
//     * 易宝回调地址
//     */
//    @Value(value="${paycenter.yibaodk.callback}")
//    private String ybCallBackUrl;
    
    /**
     * 开户用户ID前缀.
     */
    @Value(value="${paycenter.yibaodk.useridpre}")
    private String openUserIdPre = "";

    /**
     * 是否虚拟交易
     */
    @Value(value="${paycenter.yibaodk.issimpay}")
    private boolean isSimPay = true ;
    

	@Override
	public UserPayIfcChnlEnum getPayIfcChnlEnum() {
		return payIfcChnlEnum;
	}
    
	@Override
	public DtPctIfcChnlCfg getDefaultChnlCfg() {
		UserPayDftCfgInf userPayDftCfgInf = ifcChnlCfgService.getUserPayDftCfgInf();
		Long yibaoCfgId = userPayDftCfgInf.getDudcYibaoPayChnlCfgId();
		return ifcChnlCfgService.getCachedPctIfcChnlCfg(yibaoCfgId); 
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
        
        Long iccId = ifcCfgInf.getIccId();
        String logPre = payIfcChnlEnum.getDesc() + "检查用户(userId="+usrId+",渠道配置id="+iccId+")是否可以使用渠道支付-->";

        // 查找主卡.
        // 获取配置下的开户主户信息
        Map<String,Object> yibaoUserQmap = new HashMap<>();
        yibaoUserQmap.put("ifcChnlCfgId", iccId);
        yibaoUserQmap.put("userId", usrId); 
        yibaoUserQmap.put("stepStatus", UserAccStepStatusEnum.SUCCESS.getCode()); 
        yibaoUserQmap.put("mainAccStatus", UserAccMainStatusEnum.MAIN.getCode()); 
        List<String> orderBy_conditionList = new ArrayList<>();
        orderBy_conditionList.add("icuaId_desc");
        yibaoUserQmap.put("orderBy_conditionList", orderBy_conditionList); 
        List<DtPctIfcChnlUserAcc> uList = pctIfcChnlUserAccService.selectByCriteria(yibaoUserQmap, 0, 2);
        int uaccListSize = uList.size();
        if(uaccListSize < 1 ) {
            String errorStr = "用户未成功开通易宝代扣" ;
            logger.warn(logPre+errorStr);
            return  errorStr  ;
        }
        if(uaccListSize > 1 ) {
            // 主账号超过1个,则输出警告
            logger.warn(logPre + "开通主账号数为" + uaccListSize + "默认获取最新的一个");
        }
        DtPctIfcChnlUserAcc ifcChnlUserAcc = uList.get(0);
        Integer stepStatus = ifcChnlUserAcc.getStepStatus();
        UserAccStepStatusEnum bindStatus = UserAccStepStatusEnum.getByCode(stepStatus);
        if(!UserAccStepStatusEnum.SUCCESS.equals(bindStatus)) {
            return "用户未绑卡成功";
        }
        // 验证通过
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
        Integer ifcChnlId = ifcCfgInf.getIfcChnlId();
        Long ifcChnlCfgId = ifcCfgInf.getIccId();
        String chnlName = ifcCfgInf.getMemberName();
        

        YibaodkCfgInf yibaoDkCfgInf = yibaodkCfgService.getCfgInfByCfgId(ifcCfgInf);

        // 获取配置
        // 优先获取指定支付配置ID的配置
        // 获取配置下的开户信息
        Map<String,Object> yibaoUserQmap = new HashMap<>(); 
		yibaoUserQmap.put("ifcChnlCfgId", ifcChnlCfgId); 
        yibaoUserQmap.put("userId", userId); 
        yibaoUserQmap.put("mainAccStatus", UserAccMainStatusEnum.MAIN.getCode()); 
        List<DtPctIfcChnlUserAcc> uList = pctIfcChnlUserAccService.selectByCriteria(yibaoUserQmap, 0, 2);
        DtPctIfcChnlUserExtra userExtra = new DtPctIfcChnlUserExtra();
        int ulistSize = uList.size();
        if (ulistSize < 1) {
            throw new BaseServiceException(com.sdhoo.common.SebaseConstants.ERROR_CODE_BUSI_MINI, "用户未开通易宝代扣");
        }
        // 取最新开户信息
        DtPctIfcChnlUserAcc ifcChnlUserAcc = uList.get((ulistSize-1));
        if(ifcChnlUserAcc == null ) {
            throw new BaseServiceException(com.sdhoo.common.SebaseConstants.ERROR_CODE_BUSI_MINI, "用户未开通易宝代扣");
        }
        userExtra = pctIfcChnlUserExtraService.getByPrimKey(ifcChnlUserAcc.getIcuaId(), null);
        Integer stepStatus = ifcChnlUserAcc.getStepStatus();
        UserAccStepStatusEnum stepEnum = UserAccStepStatusEnum.getByCode(stepStatus);
        String openId = ifcChnlUserAcc.getOpenId();
        if( !UserAccStepStatusEnum.SUCCESS.equals(stepEnum) || StringUtils.isEmpty(openId)) {
            throw new BaseServiceException(com.sdhoo.common.SebaseConstants.ERROR_CODE_BUSI_MINI, "用户未开通易宝代扣");
        }

        String logPre = payIfcChnlEnum.getDesc() + "渠道创建支付(payCode=" + payCode + ")行为-->";

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
        // 接口渠道名称(例:支付宝即时到账,微信公众号支付等)
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
        upr.setIcuaId(ifcChnlUserAcc.getIcuaId()); 
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

        logger.debug(logPre + "支付创建成功");
        // 发起渠道请求.
        SimpleDateFormat timeFmt = TimeCalcUtils.getThreadLocalDateFormat("yyyy-MM-dd HH:mm:ss");

        // 开户类型
        String identitytype = BindcardIdentityTypeEnum.USER_ID.getCode();
        // 终端标识码
        String terminalno = yibaoDkCfgInf.getMerchantTermilalno();  
        // 请求交易号
        String requestno ;
        // 用户认证id
        String identityid ;
        // 银行卡前6位
        String cardtop ;
        // 卡片后4位
        String cardlast ;
        // 金额(元)
        String amount ;
        // 产品名称
        String productname  ;
        String callbackurl ;
        // 备注信息
        String remark ; 
        // 扩展信息
        String  extinfos ;  
        // 请求时间
        String  requesttime ;

        {
            identitytype = BindcardIdentityTypeEnum.USER_ID.getCode();
            terminalno = yibaoDkCfgInf.getMerchantTermilalno(); 
            requestno = uprCode ;
            identityid = openId ;
            
            String bankCardNo = userExtra.getBankCardNo();
            // 银行卡前6位
            cardtop = bankCardNo.substring(0, 6);
            // 银行卡后4位
            cardlast = bankCardNo.substring(bankCardNo.length()-4);
            double payAmt = uprPayRmbamt;
            amount = String.valueOf(payAmt);
            productname = "交易还款:" + requestno ;
            callbackurl = ifcCfgInf.getNotifyUrl() ;
            remark = title ; 
            extinfos = busiRelInf ; 
            requesttime  = timeFmt.format(currTime);
        }

        BkPayReq cfrmReq = new BkPayReq();
        cfrmReq.setRequestno(requestno);
        cfrmReq.setIssms(false);
        cfrmReq.setIdentityid(identityid );
        cfrmReq.setIdentitytype(identitytype);
        cfrmReq.setCardtop(cardtop);
        cfrmReq.setCardlast(cardlast);
        cfrmReq.setAmount(amount);
        cfrmReq.setProductname(productname);
        cfrmReq.setCallbackurl(callbackurl);
        cfrmReq.setRequesttime(requesttime);
        cfrmReq.setTerminalno(terminalno);
        cfrmReq.setRemark(remark);
        cfrmReq.setExtinfos(extinfos); 
        cfrmReq.setAvaliabletime("15"); // 默认15分钟过期.

        // 代扣调用结果
        BkPayRsp dkRsp = null;

        // 是否调用
        boolean isrecall = true;
        int maxRecallCnt = 3;
        int currRecalCnt = 0;

        // 调用后返回的交易记录信息.
        UserPayRecordInf callRepayInf = null ;
        while (isrecall) {
            isrecall = false;
            currRecalCnt++;
            try {

                // 是否虚拟交易
                if(isSimPay) {
                    dkRsp=simIfcPayCreate(cfrmReq, yibaoDkCfgInf);
                }else {
                    dkRsp=yibaoClientService.doExecuteBaseReq(cfrmReq, yibaoDkCfgInf);
                }
            } catch (BaseServiceException e) {
                logger.error(logPre + "代扣发起异常:", e);
                Integer errorCode = e.getErrorCode();
                if (SebaseConstants.ERROR_CODE_BUSI_UNKNOWN == errorCode) {
                    // 发起未知异常
                    boolean isRetryCheck = true;
                    int retryCheckCntMax = 3;
                    int retryCheckCucnt = 0;
                    boolean retrySuccess = false;
                    while (isRetryCheck) {
                        isRetryCheck = false;
                        retryCheckCucnt++;
                        try {
                            callRepayInf = getChnlIfcPayRecordInfByPayCode(requestno, yibaoDkCfgInf);
                            // 重试调用成功
                            retrySuccess = true;
                        } catch (Exception e2) {
                            if (retryCheckCucnt < retryCheckCntMax) {
                                isRetryCheck = true;
                                continue;
                            }
                        }
                    }
                    if(( callRepayInf == null || callRepayInf.getPayStepStatus() == null) ) {
                        if ( !retrySuccess) {
                            // 重试调用不成功,抛支付渠道异常.
                            logger.warn(logPre + "交易不存在重试调用异常超过指定次数(" + retryCheckCntMax + ")造成失败");
                            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "支付渠道异常");
                        }
                        // 交易不存在,判断重试发起
                        if (currRecalCnt < maxRecallCnt) {
                            // 重试次数小于3,继续
                            // 继续调用
                            isrecall = true;
                        } else {
                            logger.warn(logPre + "交易不存在重试调用次数超过指定次数(" + maxRecallCnt + ")造成失败");
                            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "支付渠道异常");
                        }
                    }
                }
            }
        }

        // 更新支付状态为由创建状态变更为指定的发起结果的状态(创建状态的数据才作更新处理)
        // 新的交易状态 
        UserPayStepStatusEnum newChnlStepStatusEnum = null ;
        // 渠道返回编号
        String channelRstCode = null;
        // 渠道响应信息
        String channelRstMsg = null;
        // 渠道反馈JSON内容
        String channelRstInfo = null;
        // 接口交易序列号
        String transNo = null;
        
        // 渠道响应时间
        Date chnlFebTime = null ;

        if(dkRsp != null ) {
            callRepayInf = new UserPayRecordInf();
            channelRstCode = dkRsp.getErrorcode();
            channelRstMsg = dkRsp.getErrormsg();
            String merchantno = dkRsp.getMerchantno();
            // 渠道反馈JSON内容
            channelRstInfo = JSONObject.toJSONString(dkRsp);

            // 状态及错误码
            YibaoPayStatusEnum payStatusEnum = YibaoPayStatusEnum.getByCode(dkRsp.getStatus()); 
//            YibaoPayErrorCodeEnum payErrorCodeEnum = YibaoPayErrorCodeEnum.getByCode(channelRstCode);

            callRepayInf.setPayCode(payCode);

            // 响应数据
            if (YibaoPayStatusEnum.PROCESSING.equals(payStatusEnum) || YibaoPayStatusEnum.ACCEPT.equals(payStatusEnum)) {
                // 交易处理中,则继续下一等待
                // 仍然需要监听.
                newChnlStepStatusEnum = UserPayStepStatusEnum.WAITE4NTF;
            } else if (YibaoPayStatusEnum.PAY_SUCCESS.equals(payStatusEnum)) {

                // 交易成功
                newChnlStepStatusEnum = UserPayStepStatusEnum.SUCCESS;
                transNo = merchantno ; 

            } else if (YibaoPayStatusEnum.PAY_FAIL.equals(payStatusEnum) || YibaoPayStatusEnum.TIME_OUT.equals(payStatusEnum) || YibaoPayStatusEnum.FAIL.equals(payStatusEnum) ) {
                // 交易失败处理
                newChnlStepStatusEnum = UserPayStepStatusEnum.FAIL;
                
                
            } else if (YibaoPayStatusEnum.TO_VALIDATE.equals(payStatusEnum)) {
                // 需要推进交易
                newChnlStepStatusEnum = UserPayStepStatusEnum.NEEDPUSH;
            }else {
                // 渠道状态未知,需要核对.
                newChnlStepStatusEnum = UserPayStepStatusEnum.WAITE4NTF;
            }
            
            
            if(UserPayStepStatusEnum.SUCCESS.equals(newChnlStepStatusEnum) || UserPayStepStatusEnum.FAIL.equals(newChnlStepStatusEnum)) {
            	// 发送通知
            	try {
            		if(StringUtils.isNotEmpty(uprNotifyUrl)) {
            			DtPctOutNotifyRcd ntfRcd = new DtPctOutNotifyRcd(); 
            			ntfRcd.setIfcChnlId(ifcChnlId);
            			ntfRcd.setIfcChnlCfgId(ifcChnlCfgId);
            			ntfRcd.setTradePayCode(""+payCode);
            			ntfRcd.setTradeStepStatus(""+newChnlStepStatusEnum.getCode());
            			ntfRcd.setNotifyUrl(uprNotifyUrl);
            			payctrOutNotifyService.createAndStartNotifyPlan(ntfRcd );
            		}
				} catch (Exception e) {
					logger.warn(logPre+"发送通知出现异常了",e); 
				}
            }

            // 最新支付记录状态
            callRepayInf.setPayStepStatus(newChnlStepStatusEnum.getCode());

            if (channelRstCode != null) {
                callRepayInf.setPayChnlRstcode(channelRstCode);
            }
            if (channelRstMsg != null) {
                callRepayInf.setPayChnlRstMsg(channelRstMsg);
            }
            if (channelRstInfo != null) {
                callRepayInf.setPayChnlFebinfo(channelRstInfo);
            }
            if (StringUtils.isNotEmpty(transNo)) {
                // 渠道序列号
                callRepayInf.setPayChnlSerial(transNo);
            }
            if(chnlFebTime == null ) {
                chnlFebTime = currTime;
            }
            callRepayInf.setPayChnlFebtime(chnlFebTime);

        }

        if (callRepayInf == null) {
            // 发起异常.
            newChnlStepStatusEnum = UserPayStepStatusEnum.EXCEPTION;
            channelRstCode = "-1";
            channelRstMsg = "代扣发起异常.";
        }else {
            channelRstMsg = callRepayInf.getPayChnlRstMsg();
        }

        // 空,则置为异常
        if(newChnlStepStatusEnum == null ) {
            logger.error(logPre + "调用过后检索到新的支付状态是空的,此处置为异常..");
            newChnlStepStatusEnum = UserPayStepStatusEnum.EXCEPTION;
        }
        Date ntime = new Date();
        // 数据更新
        DtPctUserPayRcd updateUpr = new DtPctUserPayRcd();
        // 支付ID
        updateUpr.setUprId(uprId);
        updateUpr.setStepStatus(newChnlStepStatusEnum.getCode());
        updateUpr.setUprMtime(ntime);
        Integer updateCnt = pctUserPayRcdService.updateByPrimKeySelective(updateUpr);

        DtPctUserPayExtra updateUpe = new DtPctUserPayExtra();
        updateUpe.setUprId(uprId);
        updateUpe.setIfcChnlRspcode(callRepayInf.getPayChnlRstcode());
        updateUpe.setIfcChnlRspmsg(callRepayInf.getPayChnlRstMsg());
        updateUpe.setIfcChnlSerial(transNo);
        if (channelRstInfo != null) {
            updateUpe.setIfcFebInf(channelRstInfo);
        }
        updateUpe.setUpeMtime(ntime);
        pctUserPayExtraService.updateByPrimKeySelective(updateUpe);
        if (updateCnt != 1) {
            logger.warn(logPre + "更新支付状态时更新条数不等于1.");
        }

        // 发起不成功,则抛出业务异常
        if (UserPayStepStatusEnum.FAIL.equals(newChnlStepStatusEnum) || UserPayStepStatusEnum.EXCEPTION.equals(newChnlStepStatusEnum)) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, channelRstMsg);
        }
        upr.setStepStatus(updateUpr.getStepStatus());
        upe.setIfcChnlRspcode(updateUpe.getIfcChnlRspcode());
        upe.setIfcChnlRspmsg(updateUpe.getIfcChnlRspmsg());
        upe.setIfcChnlSerial(updateUpe.getIfcChnlSerial());
        // 支付信息.
        UserPayRecordInf userPayInfo = paycenterBeanProcService.genUsrPayRecordInfByModel(upr, upe);
        // 需要异步处理的,则发起异步监听,15分钟内5秒发起一次.
        checkAndAddMonitorThread(upr,ifcCfgInf,0, 900);
        // 数据返回
        return userPayInfo;
    }


    /**
     * 易宝代扣渠道没有被动通知支持.
     */
    @Override
    public UserPayRecordInf verifyAndParseNotifyParams(Map<String, ? extends Object> formParam) throws BaseServiceException {
        logger.warn("易宝代扣渠道没有被动通知支持,但是方法被调用了..");
        return null;
    }

    /**
     * 核对并更新支付状态
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
        String logPre = "易宝代扣渠道代扣渠道核对并更新支付(uprId="+uprId+",payCode=" + payCode + ")状态-->";
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
        Long ifcChnlCfgId = currUpr.getIfcChnlCfgId();
        YibaodkCfgInf yibaoCfgInf = null ; 
        if(ifcChnlCfgId != null && ifcChnlCfgId > 0 ) {
            yibaoCfgInf = yibaodkCfgService.getCfgInfByCfgId(ifcCfgInf);
        }
        UserPayIfcChnlEnum rcdChnlCode = UserPayIfcChnlEnum.getByCode(payChnlId);
        if (!payIfcChnlEnum.equals(rcdChnlCode)) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付记录渠道不是本渠道的");
        }
        Integer currStepStatus = currUpr.getStepStatus();
        UserPayStepStatusEnum currPayStepStatus = UserPayStepStatusEnum.getByCode(currStepStatus);
        boolean isUpdate = false;
        String optCode = "checkPayStatus";
        // 操作密码
        String optPwd = StringUtils.createRandomStr(5);

        try {
            // 操作限制判断
            boolean checkRst = paycenterOptlmtService.checkAndExpUsrpayLmtById(uprId, optCode, optPwd, true); 

            if (UserPayStepStatusEnum.WAITE4NTF.equals(currPayStepStatus) && checkRst ) {
                UserPayRecordInf ifcChnlLstPayRcdInf = null;
                try {
                    ifcChnlLstPayRcdInf = getChnlIfcPayRecordInfByPayCode(currUpr.getUprCode(), yibaoCfgInf);
                } catch (BaseServiceException e) {
                    logger.error(logPre + "渠道接口获取数据异常", e);
                }
                if (ifcChnlLstPayRcdInf != null) {
                    // 更新支付记录
                    Integer payStepStatus = ifcChnlLstPayRcdInf.getPayStepStatus();
                    if (!currStepStatus.equals(payStepStatus)) {
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
                        	
                        	DtPctUserPayExtra tmpUpe = pctUserPayExtraService.getByPrimKey(uprId, null);
                        	String uprNotifyUrl = tmpUpe.getUprNotifyUrl(); 
                        	if(StringUtils.isNotEmpty(uprNotifyUrl)) {
                        		DtPctOutNotifyRcd ntfRcd = new DtPctOutNotifyRcd(); 
                        		ntfRcd.setIfcChnlId(upr.getIfcChnlId());
                        		ntfRcd.setIfcChnlCfgId(upr.getIfcChnlCfgId());
                        		ntfRcd.setTradePayCode(upr.getUprCode());
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
     * @param payCode
     * @return
     * @throws BaseServiceException
     */
    private UserPayRecordInf getChnlIfcPayRecordInfByPayCode(String payCode, YibaodkCfgInf cfgInfByShid) throws BaseServiceException {

        String logPre = "通过商户订单号(" + payCode + ")提取最新支付情况 ";
        
        // 新的交易状态 
        UserPayStepStatusEnum newChnlStepStatusEnum = null;
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

        SimpleDateFormat basnkSuccDateSdfmt = TimeCalcUtils.getThreadLocalDateFormat("yyyy-MM-dd HH:mm:ss");

        Date currTime = new Date();
        // 请求序列号
        BkPayRecordReq cfrmReq = new BkPayRecordReq();
        cfrmReq.setRequestno(payCode);
        BkPayRecordRsp queryRsp = null ;
        try {
            
            if(isSimPay) {
                // 虚拟调用,则调用虚拟调用信息
                queryRsp = simGetPayRstInf(cfrmReq, cfgInfByShid);
                
            }else {
                queryRsp =yibaoClientService.doExecuteBaseReq(cfrmReq, cfgInfByShid);
            }
        } catch (Exception e) {
            logger.error(logPre + "数据获取异常", e);
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "调用易宝代扣渠道查询交易记录异常");
        }
        // 状态
        channelRstCode = queryRsp.getErrorcode();
        channelRstMsg = queryRsp.getErrormsg();
        transNo = queryRsp.getMerchantno();
        String banksuccessdate = queryRsp.getBanksuccessdate();
        
        UserPayRecordInf newUprRecord = new UserPayRecordInf();
        newUprRecord.setPayCode(payCode);

        // 状态及错误码
        String rspPayStatus = queryRsp.getStatus();
        YibaoPayStatusEnum payStatusEnum = YibaoPayStatusEnum.getByCode(rspPayStatus); 
        YibaoPayErrorCodeEnum payErrorCodeEnum = YibaoPayErrorCodeEnum.getByCode(channelRstCode);
        // 不存在,则直接返回空
        if (YibaoPayErrorCodeEnum.DKPK100007.equals(payErrorCodeEnum) || YibaoPayErrorCodeEnum.DK0010002.equals(payErrorCodeEnum) ) {
            // 订单不存在
            newChnlStepStatusEnum = UserPayStepStatusEnum.FAIL;
        }
        channelRstInfo = JSONObject.toJSONString(queryRsp);
        // 最终响应的信息 
        if(StringUtils.isNotEmpty(banksuccessdate) ) {
            try {
                chnlFebTime = basnkSuccDateSdfmt.parse(banksuccessdate);
            } catch (ParseException e) {
                logger.warn(logPre + "渠道反馈时间(banksuccessdate="+banksuccessdate+")解析错误",e); 
            }
        }
        if(payStatusEnum != null ) {
            // 响应数据
            if (YibaoPayStatusEnum.PROCESSING.equals(payStatusEnum) || YibaoPayStatusEnum.ACCEPT.equals(payStatusEnum) ) {
                // 交易处理中,则继续下一等待
                // 仍然需要监听.
                newChnlStepStatusEnum = UserPayStepStatusEnum.WAITE4NTF;
            }else if (YibaoPayStatusEnum.PAY_SUCCESS.equals(payStatusEnum)) {
                // 交易成功
                newChnlStepStatusEnum = UserPayStepStatusEnum.SUCCESS;
                if(StringUtils.isEmpty(channelRstMsg)) {
                    channelRstMsg = payStatusEnum.getName();
                }
            }else if (YibaoPayStatusEnum.PAY_FAIL.equals(payStatusEnum) || YibaoPayStatusEnum.TIME_OUT.equals(payStatusEnum) || YibaoPayStatusEnum.FAIL.equals(payStatusEnum) ) {
                // 交易失败处理
                newChnlStepStatusEnum = UserPayStepStatusEnum.FAIL;
            }else if (YibaoPayStatusEnum.TO_VALIDATE.equals(payStatusEnum) ) {
                // 需要交易进一步处理
                newChnlStepStatusEnum = UserPayStepStatusEnum.NEEDPUSH;
            }
        }

        // 最新支付记录状态
        if (newChnlStepStatusEnum != null) {
            newUprRecord.setPayStepStatus(newChnlStepStatusEnum.getCode());
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
        if(chnlFebTime == null ) {
            chnlFebTime = currTime;
        }
        newUprRecord.setPayChnlFebtime(chnlFebTime);

        return newUprRecord;
    }

    /**
     * 被动通知处理,
     */
    @Override
    public UserPayRecordInf payfedbackCheckAndDoBusi(DoPayFedbackBusiReq fedbackInf, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
        
        Map<String, ? extends Object> formPramsMap = fedbackInf.getFormPramsMap();
        Object response = formPramsMap.get("response");
        Object appKeyObj = formPramsMap.get("customerIdentification"); // appKey 

        YibaodkCfgInf yibaoCfgInf = yibaoCfgService.getCfgInfByCfgId(ifcCfgInf);
        if( appKeyObj == null ) {
            throw new BaseServiceException("appKey不匹配");
        }
        if(response == null ) {
            throw new BaseServiceException("响应数据内容为空");
        }
        
        String responseStr = response.toString();

        // 获取指定APP_KEY的配置 
        PublicKey publicKey = yibaoCfgService.getPubKey(yibaoCfgInf); 
        PrivateKey privateKey = yibaoCfgService.getPrivKey(yibaoCfgInf);
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO() ;
        dto.setCipherText(responseStr);
        DigitalEnvelopeDTO decrypt = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
        
        BkPayRecordRsp payRecordRsp = JSONObject.parseObject( decrypt.getPlainText() ,BkPayRecordRsp.class);
        // 转换为 PayRecord 同时调用更新支付状态.
        DtPctUserPayRcd upr = new DtPctUserPayRcd(); 
//        upr.setUprId(uprId);
        upr.setUprCode(payRecordRsp.getRequestno()); 
        UserPayRecordInf newUpr = checkAndUpdatePayStatus(upr, ifcCfgInf);
        return newUpr ;
    }


    /**
     * 添加监控
     * 
     * @param upr 支付记录
     * @param ifcCfgInf 配置信息
     * @param fstWaiteSecs 首次等待时间秒数
     * @param maxSecs 过程最大时间秒数
     */
    private void checkAndAddMonitorThread(final DtPctUserPayRcd upr, final DtPctIfcChnlCfg ifcCfgInf, final Integer fstWaiteSecs, final Integer maxSecs ) {
        if (upr == null) {
            logger.warn("添加监控时由于支付记录入参信息为空导致监控失败");
            return;
        }
        Integer chanlIdVal = upr.getIfcChnlId();
        UserPayIfcChnlEnum repayChanelEnum = UserPayIfcChnlEnum.getByCode(chanlIdVal);
        Integer payStepStatus = upr.getStepStatus();
        if (!(UserPayStepStatusEnum.WAITE4NTF.getCode() == payStepStatus)) {
            // 不需要监听
            return;
        }
        final String logPre = "执行对支付记录(uprId=" + upr.getUprId() + ")指定渠道(" + repayChanelEnum + ")核对渠道并通知结果-->";
        // 易宝需要主动监听
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
                        UserPayRecordInf checkRst = checkAndUpdatePayStatus(upr, ifcCfgInf);
                        UserPayStepStatusEnum stepStatus = UserPayStepStatusEnum.getByCode(checkRst.getPayStepStatus());
                        logger.info(logPre + "执行结果:" + stepStatus);
                        if (!UserPayStepStatusEnum.WAITE4NTF.equals(stepStatus)) {
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
        PaycenterThreadPools.userpayMntThreadPool.submit(mntThread);
        logger.debug(logPre + "易宝代扣线程池添加了一个线程.");
    }


    /**
     * 渠道用户开户
     */
    @Override
    public UserAccRecordInf openAccount(DoCreateUsrAccReq usrAccInf, DtPctIfcChnlCfg ifcCfgInf ) throws BaseServiceException {
    	
        Long userId = usrAccInf.getUserId();
        Integer ifcChnlId = ifcCfgInf.getIfcChnlId();
        Long ifcChnlCfgId = ifcCfgInf.getIccId();
        String bindCardno = usrAccInf.getBindCardno();// 银行卡号
        String idcardno = usrAccInf.getUserIdcard();// 身份证号码
        String username = usrAccInf.getUserFullName();// 用户姓名
        String phone = usrAccInf.getUserPhone();// 银行卡绑定号码
        String bankCode = usrAccInf.getBankCode(); // 银行编号 
        String bankName = usrAccInf.getBankName(); // 银行名称 
        
        // 二次验证
        if(!bindCardno.matches(RegexUtils.REGEXSTR_LONG)) {
        	 throw new BaseServiceException(com.sdhoo.common.SebaseConstants.ERROR_CODE_BUSI_MINI, "银行卡号不正确");
        }

        YibaodkCfgInf yibaoCfgInf = yibaodkCfgService.getCfgInfByCfgId(ifcCfgInf);
        String logPre = "易宝渠道开户接口-->(time="+System.currentTimeMillis()+")-->"; 
        if(yibaoCfgInf == null ) {
            logger.warn(logPre + "未找到易宝开户配置,usrAccInf="+JSONObject.toJSONString(usrAccInf));
            throw new BaseServiceException(com.sdhoo.common.SebaseConstants.ERROR_CODE_BUSI_MINI, "未开通易宝指定渠道("+ ifcChnlCfgId + ")");
        }

        Long cfgId = yibaoCfgInf.getCfgId();// 接口渠道配置ID
        String ifcChnlName = yibaoCfgInf.getIfcChnlName();

        if(StringUtils.isEmpty(bankCode) && StringUtils.isEmpty(bankName)) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL,"银行编号或银行名称无效");
        }

        Boolean isupdate = false; // 是否有更新数据 
        DtPctIfcChnlUserAcc bankRelIcuaInf = null ; // 当前银行卡号对应的开户信息
        DtPctIfcChnlUserAcc curUnsuccIcuaInf = null ; // 当前未成功绑定的开户信息
        DtPctIfcChnlUserExtra curUnsuccIcueInf = null ; // 当前未成功开户的扩展信息. 

        // 获取当前渠道下用户的所有开户信息
        Map<String,Object> userAccQmap = new HashMap<>();
        userAccQmap.put("ifcChnlId", ifcChnlId);
        userAccQmap.put("userId", userId);
        userAccQmap.put("ifcChnlCfgId", cfgId);
        Date currTime = new Date();
        int userMaxact = 500;
        Map<Long,DtPctIfcChnlUserAcc> icuaDataMap = new HashMap<>(8);
        List<DtPctIfcChnlUserAcc> userIcuaInfList = pctIfcChnlUserAccService.selectByCriteria(userAccQmap, 0, userMaxact);
        List<Long> icuaId_inList = new ArrayList<>();
        for (DtPctIfcChnlUserAcc tmp_icuaInf : userIcuaInfList) {
        	icuaDataMap.put(tmp_icuaInf.getIcuaId(), tmp_icuaInf);
        	icuaId_inList.add(tmp_icuaInf.getIcuaId());
        	Integer tmp_stepStatus = tmp_icuaInf.getStepStatus();
        	UserAccStepStatusEnum tmp_stepStatusEnum = UserAccStepStatusEnum.getByCode(tmp_stepStatus);
        	if( !UserAccStepStatusEnum.SUCCESS.equals(tmp_stepStatusEnum)) {
        		if(curUnsuccIcuaInf != null ) {
        			logger.warn(logPre+"指定渠道配置("+ifcChnlCfgId+")对应的用户有两个或以上未开户成功的账号,此次取最后一个为基准..");
        		}
        		// 当前未绑定成功的银行卡
        		curUnsuccIcuaInf = tmp_icuaInf;
        	}
		}
        Map<Long,DtPctIfcChnlUserExtra> icueDataMap = new HashMap<>(icuaId_inList.size());
        if(icuaId_inList.size() > 0 ) {
        	Map<String,Object> icueQmap = new HashMap<>(2);
        	icueQmap.put("icueId_inList", icuaId_inList); 
        	List<DtPctIfcChnlUserExtra> icueList = pctIfcChnlUserExtraService.selectByCriteria(icueQmap, 0, icuaId_inList.size()); 
        	for (DtPctIfcChnlUserExtra tmpIcue : icueList) {
        		String tmp_bankCardNo = tmpIcue.getBankCardNo();
        		Long tmp_icueId = tmpIcue.getIcueId();
        		icueDataMap.put(tmp_icueId, tmpIcue);
        		if(bankRelIcuaInf == null && tmp_bankCardNo != null && tmp_bankCardNo.equals(bindCardno)) { // 匹配当前绑卡的开户信息
        			bankRelIcuaInf = icuaDataMap.get(tmp_icueId);
        		}
        	}
        }

        // 同一张银行卡成功开过户的不允许重复开户.
        if(bankRelIcuaInf != null ) {
        	Integer stepStatus = bankRelIcuaInf.getStepStatus();
        	UserAccStepStatusEnum currAccStepEnum = UserAccStepStatusEnum.getByCode(stepStatus);
        	if(UserAccStepStatusEnum.SUCCESS.equals(currAccStepEnum)) {
        		throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_MINI,"不能重复绑定同一张银行卡");
        	}
        }
        if(curUnsuccIcuaInf != null ){
        	curUnsuccIcueInf = icueDataMap.get(curUnsuccIcuaInf.getIcuaId());
        }

        String dftEmpty = "";
        Long icuaId = null;
//        boolean isOpened = false; //是否开户过

        // 当前有未开户成功账户,则使用当前未开户成功的账户并变更银行卡信息后继续开户 
        if(curUnsuccIcuaInf != null ) {
        	// 已有开户数据,则更新数据 
            icuaId = curUnsuccIcuaInf.getIcuaId();
            // 姓名不符合,或者身份证号不相同,或者银行卡号不相同,或者电话号码不相同,则更新开户信息
            if (!username.equals(curUnsuccIcueInf.getUserFullname()) || !idcardno.equals(curUnsuccIcueInf.getIdcardNo()) || !bindCardno.equals(curUnsuccIcueInf.getBankCardNo()) || !phone.equals(curUnsuccIcueInf.getBankCardMobile())) {
                curUnsuccIcueInf = new DtPctIfcChnlUserExtra();
                curUnsuccIcueInf.setIcueId(icuaId);
                curUnsuccIcueInf.setUserFullname(username);
                curUnsuccIcueInf.setIdcardNo(idcardno);
                curUnsuccIcueInf.setBankCardNo(bindCardno);
                curUnsuccIcueInf.setBankCardMobile(phone);
                curUnsuccIcueInf.setYbChangeCode(dftEmpty);
                curUnsuccIcueInf.setIfcChnlStatus(dftEmpty);
                curUnsuccIcueInf.setIfcRequestno(dftEmpty);
                curUnsuccIcueInf.setBankCode(bankCode);
                curUnsuccIcueInf.setBankName(bankName);
                curUnsuccIcueInf.setIfcChnlStatusMemo("重新绑卡");
                curUnsuccIcueInf.setIcueCtime(currTime);
                curUnsuccIcueInf.setIcueMtime(currTime);
                pctIfcChnlUserExtraService.updateByPrimKeySelective(curUnsuccIcueInf);

                curUnsuccIcuaInf = new DtPctIfcChnlUserAcc();
                curUnsuccIcuaInf.setIcuaId(icuaId);
                curUnsuccIcuaInf.setStepStatus(UserAccStepStatusEnum.CREATE.getCode()); // 状态变更为创建状态 
                curUnsuccIcuaInf.setIcuaMtime(currTime);
                pctIfcChnlUserAccService.updateByPrimKeySelective(curUnsuccIcuaInf);
                isupdate = true;
            }
        }else {
        	// 未有开户数据,则新建开户数据 
        	// 没有数据,则新录入一条数据
        	curUnsuccIcuaInf = new DtPctIfcChnlUserAcc();
        	PctIdData pctiddata = sequenceGenService.genAndCachePayctIdData();
        	icuaId=pctiddata.getIdVal();
        	curUnsuccIcuaInf.setIcuaId(icuaId);
        	curUnsuccIcuaInf.setIfcChnlId(ifcChnlId); 
        	curUnsuccIcuaInf.setIfcChnlCfgId(ifcChnlCfgId);
        	curUnsuccIcuaInf.setIfcChnlName(ifcChnlName);
        	curUnsuccIcuaInf.setUserId(userId);
        	curUnsuccIcuaInf.setOpenId(dftEmpty);
        	curUnsuccIcuaInf.setStepStatus(UserAccStepStatusEnum.CREATE.getCode()); 
        	curUnsuccIcuaInf.setIcuaCtime(currTime);
        	curUnsuccIcuaInf.setIcuaMtime(currTime);
        	pctIfcChnlUserAccService.insertSelective(curUnsuccIcuaInf); 
        	
        	// 附表:
        	String requestno = yibaoClientService.generateTransSerialNo();
        	curUnsuccIcueInf = new DtPctIfcChnlUserExtra();
        	curUnsuccIcueInf.setIcueId(icuaId);
        	
        	// 渠道请求号默认空
        	curUnsuccIcueInf.setIfcRequestno( requestno );
        	curUnsuccIcueInf.setUserFullname( usrAccInf.getUserFullName() );
        	curUnsuccIcueInf.setIdcardNo( usrAccInf.getUserIdcard() );
        	curUnsuccIcueInf.setBankCode( bankCode);
        	curUnsuccIcueInf.setBankName(bankName); 
        	curUnsuccIcueInf.setBankCardNo( usrAccInf.getBindCardno() );
        	curUnsuccIcueInf.setBankCardMobile( usrAccInf.getUserPhone()  );
        	curUnsuccIcueInf.setYbChangeCode( dftEmpty );
        	curUnsuccIcueInf.setIfcChnlStatus( dftEmpty );
        	curUnsuccIcueInf.setIfcChnlStatusMemo("数据创建" );
        	curUnsuccIcueInf.setIcueCtime( currTime );
        	curUnsuccIcueInf.setIcueMtime( currTime );
        	pctIfcChnlUserExtraService.insertSelective(curUnsuccIcueInf);

            isupdate = true;
        }

        Integer stepStatus = curUnsuccIcuaInf.getStepStatus();
        // 当前枚举 
        UserAccStepStatusEnum curAccStepStatusEnum = UserAccStepStatusEnum.getByCode(stepStatus); 

        // 是否需要调用渠道接口开户
        boolean isNeedToReqIfcChnl = ( UserAccStepStatusEnum.EXCEPTION.equals(curAccStepStatusEnum) || UserAccStepStatusEnum.FAIL.equals(curAccStepStatusEnum)|| UserAccStepStatusEnum.CREATE.equals(curAccStepStatusEnum) );
        if ( isNeedToReqIfcChnl  ) {

            JqBkReq req = new JqBkReq();
            // 创建交易号
            String requestno = yibaoClientService.generateTransSerialNo();
//            String requestno = currUserExtra.getIfcRequestno();
            // 用户标识类型
            String identitytype = BindcardIdentityTypeEnum.USER_ID.getCode();
            // 用户标识ID
            String identityid = openUserIdPre + userId;
            // 身份类型,身份证
            String idcardtype = BindcardIdCardTypeEnum.ID.getCode(); // "ID"
            // 是否短信校验
            Boolean issms = true;
            if(usrAccInf.getIssms()!=null) {
                issms = usrAccInf.getIssms(); 
            }
            // 请求时间
            Date date = new Date();
            String requesttime = WebDataProcUtils.defaultDateTimeFormat.format(date);
            // 鉴权类型
            String authtype = BindcardAuthtypeEnum.COMMON_FOUR.getCode();

            req.setRequestno(requestno);// 绑卡请求号
            req.setIdentityid(identityid);// 用户标识
            req.setIdentitytype(identitytype);// 用户标识类型
            req.setCardno(bindCardno);// 卡号.
            req.setIdcardno(idcardno);// 证件号.
            req.setIdcardtype(idcardtype);// 证件类型.
            req.setUsername(username);// 用户姓名
            req.setPhone(phone);// 手机号.
            req.setIssms(issms);// 是否发送短信.
            req.setRequesttime(requesttime);// 请求时间(格式yyyy-MM-dd HH:mm:ss)
            req.setAuthtype(authtype);
            JqBkRsp rsp = yibaoClientService.doExecuteBaseReq(req, yibaoCfgInf);
            String status = rsp.getStatus();
            String yborderid = rsp.getYborderid();// 易宝开户交易号
            String errormsg = rsp.getErrormsg();
            BindcardStatusEnum bindStatusEnum = BindcardStatusEnum.getByCode(status);

            // 最新状态不为空,更新数据. 
            if( bindStatusEnum != null ) {
                UserAccStepStatusEnum newStatus = null ; 
				switch (bindStatusEnum) {
				case BIND_ERROR: {
					newStatus = UserAccStepStatusEnum.FAIL;
				}
					break;
				case BIND_FAIL: {
					newStatus = UserAccStepStatusEnum.FAIL;
				}
					break;
				case TIME_OUT: {
					newStatus = UserAccStepStatusEnum.FAIL;
				}
					break;
				case FAIL: {
					newStatus = UserAccStepStatusEnum.FAIL;
				}
					break;
				case TO_VALIDATE: {
					newStatus = UserAccStepStatusEnum.WAITE_FOR_VERIFYMSG;
				}
					break;
				case BIND_SUCCESS: {
					newStatus = UserAccStepStatusEnum.SUCCESS;
				}
					break;
				default:
					newStatus = UserAccStepStatusEnum.EXCEPTION;
					break;
				}
                // 更新状态,更新渠道信息,
                DtPctIfcChnlUserAcc updateUserAcc = new DtPctIfcChnlUserAcc();
                updateUserAcc.setIcuaId(icuaId); 
                updateUserAcc.setStepStatus(newStatus.getCode());
                updateUserAcc.setOpenId(identityid); 
                updateUserAcc.setIcuaMtime(currTime); 
                pctIfcChnlUserAccService.updateByPrimKeySelective(updateUserAcc); 

                DtPctIfcChnlUserExtra updateExtra = new DtPctIfcChnlUserExtra();
                updateExtra.setIcueId(icuaId); 
                updateExtra.setIfcRequestno(requestno);
                updateExtra.setUserFullname(usrAccInf.getUserFullName());
                updateExtra.setIdcardNo(usrAccInf.getUserIdcard());
                updateExtra.setBankCode(bankCode);
                updateExtra.setBankCardNo(usrAccInf.getBindCardno());
                updateExtra.setBankCardMobile(usrAccInf.getUserPhone());
                updateExtra.setYbChangeCode(yborderid);
                updateExtra.setIfcChnlStatus(status);
                if(StringUtils.isNotEmpty(errormsg) && !BindcardStatusEnum.BIND_SUCCESS.equals(bindStatusEnum) ) {
                    updateExtra.setIfcChnlStatusMemo(errormsg);
                }else {
                    updateExtra.setIfcChnlStatusMemo(bindStatusEnum.getName());
                }
                updateExtra.setIcueMtime(currTime);
                pctIfcChnlUserExtraService.updateByPrimKeySelective(updateExtra);
                isupdate = true ;
            }
        }
        if(isupdate) {
            curUnsuccIcuaInf = pctIfcChnlUserAccService.getByPrimKey(icuaId, null);
            curUnsuccIcueInf = pctIfcChnlUserExtraService.getByPrimKey(icuaId, null);
        }
        UserAccRecordInf rtVal = paycenterBeanProcService.genUserAccRecordByModel(curUnsuccIcuaInf , curUnsuccIcueInf); 

        return rtVal;
    }

//    /**
//     * 查询开户信息
//     */
//    @Override
//    public List<UserAccRecordInf> queryOpenAccount(DoGetUsrAccRecordInfReq req) throws BaseServiceException {
//        List<UserAccRecordInf> rtList= new ArrayList<>();
//        
//        Long shId = req.getShId();// 商户ID
//        // Integer chnlId = req.getIfcChnlId();// 渠道ID
//        Long userId = req.getUserId();
//        UserAccRecordInf uinfo = null;
//       
//        Map<String, Object> pyuQmap = new HashMap<>();
//        Long ifcChnlCfgId = req.getIfcChnlCfgId();
//
//        YibaoCfgInf yibaoCfgInf = null;
//        if( ifcChnlCfgId != null && ifcChnlCfgId > 0  ) {
//            yibaoCfgInf = yibaoCfgService.getCfgInfByCfgId(ifcChnlCfgId); 
//        }else {
//            yibaoCfgInf =  yibaoCfgService.getCfgInfByShid(shId);
//        }
//        if(yibaoCfgInf == null) {
//            logger.warn("指定易宝配置无效,req="+JSONObject.toJSONString(req));
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN,"指定易宝配置无效");
//        }
//
//        pyuQmap.put("ifcChnlCfgId", yibaoCfgInf.getCfgId());
//        pyuQmap.put("userId", userId);
//        // 查询主卡 
////        pyuQmap.put("mainAccStatus", UserAccMainStatusEnum.MAIN.getCode()); 
//        List<PctIfcChnlUserAcc> pyuList = pctIfcChnlUserAccService.selectByCriteria(pyuQmap, 0, Integer.MAX_VALUE);
//        if (pyuList != null && pyuList.size() > 0) {
////            pctIfcChnlUserAcc = pyuList.get(0);
//            for (PctIfcChnlUserAcc tmpbean : pyuList) {
//                PctIfcChnlUserExtra userExtra = pctIfcChnlUserExtraService.getByPrimKey(tmpbean.getIcuaId(), null);
//                String bankCode = userExtra.getBankCode();
//                Map<String,Object> bankQmap = new HashMap<>();
//                bankQmap.put("biCode", bankCode);
//                List<PctBankInfo> bankInfs = pctBankInfoService.selectByCriteria(bankQmap, 0, 2);
//                if(bankInfs.size() != 1 ) {
//                    throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "银行编号"+bankCode+"对应银行信息无效");
//                }
//                PctBankInfo bkInfo = bankInfs.get(0);
//                uinfo = paycenterBeanProcService.genUserAccRecordByModel(tmpbean, userExtra); 
//                uinfo.setBankName(bkInfo.getBiName());
//                uinfo.setBankCode(bankCode);
//                uinfo.setPayChnlName(yibaoCfgInf.getIfcChnlName());
//                uinfo.setShId(shId);
//                rtList.add(uinfo);
//            }
//        }             
//        return rtList;
//    }

    /**
     * 绑卡短验确认
     */
    @Override
    public UserAccRecordInf toBindCheck(DoCreateUsrAccVerifyCheckReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
        UserAccRecordInf uinfo = new UserAccRecordInf();
        Long icuaId = req.getIcuaId();
        Long ifcChnlCfgId = req.getIfcChnlCfgId();
        Integer ifcChnlId = req.getIfcChnlId();
        Long userId = req.getUserId();

        YibaodkCfgInf yibaoCfgInf =  yibaoCfgService.getCfgInfByCfgId(ifcCfgInf);
        if(yibaoCfgInf == null) {
            logger.warn("指定易宝配置无效,req="+JSONObject.toJSONString(req));
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN,"指定易宝配置无效");
        }
        DtPctIfcChnlUserAcc userAcc = pctIfcChnlUserAccService.getByPrimKey(icuaId, null);
        DtPctIfcChnlUserExtra userExtra = pctIfcChnlUserExtraService.getByPrimKey(icuaId, null);
        
        if (userAcc.getStepStatus() != UserAccStepStatusEnum.WAITE_FOR_VERIFYMSG.getCode()) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "非待短验状态，不允许绑卡");
        }
        BeanProcUtils.copyProperties(userAcc, uinfo, true);
        BeanProcUtils.copyProperties(userExtra, uinfo, true);
        
        String bankCode = userExtra.getBankCode();
        Map<String,Object> bankQmap = new HashMap<>();
        bankQmap.put("biCode", bankCode);
        List<DtPctBankInfo> bankInfs = pctBankInfoService.selectByCriteria(bankQmap, 0, 2);
        if(bankInfs.size() != 1 ) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "银行编号"+bankCode+"对应银行信息无效");
        }
        DtPctBankInfo bkInfo = bankInfs.get(0);
        uinfo.setBankName(bkInfo.getBiName());

        String requestno = userExtra.getIfcRequestno();
        String validatecode = req.getVerifyCode();

        JqBkSmsValiReq cfrmReq = new JqBkSmsValiReq();
        cfrmReq.setRequestno(requestno);
        cfrmReq.setValidatecode(validatecode);
        JqBkSmsValiRsp rsp = yibaoClientService.doExecuteBaseReq(cfrmReq, yibaoCfgInf);
        logger.debug("易宝绑卡短验确认返回结果"+JSONObject.toJSONString(rsp));
        String status = rsp.getStatus();
        String errMsg = rsp.getErrormsg();
        BindcardStatusEnum statusEnum = BindcardStatusEnum.getByCode(status);
        Integer newStepStatus=null;
        String memo=null;
        Boolean isSuccess = false;
        if (status != null) {
            switch (statusEnum) {
            case BIND_SUCCESS: {// 绑卡成功
                newStepStatus = UserAccStepStatusEnum.SUCCESS.getCode();// 开户成功
                memo=BindcardStatusEnum.BIND_SUCCESS.getName();
                isSuccess = true;
            }
                break;
            case TO_VALIDATE: {// 待短验
                newStepStatus = UserAccStepStatusEnum.WAITE_FOR_VERIFYMSG.getCode();
                memo=errMsg;
            }
            break;
            case BIND_FAIL: {// 绑卡失败
                newStepStatus = UserAccStepStatusEnum.FAIL.getCode();// 开户失败
                memo=errMsg;
            }
            break;
            case TIME_OUT: {// 
                newStepStatus = UserAccStepStatusEnum.EXCEPTION.getCode();//
                memo=errMsg;
            }
            break;
            case BIND_ERROR: {// 
                newStepStatus = UserAccStepStatusEnum.EXCEPTION.getCode();//
                memo=errMsg;
            }
            break;
            case FAIL: {// 
                newStepStatus = UserAccStepStatusEnum.EXCEPTION.getCode();//
                memo=errMsg;
            }
            break;
            default:{
                newStepStatus = UserAccStepStatusEnum.EXCEPTION.getCode();//
                memo=errMsg;
            }
                break;
            }
        }
        uinfo.setAccStepStatus(newStepStatus);
        uinfo.setAccStepStatusMemo(memo);
        // 更新绑卡信息
        DtPctIfcChnlUserAcc userAccUp = new DtPctIfcChnlUserAcc();
        userAccUp.setIcuaId(icuaId);
        userAccUp.setStepStatus(uinfo.getAccStepStatus());
        if(isSuccess) {
            //升级主卡
            userAccUp.setMainAccStatus(UserAccMainStatusEnum.MAIN.getCode());
        }   
        userAccUp.setIcuaMtime(new Date());
        pctIfcChnlUserAccService.updateByPrimKeySelective(userAccUp);

        DtPctIfcChnlUserExtra userExtraUp=new DtPctIfcChnlUserExtra();
        userExtraUp.setIcueId(icuaId);
        userExtraUp.setIfcChnlStatus(statusEnum.getCode());
        userExtraUp.setIfcChnlStatusMemo(uinfo.getAccStepStatusMemo());
        userExtraUp.setIcueMtime(new Date());
        
        pctIfcChnlUserExtraService.updateByPrimKeySelective(userExtraUp);

        // 降为副卡操作
        Map<String, Object> userAccQmap = new HashMap<>();
        userAccQmap.put("ifcChnlId", ifcChnlId);
        userAccQmap.put("userId", userId);
        userAccQmap.put("ifcChnlCfgId", ifcChnlCfgId);
        List<DtPctIfcChnlUserAcc> userAccList = pctIfcChnlUserAccService.selectByCriteria(userAccQmap, 0, Integer.MAX_VALUE);
        if (userAccList != null && userAccList.size() > 1) {
            for (DtPctIfcChnlUserAcc tmpbean : userAccList) {
                if (!tmpbean.getIcuaId().equals(icuaId)) {
                    DtPctIfcChnlUserAcc accDownBean = new DtPctIfcChnlUserAcc();
                    accDownBean.setIcuaId(tmpbean.getIcuaId());
                    accDownBean.setMainAccStatus(UserAccMainStatusEnum.SUBACC.getCode());
                    accDownBean.setIcuaMtime(new Date());
                    pctIfcChnlUserAccService.updateByPrimKeySelective(accDownBean);
                }
            }
        }
        
        uinfo.setPayChnlName(yibaoCfgInf.getIfcChnlName());
        return uinfo;
    }
    
    /**
     * 绑卡短验重发
     */
    @Override
    public UserAccRecordInf toCreateUserAccResendVerify(DoCreateUsrAccResendVerifyReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
        UserAccRecordInf uinfo = new UserAccRecordInf();
        Long icuaId = req.getIcuaId();

        YibaodkCfgInf cfg = yibaoCfgService.getCfgInfByCfgId(ifcCfgInf);
        
        DtPctIfcChnlUserAcc userAcc = pctIfcChnlUserAccService.getByPrimKey(icuaId, null);
        DtPctIfcChnlUserExtra userExtra = pctIfcChnlUserExtraService.getByPrimKey(icuaId, null);
        
        //状态必须是待短验
        if (userAcc.getStepStatus() != UserAccStepStatusEnum.WAITE_FOR_VERIFYMSG.getCode()) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "非待短验状态，不允许短信重发");
        }
        userExtra = pctIfcChnlUserExtraService.getByPrimKey(userAcc.getIcuaId(), null);
        BeanProcUtils.copyProperties(userAcc, uinfo, true);
        BeanProcUtils.copyProperties(userExtra, uinfo, true);
        String bankCode = userExtra.getBankCode();
        Map<String,Object> bankQmap = new HashMap<>();
        bankQmap.put("biCode", bankCode);
        List<DtPctBankInfo> bankInfs = pctBankInfoService.selectByCriteria(bankQmap, 0, 2);
        if(bankInfs.size() != 1 ) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "银行编号"+bankCode+"对应银行信息无效");
        }
        DtPctBankInfo bkInfo = bankInfs.get(0);

        uinfo.setAccStepStatus(userAcc.getStepStatus());
        uinfo.setBankName(bkInfo.getBiName());
        String requestno = userExtra.getIfcRequestno();
        JqBkSmsValiAgainReq cfrmReq = new JqBkSmsValiAgainReq();
        cfrmReq.setRequestno(requestno);
        JqBkSmsValiAgainRsp rsp = yibaoClientService.doExecuteBaseReq(cfrmReq, cfg);
        logger.debug("易宝绑卡短验重发返回结果："+JSONObject.toJSONString(rsp));
        BindcardStatusEnum statusEnum = BindcardStatusEnum.getByCode(rsp.getStatus());
        String status=rsp.getStatus();
        String errMsg=rsp.getErrormsg();
        Integer newStepStatus=null;
        String memo=null;
       
        if (status != null) {
            switch (statusEnum) {
            case TO_VALIDATE: {// 待短验
                newStepStatus = UserAccStepStatusEnum.WAITE_FOR_VERIFYMSG.getCode();
                memo=errMsg;
            }
            break;
            case TIME_OUT: { // 绑卡超时 
                newStepStatus = UserAccStepStatusEnum.FAIL.getCode(); 
                memo=errMsg;
            }
            break;
            case BIND_ERROR: {// 绑卡错误 
                newStepStatus = UserAccStepStatusEnum.FAIL.getCode(); 
                memo=errMsg;
            }
            break;
            case FAIL: {// 失败
                newStepStatus = UserAccStepStatusEnum.FAIL.getCode(); 
                memo=errMsg;
            }
            break;
            default:{
                newStepStatus = UserAccStepStatusEnum.EXCEPTION.getCode();
                memo=errMsg;
            }
                break;
            }
        }
        
        uinfo.setUarId(icuaId);
        uinfo.setAccStepStatus(newStepStatus);
        uinfo.setAccStepStatusMemo(memo);
        uinfo.setPayChnlName(cfg.getIfcChnlName());
        return uinfo;
    }
    
    
    /* ************************ */

    /**
     * 虚拟交易
     * @param cfrmReq
     * @param yibaoCfgInf
     * @return
     */
    private BkPayRsp simIfcPayCreate(BkPayReq cfrmReq, YibaodkCfgInf yibaoCfgInf) {
        String yborderid = cfrmReq.getRequestno() + "_SIM";
 
        BkPayRsp rsp = new BkPayRsp();
        rsp.setAmount(cfrmReq.getAmount());
        rsp.setIssms(cfrmReq.getIssms().toString()); 
        rsp.setMerchantno(yibaoCfgInf.getMerchantAccount());
        rsp.setRequestno(cfrmReq.getRequestno());
        rsp.setStatus(YibaoPayStatusEnum.PROCESSING.getCode());
        // 虚拟订单ID为
        rsp.setYborderid( yborderid);
        rsp.setRemark(cfrmReq.getRemark()); 
        return rsp;
    }

    /**
     * 虚拟获取交易状态
     * @param cfrmReq
     * @param cfgInfByShid
     * @return
     */
    private BkPayRecordRsp simGetPayRstInf(BkPayRecordReq cfrmReq, YibaodkCfgInf cfgInfByShid) {

        // 请求交易号
        String requestno = cfrmReq.getRequestno();
        
        DtPctUserPayRcd pctUserPayRcd = null ;
        {
            Map<String,Object> qmap = new HashMap<>();
            qmap.put("uprCode", requestno); 
            List<DtPctUserPayRcd> uprInfList = pctUserPayRcdService.selectByCriteria(qmap, 0, 2);
            if(uprInfList.size() == 1 ) {
                pctUserPayRcd = uprInfList.get(0);
            }
        }
        Date currTime = new Date();
        SimpleDateFormat sdfmt = TimeCalcUtils.getThreadLocalDateFormat("yyyy-MM-dd HH:mm:ss"); 
        String banksuccessdate = sdfmt.format(currTime);

        // 支付错误枚举,默认空(成功)
        YibaoPayErrorCodeEnum payErrCodeEnum = null;
        // 支付状态,默认成功
        YibaoPayStatusEnum payStatusEnum = YibaoPayStatusEnum.PAY_SUCCESS;
        String yborderid = cfrmReq.getRequestno() + "_SIM";

        {
            // 根据支付ID虚拟构建支付状态
            String tailpayCode = requestno.substring(requestno.length()-1);
            int tailNum = 0; 
            if(!tailpayCode.matches(RegexConstants.regexStr_long)) {
                tailpayCode = requestno.substring(( requestno.length()-3),(requestno.length()-2));
            }
            tailNum = Integer.parseInt(tailpayCode);
            // 交易号余2==0,则识别为余额不足.
            if(tailNum%2 == 0 ) {
                payStatusEnum = YibaoPayStatusEnum.PAY_FAIL;
                payErrCodeEnum = YibaoPayErrorCodeEnum.DK8010011;
            }
        }

        BkPayRecordRsp rsp = new BkPayRecordRsp();
        rsp.setAmount(pctUserPayRcd.getPayRmbamt().toString());
        rsp.setBankcode("CIB");
//        rsp.setCardlast(cardlast);
//        rsp.setCardtop(cardtop);
        rsp.setBanksuccessdate(banksuccessdate);
        if(payErrCodeEnum != null ) {
            rsp.setErrorcode(payErrCodeEnum.getCode());
            rsp.setErrormsg(payErrCodeEnum.getName());
        }
        rsp.setMerchantno(cfgInfByShid.getMerchantAccount());
        rsp.setPaytype("绑卡支付"); 
        rsp.setRequestno(requestno);
        rsp.setStatus(payStatusEnum.getCode());
        rsp.setYborderid(yborderid);

        return rsp;
    }



}
