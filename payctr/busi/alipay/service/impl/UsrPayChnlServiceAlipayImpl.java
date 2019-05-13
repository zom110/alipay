package com.sdhoo.pdloan.payctr.busi.alipay.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.common.base.util.TimeCalcUtils;
import com.sdhoo.common.base.util.WebOptUtils;
import com.sdhoo.common.base.util.WebOptUtils.WebRequest;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctOutNotifyRcd;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayExtraService;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayRcdService;
import com.sdhoo.pdloan.payctr.PaycenterConstants;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.busi.alipay.service.AlipayClientService;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req.BkPayRecordReq;
import com.sdhoo.pdloan.payctr.dto.UserAccRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserPayDftCfgInf;
import com.sdhoo.pdloan.payctr.dto.UserPayRecordInf;
import com.sdhoo.pdloan.payctr.enums.UserPayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayStepStatusEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayTypeEnum;
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
 * 支付宝渠道支付实现
 *
 * @author SDPC_LIU
 */
@Service(value = (PaycenterConstants.USR_PAYCHNL_SERVICENAME_PRE + PaycenterConstants.USERPAY_CHNL_ALIPAY))
public class UsrPayChnlServiceAlipayImpl implements UserPayChnlService {

    private static final Logger logger = LoggerFactory.getLogger(UsrPayChnlServiceAlipayImpl.class);

    /**
     * 当前支付渠道(支付宝).
     */
    private UserPayIfcChnlEnum payIfcChnlEnum = UserPayIfcChnlEnum.ALIPAY;

    @Autowired
    private PctIdGenService sequenceGenService;

    @Autowired
    private DtPctUserPayRcdService pctUserPayRcdService;

    @Autowired
    private DtPctUserPayExtraService pctUserPayExtraService;

    @Autowired
    private PayctrBeanProcService paycenterBeanProcService;

    @Resource
    private AlipayClientService alipayClientService;

    /**
     * 支付中心操作限制服务.
     */
    @Autowired
    private PayctrOptlmtService paycenterOptlmtService;

    @Autowired
    private IfcChnlCfgService ifcChnlCfgService;

    @Autowired
    private PayctrOutNotifyService payctrOutNotifyService;

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
        Long dftCfgId = userPayDftCfgInf.getAlipayChnlCfgId();
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
        // 支付宝默认验证通过
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

        AlipayClient alipayClient = alipayClientService.getAlipayClientByCfg(ifcCfgInf);
        UserPayTypeEnum curUserPayType = UserPayTypeEnum.getByCode(payType);

