package com.sdhoo.pdloan.payctr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.service.ServiceValidator;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctBankInfo;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlUserAcc;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlUserExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctIfcChnlUserAccService;
import com.sdhoo.pdloan.bcrud.service.DtPctIfcChnlUserExtraService;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayExtraService;
import com.sdhoo.pdloan.bcrud.service.DtPctUserPayRcdService;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.dto.UserAccRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserPayRecordInf;
import com.sdhoo.pdloan.payctr.enums.BankInfStepEnum;
import com.sdhoo.pdloan.payctr.enums.UserAccMainStatusEnum;
import com.sdhoo.pdloan.payctr.enums.UserAccStepStatusEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayTypeEnum;
import com.sdhoo.pdloan.payctr.service.BankInfService;
import com.sdhoo.pdloan.payctr.service.IfcChnlCfgService;
import com.sdhoo.pdloan.payctr.service.PayctrBeanProcService;
import com.sdhoo.pdloan.payctr.service.UserPayCacheService;
import com.sdhoo.pdloan.payctr.service.UserPayChnlService;
import com.sdhoo.pdloan.payctr.service.UserPayService;
import com.sdhoo.pdloan.payctr.service.dto.UserPayChnlServiceAndCfg;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccResendVerifyReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccVerifyCheckReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrPayReq;
import com.sdhoo.pdloan.payctr.service.req.DoGetUsrAccRecordInfReq;
import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;

/**
 * 用户支付服务实现类
 *
 * @author SDPC_LIU
 */
@Service
public class UserPayServiceImpl implements UserPayService {

    private static final Logger logger = LoggerFactory.getLogger(UserPayServiceImpl.class);

    /**
     * 服务端验证
     */
    @Autowired
    private ServiceValidator serviceValidator;

    /**
     * 用户支付缓存服务.
     */
    @Autowired
    private UserPayCacheService userPayCacheService;

    @Autowired
    private PctIdGenService pctIdGenService;

    @Autowired
    private DtPctUserPayRcdService dtPctUserPayRcdService;

    @Autowired
    private DtPctUserPayExtraService dtPctUserPayExtraService;

    @Autowired
    private PayctrBeanProcService paycenterBeanProcService;

    @Autowired
    private IfcChnlCfgService ifcChnlCfgService;

    @Autowired
    private DtPctIfcChnlUserAccService dtPctIfcChnlUserAccService;

    @Autowired
    private DtPctIfcChnlUserExtraService dtPctIfcChnlUserExtraService;

    /**
     * 等待支付创建中的时间点,0秒,半秒,1秒,2秒,5秒,10秒,15秒,20秒.
     */
    private static int[] checkWaiteCreateMillSecs = {0, 500, 500, 1000, 2000, 2000, 4000, 5000, 10000};

    /**
     * 支付编号前缀
     */
    @Value(value = "${paycenter.busi.uprcode_pre}")
    private String uprCodePre;

    @Autowired
    private BankInfService bankInfService;

