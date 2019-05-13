//package com.sdhoo.pdloan.payctr.busi.baofudf.service.impl;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.annotation.Resource;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.sdhoo.common.SebaseConstants;
//import com.sdhoo.common.base.exception.BaseServiceException;
//import com.sdhoo.common.base.util.RegexUtils;
//import com.sdhoo.common.base.util.StringUtils;
//import com.sdhoo.pdloan.bcrud.model.PctSysPayExtra;
//import com.sdhoo.pdloan.bcrud.model.PctSysPayRcd;
//import com.sdhoo.pdloan.bcrud.service.PctSysPayExtraService;
//import com.sdhoo.pdloan.bcrud.service.PctSysPayRcdService;
//import com.sdhoo.pdloan.payctr.PaycenterConstants;
//import com.sdhoo.pdloan.payctr.PaycenterThreadPools;
//import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
//import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
//import com.sdhoo.pdloan.payctr.busi.baofudf.bens.TransHead;
//import com.sdhoo.pdloan.payctr.busi.baofudf.bens.req.BaofudfReqBF0040002;
//import com.sdhoo.pdloan.payctr.busi.baofudf.bens.req.BaofudfReqBF0040002.TransReqBF0040002;
//import com.sdhoo.pdloan.payctr.busi.baofudf.bens.req.BaofudfReqBF0040004;
//import com.sdhoo.pdloan.payctr.busi.baofudf.bens.req.BaofudfReqBF0040004.TransReqBF0040004;
//import com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp.BaofudfRspBF0040002;
//import com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp.BaofudfRspBF0040002.TransRespBF0040002;
//import com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp.BaofudfRspBF0040004;
//import com.sdhoo.pdloan.payctr.busi.baofudf.dto.BaofudfCfgInf;
//import com.sdhoo.pdloan.payctr.busi.baofudf.enums.BaofudfRstCodeEnum;
//import com.sdhoo.pdloan.payctr.busi.baofudf.service.BaofudfCfgService;
//import com.sdhoo.pdloan.payctr.busi.baofudf.service.BaofudfCltService;
//import com.sdhoo.pdloan.payctr.dto.SysPayRecordInf;
//import com.sdhoo.pdloan.payctr.enums.NotifyBusiTypeEnum;
//import com.sdhoo.pdloan.payctr.enums.PayeeUnitTypeEnum;
//import com.sdhoo.pdloan.payctr.enums.SysPayStepStatusEnum;
//import com.sdhoo.pdloan.payctr.enums.SyspayIfcChnlEnum;
//import com.sdhoo.pdloan.payctr.service.PaycenterBeanProcService;
//import com.sdhoo.pdloan.payctr.service.PaycenterOptlmtService;
//import com.sdhoo.pdloan.payctr.service.StatusChangeService;
//import com.sdhoo.pdloan.payctr.service.SysPayChnlService;
//import com.sdhoo.pdloan.payctr.service.req.DoCreateSysPayReq;
//import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;
//import com.sdhoo.pdloan.payctr.service.req.NotifyReq;
//
///**
// * 系统支付宝付代付的实现.
// * 
// * @author SDPC_LIU
// *
// */
//@Service(value = (PaycenterConstants.SYS_PAYCHNL_SERVICENAME_PRE + PaycenterConstants.SYSPAY_CHNL_BAOFUDF))
//public class SysPayChnlServiceBaofudfImpl implements SysPayChnlService {
//
//    private static final Logger logger = LoggerFactory.getLogger(SysPayChnlServiceBaofudfImpl.class);
//
//	@Override
//	public DtPctIfcChnlCfg getDefaultChnlCfg() {
//		SyspayDftCfgInf syspayDftCfgInf = ifcChnlCfgService.getSyspayDftCfgInf();
//		Long syspayCfgId = syspayDftCfgInf.getSysYibaoPayChnlCfgId();
//		return ifcChnlCfgService.getCachedPctIfcChnlCfg(syspayCfgId);
//	}
//	
//    /**
//     * 当前渠道枚举
//     */
//    private SyspayIfcChnlEnum currIfcChnlEnum = SyspayIfcChnlEnum.BAOFUDF;
//
//    @Resource(name = "baofudfCltServiceImpl")
//    private BaofudfCltService baofudfCltService;
//
//    @Autowired
//    private PctSysPayRcdService sdtSysPayRcdService;
//
//    @Autowired
//    private PctSysPayExtraService sdtSysPayIfcRcdService;
//
//    @Autowired
//    private BaofudfCfgService baofudfCfgService;
//
//    @Autowired
//    private PctIdGenService pctIdGenService;
//
//    @Autowired
//    private PaycenterOptlmtService paycenterOptlmtService;
//
//    @Autowired
//    private StatusChangeService statusChangeService;
//
//    @Autowired
//    private PaycenterBeanProcService paycenterBeanProcService ;
//
//    
//    public PctSysPayRcd generateSysPayRcdByReq(DoCreateSysPayReq req) {
//        String sprCode = req.getSprCode();
//        String payTitle = req.getPayTitle();
//        // 交易金额,精确小数点后两位
//        Double payamtYuan = new BigDecimal(req.getPayamtYuan()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        Integer payeeChnlType = req.getPayeeChnlType();
//        // 收款渠道ID,暂定0未指定,
//        Integer payeeChnlId = 0;
//        String payeeChnlName = req.getPayeeChnlName();
//        String payeeChnlFullname = req.getPayeeChnlFullname();
//        // 收款人账户单位类型
//        Integer payeeAccUnitType = req.getPayeeAccUnitType();
//        // 户号
//        String payeeAccNo = req.getPayeeAccNo();
//        // 收款人姓名
//        String payeeAccFullName = req.getPayeeAccFullName();
//
//        String payeeAccIdcard = req.getPayeeAccIdcard();
//        String payeeAccMobile = req.getPayeeAccMobile();
//        Integer stepStatus = SysPayStepStatusEnum.CREATE.getCode();
//        String payeeChnlProvname = req.getPayeeChnlProvname();
//        String payeeChnlCityname = req.getPayeeChnlCityname();
//        // 调用接口渠道ID
//        long ifcChnlId = currIfcChnlEnum.getCode();
//
//        PctSysPayRcd sdtSysPayRcd = new PctSysPayRcd();
//        sdtSysPayRcd.setSprCode(sprCode);
//        sdtSysPayRcd.setPayTitle(payTitle);
//        sdtSysPayRcd.setIfcChnlId(ifcChnlId);
//        sdtSysPayRcd.setPayRmbamt(payamtYuan);
//        sdtSysPayRcd.setPayeeChnlId(payeeChnlId);
//        sdtSysPayRcd.setPayeeChnlType(payeeChnlType);
//        sdtSysPayRcd.setPayeeChnlName(payeeChnlName);
//        sdtSysPayRcd.setPayeeChnlFullname(payeeChnlFullname);
//        sdtSysPayRcd.setPayeeChnlProvname(payeeChnlProvname);
//        sdtSysPayRcd.setPayeeChnlCityname(payeeChnlCityname);
//        sdtSysPayRcd.setPayeeAccUnitType(payeeAccUnitType);
//        sdtSysPayRcd.setPayeeAccNo(payeeAccNo);
//        sdtSysPayRcd.setPayeeAccFullname(payeeAccFullName);
//        sdtSysPayRcd.setPayeeAccIdcard(payeeAccIdcard);
//        sdtSysPayRcd.setPayeeAccMobile(payeeAccMobile);
//        sdtSysPayRcd.setStepStatus(stepStatus);
//
//        return sdtSysPayRcd;
//    }
//
//    /**
//     * 创建扩展信息
//     * 
//     * @param req
//     * @return
//     */
//    public PctSysPayExtra generateExtraByReq(DoCreateSysPayReq req) {
//
//        // 业务关联信息
//        String busiRelInf = req.getBusiRelInf();
//        Integer initUserType = req.getInitUserType();
//        String initUserId = req.getInitUserId();
//        String initMemo = req.getInitMemo();
//
//        PctSysPayExtra extra = new PctSysPayExtra();
//        extra.setBusiRelInf(busiRelInf);
//        extra.setInitUtype(initUserType);
//        extra.setInitUid(initUserId);
//        extra.setInitMemo(initMemo);
//
//        return extra;
//
//    }
//
//    /**
//     * 支付信息验证
//     * 
//     * @param model
//     */
//    public void validatePctSysPayRcd4Create(PctSysPayRcd model) throws BaseServiceException {
//
//        // 基本数据验证
//        if (model == null) {
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付信息不能为空");
//        }
//        if (currIfcChnlEnum.getCode() != model.getIfcChnlId()) {
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "接口渠道不匹配");
//        }
//        if (model.getPayRmbamt() <= 0) {
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付金额不能小于0");
//        }
//        if (StringUtils.isEmpty(model.getPayeeChnlName())) {
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "银行名称不能为空");
//        }
//        Integer payeeAccUnitType = model.getPayeeAccUnitType();
//        PayeeUnitTypeEnum payeeUnitTypeEnum = PayeeUnitTypeEnum.getByCode(payeeAccUnitType);
//        if (payeeUnitTypeEnum == null) {
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款单位类型无效");
//        }
//        if (StringUtils.isEmpty(model.getPayeeAccNo())) {
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款账号不能为空");
//        }
//        if (StringUtils.isEmpty(model.getPayeeAccFullname())) {
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款户名不能为空");
//        }
//
//        // 个人需要验证身份证号及手机号
//        if (PayeeUnitTypeEnum.PERSON.equals(payeeUnitTypeEnum)) {
//
//            if (StringUtils.isEmpty(model.getPayeeAccIdcard())) {
//                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款人身份证号不能为空");
//            }
//            if (StringUtils.isEmpty(model.getPayeeAccMobile())) {
//                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款人手机号不能为空");
//            }
//        }
//
//        // 企业支付额外验证省份,地市,分行名称
//        if (PayeeUnitTypeEnum.COMPANY.equals(payeeUnitTypeEnum)) {
//            if (StringUtils.isEmpty(model.getPayeeChnlFullname())) {
//                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "分行名称不能为空");
//            }
//            if (StringUtils.isEmpty(model.getPayeeChnlProvname())) {
//                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "省份名称不能为空");
//            }
//            if (StringUtils.isEmpty(model.getPayeeChnlCityname())) {
//                throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "地市名称不能为空");
//            }
//        }
//    }
//
//    @Override
//    public SysPayRecordInf doCreateSysPayRecord(DoCreateSysPayReq req) throws BaseServiceException {
//
//        // 创建支付请求Bean
//        PctSysPayRcd sdtSysPayRcd = generateSysPayRcdByReq(req);
//        PctSysPayExtra sysPayExtra = generateExtraByReq(req);
//
//        // 最大错误尝试次数(3)
//        Integer maxErrorTryCount = 3;
//
//        // 基本信息验证
//        validatePctSysPayRcd4Create(sdtSysPayRcd);
//
//        // 生成ID
//        PctIdData nextRcdId = pctIdGenService.genAndCachePayctIdData();
//        Date currTime = nextRcdId.getIdDate();
//
//        req.getIfcChnlCfgId();
//        BaofudfCfgInf bfCfg = baofudfCfgService.getCfgInfByShid(shId);
//        String ifcChnlAcc = bfCfg.getMemberId();
//        sdtSysPayRcd.setIfcChnlAcc(ifcChnlAcc);
//
//        Long sprId = nextRcdId.getIdVal();
//        sdtSysPayRcd.setSprId(sprId);
//
//        // 当前时间
//        sdtSysPayRcd.setSprCtime(currTime);
//        sdtSysPayRcd.setSprMtime(currTime);
//
//        // 信息入库
//        sdtSysPayRcdService.insertSelective(sdtSysPayRcd);
//
//        // 调用接口发起代付
//        // 金额
//        String transMoneyStr = sdtSysPayRcd.getPayRmbamt().toString();
//        BaofudfReqBF0040004 baseReq = new BaofudfReqBF0040004();
//        TransHead trans_head = new TransHead();
//        // 交易总笔数,暂定1
//        trans_head.setTrans_count("1");
//        // 交易总金额
//        trans_head.setTrans_totalMoney(transMoneyStr);
//
//        TransReqBF0040004 transReqData = new TransReqBF0040004();
//        transReqData.setTrans_no(sdtSysPayRcd.getSprCode());
//        transReqData.setTrans_money(transMoneyStr);
//        transReqData.setTo_acc_name(sdtSysPayRcd.getPayeeAccFullname());
//        transReqData.setTo_acc_no(sdtSysPayRcd.getPayeeAccNo());
//        transReqData.setTo_bank_name(sdtSysPayRcd.getPayeeChnlName());
//        // 省份
//        if (StringUtils.isNotEmpty(sdtSysPayRcd.getPayeeChnlProvname())) {
//            transReqData.setTo_pro_name(sdtSysPayRcd.getPayeeChnlProvname());
//        }
//        // 地市
//        if (StringUtils.isNotEmpty(sdtSysPayRcd.getPayeeChnlCityname())) {
//            transReqData.setTo_city_name(sdtSysPayRcd.getPayeeChnlCityname());
//        }
//        // 支行
//        if (StringUtils.isNotEmpty(sdtSysPayRcd.getPayeeChnlFullname())) {
//            transReqData.setTo_acc_dept(sdtSysPayRcd.getPayeeChnlFullname());
//        }
//        // 身份证号
//        if (StringUtils.isNotEmpty(sdtSysPayRcd.getPayeeAccIdcard())) {
//            transReqData.setTrans_card_id(sdtSysPayRcd.getPayeeAccIdcard());
//        }
//        // 手机号
//        if (StringUtils.isNotEmpty(sdtSysPayRcd.getPayeeAccMobile())) {
//            transReqData.setTrans_mobile(sdtSysPayRcd.getPayeeAccMobile());
//        }
//        baseReq.appendTransReq(transReqData);
//        baseReq.setTrans_head(trans_head);
//        BaofudfRspBF0040004 rspData;
//        // 渠道响应信息Jsn
//        String ifcChnlFebInfJsn = "";
//        BaofudfRstCodeEnum rstCodeEnum = null;
//        String chnlRspCode = "";
//        String chnlRspMsg = "";
//        // Integer tradStatus = 0 ; // 交易状态
//        // 是否需要尝试调用
//        boolean isNeedTrycall = true;
//        // 当前尝试次数为0
//        int currTryCnt = 0;
//
//        while (isNeedTrycall && currTryCnt < maxErrorTryCount) {
//            // 是否出现未知异常
//            boolean isUnknownException = false;
//            try {
//                // 假设成功,不需要重新调用
//                isNeedTrycall = false;
//                // 尝试次数加1
//                currTryCnt++;
//                rspData = baofudfCltService.doExecuteBaseReq(baseReq, bfCfg);
//                // 响应头部
//                TransHead rspTransHead = rspData.getTrans_head();
//                if (rspTransHead != null) {
//                    // 响应错误码
//                    chnlRspCode = rspTransHead.getReturn_code();
//                    if(BaofudfRstCodeEnum.CODE_0000.getCode().equals(chnlRspCode)) {
//                        chnlRspMsg = "请求已发起";
//                    }else {
//                        // 响应信息
//                        chnlRspMsg = rspTransHead.getReturn_msg();
//                    }
//                }
//                rstCodeEnum = BaofudfRstCodeEnum.getByCode(chnlRspCode);
//                ifcChnlFebInfJsn = JSONObject.toJSONString(rspData);
//                // 调用出结果了
//                // 不需要重新调用了
//            } catch (Exception e) {
//                logger.error("支付实现类(宝付)调用异常", e);
//                if (e instanceof BaseServiceException) {
//                    BaseServiceException bse = (BaseServiceException) e;
//                    // 已知异常
//                    chnlRspCode = "-1";
//                    chnlRspMsg = bse.getErrorMsg();
//                } else {
//                    // 出现未知异常,则后续继续处理
//                    isUnknownException = true;
//                }
//            }
//            // 出现未知异常了,则调用查询交易状态,检查是否需要重新调用
//            if (isUnknownException) {
//                BaofudfReqBF0040002 queryReq = new BaofudfReqBF0040002();
//                TransReqBF0040002 queryTreq = new TransReqBF0040002();
//                queryTreq.setTrans_no(sdtSysPayRcd.getSprCode());
//                queryReq.appendTransReq(queryTreq);
//                BaofudfRspBF0040002 queryRsp = baofudfCltService.doExecuteBaseReq(queryReq, bfCfg);
//                TransHead queryThead = queryRsp.getTrans_head();
//                String queryRtCode = queryThead.getReturn_code();
//                String qrtMsg = queryThead.getReturn_msg();
//                BaofudfRstCodeEnum qrstCodeEnum = BaofudfRstCodeEnum.getByCode(queryRtCode);
//                if (BaofudfRstCodeEnum.CODE_0401.equals(qrstCodeEnum)) {
//                    // 交易不存在,则需要重新调用
//                    isNeedTrycall = true;
//                    continue;
//                } else {
//                    // 有调用信息,
//                    String tranState = null;
//                    List<Map<String, List<TransRespBF0040002>>> qryTransReqDatas = queryRsp.getTrans_reqDatas();
//                    if (qryTransReqDatas != null) {
//                        Map<String, List<TransRespBF0040002>> map = qryTransReqDatas.get(0);
//                        TransRespBF0040002 tmpRsp = null;
//                        Set<String> keySet = map.keySet();
//                        for (String tmpKey : keySet) {
//                            tmpRsp = map.get(tmpKey).get(0);
//                        }
//                        if (tmpRsp != null) {
//                            tranState = tmpRsp.getState();
//                        }
//                    }
//                    if (StringUtils.isNotEmpty(tranState)) {
//                        // 有状态值,则说明调用是成功的
//                        chnlRspCode = queryRtCode;
//                        rstCodeEnum = qrstCodeEnum;
//                        chnlRspMsg = qrtMsg;
//                    }
//                    // 渠道响应数据.
//                    ifcChnlFebInfJsn = JSONObject.toJSONString(queryRsp);
//                }
//            }
//        }
//
//        // 处理调用结果:
//        if (StringUtils.isEmpty(chnlRspCode)) {
//            chnlRspCode = "-1";
//        }
//        if (chnlRspMsg == null) {
//            chnlRspMsg = "";
//        }
//
//        // 主数据状态更新,
//        SysPayStepStatusEnum newStepStatus = null;
//        PctSysPayRcd mdfPayRcd = new PctSysPayRcd();
//        mdfPayRcd.setSprId(sprId);
//        if (BaofudfRstCodeEnum.CODE_0000.equals(rstCodeEnum) || BaofudfRstCodeEnum.CODE_200.equals(rstCodeEnum)) {
//            // 调整为需要监测状态
//            newStepStatus = SysPayStepStatusEnum.WAITE4NTF;
//        } else {
//            // 调用失败
//            newStepStatus = SysPayStepStatusEnum.FAIL;
//            
//            //发送通知
//            NotifyReq nreq = new NotifyReq();
//            nreq.setBusiCode(sdtSysPayRcd.getSprCode());
//            nreq.setBusiStatus(newStepStatus.getCode());
//            nreq.setBusiType(Integer.valueOf(NotifyBusiTypeEnum.SYS_PAY_STATUS.getCode()));
//            nreq.setBusiUrl(req.getStatusNotifyUrl());
//            
//            statusChangeService.notify(nreq);
//        }
//        mdfPayRcd.setStepStatus(newStepStatus.getCode());
//        mdfPayRcd.setStepMemo(chnlRspMsg);
//        sdtSysPayRcdService.updateByPrimKeySelective(mdfPayRcd);
//
//        sysPayExtra.setSprId(sprId);
//        sysPayExtra.setLastFebInf(ifcChnlFebInfJsn);
//        sysPayExtra.setLastRspCode(chnlRspCode);
//        sysPayExtra.setLastRspMsg(chnlRspMsg);
//        sysPayExtra.setSpeCtime(currTime);
//        sysPayExtra.setSpeMtime(currTime);
//        sysPayExtra.setStatusNotifyUrl(req.getStatusNotifyUrl());
//        // 调用成功,更新响应数据
//        sdtSysPayIfcRcdService.insertSelective(sysPayExtra);
//
//        // 支付结果核对并返回,
//        sdtSysPayRcd.setStepStatus(newStepStatus.getCode());
//        sdtSysPayRcd.setStepMemo(chnlRspMsg);
//
//        if (SysPayStepStatusEnum.WAITE4NTF.equals(newStepStatus)) {
//            // 需要异步处理的,则发起异步监听
//            checkAndAddMonitorThread(sdtSysPayRcd, 0, 900);
//        }
//
//        SysPayRecordInf payRcdInf = paycenterBeanProcService.genSysPayRecordInfByModel(sdtSysPayRcd, sysPayExtra);
//
//        return payRcdInf;
//    }
//
//    /**
//     * 添加监控
//     * 
//     * @param payRecordInf 支付记录 
//     * @param fstWaiteSecs 首次等待时间秒数
//     * @param maxSecs 过程最大时间秒数
//     */
//    private void checkAndAddMonitorThread(final PctSysPayRcd payRecordInf, final Integer fstWaiteSecs, final Integer maxSecs) {
//        if (payRecordInf == null) {
//            logger.warn("添加监控时由于支付记录入参信息为空导致监控失败");
//            return;
//        }
//        Long chanlIdVal = payRecordInf.getIfcChnlId();
//        SyspayIfcChnlEnum repayChanelEnum = SyspayIfcChnlEnum.getByCode(chanlIdVal);
//        Integer payStepStatus = payRecordInf.getStepStatus();
//        if (!(SysPayStepStatusEnum.WAITE4NTF.getCode() == payStepStatus)) {
//            // 不需要监听
//            return;
//        }
//        final String logPre = "执行对支付记录(uprId=" + payRecordInf.getSprId() + ")指定渠道(" + repayChanelEnum + ")核对渠道并通知结果-->";
//        // 宝付需要主动监听
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
//                    
//                    try {
//                        SysPayRecordInf checkRst = checkAndUpdatePayStatus(payRecordInf);
//                        SysPayStepStatusEnum stepStatus = SysPayStepStatusEnum.getByCode(checkRst.getStepStatus());
//                        logger.info(logPre + "执行结果:" + stepStatus);
//                        if (!SysPayStepStatusEnum.WAITE4NTF.equals(stepStatus)) {
//                            // 不是待监听状态,则不需要监听了
//                            isContinue = false;
//                        }
//                    } catch (Exception e) {
//                        logger.error(logPre + "线程执行异常", e);
//                    }
//
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
//        PaycenterThreadPools.baofuDfMntThreadPool.submit(mntThread);
//        logger.debug(logPre + "宝付代付线程池添加了一个线程.");
//    }
//
//    @Override
//    public SysPayRecordInf checkAndUpdatePayStatus(PctSysPayRcd sysPayRcd) throws BaseServiceException {
//
//        if (sysPayRcd == null) {
//            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "代付记录不能为空");
//        }
//        // 返回值,默认为入参值.
//        PctSysPayRcd rtRcd = sysPayRcd;
//        Long curSprId = sysPayRcd.getSprId();
//
////        String logPre = "核对代付记录(sprId=" + curSprId + ")交易状态-->";
//        // 需要监听状态的数据才要发起监听
//        String optCode = "checkPayStatus";
//        // 操作密码
//        String optPwd = StringUtils.createRandomStr(5);
//        try {
//            // 核对可操作.
//            boolean checkRst = paycenterOptlmtService.checkAndExpSyspayLmtById(curSprId, optCode, optPwd, true);
//
//            Integer stepStatus = sysPayRcd.getStepStatus();
//            SysPayStepStatusEnum currStepEnum = SysPayStepStatusEnum.getByCode(stepStatus);
//            SysPayStepStatusEnum newStepEnum = null;
//
//            Long shId = sysPayRcd.getShId();
//            BaofudfCfgInf bfCfg = baofudfCfgService.getCfgInfByShid(shId);
//
//            Long ifcChnlId = sysPayRcd.getIfcChnlId();
//            SyspayIfcChnlEnum ifcChnlEnum = SyspayIfcChnlEnum.getByCode(ifcChnlId);
//            if(!SyspayIfcChnlEnum.BAOFUDF.equals(ifcChnlEnum)) {
//                throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付记录对应的渠道不是本渠道的");
//            }
//            if (SysPayStepStatusEnum.WAITE4NTF.equals(currStepEnum) && checkRst ) {
//
//                Integer currTranState = null;
//                String febJsn = null;
//                String newStateMemo = null;
//                BaofudfReqBF0040002 queryReq = new BaofudfReqBF0040002();
//                TransReqBF0040002 queryTreq = new TransReqBF0040002();
//                // 商户订单号
//                queryTreq.setTrans_no(sysPayRcd.getSprCode());
//                queryReq.appendTransReq(queryTreq);
//                BaofudfRspBF0040002 queryRsp = baofudfCltService.doExecuteBaseReq(queryReq, bfCfg);
//                TransHead queryThead = queryRsp.getTrans_head();
//                String queryRtCode = queryThead.getReturn_code();
//                BaofudfRstCodeEnum qrstCodeEnum = BaofudfRstCodeEnum.getByCode(queryRtCode);
//                febJsn = JSONObject.toJSONString(queryRsp);
//                if (BaofudfRstCodeEnum.CODE_0401.equals(qrstCodeEnum)) {
//                    // 交易不存在,
//                    newStepEnum = SysPayStepStatusEnum.EXCEPTION;
//                } else {
//                    // 有调用信息,
//                    String tranState = null;
//                    List<Map<String, List<TransRespBF0040002>>> qryTransReqDatas = queryRsp.getTrans_reqDatas();
//                    if (qryTransReqDatas != null && qryTransReqDatas.size() > 0) {
//                        Map<String, List<TransRespBF0040002>> map = qryTransReqDatas.get(0);
//                        TransRespBF0040002 tmpRsp = null;
//                        Set<String> keySet = map.keySet();
//                        for (String tmpKey : keySet) {
//                            tmpRsp = map.get(tmpKey).get(0);
//                        }
//                        if (tmpRsp != null) {
//                            tranState = tmpRsp.getState();
//                            // 订单交易状态, 0转账中,1转账成功,-1转账失败,2转账退款
//                            if (tranState != null && tranState.matches(RegexUtils.REGEXSTR_LONG)) {
//                                currTranState = Integer.valueOf(tranState);
//                            }
//                        }
//                    }
//                }
//                if (currTranState != null) {
//                    switch (currTranState) {
//                    case 0:
//                        newStepEnum = SysPayStepStatusEnum.WAITE4NTF;
//                        break;
//                    case 1:
//                        newStepEnum = SysPayStepStatusEnum.SUCCESS;
//                        newStateMemo = "代付成功";
//                        break;
//                    case -1:
//                        newStepEnum = SysPayStepStatusEnum.FAIL;
//                        newStateMemo = "转账失败";
//                        break;
//                    case -2:
//                        newStepEnum = SysPayStepStatusEnum.FAIL;
//                        newStateMemo = "转账退款";
//                        break;
//                    default:
//                        break;
//                    }
//                }
//                // 状态有变化,则更新内容.
//                if ( newStepEnum != null && !currStepEnum.equals(newStepEnum)) {
//                    Date currTime = new Date();
//                    // 状态有变化,则更新状态及内容
//                    Long sprId = curSprId;
//                    PctSysPayRcd mdfRcd = new PctSysPayRcd();
//                    mdfRcd.setSprId(sprId);
//                    mdfRcd.setStepStatus(newStepEnum.getCode());
//                    mdfRcd.setStepMemo(newStateMemo);
//                    mdfRcd.setSprMtime(currTime);
//                    sdtSysPayRcdService.updateByPrimKeySelective(mdfRcd);
//                    PctSysPayExtra mdfIfcRcd = new PctSysPayExtra();
//                    mdfIfcRcd.setSprId(sprId);
//                    mdfIfcRcd.setLastFebInf(febJsn);
//                    mdfIfcRcd.setLastRspMsg(newStateMemo);
//                    sdtSysPayIfcRcdService.updateByPrimKeySelective(mdfIfcRcd);
//                    rtRcd = sdtSysPayRcdService.getByPrimKey(sprId, null);
//                    
//                    if (newStepEnum.getCode() == SysPayStepStatusEnum.SUCCESS.getCode() || newStepEnum.getCode() == SysPayStepStatusEnum.FAIL.getCode()) {
//                        // 发送通知
//                        NotifyReq nreq = new NotifyReq();
//                        nreq.setBusiCode(sysPayRcd.getSprCode());
//                        nreq.setBusiStatus(newStepEnum.getCode());
//                        nreq.setBusiType(Integer.valueOf(NotifyBusiTypeEnum.SYS_PAY_STATUS.getCode()));
//                        PctSysPayExtra extraInf = sdtSysPayIfcRcdService.getByPrimKey(curSprId, null);
//                        nreq.setBusiUrl(extraInf.getStatusNotifyUrl());
//
//                        statusChangeService.notify(nreq);
//                    }
//                }
//            }
//            PctSysPayExtra extraInf = sdtSysPayIfcRcdService.getByPrimKey(curSprId, null);
//            return paycenterBeanProcService.genSysPayRecordInfByModel(rtRcd, extraInf);
//        } finally {
//            paycenterOptlmtService.releaseSyspayLmtById(curSprId, optCode, optPwd);
//        }
//    }
//
//    /**
//     * 被动处理
//     */
//    @Override
//    public SysPayRecordInf notifyAndUpdatePayStatus(DoPayFedbackBusiReq fedbackInf) throws BaseServiceException {
//        throw new BaseServiceException("宝付代扣不支持被动通知");
//        // return null;
//    }
//
//}
