package com.sdhoo.pdloan.payctr.service.impl;

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

import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.service.ServiceValidator;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctBankInfo;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctBankInfoService;
import com.sdhoo.pdloan.bcrud.service.DtPctSysPayExtraService;
import com.sdhoo.pdloan.bcrud.service.DtPctSysPayRcdService;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.dto.SyspayRecordInf;
import com.sdhoo.pdloan.payctr.enums.SyspayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.service.IfcChnlCfgService;
import com.sdhoo.pdloan.payctr.service.PayctrBeanProcService;
import com.sdhoo.pdloan.payctr.service.SysPayCacheService;
import com.sdhoo.pdloan.payctr.service.SysPayChnlService;
import com.sdhoo.pdloan.payctr.service.SysPayService;
import com.sdhoo.pdloan.payctr.service.dto.SysPayChnlServiceAndCfg;
import com.sdhoo.pdloan.payctr.service.req.DoCreateSysPayReq;
import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;

/**
 * 代付功能实现.
 * @author SDPC_LIU
 *
 */
@Service
public class SysPayServiceImpl implements SysPayService {
    
    /**
     * 日志服务.
     */
    private static final Logger logger = LoggerFactory.getLogger(SysPayServiceImpl.class); 

    
    /**
     * 服务端验证
     */
    @Autowired
    private ServiceValidator serviceValidator ;


    /**
     * ID生成器 
     */
    @Autowired
    private PctIdGenService pctIdGenService ;
    
    /**
     * 代付缓存服务.
     */
    @Autowired
    private SysPayCacheService sysPayCacheService ;

    /**
     * 代付记录服务.
     */
    @Autowired
    private DtPctSysPayRcdService pctSysPayRcdService ;
    
    /**
     * 扩展记录服务.
     */
    @Autowired
    private DtPctSysPayExtraService pctSysPayExtraService ;
    
    @Autowired
    private PayctrBeanProcService paycenterBeanProcService ;
    
    @Autowired
    private DtPctBankInfoService dtPctBankInfoService ;
    
    @Autowired
    private IfcChnlCfgService ifcChnlCfgService ;
    

    /**
     * 支付编号前缀
     */
    @Value(value="${paycenter.syspay.sprcode_pre}")
    private String sprCodePre ;


    /**
     *  等待支付创建中的时间点,0秒,半秒,1秒,2秒,5秒,10秒,15秒,20秒. 
     */
    private static int[] checkWaiteCreateMillSecs = {0,500,500,1000,2000,2000,4000,5000,10000}; 
    


    
    
    @Override
    public String genSysPayCode() {
        PctIdData genNextRcdId = pctIdGenService.genAndCachePayctIdData();
        long newId = genNextRcdId.getIdVal();
        String payCode = sprCodePre + newId;
        // 用户ID小于10000的新浪渠道需要特殊处理.
        sysPayCacheService.doAddPayCodeToPool(payCode); 
        return payCode;
    }

    /**
     * 根据银行名称获取银行编号
     * @param bankName
     * @return
     */
    private DtPctBankInfo getBankInfByName(String bankName ) {
        Map<String,Object> bankQmap = new HashMap<>(2);
        bankQmap.put("biName", bankName);
        List<DtPctBankInfo> bankInfs = dtPctBankInfoService.selectByCriteria(bankQmap, 0, 2);
        if(bankInfs.size() == 1 ) {
            return bankInfs.get(0);
        }else {
            return null ;
        }
    }