        if (!UserPayTypeEnum.APP.equals(curUserPayType)) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付宝当前只支持APP支付");
        }

        String logPre = "用户支付[支付宝渠道]-->发起支付创建-->";

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
        Double uprPayRmbamt = new BigDecimal(payamtYuan).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); // 交易金额  ;
        // 接口渠道ID
        Integer uprPaychanlId = payIfcChnlEnum.getCode();
        // 接口渠道名称(例:支付宝即时到账,微信公众号支付等)
        String uprPaychanlName = chnlName; // currPayChanelEnum.getDesc();
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
        UserPayStepStatusEnum new_userPayStepEnum = null;
        // 渠道返回编号
        String channelRstCode = null;
        // 渠道响应信息
        String channelRstMsg = null;
        // 渠道反馈JSON内容
        String channelRstInfo = null;
        // 接口交易序列号
        String chnlTransNo = "";
        // 渠道响应时间
        Date chnlFebTime = null;

        // 支付准备信息
        String prepayJsn = null;

        // 发起渠道请求.

        Date ifcChnlExpiretime = null;
        if (UserPayTypeEnum.APP.equals(curUserPayType)) { // app支付

            AlipayTradeAppPayRequest tradeAppPayReq = new AlipayTradeAppPayRequest();
            String subject; // 交易标题
            String outTradeNo; // 交易号
            String totalAmount; // 交易金额
            String productCode; // 产品编号
            String body; // 交易描述
            String timeExpire;
            String notifyUrl;

            {
                productCode = "QUICK_MSECURITY_PAY";
                notifyUrl = ifcCfgInf.getNotifyUrl();
                Calendar timeExp = Calendar.getInstance();
                timeExp.add(Calendar.MINUTE, 5); // 支付宝主动还款过期时间默认5分钟
                ifcChnlExpiretime = timeExp.getTime();

                subject = upr.getPayTitle();
                body = uprMemo;
                outTradeNo = uprCode;
                totalAmount = moneyYuanFmt.format(upr.getPayRmbamt());
                timeExpire = expTimeFmt.format(ifcChnlExpiretime);
            }

            AlipayTradeAppPayModel bizModel = new AlipayTradeAppPayModel();
            bizModel.setBody(body);
            bizModel.setSubject(subject);
            bizModel.setOutTradeNo(outTradeNo);
            bizModel.setTotalAmount(totalAmount);
            bizModel.setProductCode(productCode);
            bizModel.setTimeExpire(timeExpire);
            tradeAppPayReq.setBizModel(bizModel);
            tradeAppPayReq.setNotifyUrl(notifyUrl);
            AlipayTradeAppPayResponse sdkExecRsp;
            try {
                sdkExecRsp = alipayClient.sdkExecute(tradeAppPayReq);
                if (sdkExecRsp != null) {
                    channelRstCode = sdkExecRsp.getCode();
                    channelRstMsg = sdkExecRsp.getMsg();
                    channelRstInfo = JSONObject.toJSONString(sdkExecRsp);
                    chnlFebTime = new Date();
                }
                if (sdkExecRsp.isSuccess()) {
                    // 创建成功, 返回交易号
                    chnlTransNo = sdkExecRsp.getTradeNo();
                    prepayJsn = sdkExecRsp.getBody();
                    new_userPayStepEnum = UserPayStepStatusEnum.WAITE4NTF; //
                }
            } catch (Exception e) {
                logger.error(logPre + "调用支付接口异常", e);
            }
        } else {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付类型未支持");
        }

        // 空,则置为异常
        if (new_userPayStepEnum == null) {
            logger.error(logPre + "调用过后检索到新的支付状态是空的,此处置为异常..");
            new_userPayStepEnum = UserPayStepStatusEnum.EXCEPTION;
        }
        Date ntime = new Date();
        // 数据更新
        DtPctUserPayRcd updateUpr = new DtPctUserPayRcd();
        // 支付ID
        updateUpr.setUprId(uprId);
        updateUpr.setStepStatus(new_userPayStepEnum.getCode());
        updateUpr.setIfcChnlFebtime(chnlFebTime);
        updateUpr.setIfcChnlExpiretime(ifcChnlExpiretime);
        updateUpr.setUprMtime(ntime);
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
        updateUpe.setUpeMtime(ntime);
        pctUserPayExtraService.updateByPrimKeySelective(updateUpe);
        if (updateCnt != 1) {
            logger.warn(logPre + "接口调用后更新支付状态时更新条数不等于1.");
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

        // 异步监听(支付宝不需要)
        // 数据返回
        return userPayInfo;
    }


    /**
     * 支付宝渠道没有被动通知支持.
     */
    @Override
    public UserPayRecordInf verifyAndParseNotifyParams(Map<String, ? extends Object> formParam) throws BaseServiceException {
        logger.warn("支付宝代扣渠道没有被动通知支持,但是方法被调用了..");
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
        String logPre = "支付宝代扣渠道代扣渠道核对并更新支付(uprId=" + uprId + ",payCode=" + payCode + ")状态-->";
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

            if (UserPayStepStatusEnum.WAITE4NTF.equals(currPayStepStatus) && checkRst) {
                UserPayRecordInf ifcChnlLstPayRcdInf = null;
                try {
                    ifcChnlLstPayRcdInf = getChnlIfcPayRecordInfByPayCode(currUpr.getUprCode(), ifcCfgInf);
                } catch (BaseServiceException e) {
                    logger.error(logPre + "渠道接口获取数据异常", e);
                }
                if (ifcChnlLstPayRcdInf != null) {
                    // 更新支付记录
                    Integer payStepStatus = ifcChnlLstPayRcdInf.getPayStepStatus();
                    if (payStepStatus != null && !currStepStatus.equals(payStepStatus)) {
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
                            if (StringUtils.isNotEmpty(uprNotifyUrl)) {
                                DtPctOutNotifyRcd ntfRcd = new DtPctOutNotifyRcd();
                                ntfRcd.setIfcChnlId(currUpr.getIfcChnlId());
                                ntfRcd.setIfcChnlCfgId(currUpr.getIfcChnlCfgId());
                                ntfRcd.setTradePayCode(currUpr.getUprCode());
                                ntfRcd.setTradeStepStatus("" + payStepStatus);
                                ntfRcd.setNotifyUrl(uprNotifyUrl);
                                payctrOutNotifyService.createAndStartNotifyPlan(ntfRcd);
                            }
                        } catch (Exception e) {
                            logger.error(logPre + "支付状态变更后通知出现异常了.此处未处理直接跳过", e);
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

        String logPre = "支付宝通过渠道商户订单号(" + chnlOutTradeNo + ")提取最新支付情况 ";

        // 新的交易状态 
        UserPayStepStatusEnum newUserpayStepStatusEnum = null;
        // 渠道返回编号
        String channelRstCode = null;
        // 渠道响应信息
        String channelRstMsg = null;
        // 渠道反馈JSON内容
        String channelRstInfo = null;
        // 渠道交易号
        String transNo = null;
        // 渠道响应时间
        Date chnlFebTime = null;

        Date currTime = new Date();
        // 请求序列号
        try {
            logger.debug("支付宝ifcCfgInf-->"+JSON.toJSONString(ifcCfgInf));

            AlipayClient alipayClient = alipayClientService.getAlipayClientByCfg(ifcCfgInf);

            AlipayTradeQueryRequest queryReq = new AlipayTradeQueryRequest();
            AlipayTradeQueryModel bizModel = new AlipayTradeQueryModel();
            bizModel.setOutTradeNo(chnlOutTradeNo);
            queryReq.setBizModel(bizModel);
            AlipayTradeQueryResponse queryRsp = alipayClient.execute(queryReq);
            String tradeStatus = "";
            // 交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
            tradeStatus = queryRsp.getTradeStatus();

            if (!queryRsp.isSuccess()) {
                String body = queryRsp.getBody();
                // 核对交易是否超时
                logger.debug("支付宝返回信息-->"+body);
                String rspSubCode = queryRsp.getSubCode();
                if ("ACQ.TRADE_NOT_EXIST".equals(rspSubCode)) { // 交易不存在,即未产生交易,则查询是否超时,超时则将记录调整为失败

                    Map<String, Object> pcdQmap = new HashMap<>(2);
                    pcdQmap.put("uprCode", chnlOutTradeNo);
                    List<DtPctUserPayRcd> userPayRstList = pctUserPayRcdService.selectByCriteria(pcdQmap, 0, 2);
                    if (userPayRstList.size() == 1) {
                        DtPctUserPayRcd uprInf = userPayRstList.get(0);
                        Date ifcChnlExpiretime = uprInf.getIfcChnlExpiretime();
                        if (ifcChnlExpiretime != null && (ifcChnlExpiretime.getTime() + 5000) < currTime.getTime()) { // 交易已经超时超过5秒了
                            newUserpayStepStatusEnum = UserPayStepStatusEnum.FAIL;
                            channelRstMsg = "交易超时";
                        }
                    }
                }

            } else if (tradeStatus != null) {
                if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
                    newUserpayStepStatusEnum = UserPayStepStatusEnum.WAITE4NTF;
                } else if ("TRADE_CLOSED".equals(tradeStatus)) {
                    newUserpayStepStatusEnum = UserPayStepStatusEnum.FAIL;
                } else if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    newUserpayStepStatusEnum = UserPayStepStatusEnum.SUCCESS;
                } else if ("TRADE_FINISHED".equals(tradeStatus)) {
                    newUserpayStepStatusEnum = UserPayStepStatusEnum.SUCCESS;
                }
                channelRstMsg = queryRsp.getSubMsg();
            }
            transNo = queryRsp.getTradeNo();

            channelRstCode = tradeStatus;

            queryRsp.getMsg();

            channelRstInfo = JSONObject.toJSONString(queryRsp);

            Date sendPayDate = queryRsp.getSendPayDate();
            chnlFebTime = sendPayDate;

        } catch (Exception e) {
            logger.error(logPre + "数据获取异常", e);
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "调用支付宝代扣渠道查询交易记录异常");
        }

        UserPayRecordInf newUprRecord = new UserPayRecordInf();
        newUprRecord.setPayCode(chnlOutTradeNo);


        // 最新支付记录状态
        if (newUserpayStepStatusEnum != null) {
            newUprRecord.setPayStepStatus(newUserpayStepStatusEnum.getCode());
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
        if (chnlFebTime == null) {
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
        Map<String, String> paramMap = new HashMap<String, String>();
        if (formPramsMap == null) {
            return null;
        }
        for (String paramKey : formPramsMap.keySet()) {
            Object object = formPramsMap.get(paramKey);
            if (object == null) {
                continue;
            }
            if (object instanceof String) {
                paramMap.put(paramKey, (String) object);
            } else {
                paramMap.put(paramKey, object.toString());
            }
        }
        String logPre = "支付宝被动回调-->";
        if (ifcCfgInf == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付配置无效");
        }
        Boolean rsaCheckV2 = null;
        try {
            String charset = "utf-8";
            rsaCheckV2 = AlipaySignature.rsaCheckV1(paramMap, ifcCfgInf.getChnlPubFileTxt(), charset, ifcCfgInf.getChnlSignType());
            if (!rsaCheckV2) {
                logger.warn(logPre + "验签失败,请求数据:" + JSONObject.toJSONString(formPramsMap));
            } else {
                logger.debug(logPre + "验签成功...");
            }
        } catch (AlipayApiException e) {
            logger.error(logPre + "抛出异常了", e);
        }
        String uprCode = paramMap.get("out_trade_no"); // 商户订单号

        if (StringUtils.isEmpty(uprCode)) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "请求商户订单号无效");
        }
        DtPctUserPayRcd upr = new DtPctUserPayRcd();
        upr.setUprCode(uprCode);
        UserPayRecordInf newUpr = checkAndUpdatePayStatus(upr, ifcCfgInf);
        return newUpr;
    }


