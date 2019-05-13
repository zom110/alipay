package com.sdhoo.pdloan.payctr.busi.yibaodf.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.sdhoo.common.base.util.TimeCalcUtils;
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
import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YbdfTransferRst;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.YibaodfCfgInf;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.req.TransferQueryReq;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.req.TransferSendReq;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.rsp.TransferQueryRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.rsp.TransferSendRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodf.enums.YibaodfBankProcStatusEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodf.enums.YibaodfErrorCodeEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodf.enums.YibaodfFeetypeEnum;
import com.sdhoo.pdloan.payctr.busi.yibaodf.service.YibaodfCfgService;
import com.sdhoo.pdloan.payctr.busi.yibaodf.service.YibaodfClientService;
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
@Service(value = (PaycenterConstants.SYSPAY_CHNLSERVICENAME_PRE + PaycenterConstants.SYSPAY_CHNL_YIBAODF))
public class SysPayChnlServiceYibaodfImpl implements SysPayChnlService {

    private static final Logger logger = LoggerFactory.getLogger(SysPayChnlServiceYibaodfImpl.class);

    /**
     * 当前渠道枚举
     */
    private SyspayIfcChnlEnum currIfcChnlEnum = SyspayIfcChnlEnum.YIBAODF;

    @Autowired
    private YibaodfClientService yibaodfClientService ;

    @Autowired
    private DtPctSysPayRcdService dtPctSysPayRcdService;
    
    @Autowired
    private DtPctSysPayExtraService dtSysPayExtraService; 

    @Autowired
    private YibaodfCfgService yibaodfCfgService ;

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

    /**
     * 渠道银行名称编号映射表
     */
    private static Map<String,String> bankNameCodeMap = null ; 
    
    