    /**
     * 支付渠道开户请求
     *
     * @throws BaseServiceException
     */
    @Override
    public UserAccRecordInf createUserAcc(DoCreateUsrAccReq req) throws BaseServiceException {

        serviceValidator.validate(req);
        String bankName = req.getBankName();

        DtPctBankInfo bankInf = bankInfService.getBankInfByName(bankName);

        if (bankInf == null || !BankInfStepEnum.AVALABLE.equals(BankInfStepEnum.getByCode(bankInf.getStepStatus()))) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "银行未支持");
        }
        // 补充入库数据
        req.setBankCode(bankInf.getBiCode());

        Long userId = req.getUserId();
        Integer ifcChnlId = req.getIfcChnlId();
        Long ifcChnlCfgId = req.getIfcChnlCfgId();
        String logPre = "用户开户流程接口-->[创建并开始开户]被调用了-->关键入参(userId=" + userId + ",ifcChnlId=" + ifcChnlId + ",cfgId=" + ifcChnlCfgId + ")-->";
        logger.debug(logPre);
        UserPayChnlServiceAndCfg payChnlServiceAndCfg = ifcChnlCfgService.getUserPayChnlServiceAndCfg(UserPayTypeEnum.DUDC.getCode(), ifcChnlId, ifcChnlCfgId);
        UserPayChnlService userPayChnlService = payChnlServiceAndCfg.getUserPayChnlService();
        DtPctIfcChnlCfg pctIfcChnkCfg = payChnlServiceAndCfg.getPctIfcChnkCfg();
        req.setIfcChnlId(pctIfcChnkCfg.getIfcChnlId());
        req.setIfcChnlCfgId(pctIfcChnkCfg.getIccId());
        UserAccRecordInf userAccInfo = userPayChnlService.openAccount(req, pctIfcChnkCfg);

        return userAccInfo;
    }

    @Override
    public UserAccRecordInf createUserAccResendVerify(DoCreateUsrAccResendVerifyReq req) throws BaseServiceException {

        serviceValidator.validate(req);
        Long ifcChnlCfgId = req.getIfcChnlCfgId();
        Integer ifcChnlId = req.getIfcChnlId();
        Long icuaId = req.getIcuaId();
        String logPre = "用户开户流程接口-->[重发验证码]被调用了-->";
        logger.debug(logPre + "关键入参:(icuaId=" + icuaId + ")");
        DtPctIfcChnlUserAcc icuaInf = dtPctIfcChnlUserAccService.getByPrimKey(icuaId, null);
        if (icuaInf == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "开户信息无效");
        }
        ifcChnlCfgId = icuaInf.getIfcChnlCfgId();

        UserPayChnlServiceAndCfg payChnlServiceAndCfg = ifcChnlCfgService.getUserPayChnlServiceAndCfg(UserPayTypeEnum.DUDC.getCode(), ifcChnlId, ifcChnlCfgId);
        UserPayChnlService userPayChnlService = payChnlServiceAndCfg.getUserPayChnlService();
        DtPctIfcChnlCfg pctIfcChnkCfg = payChnlServiceAndCfg.getPctIfcChnkCfg();
        req.setIfcChnlId(pctIfcChnkCfg.getIfcChnlId());
        req.setIfcChnlCfgId(pctIfcChnkCfg.getIccId());
        UserAccRecordInf userAccInfo = userPayChnlService.toCreateUserAccResendVerify(req, pctIfcChnkCfg);
        return userAccInfo;
    }


    @Override
    public UserAccRecordInf createUserAccVerifyCheck(DoCreateUsrAccVerifyCheckReq req) throws BaseServiceException {
        serviceValidator.validate(req);
        Long icuaId = req.getIcuaId();
        Integer ifcChnlId = req.getIfcChnlId();
        Long userId = req.getUserId();
        Long ifcChnlCfgId = req.getIfcChnlCfgId();
        String logPre = "用户开户流程接口-->[验证码确认绑卡]被调用了-->关键入参(userId=" + userId + ",ifcChnlId=" + ifcChnlId + ",cfgId=" + ifcChnlCfgId + ")-->";

        DtPctIfcChnlUserAcc icuaInf = dtPctIfcChnlUserAccService.getByPrimKey(icuaId, null);
        if (icuaInf == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "开户信息无效");
        }
        ifcChnlCfgId = icuaInf.getIfcChnlCfgId();
        logger.debug(logPre);
        UserPayChnlServiceAndCfg payChnlServiceAndCfg = ifcChnlCfgService.getUserPayChnlServiceAndCfg(UserPayTypeEnum.DUDC.getCode(), ifcChnlId, ifcChnlCfgId);
        UserPayChnlService userPayChnlService = payChnlServiceAndCfg.getUserPayChnlService();
        DtPctIfcChnlCfg pctIfcChnkCfg = payChnlServiceAndCfg.getPctIfcChnkCfg();
        req.setIfcChnlId(pctIfcChnkCfg.getIfcChnlId());
        req.setIfcChnlCfgId(pctIfcChnkCfg.getIccId());
        UserAccRecordInf userAccInfo = userPayChnlService.toBindCheck(req, pctIfcChnkCfg);
        return userAccInfo;
    }

    @Override
    public UserAccRecordInf getIfcChnlUserMainAcc(DoGetUsrAccRecordInfReq req) throws BaseServiceException {

        serviceValidator.validate(req);
        Long userId = req.getUserId();
        Integer ifcChnlId = req.getIfcChnlId();
        Long ifcChnlCfgId = req.getIfcChnlCfgId();
        String logPre = "获取开户信息,关键入参:(userId=" + userId + ",ifcChnlCfgId=" + ifcChnlCfgId + ",ifcChnlId=" + ifcChnlId + ")";
        logger.debug(logPre);
        UserPayChnlServiceAndCfg payChnlServiceAndCfg = ifcChnlCfgService.getUserPayChnlServiceAndCfg(UserPayTypeEnum.DUDC.getCode(), ifcChnlId, ifcChnlCfgId);
//        UserPayChnlService userPayChnlService = payChnlServiceAndCfg.getUserPayChnlService();
        DtPctIfcChnlCfg pctIfcChnkCfg = payChnlServiceAndCfg.getPctIfcChnkCfg();

        UserAccRecordInf tmpInf = new UserAccRecordInf();
        Map<String, Object> accQmap = new HashMap<>();
        accQmap.put("ifcChnlCfgId", pctIfcChnkCfg.getIccId());
        accQmap.put("ifcChnlId", pctIfcChnkCfg.getIfcChnlId());
        accQmap.put("userId", userId);
        accQmap.put("stepStatus", UserAccStepStatusEnum.SUCCESS.getCode()); // 开户成功
        accQmap.put("mainAccStatus", UserAccMainStatusEnum.MAIN.getCode()); // 主卡
        List<String> orderBy_conditionList = new ArrayList<>();
        orderBy_conditionList.add("icuaId_desc");
        accQmap.put("orderBy_conditionList", orderBy_conditionList);
        List<DtPctIfcChnlUserAcc> accList = dtPctIfcChnlUserAccService.selectByCriteria(accQmap, 0, 1);
        if (accList != null && accList.size() > 0) {
            DtPctIfcChnlUserAcc userAcc = accList.get(0);
            DtPctIfcChnlUserExtra extra = dtPctIfcChnlUserExtraService.getByPrimKey(userAcc.getIcuaId(), null);
            tmpInf = paycenterBeanProcService.genUserAccRecordByModel(userAcc, extra);
        }
        return tmpInf;
    }

    /**
     * 根据ID获取开户信息
     */
    @Override
    public UserAccRecordInf getUserAccRecordById(Long icuaId) throws BaseServiceException {
        UserAccRecordInf tmpInf = null;
        DtPctIfcChnlUserAcc icuaInf = dtPctIfcChnlUserAccService.getByPrimKey(icuaId, null);
        DtPctIfcChnlUserExtra icueInf = null;
        if (icuaInf != null) {
            icueInf = dtPctIfcChnlUserExtraService.getByPrimKey(icuaId, null);
        }
        tmpInf = paycenterBeanProcService.genUserAccRecordByModel(icuaInf, icueInf);
        return tmpInf;
    }

    @Override
    public List<UserAccRecordInf> queryIfcChnlUserAcc(DoGetUsrAccRecordInfReq req) throws BaseServiceException {

        serviceValidator.validate(req);
        Integer ifcChnlId = req.getIfcChnlId();
        Long ifcChnlCfgId = req.getIfcChnlCfgId();
        Long userId = req.getUserId();

        List<UserAccRecordInf> rtList = new ArrayList<>();
        Map<String, Object> pyuQmap = new HashMap<>();

        if (ifcChnlId != null) {
            pyuQmap.put("ifcChnlId", ifcChnlId);
        }
        if (ifcChnlCfgId != null) {
            pyuQmap.put("ifcChnlCfgId", ifcChnlCfgId);
        }

        // 通过请求获取定位使用的配置ID
        if (ifcChnlId != null) {
            pyuQmap.put("ifcChnlId", ifcChnlId);
        }
        if (ifcChnlCfgId != null) {
            pyuQmap.put("ifcChnlCfgId", ifcChnlCfgId);
        }
        pyuQmap.put("userId", userId);


        // 查开户信息
        List<DtPctIfcChnlUserAcc> pyuList = dtPctIfcChnlUserAccService.selectByCriteria(pyuQmap, 0, Integer.MAX_VALUE);
        List<Long> cfgId_inList = new ArrayList<>();
        for (DtPctIfcChnlUserAcc pctIfcChnlUserAcc : pyuList) {
            Long tmpPayCfgId = pctIfcChnlUserAcc.getIfcChnlCfgId();
            cfgId_inList.add(tmpPayCfgId);
        }

        if (pyuList != null && pyuList.size() > 0) {
            int pyuListSize = pyuList.size();
            Map<Long, DtPctIfcChnlUserExtra> userExtraDataMap = new HashMap<>();
            List<Long> icuaId_inList = new ArrayList<>();
            for (DtPctIfcChnlUserAcc tmpbean : pyuList) {
                icuaId_inList.add(tmpbean.getIcuaId());
            }
            Map<String, Object> extraQmap = new HashMap<>(2);
            extraQmap.put("icueId_inList", icuaId_inList);
            List<DtPctIfcChnlUserExtra> extraInfList = dtPctIfcChnlUserExtraService.selectByCriteria(extraQmap, 0, pyuListSize);
            for (DtPctIfcChnlUserExtra infExtra : extraInfList) {
                userExtraDataMap.put(infExtra.getIcueId(), infExtra);
            }

            for (DtPctIfcChnlUserAcc tmpbean : pyuList) {
                Long tmpIcuaId = tmpbean.getIcuaId();
                DtPctIfcChnlUserExtra userExtra = userExtraDataMap.get(tmpIcuaId);
                UserAccRecordInf uinfo = paycenterBeanProcService.genUserAccRecordByModel(tmpbean, userExtra);
                rtList.add(uinfo);
            }
        }

        return rtList;

    }


    @Override
    public String genUserPayCode(Long userId) {
        PctIdData genNextRcdId = pctIdGenService.genAndCachePayctIdData();
        long newId = genNextRcdId.getIdVal();
        String payCode = uprCodePre + newId;
        // 用户ID小于10000的新浪渠道需要特殊处理.
        userPayCacheService.doAddPayCodeToPool(payCode);
        return payCode;
    }


    /**
     * 无需事务控制
     */
    @Transactional(propagation = Propagation.NEVER, rollbackFor = {}) // 无需事务控制
    @Override
    public UserPayRecordInf createAndStartUserPay(DoCreateUsrPayReq req) throws BaseServiceException {
        serviceValidator.validate(req);

        Long userId = req.getUserId();
        String payCode = req.getPayCode();
        Map<String, Object> extraInf = req.getExtraInf();
        Integer ifcChnlId = req.getIfcChnlId();
        Long ifcChnlCfgId = req.getIfcChnlCfgId();
        Integer payType = req.getPayType();
        String logPre = "用户支付中心接口->关键入参(userId=" + userId + ",payCode=" + payCode + ",ifcChnlId=" + ifcChnlId + ",cfgId=" + ifcChnlCfgId + ")发起支付-->";

        // 核对支付号是否有效.
        boolean checkRt = userPayCacheService.checkAndGetPayCodeFromPool(payCode);
        if (!checkRt) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付编号已过期.");
        }

        // 通过请求获取定位使用的配置ID
        try {

            // 锁定支付创建操作.
            userPayCacheService.cacheLockPayCode(payCode);

            // 检查是否可以发起操作.
            logger.debug(logPre);
            UserPayChnlServiceAndCfg payChnlServiceAndCfg = ifcChnlCfgService.getUserPayChnlServiceAndCfg(payType, ifcChnlId, ifcChnlCfgId);
            UserPayChnlService userPayChnlService = payChnlServiceAndCfg.getUserPayChnlService();
            DtPctIfcChnlCfg pctIfcChnkCfg = payChnlServiceAndCfg.getPctIfcChnkCfg();
            req.setIfcChnlId(pctIfcChnkCfg.getIfcChnlId());
            req.setIfcChnlCfgId(pctIfcChnkCfg.getIccId());

            String checkCanpay = userPayChnlService.checkUsrCanUseChnl(userId, pctIfcChnkCfg, extraInf);
            UserPayIfcChnlEnum ifcChnlEnum = UserPayIfcChnlEnum.getByCode(pctIfcChnkCfg.getIfcChnlId());

            if (StringUtils.isNotEmpty(checkCanpay)) {
                logger.warn(logPre + "指定渠道支付校验失败,原因:" + checkCanpay);
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "渠道接口(" + ifcChnlEnum.getDesc() + ")验证失败,原因:" + checkCanpay);
            }

            // 添加渠道代扣发起次数
            userPayCacheService.incUserCudayPaycreatCnt(ifcChnlEnum, userId);
            UserPayRecordInf payRcd = userPayChnlService.doCreatePayRecord(req, pctIfcChnkCfg);
            // 支付发起成功,

            // 编号-id 关联信息缓存
            userPayCacheService.cachePaycodeRelPayId(payRcd.getPayCode(), payRcd.getUprId());

            // 返回值赋值.
            return payRcd;

        } catch (Exception e) { // 发起失败
            String reqJsn = JSONObject.toJSONString(req);
            if (e instanceof BaseServiceException) {
                BaseServiceException bse = (BaseServiceException) e;
                logger.warn(logPre + "创建支付出现业务异常-->请求JSN:" + reqJsn, bse);
                throw bse;
            } else {
                logger.error(logPre + "创建支付出现未知异常-->请求JSN:" + reqJsn, e);
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "支付创建异常");
            }
        } finally {
            // 解锁支付创建操作.
            userPayCacheService.freeCacheLockPayCode(payCode);
        }
    }

    @Override
    public UserPayRecordInf getUserPayRecordInf(String uprCode) {
        Long uprId;
        DtPctUserPayRcd upr = getUprByCode(uprCode);
        if (upr == null) {
            return null;
        }
        uprId = upr.getUprId();
        DtPctUserPayExtra upe = dtPctUserPayExtraService.getByPrimKey(uprId, null);
        return paycenterBeanProcService.genUsrPayRecordInfByModel(upr, upe);
    }

    /**
     * 获取支付记录
     *
     * @param uprCode
     * @return
     */
    private DtPctUserPayRcd getUprByCode(String uprCode) {
        if (StringUtils.isEmpty(uprCode)) {
            return null;
        }
        Long uprId = userPayCacheService.getCachedPayIdByPaycode(uprCode);
        Map<String, Object> uprQmap = new HashMap<>(2);
        uprQmap.put("uprCode", uprCode);
        if (uprId != null) {
            uprQmap.put("uprId", uprId);
        }
        List<DtPctUserPayRcd> payRcdRsts = dtPctUserPayRcdService.selectByCriteria(uprQmap, 0, 2);
        if (payRcdRsts.size() != 1) {
            logger.warn("根据支付编号获取支付信息结果不是1条数据,导致查询失败");
            return null;
        }
        DtPctUserPayRcd upr = payRcdRsts.get(0);
        return upr;
    }

    @Override
    public UserPayRecordInf payfedbackCheckAndDoBusi(DoPayFedbackBusiReq req) throws BaseServiceException {
        serviceValidator.validate(req);
        Integer ifcChnlId = req.getIfcChnlId();
        Long ifcChnlCfgId = req.getIfcChnlCfgId();
        Integer payType = req.getPayType();
        UserPayChnlServiceAndCfg payChnlServiceAndCfg = ifcChnlCfgService.getUserPayChnlServiceAndCfg(payType, ifcChnlId, ifcChnlCfgId);
        UserPayChnlService userPayChnlService = payChnlServiceAndCfg.getUserPayChnlService();
        DtPctIfcChnlCfg pctIfcChnkCfg = payChnlServiceAndCfg.getPctIfcChnkCfg();
        req.setIfcChnlId(pctIfcChnkCfg.getIfcChnlId());
        req.setIfcChnlCfgId(pctIfcChnkCfg.getIccId());
        UserPayRecordInf userPayRcd = userPayChnlService.payfedbackCheckAndDoBusi(req, pctIfcChnkCfg);
        return userPayRcd;
    }


    @Override
    public UserPayRecordInf payRecordCheckAndDoBusi(String uprCode) throws BaseServiceException {

        String logPre = "支付(payCode=" + uprCode + ")主动核对处理-->";
        // 等待发起结束.
        waiteForPayCreateFinish(uprCode);

        DtPctUserPayRcd upr = getUprByCode(uprCode);
        if (upr == null) {
            logger.warn(logPre + "找不到支付记录导致核对失败");
            return null;
        }
        Integer ifcChnlId = upr.getIfcChnlId();
        Long ifcChnlCfgId = upr.getIfcChnlCfgId();
        Integer payType = upr.getPayType();

        logger.debug(logPre);
        UserPayChnlServiceAndCfg payChnlServiceAndCfg = ifcChnlCfgService.getUserPayChnlServiceAndCfg(payType, ifcChnlId, ifcChnlCfgId);
        UserPayChnlService userPayChnlService = payChnlServiceAndCfg.getUserPayChnlService();
        DtPctIfcChnlCfg pctIfcChnkCfg = payChnlServiceAndCfg.getPctIfcChnkCfg();

        UserPayRecordInf checkRst = userPayChnlService.checkAndUpdatePayStatus(upr, pctIfcChnkCfg);
//        Integer payStepStatus = checkRst.getPayStepStatus();
        return checkRst;

    }


    /**
     * 等待支付创建结束,
     *
     * @param payCode
     * @throws BaseServiceException
     */
    private void waiteForPayCreateFinish(String payCode) throws BaseServiceException {
        if (payCode == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付码不能为空");
        }
        boolean checkRst = false;
        for (int waiteMilSec : checkWaiteCreateMillSecs) {
            boolean isFree = userPayCacheService.checkCacheLockPayCodeIsfree(payCode);
            if (isFree) {
                // 已被释放了,则不用继续下一等待了.
                checkRst = true;
                break;
            }
            // 不为空,则说明未释放,
            try {
                Thread.sleep(waiteMilSec);
            } catch (InterruptedException e) {
                logger.warn("线程等待异常.");
            }
        }
        if (!checkRst) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "业务线程等待超时");
        }
    }


}