//    /**
//     * 添加监控线程
//     * 
//     * @param upr 支付记录
//     * @param ifcCfgInf 配置信息
//     * @param fstWaiteSecs 首次等待时间秒数
//     * @param maxSecs 过程最大时间秒数
//     */
//    private void checkAndAddMonitorThread(final DtPctUserPayRcd upr, final DtPctIfcChnlCfg ifcCfgInf, final Integer fstWaiteSecs, final Integer maxSecs ) {
//        if (upr == null) {
//            logger.warn("添加监控时由于支付记录入参信息为空导致监控失败");
//            return;
//        }
//        Integer chanlIdVal = upr.getIfcChnlId();
//        UserPayIfcChnlEnum repayChanelEnum = UserPayIfcChnlEnum.getByCode(chanlIdVal);
//        Integer payStepStatus = upr.getStepStatus();
//        if (!(UserPayStepStatusEnum.WAITE4NTF.getCode() == payStepStatus)) {
//            // 不需要监听
//            return;
//        }
//        final String logPre = "执行对支付记录(uprId=" + upr.getUprId() + ")指定渠道(" + repayChanelEnum + ")核对渠道并通知结果-->";
//        // 支付宝需要主动监听
//        Runnable mntThread = new Runnable() {
//            @Override
//            public void run() {
//                
//                Integer fstWaiteSec = fstWaiteSecs ;
//                Integer maxSec = maxSecs;
//                // 每次休眠5秒
//                int perSlpSec = 5 ;
//                
//                if(fstWaiteSec == null ) {
//                    fstWaiteSec = 0;
//                }
//                if( maxSec == null || maxSec <= 0 ) {
//                    maxSec = 1 ;
//                }
//
//                if(fstWaiteSec > 0 ) {
//                    try {
//                        Thread.sleep(fstWaiteSec);
//                    } catch (InterruptedException e) {
//                        logger.warn(logPre + "第一次休眠异常..");
//                    } 
//                }
//
//                Integer currSec = 0 ;
//                Boolean isContinue = true;
//                while (isContinue && (currSec < maxSec )) {
//                    try {
//                        UserPayRecordInf checkRst = checkAndUpdatePayStatus(upr, ifcCfgInf);
//                        UserPayStepStatusEnum stepStatus = UserPayStepStatusEnum.getByCode(checkRst.getPayStepStatus());
//                        logger.info(logPre + "执行结果:" + stepStatus);
//                        if (!UserPayStepStatusEnum.WAITE4NTF.equals(stepStatus)) {
//                            // 不是待监听状态,则不需要监听了
//                            isContinue = false;
//                        }
//                    } catch (Exception e) {
//                        logger.error(logPre + "线程执行异常", e);
//                    }
//                    // 休眠.
//                    try {
//                        currSec += perSlpSec ; 
//                        if(isContinue) {
//                            Thread.sleep((1000 * perSlpSec));
//                        }
//                    } catch (InterruptedException e1) {
//                        logger.error(logPre + "休眠异常", e1);
//                        e1.printStackTrace();
//                    }
//                }
//            }
//        };
//        // 加入线程池
//        PaycenterThreadPools.userpayMntThreadPool.submit(mntThread);
//        logger.debug(logPre + "用户支付线程池添加了一个支付宝监听线程.");
//    }


    /**
     * 渠道用户开户
     */
    @Override
    public UserAccRecordInf openAccount(DoCreateUsrAccReq usrAccInf, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {

        throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付宝暂不支持开户绑定");
//        return null;
    }

    /**
     * 绑卡短验确认
     */
    @Override
    public UserAccRecordInf toBindCheck(DoCreateUsrAccVerifyCheckReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
        throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付宝暂不支持开户绑定");
//      return null;
    }

    /**
     * 绑卡短验重发
     */
    @Override
    public UserAccRecordInf toCreateUserAccResendVerify(DoCreateUsrAccResendVerifyReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
        throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付宝暂不支持开户绑定");
//      return null;
    }



    /* ************************ */


}