    /**
     * 无需事务控制
     */
    @Transactional(propagation=Propagation.NEVER,rollbackFor= {})
    @Override
    public SyspayRecordInf doCreateSysPay(DoCreateSysPayReq req) throws BaseServiceException {
        serviceValidator.validate(req); 

        String payCode = req.getSprCode();
        Integer ifcChnlId = req.getIfcChnlId(); // 渠道ID 
        Long ifcChnlCfgId = req.getIfcChnlCfgId(); // 渠道配置ID 
        SyspayIfcChnlEnum payEnum = SyspayIfcChnlEnum.getByCode(ifcChnlId); 
        String logPre = "系统支付中心接口->(payDode="+payCode+")指定渠道("+payEnum+")发起代付-->";
        
        
        // 核对支付号是否有效.
        boolean checkRt = sysPayCacheService.checkAndGetPayCodeFromPool(payCode);
        if(!checkRt){
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付编号已过期.");
        }
        try{
            // 锁定支付创建操作.
            sysPayCacheService.cacheLockPayCode(payCode);
            String payeeChnlCode = req.getPayeeChnlCode();
            String payeeChnlName = req.getPayeeChnlName();
            if(StringUtils.isEmpty(payeeChnlCode)) {
                DtPctBankInfo bankInfByName = getBankInfByName(payeeChnlName);
                if(bankInfByName == null ) {
                    throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "收款渠道无效");
                }
                req.setPayeeChnlCode(bankInfByName.getBiCode()); 
            }
            SysPayChnlServiceAndCfg chnlServiceAndCfg = ifcChnlCfgService.getSysPayChnlServiceAndCfg(null, ifcChnlId, ifcChnlCfgId);
            DtPctIfcChnlCfg pctIfcChnkCfg = chnlServiceAndCfg.getPctIfcChnkCfg();
            SysPayChnlService sysPayChnlService = chnlServiceAndCfg.getSysPayChnlService();
            req.setIfcChnlId(pctIfcChnkCfg.getIfcChnlId());
            req.setIfcChnlCfgId(pctIfcChnkCfg.getIccId()); 
            
            SyspayRecordInf payRcd = sysPayChnlService.doCreateSysPayRecord(req, pctIfcChnkCfg);
            // 支付发起成功,

            // 编号-id 关联信息缓存
            sysPayCacheService.cachePaycodeRelPayId(payRcd.getSprCode(),payRcd.getSprId()); 

            // 返回值赋值.
            return payRcd;

        }catch (Exception e) { // 发起失败 
            if(e instanceof BaseServiceException ){
                BaseServiceException bse = (BaseServiceException)e ;
                logger.warn(logPre+"创建代付出现业务异常.",bse);
                throw bse;
            }else{
                logger.error(logPre+"创建代付出现未知异常.",e);
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "支付创建异常");
            }
        }finally {
            // 解锁代付创建操作.
            sysPayCacheService.freeCacheLockPayCode(payCode);
        }
    }


    @Override
    public SyspayRecordInf doGetSysPayRcd(String sprCode) {
        Long sprId;
        DtPctSysPayRcd upr = getSprByCode(sprCode);
        if(upr == null) {
            return null ;
        }
        sprId = upr.getSprId();
        DtPctSysPayExtra upe = pctSysPayExtraService.getByPrimKey(sprId, null);
        return paycenterBeanProcService.genSysPayRecordInfByModel(upr, upe);
    }


    @Override
    public SyspayRecordInf doPayRcdCheckAndDoBusi(String sprCode) throws BaseServiceException {

        String logPre = "代付(payCode="+sprCode+")主动核对处理-->"; 
        // 等待发起结束.
        waiteForPayCreateFinish(sprCode); 

        DtPctSysPayRcd spr = getSprByCode(sprCode);
        if( spr == null ){
            logger.warn(logPre + "找不到支付记录导致核对失败"); 
            return null ;
        }
        Integer ifcChnlId = spr.getIfcChnlId(); 
        Long ifcChnlCfgId = spr.getIfcChnlCfgId();

		SysPayChnlServiceAndCfg chnlServiceAndCfg = ifcChnlCfgService.getSysPayChnlServiceAndCfg(null, ifcChnlId, ifcChnlCfgId);
		DtPctIfcChnlCfg pctIfcChnkCfg = chnlServiceAndCfg.getPctIfcChnkCfg();
		SysPayChnlService sysPayChnlService = chnlServiceAndCfg.getSysPayChnlService();

        SyspayRecordInf checkRst = sysPayChnlService.checkAndUpdatePayStatus(spr, pctIfcChnkCfg);
        return checkRst; 
    }


    @Override
    public SyspayRecordInf payfedbackCheckAndDoBusi(DoPayFedbackBusiReq req) throws BaseServiceException {
        
        serviceValidator.validate(req); 
        
        Integer ifcChnlId = req.getIfcChnlId();
        Long ifcChnlCfgId = req.getIfcChnlCfgId();

		SysPayChnlServiceAndCfg chnlServiceAndCfg = ifcChnlCfgService.getSysPayChnlServiceAndCfg(null, ifcChnlId, ifcChnlCfgId);
		DtPctIfcChnlCfg pctIfcChnkCfg = chnlServiceAndCfg.getPctIfcChnkCfg();
		SysPayChnlService sysPayChnlService = chnlServiceAndCfg.getSysPayChnlService();
        req.setIfcChnlId(pctIfcChnkCfg.getIfcChnlId());
        req.setIfcChnlCfgId(pctIfcChnkCfg.getIccId()); 
        SyspayRecordInf checkRst = sysPayChnlService.fedbackCheckAndDoBusi(req, pctIfcChnkCfg);
        return checkRst ;
    }


    /**
     * 获取支付记录
     * @param sprCode
     * @return
     */
    private DtPctSysPayRcd getSprByCode(String sprCode) {
        if(StringUtils.isEmpty(sprCode)) {
            return null ;
        }
        Long sprId = sysPayCacheService.getCachedPayIdByPaycode(sprCode);
        Map<String,Object> uprQmap = new HashMap<>(2);
        uprQmap.put("sprCode", sprCode);
        if(sprId != null ) {
            uprQmap.put("sprId", sprId);
        }
        List<DtPctSysPayRcd> payRcdRsts = pctSysPayRcdService.selectByCriteria(uprQmap, 0, 2);
        if(payRcdRsts.size() != 1 ) {
            logger.warn("根据支付编号获取代付信息结果不是1条数据,导致查询失败");
            return null ;
        }
        DtPctSysPayRcd upr = payRcdRsts.get(0);
        return upr;
    }
    
    /**
     * 等待支付创建结束,
     * @param payCode
     * @throws BaseServiceException 
     */
    private void waiteForPayCreateFinish(String payCode ) throws BaseServiceException{
        if(payCode == null ){
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "支付码不能为空" );
        }
        boolean checkRst = false ;
        for( int waiteMilSec : checkWaiteCreateMillSecs){
            boolean isFree = sysPayCacheService.checkCacheLockPayCodeIsfree(payCode);
            if(isFree){ 
                // 已被释放了,则不用继续下一等待了.
                checkRst = true ;
                break ; 
            }
            // 不为空,则说明未释放,
            try {
                Thread.sleep(waiteMilSec);
            } catch (InterruptedException e) {
                logger.warn("线程等待异常.");
            }
        }
        if(!checkRst){
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "业务线程等待超时");
        }
    }


}