	@Override
	public DtPctIfcChnlCfg getDefaultChnlCfg() {
		SyspayDftCfgInf syspayDftCfgInf = ifcChnlCfgService.getSyspayDftCfgInf();
		Long syspayCfgId = syspayDftCfgInf.getSysYibaoPayChnlCfgId();
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

        String payeeChnlCode = getBankNameCodeMap().get(payeeChnlName);
        if(payeeChnlCode == null ) {
            payeeChnlCode = "";
        }

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
        sdtSysPayRcd.setPayeeChnlCode(payeeChnlCode);
        sdtSysPayRcd.setPayeeChnlFullname(payeeChnlFullname);
        sdtSysPayRcd.setPayeeChnlProvname(payeeChnlProvname);
        sdtSysPayRcd.setPayeeChnlCityname(payeeChnlCityname);
        sdtSysPayRcd.setPayeeAccUnitType(payeeAccUnitType);
        sdtSysPayRcd.setPayeeAccNo(payeeAccNo);
        sdtSysPayRcd.setPayeeAccFullname(payeeAccFullName);
        sdtSysPayRcd.setPayeeAccIdcard(payeeAccIdcard);
        sdtSysPayRcd.setPayeeAccMobile(payeeAccMobile);
        sdtSysPayRcd.setStepStatus(stepStatus);
        sdtSysPayRcd.setPayeeChnlCode(payeeChnlCode); // 银行编号 

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

        // 最大错误尝试次数(3)
        Integer maxErrorTryCount = 3;

        // 基本信息验证
        validateDtPctSysPayRcd4Create(dtPctSysPayRcd);

        // 生成ID
        PctIdData nextRcdId = pctIdGenService.genAndCachePayctIdData();
        Date currTime = nextRcdId.getIdDate();

        String sprCode = req.getSprCode();
        String batchNo = PayctUtils.getNumberFromStr(sprCode); 
        String payeeAccFullName = req.getPayeeAccFullName(); // 姓名 
        String payeeAccNo = req.getPayeeAccNo(); // 收款账号
        String payeeChnlName = req.getPayeeChnlName(); // 银行名称 

        String initMemo = req.getInitMemo(); // 发起备注
        
        String bankCode = getBankNameCodeMap().get(payeeChnlName); // 易宝的银行编号
        
        YibaodfCfgInf ybdffg = yibaodfCfgService.getYibaoCfgInfByModel(ifcCfgInf);
        String ifcChnlName = ybdffg.getIfcChnlName();
        dtPctSysPayRcd.setIfcChnlName(ifcChnlName);
        dtPctSysPayRcd.setIfcChnlBatchNo(batchNo);

        Long sprId = nextRcdId.getIdVal();
        dtPctSysPayRcd.setSprId(sprId);

        // 当前时间
        dtPctSysPayRcd.setSprCtime(currTime);
        dtPctSysPayRcd.setSprMtime(currTime);

        // 信息入库
        dtPctSysPayRcdService.insertSelective(dtPctSysPayRcd);

        // 调用接口发起代付
        // 金额
        Double payRmbamt = dtPctSysPayRcd.getPayRmbamt();
        // 调用接口进行放款 
        DecimalFormat dcmFmt = new DecimalFormat("#0.00"); 
        String amount = dcmFmt.format(payRmbamt); // 金额格式化.必须保留到小数点后两位
        TransferSendReq reqCtx = new TransferSendReq(); 
        reqCtx.setCustomerNumber(ybdffg.getMerchantAppKey());
        reqCtx.setGroupNumber(ybdffg.getMerchantAccount());
        reqCtx.setBatchNo(batchNo);
        reqCtx.setOrderId(sprCode);
        reqCtx.setAmount(amount);
        reqCtx.setProduct("");
        reqCtx.setUrgency("1"); // 默认加急
        reqCtx.setAccountName(payeeAccFullName);
        reqCtx.setAccountNumber(payeeAccNo);
        reqCtx.setBankCode(bankCode);
        reqCtx.setBankName(payeeChnlName);
        reqCtx.setProvinceCode(""); // 省份编号,暂未支持
        reqCtx.setCityCode(""); // 地市编号,暂未支持
        reqCtx.setDesc(initMemo);
        reqCtx.setLeaveWord(initMemo); 
        reqCtx.setAbstractInfo("代付摘要");
        reqCtx.setFeeType(YibaodfFeetypeEnum.SOURCE.getCode()); // 手续费承担方(商户)
        TransferSendRsp sendRsp = null ;

        // 渠道响应信息Jsn
        String ifcChnlFebInfJsn = "";
        YibaodfErrorCodeEnum rstCodeEnum = null;
        String chnlRspCode = "";
        String chnlRspMsg = "";
        // Integer tradStatus = 0 ; // 交易状态
        // 是否需要尝试调用
        boolean isNeedTrycall = true;
        // 当前尝试次数为0
        int currTryCnt = 0;

        while (isNeedTrycall && currTryCnt < maxErrorTryCount) {
            // 是否出现未知异常
            boolean isUnknownException = false;
            try {
                // 假设成功,不需要重新调用
                isNeedTrycall = false;
                // 尝试次数加1
                currTryCnt++;
                sendRsp = yibaodfClientService.doExecuteBaseReq(reqCtx , ybdffg);
                if (sendRsp != null) {
                    // 响应错误码
                    String errorCode = sendRsp.getErrorCode();
                    rstCodeEnum = YibaodfErrorCodeEnum.getByCode(errorCode);
                }
                ifcChnlFebInfJsn = JSONObject.toJSONString(sendRsp);
                // 调用出结果了
                // 不需要重新调用了
            } catch (Exception e) {
                logger.error("代付实现类(易宝)调用异常", e);
                if (e instanceof BaseServiceException) {
                    BaseServiceException bse = (BaseServiceException) e;
                    // 已知异常
                    chnlRspCode = "-1";
                    chnlRspMsg = bse.getErrorMsg();
                } else {
                    // 出现未知异常,则后续继续处理
                    isUnknownException = true;
                }
            }

            // 出现未知异常了,则调用查询交易状态,检查是否需要重新调用
            if (isUnknownException) {
                TransferQueryReq queryReq = new TransferQueryReq();
                queryReq.setBatchNo(batchNo);
                queryReq.setOrderId(sprCode);
                TransferQueryRsp queryRsp;
                try {
                    queryRsp = yibaodfClientService.doExecuteBaseReq(queryReq, ybdffg);
                    String errorCode = queryRsp.getErrorCode();
                    rstCodeEnum = YibaodfErrorCodeEnum.getByCode(errorCode);
                    if (YibaodfErrorCodeEnum.BAC000048.equals(rstCodeEnum)) {
                        // 交易不存在,则需要重新调用
                        isNeedTrycall = true;
                        continue;
                    } else {
                        // 有交易记录,则进入对账
                        // 渠道响应数据.
                        ifcChnlFebInfJsn = JSONObject.toJSONString(queryRsp);
                    }
                } catch (Exception e) {
                    logger.error("代付实现类(易宝)查询交易记录异常了",e); 
                }
            }
        }
        
        if(rstCodeEnum != null ) {
            chnlRspCode =  rstCodeEnum.getCode();
            if(StringUtils.isEmpty(chnlRspMsg)) {
                chnlRspMsg = rstCodeEnum.getName();
            }
        }

        // 处理调用结果:
        if (StringUtils.isEmpty(chnlRspCode)) {
            chnlRspCode = "-1";
        }
        if (chnlRspMsg == null) {
            chnlRspMsg = "";
        }
        // 主数据状态更新,
        SysPayStepStatusEnum newStepStatus = null;
        DtPctSysPayRcd mdfPayRcd = new DtPctSysPayRcd();
        mdfPayRcd.setSprId(sprId);
        if ( YibaodfErrorCodeEnum.BAC001.equals(rstCodeEnum)) { // 已接收 
            // 调整为需要监测状态
        	newStepStatus = SysPayStepStatusEnum.WAITE4NTF;
        } else {
            // 调用失败
            newStepStatus = SysPayStepStatusEnum.FAIL;
          
        }
        mdfPayRcd.setStepStatus(newStepStatus.getCode());
        mdfPayRcd.setStepMemo(chnlRspMsg);
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

        if (SysPayStepStatusEnum.WAITE4NTF.equals(newStepStatus)) {
            // 需要异步处理的,则发起异步监听
            checkAndAddMonitorThread(dtPctSysPayRcd, ifcCfgInf, 0, 900);
        }else if ( SysPayStepStatusEnum.FAIL.equals(newStepStatus) ) {
        	// 发起失败, 发送通知 
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
        String logPre = "易宝代付核对代付记录(sprId="+curSprId+")-->";
        try {
            // 核对可操作.
            paycenterOptlmtService.checkAndExpSyspayLmtById(curSprId, optCode, optPwd, true);

            Integer stepStatus = sysPayRcd.getStepStatus();
            SysPayStepStatusEnum currStepEnum = SysPayStepStatusEnum.getByCode(stepStatus);
            SysPayStepStatusEnum newStepEnum = null;

            YibaodfCfgInf bfCfg = yibaodfCfgService.getYibaoCfgInfByModel(ifcCfgInf);

            Integer ifcChnlId = sysPayRcd.getIfcChnlId();
            SyspayIfcChnlEnum ifcChnlEnum = SyspayIfcChnlEnum.getByCode(ifcChnlId);
            if(!currIfcChnlEnum.equals(ifcChnlEnum)) {
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付记录对应的渠道不是本渠道的");
            }

            Date currTime = new Date();
            
            if (SysPayStepStatusEnum.WAITE4NTF.equals(currStepEnum) ) {

                String sprCode = sysPayRcd.getSprCode(); // 交易号 
                String ifcChnlBatchNo = sysPayRcd.getIfcChnlBatchNo();
                String febJsn = null;
                String newStateMemo = null;
                Date finishDate = null ; // 交易结束时间 

                TransferQueryReq queryReq = new TransferQueryReq();
                queryReq.setBatchNo(ifcChnlBatchNo);
                queryReq.setOrderId(sprCode);
                TransferQueryRsp queryRsp;
                YibaodfBankProcStatusEnum bankProcRstEnum = null ;
                try {
                    queryRsp = yibaodfClientService.doExecuteBaseReq(queryReq, bfCfg);
                    String errorCode = queryRsp.getErrorCode();
                    YibaodfErrorCodeEnum rstCodeEnum = YibaodfErrorCodeEnum.getByCode(errorCode); 
                    if (YibaodfErrorCodeEnum.BAC000048.equals(rstCodeEnum)) {
                    	// 交易不存在,
                    	newStepEnum = SysPayStepStatusEnum.NOT_EXISTS;
                    } else {
                    	
                    	List<YbdfTransferRst> dataList = queryRsp.getList();
                    	
                    	YbdfTransferRst ybdfTransferRst = dataList.get(0); // 第一笔交易 ,TODO 代付可能会有多笔交易需要处理
                    	String bankTrxStatusCode = ybdfTransferRst.getBankTrxStatusCode();
                    	bankProcRstEnum = YibaodfBankProcStatusEnum.getByCode(bankTrxStatusCode);
                    	String ybdfFinishDateStr = ybdfTransferRst.getFinishDate();
                    	SimpleDateFormat finishDateSdfmt = TimeCalcUtils.getThreadLocalDateFormat("yyyy-MM-dd HH:mm:ss");
                    	if(StringUtils.isNotEmpty(ybdfFinishDateStr)) {
                    		try {
								Date parseDate = finishDateSdfmt.parse(ybdfFinishDateStr);
								finishDate = parseDate ;
							} catch (Exception e) {
								logger.error(logPre+"出现异常了,此处忽略处理",e); 
							}
                    	}
                    }
                    if (bankProcRstEnum != null) {
                    	switch (bankProcRstEnum) {
                    	case CODE_I:
                    		newStepEnum = SysPayStepStatusEnum.WAITE4NTF;
                    		break;
                    	case CODE_W:
                    		newStepEnum = SysPayStepStatusEnum.WAITE4NTF;
                    		break;
                    	case CODE_U:
                    		newStepEnum = SysPayStepStatusEnum.WAITE4NTF;
                    		break;
                    	case CODE_S:
                    		newStepEnum = SysPayStepStatusEnum.SUCCESS;
                    		newStateMemo = "代付成功";
                    		break;
                    	case CODE_F:
                    		newStepEnum = SysPayStepStatusEnum.FAIL;
                    		newStateMemo = "失败";
                    		break;
                    	default:
                    		break;
                    	}
                    }
                } catch (Exception e) {
                	logger.error("易宝(代付)对账调用接口出现异常...",e);
                }
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
            }
            DtPctSysPayExtra extraInf = dtSysPayExtraService.getByPrimKey(curSprId, null);
            return paycenterBeanProcService.genSysPayRecordInfByModel(rtRcd, extraInf);
        } finally {
            paycenterOptlmtService.releaseSyspayLmtById(curSprId, optCode, optPwd);
        }
    }

    
    /**
     * 
     * @return
     */
    private static Map<String,String> getBankNameCodeMap (){
        
        if(bankNameCodeMap == null ) {
            bankNameCodeMap = new HashMap<>();
            
            bankNameCodeMap.put("安徽省农村信用社","AHNXS");
            bankNameCodeMap.put("安顺农村商业银行","ASRCB");
            bankNameCodeMap.put("安阳银行","AYYH");
            bankNameCodeMap.put("鞍山银行","ASYH");
            bankNameCodeMap.put("奥地利中央合作银行","AUSTRIACENTRALCB");
            bankNameCodeMap.put("澳大利亚和新西兰银行集团","ADLYHXXL");
            bankNameCodeMap.put("澳门地区","MACAOAREA");
            bankNameCodeMap.put("包商银行","BTCB");
            bankNameCodeMap.put("保定银行","BDYH");
            bankNameCodeMap.put("北京农村商业银行","BRCB");
            bankNameCodeMap.put("北京顺义银座村镇银行","SYYZCZB");
            bankNameCodeMap.put("北京银行","BCCB");
            bankNameCodeMap.put("本溪市商业银行","BXSCCB");
            bankNameCodeMap.put("比利时联合银行","KBC");
            bankNameCodeMap.put("渤海银行","CBHB");
            bankNameCodeMap.put("沧州银行","CZYH");
            bankNameCodeMap.put("朝阳银行","CYYH");
            bankNameCodeMap.put("成都农商银行","CDNSYH");
            bankNameCodeMap.put("成都银行","CDYH");
            bankNameCodeMap.put("承德银行","CDBANK");
            bankNameCodeMap.put("城市商业银行","CSCCB");
            bankNameCodeMap.put("城市商业银行资金清算中心","FUNDCCCB");
            bankNameCodeMap.put("城市信用合作社","CSXYHZS");
            bankNameCodeMap.put("创兴银行有限公司","CXYHYXGS");
            bankNameCodeMap.put("村镇银行","CUNZB");
            bankNameCodeMap.put("达州市商业银行","DZSCCB");
            bankNameCodeMap.put("大华银行（中国）有限公司","UOBCHINA");
            bankNameCodeMap.put("大连银行","DLYH");
            bankNameCodeMap.put("大同银行","DTYH");
            bankNameCodeMap.put("大新银行（中国）有限公司","DAHSING");
            bankNameCodeMap.put("大众银行（香港）有限公司","DZYHXGYXGS");
            bankNameCodeMap.put("代收付中心、电子结算中心","COPCESC");
            bankNameCodeMap.put("丹东银行","DDYH");
            bankNameCodeMap.put("德国北德意志州银行","NORDLB");
            bankNameCodeMap.put("德国商业银行","COMMERZBANK");
            bankNameCodeMap.put("德阳银行","DYYH");
            bankNameCodeMap.put("德意志银行（中国）有限公司","DB");
            bankNameCodeMap.put("德州银行","DZYH");
            bankNameCodeMap.put("电子联行转换中心","DZLHZHZX");
            bankNameCodeMap.put("东方汇理银行（中国）有限公司","BI");
            bankNameCodeMap.put("东莞农村商业银行","DGRCB");
            bankNameCodeMap.put("东莞银行","DGYH");
            bankNameCodeMap.put("东亚银行（中国）有限公司","HKBEA");
            bankNameCodeMap.put("东营银行","DOYYH");
            bankNameCodeMap.put("鄂尔多斯银行","EEDSYH");
            bankNameCodeMap.put("法国巴黎银行（中国）有限公司","BNPPARIBAS");
            bankNameCodeMap.put("法国外贸银行","NBPBK");
            bankNameCodeMap.put("法国兴业银行","SOCIETEGENERATE");
            bankNameCodeMap.put("菲律宾首都银行及信托","PHILIPPINESCB");
            bankNameCodeMap.put("佛山农村商业银行","FSRCB");
            bankNameCodeMap.put("福建福州农村商业银行","FJFZRCB");
            bankNameCodeMap.put("福建海峡银行","FJHXYH");
            bankNameCodeMap.put("福建莆田农村商业银行","FJPTRCB");
            bankNameCodeMap.put("福建省农村信用社","FJNX");
            bankNameCodeMap.put("福建石狮农村商业银行","FJSSRCB");
            bankNameCodeMap.put("福建漳州农村商业银行","FJZZRCB");
            bankNameCodeMap.put("抚顺银行","FSYH");
            bankNameCodeMap.put("阜新银行","FXYH");
            bankNameCodeMap.put("富滇银行","FDYH");
            bankNameCodeMap.put("甘肃灵台农村商业银行","GSLTRCB");
            bankNameCodeMap.put("甘肃银行","GSYH");
            bankNameCodeMap.put("赣州银行","GZBC");
            bankNameCodeMap.put("公开市场业务操作室","GKSCYWCZS");
            bankNameCodeMap.put("广东博罗农村商业银行","GDBLRCB");
            bankNameCodeMap.put("广东发展银行","GDB");
            bankNameCodeMap.put("广东高要农村商业银行","GDGYRCB");
            bankNameCodeMap.put("广东河源农村商业银行","GDHYNRCB");
            bankNameCodeMap.put("广东华兴银行","GDHXYH");
            bankNameCodeMap.put("广东惠东农村商业银行","GDHDRCB");
            bankNameCodeMap.put("广东揭东农村商业银行","GDJDRCB");
            bankNameCodeMap.put("广东揭西农村商业银行","GDJXRCB");
            bankNameCodeMap.put("广东揭阳农村商业银行","GDJYRCB");
            bankNameCodeMap.put("广东南海农村商业银行","GDNHRCB");
            bankNameCodeMap.put("广东南粤银行","GDNYYH");
            bankNameCodeMap.put("广东清远农村商业银行","GDQYRCB");
            bankNameCodeMap.put("广东阳春农村商业银行","GDYCRCB");
            bankNameCodeMap.put("广东阳东农村商业银行","GDYDRCB");
            bankNameCodeMap.put("广发银行","CGB");
            bankNameCodeMap.put("广西北部湾银行","BBWYH");
            bankNameCodeMap.put("广西农村信用社","GXNXS");
            bankNameCodeMap.put("广州农村商业银行","GZRCB");
            bankNameCodeMap.put("广州银行","GUAZYH");
            bankNameCodeMap.put("贵阳农村商业银行","GYRCB");
            bankNameCodeMap.put("贵阳银行","GYYH");
            bankNameCodeMap.put("贵州毕节农村商业银行","GZBJRCB");
            bankNameCodeMap.put("贵州独山农村商业银行","GZDSRCB");
            bankNameCodeMap.put("贵州湄潭农村商业银行","GZMTRCB");
            bankNameCodeMap.put("贵州仁怀茅台农村商业银行","GZRHMRCB");
            bankNameCodeMap.put("贵州瓮安农村商业银行","GZWARCB");
            bankNameCodeMap.put("贵州兴义农村商业银行","GZXYRCB");
            bankNameCodeMap.put("贵州银行","GUZYH");
            bankNameCodeMap.put("桂林银行","GLYH");
            bankNameCodeMap.put("国家金库","ZHRMGHG");
            bankNameCodeMap.put("国家开发银行","CDBB");
            bankNameCodeMap.put("国民银行（中国）有限公司","GMYHZHYXGS");
            bankNameCodeMap.put("哈尔滨银行","HAEBYH");
            bankNameCodeMap.put("哈密市商业银行","HMSCCB");
            bankNameCodeMap.put("海口联合农村商业银行","HKLHRCB");
            bankNameCodeMap.put("海南省农村信用社","HNNXS");
            bankNameCodeMap.put("海南银行","HNYH");
            bankNameCodeMap.put("邯郸银行","HDCB");
            bankNameCodeMap.put("韩国产业银行","KDBK");
            bankNameCodeMap.put("韩国外换银行","KEB");
            bankNameCodeMap.put("韩国中小企业银行","IBKK");
            bankNameCodeMap.put("韩亚银行（中国）有限公司","HYYH");
            bankNameCodeMap.put("汉口银行","HKYH");
            bankNameCodeMap.put("杭州银行","HZYH");
            bankNameCodeMap.put("河北井陉农村商业银行","HBJJRCB");
            bankNameCodeMap.put("河北唐山曹妃甸农村商业银行","HBTSCFDRCB");
            bankNameCodeMap.put("河北唐山农村商业银行","HBTSRCB");
            bankNameCodeMap.put("河北万全农村商业银行","HBWQRCB");
            bankNameCodeMap.put("河北献县农村商业银行","HBXXRCB");
            bankNameCodeMap.put("河北银行","HBYH");
            bankNameCodeMap.put("河北张家口宣泰农村商业银行","HBZJKXTRCB");
            bankNameCodeMap.put("荷兰安智银行","HLAZYH");
            bankNameCodeMap.put("荷兰合作银行","HLHZYH");
            bankNameCodeMap.put("恒丰银行","EGB");
            bankNameCodeMap.put("恒生银行（中国）有限公司","HSB");
            bankNameCodeMap.put("衡水银行","HSYH");
            bankNameCodeMap.put("葫芦岛银行","HLDYH");
            bankNameCodeMap.put("湖北省农村信用社","HBNXS");
            bankNameCodeMap.put("湖北仙桃北农商村镇银行","HBXTBCZB");
            bankNameCodeMap.put("湖北银行","HUBYH");
            bankNameCodeMap.put("湖州银行","HUZYH");
            bankNameCodeMap.put("花旗银行","CITIBANK");
            bankNameCodeMap.put("华美银行","EASTWESTBANK");
            bankNameCodeMap.put("华南商业银行","HNCCB");
            bankNameCodeMap.put("华侨银行（中国）有限公司","OCBC");
            bankNameCodeMap.put("华融湘江银行","HRXJYH");
            bankNameCodeMap.put("华夏银行","HXB");
            bankNameCodeMap.put("华夏银行1","HX");
            bankNameCodeMap.put("华一银行","COB");
            bankNameCodeMap.put("华裔银行","CHINESEBANK");
            bankNameCodeMap.put("黄河农村商业银行","HHRCB");
            bankNameCodeMap.put("徽商银行","AHCB");
            bankNameCodeMap.put("汇丰银行（中国）有限公司","HFB");
            bankNameCodeMap.put("惠州农村商业银行","HZRCB");
            bankNameCodeMap.put("吉林德惠农村商业银行","JLHDNCYH");
            bankNameCodeMap.put("吉林省农村信用社","JLNXS");
            bankNameCodeMap.put("吉林双阳农村商业银行","JLSYRCB");
            bankNameCodeMap.put("吉林银行","JLYH");
            bankNameCodeMap.put("集友银行有限公司","CHIYUBANK");
            bankNameCodeMap.put("济宁银行","JNYH");
            bankNameCodeMap.put("加拿大丰业银行","SCTABK");
            bankNameCodeMap.put("嘉兴银行","JXCCB");
            bankNameCodeMap.put("江门融和农村商业银行","JMRHRCB");
            bankNameCodeMap.put("江门新会农村商业银行","JMXHRCB");
            bankNameCodeMap.put("江苏滨海农村商业","JSBHRCB");
            bankNameCodeMap.put("江苏常熟农村商业银行","CSRCB");
            bankNameCodeMap.put("江苏赣榆农村商业银行","JSGYRCB");
            bankNameCodeMap.put("江苏淮安农村商业银行","JSHARCB");
            bankNameCodeMap.put("江苏江南农村商业银行","JSJNRCB");
            bankNameCodeMap.put("江苏江阴农村商业银行","JSJYRCB");
            bankNameCodeMap.put("江苏金湖农村商业银行","JSJHRCB");
            bankNameCodeMap.put("江苏溧水农村商业银行","JSLSRCB");
            bankNameCodeMap.put("江苏射阳农村商业银行","JSSYRCB");
            bankNameCodeMap.put("江苏省农村信用社","JSNXS");
            bankNameCodeMap.put("江苏盐城黄海农村商业银行","JSYCHHRCB");
            bankNameCodeMap.put("江苏扬州农村商业银行","JISYZRCB");
            bankNameCodeMap.put("江苏仪征农村商业银行","JSYZRCB");
            bankNameCodeMap.put("江苏宜兴农村商业银行","JSYXRCB");
            bankNameCodeMap.put("江苏银行","JSBCHINA");
            bankNameCodeMap.put("江苏长江商业银行","JSCJCCB");
            bankNameCodeMap.put("江西赣州银座村镇银行","GZYZCZB");
            bankNameCodeMap.put("江西会昌农村商业银行","JXHCRCB");
            bankNameCodeMap.put("江西银行(南昌银行)","NCYH");
            bankNameCodeMap.put("交通银行","BOCO");
            bankNameCodeMap.put("焦作中旅银行","JZSCCB");
            bankNameCodeMap.put("金华银行","JHYH");
            bankNameCodeMap.put("锦州天桥农村商业银行","JZTQRCB");
            bankNameCodeMap.put("锦州银行","JINZYH");
            bankNameCodeMap.put("晋城银行","JCYH");
            bankNameCodeMap.put("晋商银行","JSYH");
            bankNameCodeMap.put("晋中银行","JZYH");
            bankNameCodeMap.put("景德镇市商业银行","JDZSCCB");
            bankNameCodeMap.put("九江农村商业银行","JJRCB");
            bankNameCodeMap.put("九江银行","JJYH");
            bankNameCodeMap.put("开封市商业银行","KFCCB");
            bankNameCodeMap.put("库尔勒市商业银行","KELSCCB");
            bankNameCodeMap.put("昆仑银行","KLYH");
            bankNameCodeMap.put("昆山农村商业银行","KSRCB");
            bankNameCodeMap.put("莱商银行","ISBC");
            bankNameCodeMap.put("兰州银行","LZYH");
            bankNameCodeMap.put("廊坊银行","LFYH");
            bankNameCodeMap.put("乐山市商业银行","LSSCCB");
            bankNameCodeMap.put("连云港东方农村商业银行","LYGDFRCB");
            bankNameCodeMap.put("凉山州商业银行","LSZCCB");
            bankNameCodeMap.put("辽阳银行","LYYH");
            bankNameCodeMap.put("林州德丰村镇银行","LZDFCZB");
            bankNameCodeMap.put("临商银行","LSYH");
            bankNameCodeMap.put("柳州银行","LIUZYH");
            bankNameCodeMap.put("龙江银行","LJYH");
            bankNameCodeMap.put("泸州江阳农村商业银行","LZJYRCB");
            bankNameCodeMap.put("泸州龙马潭农村商业银行","LZLMTRCB");
            bankNameCodeMap.put("泸州市商业银行","HZSCCB");
            bankNameCodeMap.put("洛阳银行","LYBC");
            bankNameCodeMap.put("漯河银行","LHYH");
            bankNameCodeMap.put("马来西亚马来亚银行","MALAYSIABANK");
            bankNameCodeMap.put("美国建东银行","AMERICANBUILTEASTBANK");
            bankNameCodeMap.put("美国银行有限公司","BAC");
            bankNameCodeMap.put("蒙特利尔银行（中国）有限公司","BMOBK");
            bankNameCodeMap.put("绵阳市商业银行","MYCCB");
            bankNameCodeMap.put("摩根大通银行（中国）有限公司","JPMC");
            bankNameCodeMap.put("摩根士丹利国际","MGSDL");
            bankNameCodeMap.put("南充市商业银行","NCSCCB");
            bankNameCodeMap.put("南京银行","NJYH");
            bankNameCodeMap.put("南阳银行","NYYH");
            bankNameCodeMap.put("南洋商业银行（中国）有限公司","NOBC");
            bankNameCodeMap.put("内蒙古银行","NMGYH");
            bankNameCodeMap.put("宁波东海银行","NBDHYH");
            bankNameCodeMap.put("宁波通商银行","NBTSYH");
            bankNameCodeMap.put("宁波银行","NBYH");
            bankNameCodeMap.put("宁夏银行","NXYH");
            bankNameCodeMap.put("农村合作银行","ZJRCU");
            bankNameCodeMap.put("农村商业银行","NCSY");
            bankNameCodeMap.put("农村信用合作社","NXS");
            bankNameCodeMap.put("攀枝花农村商业银行","PZHRCB");
            bankNameCodeMap.put("攀枝花市商业银行","PZHCCB");
            bankNameCodeMap.put("盘古银行","BANGKOKBANK");
            bankNameCodeMap.put("盘锦市商业银行","PJSCCB");
            bankNameCodeMap.put("平安银行(深圳发展银行)","SDB");
            bankNameCodeMap.put("平顶山银行","PDSYH");
            bankNameCodeMap.put("濮阳银行","PYYH");
            bankNameCodeMap.put("齐鲁银行","QLYH");
            bankNameCodeMap.put("齐商银行","QSYH");
            bankNameCodeMap.put("祁阳村镇银行","QYCZB");
            bankNameCodeMap.put("秦皇岛银行","QHDYH");
            bankNameCodeMap.put("青岛银行","QDYH");
            bankNameCodeMap.put("青海银行","QHYH");
            bankNameCodeMap.put("曲靖市商业银行","QJSCCB");
            bankNameCodeMap.put("泉州农村商业银行","QZRCB");
            bankNameCodeMap.put("泉州银行","QZYH");
            bankNameCodeMap.put("日本山口银行","JAPANYAMAGUCHIBANK");
            bankNameCodeMap.put("日本住友信托银行","SUMITOMOTB");
            bankNameCodeMap.put("日照银行","RZYH");
            bankNameCodeMap.put("瑞典北欧斯安银行有限公司","RDBOSA");
            bankNameCodeMap.put("瑞典商业银行公共","SWEDISHCB");
            bankNameCodeMap.put("瑞典银行有限公司","RDYHYXGS");
            bankNameCodeMap.put("瑞士信贷银行","SWISSCB");
            bankNameCodeMap.put("瑞士银行有限公司","UBS");
            bankNameCodeMap.put("瑞穗实业银行（中国）有限公司","RSSY");
            bankNameCodeMap.put("三井住友银行（中国）有限公司","SMBC");
            bankNameCodeMap.put("三菱东京日联银行","TOKYOMITSUBISHIBANK");
            bankNameCodeMap.put("三门峡银行","SMXYH");
            bankNameCodeMap.put("三亚农村商业银行","SYRCB");
            bankNameCodeMap.put("厦门国际银行","XIB");
            bankNameCodeMap.put("厦门银行","XMYH");
            bankNameCodeMap.put("山东省农村信用社","SDNXS");
            bankNameCodeMap.put("汕尾农村商业银行","SWRCB");
            bankNameCodeMap.put("商丘银行","SQYH");
            bankNameCodeMap.put("上海农村商业银行","SHRCB");
            bankNameCodeMap.put("上海浦东发展银行","SPDB");
            bankNameCodeMap.put("上海商业银行","SHCB");
            bankNameCodeMap.put("上海银行","SHYH");
            bankNameCodeMap.put("绍兴银行","SXYH");
            bankNameCodeMap.put("深圳福田银座村镇银行","FTYZCZB");
            bankNameCodeMap.put("深圳农村商业银行","SZRCB");
            bankNameCodeMap.put("盛京银行","SJYH");
            bankNameCodeMap.put("石嘴山银行","SZSYH");
            bankNameCodeMap.put("顺德农村商业银行","SDRCB");
            bankNameCodeMap.put("四川叙永农村商业银行","SCXYRCB");
            bankNameCodeMap.put("四川仪陇农村商业银行","SCYLRCB");
            bankNameCodeMap.put("苏格兰皇家银行（中国）有限公司","SGLHJYH");
            bankNameCodeMap.put("苏州银行","SZYH");
            bankNameCodeMap.put("遂宁市商业银行","SNSCCB");
            bankNameCodeMap.put("台州银行","TZYH");
            bankNameCodeMap.put("太仓农村商业银行","TCRCB");
            bankNameCodeMap.put("泰安市商业银行","TACCB");
            bankNameCodeMap.put("泰华农民银行大众","THAIFARMERSBANK");
            bankNameCodeMap.put("唐山市商业银行","TSSCCB");
            bankNameCodeMap.put("天津农村商业银行","TJRCB");
            bankNameCodeMap.put("天津市北辰村镇银行","TJSBCCZB");
            bankNameCodeMap.put("天津银行","TJYH");
            bankNameCodeMap.put("铁岭银行","TLYH");
            bankNameCodeMap.put("铜仁农村商业银行","TRRCB");
            bankNameCodeMap.put("威海市商业银行","WHCCB");
            bankNameCodeMap.put("潍坊银行","WFYH");
            bankNameCodeMap.put("温州银行","WZYH");
            bankNameCodeMap.put("乌海银行","WHYH");
            bankNameCodeMap.put("乌鲁木齐市商业银行","WLMQCCB");
            bankNameCodeMap.put("无锡农村商业银行","WXRCB");
            bankNameCodeMap.put("吴江农村商业银行","WJRCB");
            bankNameCodeMap.put("武威农村商业银行","WWRCB");
            bankNameCodeMap.put("西安银行","XAYH");
            bankNameCodeMap.put("西藏银行","XICYH");
            bankNameCodeMap.put("香港地区","HONGKONGAREA");
            bankNameCodeMap.put("新韩银行（中国）有限公司","XHYH");
            bankNameCodeMap.put("新乡银行","XXYH");
            bankNameCodeMap.put("星展银行","DBSBANK");
            bankNameCodeMap.put("邢台银行","XTYH");
            bankNameCodeMap.put("兴业银行","CIB");
            bankNameCodeMap.put("许昌银行","XCYH");
            bankNameCodeMap.put("雅安农村商业银行","YARCB");
            bankNameCodeMap.put("雅安市商业银行","YASCCB");
            bankNameCodeMap.put("烟台银行","YTYH");
            bankNameCodeMap.put("阳泉市商业银行","YQSCCB");
            bankNameCodeMap.put("宜宾市商业银行","YBSCCB");
            bankNameCodeMap.put("意大利联合圣保罗银行","YDLLHSBL");
            bankNameCodeMap.put("意大利罗马银行","ROMECB");
            bankNameCodeMap.put("银行间市场清算所","YHJSCQSS");
            bankNameCodeMap.put("鄞州银行","YZYH");
            bankNameCodeMap.put("英国巴克莱银行有限公司","YGBLKYH");
            bankNameCodeMap.put("营口融生农村商业银行","YKRSRCB");
            bankNameCodeMap.put("营口沿海银行","YKYHYH");
            bankNameCodeMap.put("营口银行","YKYH");
            bankNameCodeMap.put("永亨银行（中国）有限公司","WHBCN");
            bankNameCodeMap.put("永隆银行有限公司","WINGLBANK");
            bankNameCodeMap.put("友利银行（中国）有限公司","YLYH");
            bankNameCodeMap.put("玉溪市商业银行","YXSCCB");
            bankNameCodeMap.put("云南省农村信用社","YNNXS");
            bankNameCodeMap.put("枣庄银行","ZAZYH");
            bankNameCodeMap.put("渣打银行（中国）有限责任公司","ZDYH");
            bankNameCodeMap.put("张家港农村商业银行","ZJGRCB");
            bankNameCodeMap.put("张家口银行","ZJKYH");
            bankNameCodeMap.put("长安银行","CAYH");
            bankNameCodeMap.put("长春发展农村商业银行","CCFZRCB");
            bankNameCodeMap.put("长沙商业银行","CSYH");
            bankNameCodeMap.put("长沙银行","CSCB");
            bankNameCodeMap.put("长治银行","CHANGZYH");
            bankNameCodeMap.put("招商银行","CMBCHINA");
            bankNameCodeMap.put("肇庆端州农村商业银行","ZQDZRCB");
            bankNameCodeMap.put("浙江稠州商业银行","CZCB");
            bankNameCodeMap.put("浙江景宁银座村镇银行","JNYZCZB");
            bankNameCodeMap.put("浙江民泰商业银行","MINTAIBANK");
            bankNameCodeMap.put("浙江三门银座村镇银行","SMYZCZB");
            bankNameCodeMap.put("浙江省农村信用社","ZJNXS");
            bankNameCodeMap.put("浙江泰隆商业银行","tlsyyh");
            bankNameCodeMap.put("浙商银行","CZ");
            bankNameCodeMap.put("郑州银行","ZHENGZYH");
            bankNameCodeMap.put("支付业务收费专户","AFYWSFZH");
            bankNameCodeMap.put("中德住房储蓄银行有限责任公司","SGB");
            bankNameCodeMap.put("中国工商银行","ICBC");
            bankNameCodeMap.put("中国光大银行","CEB");
            bankNameCodeMap.put("中国建设银行","CCB");
            bankNameCodeMap.put("中国进出口银行","EXIMBANK");
            bankNameCodeMap.put("中国民生银行","CMBC");
            bankNameCodeMap.put("中国农业发展银行","NYFZYH");
            bankNameCodeMap.put("中国农业银行","ABC");
            bankNameCodeMap.put("中国农业银行股份有限公司北京","ABCBJ");
            bankNameCodeMap.put("中国人民银行","PBC");
            bankNameCodeMap.put("中国外汇交易中心","ZGWHJYZX");
            bankNameCodeMap.put("中国银行","BOC");
            bankNameCodeMap.put("中国银联","CHINAUNIONPAY");
            bankNameCodeMap.put("中国邮政储蓄","PSBC");
            bankNameCodeMap.put("中国邮政储蓄银行","POST");
            bankNameCodeMap.put("中山农村商业银行","ZSRCB");
            bankNameCodeMap.put("中信嘉华银行","CITICKAWAHBANK");
            bankNameCodeMap.put("中信银行","ECITIC");
            bankNameCodeMap.put("中央国债登记结算有限责任公司","zygzdjjs");
            bankNameCodeMap.put("中原银行","ZYYH");
            bankNameCodeMap.put("重庆农村商业银行","CQRCB");
            bankNameCodeMap.put("重庆黔江银座村镇银行","QJYZCZB");
            bankNameCodeMap.put("重庆三峡银行","CQSXYH");
            bankNameCodeMap.put("重庆银行","CQYH");
            bankNameCodeMap.put("重庆渝北银座村镇银行","YBYZCZB");
            bankNameCodeMap.put("周口银行","ZKYH");
            bankNameCodeMap.put("珠海华润银行","ZHCCB");
            bankNameCodeMap.put("珠海农村商业银行","ZHRCB");
            bankNameCodeMap.put("自贡市商业银行","ZGCCB");
        }
        return bankNameCodeMap ;
    }
    

    /**
     * 被动处理
     */
    @Override
    public SyspayRecordInf fedbackCheckAndDoBusi(DoPayFedbackBusiReq fedbackInf, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException {
        throw new BaseServiceException("易宝代付未支持被动通知处理");
        // return null;
    }

}
