package com.sdhoo.pdloan.payctr.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.service.BaseBytesCacheService;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctConfigInf;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.service.DtPctConfigInfService;
import com.sdhoo.pdloan.bcrud.service.DtPctIfcChnlCfgService;
import com.sdhoo.pdloan.payctr.PaycenterConstants;
import com.sdhoo.pdloan.payctr.dto.SyspayDftCfgInf;
import com.sdhoo.pdloan.payctr.dto.UserPayDftCfgInf;
import com.sdhoo.pdloan.payctr.enums.SyspayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.enums.UserPayTypeEnum;
import com.sdhoo.pdloan.payctr.service.IfcChnlCfgService;
import com.sdhoo.pdloan.payctr.service.SysPayCacheService;
import com.sdhoo.pdloan.payctr.service.SysPayChnlService;
import com.sdhoo.pdloan.payctr.service.UserPayCacheService;
import com.sdhoo.pdloan.payctr.service.UserPayChnlService;
import com.sdhoo.pdloan.payctr.service.dto.SysPayChnlServiceAndCfg;
import com.sdhoo.pdloan.payctr.service.dto.UserPayChnlServiceAndCfg;

/**
 * 服务实现类.
 * @author Administrator
 *
 */
@Service
public class IfcChnlCfgServiceImpl implements IfcChnlCfgService {
    

    /**
     * 缓存服务. 
     */
    @Autowired
    private BaseBytesCacheService baseBytesCacheService ;

    @Autowired
    private DtPctIfcChnlCfgService pctIfcChnlCfgService ;
    
    @Autowired
    private DtPctConfigInfService dtPctConfigInfService ;
    
    @Autowired
    private UserPayCacheService userPayCacheService ;

    /**
     * 缓存KEY 
     */
    private String cacheKey_cfgInf = "payct_ifcchnlcfg:";
    
//    /**
//     * 缓存KEY 
//     */
//    private String cacheKey_cfgInfByChnl = "payct_ifcchnlcfg_chnl_appkey:";

    /**
     * 每分钟取一次
     */
    private int cacheTimeSecd_cfgInf = 60; 
    

    /**
     * Bean服务
     */
    @Autowired
    private ApplicationContext applicationContext ; 
    
    /**
     * 渠道支付服务的Map 
     */
    private Map<String, UserPayChnlService> userPayChnlServiceMap = new HashMap<>(8) ; 
    

    /**
     * 渠道支付服务的Map 
     */
    private Map<String, SysPayChnlService> sysPayChnlServiceMap = new HashMap<>(8) ; 
    
    @Autowired
    private SysPayCacheService sysPayCacheService ;

    
    /**
     * 初始化方法.
     * @throws BaseServiceException
     */
    @PostConstruct
    public void init() throws BaseServiceException{
        Map<String, UserPayChnlService> channelServiceMap = applicationContext.getBeansOfType(UserPayChnlService.class); 
        userPayChnlServiceMap = channelServiceMap;
        UserPayDftCfgInf userpayDftCfgInf = getUserPayDftCfgInf();
        if(userpayDftCfgInf == null ) {
            throw new BaseServiceException("默认代扣配置信息不完整,请核对...");
        }
        Integer dudcPayChnlId = userpayDftCfgInf.getDudcPayChnlId();
        UserPayIfcChnlEnum payIfcChnlEnum = UserPayIfcChnlEnum.getByCode(dudcPayChnlId);
        UserPayChnlService dftDudcUserPayChnlService = getUserPayChnlServiceByEnum(payIfcChnlEnum);
        if(dftDudcUserPayChnlService == null ) {
            throw new BaseServiceException("默认用户配实现未找到,请核对...");
        }
        
        sysPayChnlServiceMap = applicationContext.getBeansOfType(SysPayChnlService.class); 
        
        SyspayDftCfgInf syspayDftCfgInf = getSyspayDftCfgInf();
        if(syspayDftCfgInf == null ) {
            throw new BaseServiceException("默认代付配置信息不完整,请核对...");
        }
        
        
    }


    @Override
    public DtPctIfcChnlCfg getCachedPctIfcChnlCfg(Long cfgId) {
        if(cfgId == null ) {
            return null ;
        }
        String key = cacheKey_cfgInf + cfgId; 
        DtPctIfcChnlCfg cfgInf = null ;
        byte[] bytes = baseBytesCacheService.getAndExpireByteArray(key, cacheTimeSecd_cfgInf);
        if( bytes != null && bytes.length > 10) {
            String jsonStr = new String(bytes); 
            cfgInf = JSONObject.parseObject(jsonStr, DtPctIfcChnlCfg.class); 
        }else {
            // 配置
            cfgInf = pctIfcChnlCfgService.getByPrimKey(cfgId, null);
            if(cfgInf != null ) {
                String jsonStr = JSONObject.toJSONString(cfgInf);
                baseBytesCacheService.putAndExpireByteArray(key, jsonStr.getBytes(), cacheTimeSecd_cfgInf);
            }
        }
        return cfgInf;
    }

//    @Override
//    public DtPctIfcChnlCfg getCachedPctIfcChnlCfgByChnlIdAndAppkey(Long ifcChnlId , String appKey) {
//        
//        if(ifcChnlId == null || appKey == null ) {
//            return null ;
//        }
//        String key = cacheKey_cfgInfByChnl + ifcChnlId + "_" + appKey ; 
//        DtPctIfcChnlCfg cfgInf = null ;
//        byte[] bytes = baseBytesCacheService.getAndExpireByteArray(key, cacheTimeSecd_cfgInf);
//        if( bytes != null && bytes.length > 10) {
//            String jsonStr = new String(bytes); 
//            cfgInf = JSONObject.parseObject(jsonStr, DtPctIfcChnlCfg.class); 
//        }else {
//            // 配置
//            Map<String,Object> cfgQmap = new HashMap<>(2);
//            cfgQmap.put("ifcChnlId", ifcChnlId);
//            cfgQmap.put("appKey", appKey );
//            List<DtPctIfcChnlCfg> cfgInfList = pctIfcChnlCfgService.selectByCriteria(cfgQmap, 0, 2);
//            if(cfgInfList.size() == 1 ) {
//                cfgInf = cfgInfList.get(0);
//            }
//            if(cfgInf != null ) { // 缓存 
//                String jsonStr = JSONObject.toJSONString(cfgInf);
//                baseBytesCacheService.putAndExpireByteArray(key, jsonStr.getBytes(), cacheTimeSecd_cfgInf);
//            }
//        }
//        return cfgInf;
//    }

    @Override
    public UserPayDftCfgInf getUserPayDftCfgInf() {

        UserPayDftCfgInf cachedInf = userPayCacheService.getCachedUserpayDftCfgInf();
        if(cachedInf == null ) {
            synchronized (PaycenterConstants.CFGKEY_USERDUDC_DFT_PAYCHNL_ID) {
                Map<String,Object> cfgQmap = new HashMap<>(4);
                List<String> cfgKey_inList = new ArrayList<>();
                cfgKey_inList.add(PaycenterConstants.CFGKEY_USERDUDC_DFT_PAYCHNL_ID);
                cfgKey_inList.add(PaycenterConstants.CFGKEY_USERDUDC_YIBAO_DFT_PAYCHNL_CFGID);
                cfgKey_inList.add(PaycenterConstants.CFGKEY_USERPAY_ALIPAY_DFT_PAYCHNL_CFGID);
                cfgKey_inList.add(PaycenterConstants.CFGKEY_USERDUDC_FUIOU_DFT_PAYCHNL_CFGID);
                cfgKey_inList.add(PaycenterConstants.CFGKEY_USERPAY_OFLINE_DFT_PAYCHNL_CFGID);
                cfgQmap.put("cfgKey_inList", cfgKey_inList);
                List<DtPctConfigInf> cfgInfList = dtPctConfigInfService.selectByCriteria(cfgQmap, 0, 20);
                if(cfgInfList == null ) {
                    return cachedInf ;
                }
                Map<String,DtPctConfigInf> cfgInfMap = new HashMap<>();
                for (DtPctConfigInf tmp_configInf : cfgInfList) {
                    cfgInfMap.put(tmp_configInf.getCfgKey(), tmp_configInf);
                }
                cachedInf = new UserPayDftCfgInf();
                DtPctConfigInf userdudcDftPayChnlId = cfgInfMap.get(PaycenterConstants.CFGKEY_USERDUDC_DFT_PAYCHNL_ID);
                if(userdudcDftPayChnlId != null && StringUtils.isNotEmpty(userdudcDftPayChnlId.getCfgVal())) {
                	Integer dudcPayChnlId = Integer.valueOf(userdudcDftPayChnlId.getCfgVal()) ;
                	cachedInf.setDudcPayChnlId(dudcPayChnlId);
                }
                DtPctConfigInf userdudcDftPayChnlCfgid = cfgInfMap.get(PaycenterConstants.CFGKEY_USERDUDC_YIBAO_DFT_PAYCHNL_CFGID);
                if(userdudcDftPayChnlCfgid != null && StringUtils.isNotEmpty(userdudcDftPayChnlCfgid.getCfgVal())) {
                	Long dudcYibaoCfgId = Long.valueOf(userdudcDftPayChnlCfgid.getCfgVal()) ;
                	cachedInf.setDudcYibaoPayChnlCfgId(dudcYibaoCfgId);
                }
                DtPctConfigInf userpayAlipayDftPayChnlCfgid = cfgInfMap.get(PaycenterConstants.CFGKEY_USERPAY_ALIPAY_DFT_PAYCHNL_CFGID);
                if(userpayAlipayDftPayChnlCfgid != null && StringUtils.isNotEmpty(userpayAlipayDftPayChnlCfgid.getCfgVal())) {
                	Long alipayCfgId = Long.valueOf(userpayAlipayDftPayChnlCfgid.getCfgVal()) ;
                	cachedInf.setAlipayChnlCfgId(alipayCfgId); 
                }
                DtPctConfigInf fuyouCfgIdInf = cfgInfMap.get(PaycenterConstants.CFGKEY_USERDUDC_FUIOU_DFT_PAYCHNL_CFGID);
                if(fuyouCfgIdInf != null && StringUtils.isNotEmpty(fuyouCfgIdInf.getCfgVal())) {
                	Long tmpCfgId = Long.valueOf(fuyouCfgIdInf.getCfgVal()) ;
                	cachedInf.setDudcFuiouPayChnlCfgId(tmpCfgId); 
                }
                DtPctConfigInf oflineCfgIdInf = cfgInfMap.get(PaycenterConstants.CFGKEY_USERPAY_OFLINE_DFT_PAYCHNL_CFGID);
                if(oflineCfgIdInf != null && StringUtils.isNotEmpty(oflineCfgIdInf.getCfgVal())) {
                	Long tmpCfgId = Long.valueOf(oflineCfgIdInf.getCfgVal()) ;
                	cachedInf.setOflineChnlCfgId(tmpCfgId); 
                }
                userPayCacheService.cacheUserpayDftCfgInf(cachedInf);
            }
        }
        return cachedInf;
    }

    /**
     * 根据枚举获取支付渠道服务.
     * @param chnlEnu
     * @return
     * @throws BaseServiceException 
     */
    private UserPayChnlService getUserPayChnlServiceByEnum(UserPayIfcChnlEnum chnlEnu){
        if(chnlEnu == null ) {
            throw new IllegalArgumentException("渠道枚举不能为空"); 
        }
        String serviceName = PaycenterConstants.USR_PAYCHNL_SERVICENAME_PRE + chnlEnu.getCode();
        UserPayChnlService usrPayChnlService = userPayChnlServiceMap.get(serviceName);
        if(usrPayChnlService == null) {
            String errorMsg = "支付渠道("+chnlEnu.getDesc()+")未支持";
            throw new IllegalArgumentException(errorMsg); 
        }
        return usrPayChnlService ;
    }

    /**
     * 获取支付服务及配置
     * @param payType
     * @param chnlId
     * @param cfgId
     * @return
     * @throws BaseServiceException
     */
    @Override
    public UserPayChnlServiceAndCfg getUserPayChnlServiceAndCfg(Integer payType , Integer chnlId , Long cfgId ) throws BaseServiceException {
    	UserPayChnlServiceAndCfg rtVal = null ;
    	if(payType == null && chnlId == null && cfgId == null ) {
    		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付类型,");
    	}

    	UserPayDftCfgInf dftCfgInf = null ; 
    	UserPayChnlService userPayChnlService = null ;
    	DtPctIfcChnlCfg pctIfcChnkCfg = null ;
    	
    	if(cfgId != null ) {
    		// 根据配置ID获取 
    		pctIfcChnkCfg = getCachedPctIfcChnlCfg(cfgId);
    		if(pctIfcChnkCfg == null ) {
    			throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付渠道配置未找到");
    		}
    		Integer ifcChnlId = pctIfcChnkCfg.getIfcChnlId();
    		UserPayIfcChnlEnum chnlEnum = UserPayIfcChnlEnum.getByCode(ifcChnlId);
    		userPayChnlService = getUserPayChnlServiceByEnum(chnlEnum);
    		if(userPayChnlService == null ) {
        		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付渠道服务未找到");
    		}
    	}else if ( chnlId != null ) {
    		// 根据渠道ID获取 
    		UserPayIfcChnlEnum chnlEnum = UserPayIfcChnlEnum.getByCode(chnlId);
    		userPayChnlService = getUserPayChnlServiceByEnum(chnlEnum);
    		if(userPayChnlService == null ) {
        		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付渠道服务未找到");
    		}
    		pctIfcChnkCfg = userPayChnlService.getDefaultChnlCfg();
    		if(pctIfcChnkCfg == null ) {
    			throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付渠道配置未找到");
    		}
    	}else if ( payType != null ) { 
    	 	UserPayTypeEnum payTypeEnum = UserPayTypeEnum.getByCode(payType);
        	if(payTypeEnum == null ) {
        		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付类型未支持");
        	}
        	if(UserPayTypeEnum.DUDC.equals(payTypeEnum)) {
        		// 默认代扣 
        		// 根据支付类型来获取 
        		if(dftCfgInf == null ) {
        			dftCfgInf = this.getUserPayDftCfgInf();
        		}
        		chnlId = dftCfgInf.getDudcPayChnlId();
        		UserPayIfcChnlEnum chnlEnum = UserPayIfcChnlEnum.getByCode(chnlId);
        		userPayChnlService = getUserPayChnlServiceByEnum(chnlEnum);
        		if(userPayChnlService == null ) {
        			throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付渠道获取错误");
        		}
        		pctIfcChnkCfg = userPayChnlService.getDefaultChnlCfg();
        		if(pctIfcChnkCfg == null ) {
        			throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付渠道配置获取错误");
        		}
        	}else {
        		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "指定支付类型获取配置未支持");
        	}
    	}else {
    		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "渠道实现及配置获取错误");
    	}
    	if(userPayChnlService == null || pctIfcChnkCfg == null ) {
    		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付服务渠道及配置获取错误");
    	}
    	if( userPayChnlService.getPayIfcChnlEnum().getCode() != pctIfcChnkCfg.getIfcChnlId() ) {
    		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "获取到的支付渠道与配置不一致");
    	}
    	rtVal = new UserPayChnlServiceAndCfg();
    	rtVal.setUserPayChnlService(userPayChnlService);
    	rtVal.setPctIfcChnkCfg(pctIfcChnkCfg);
    	return rtVal ;
    }
    

    @Override
    public SyspayDftCfgInf getSyspayDftCfgInf() {

        SyspayDftCfgInf cachedInf = sysPayCacheService.getCachedSyspayDftCfgInf();
        if(cachedInf == null ) {
            synchronized (PaycenterConstants.CFGKEY_SYSPAY_DFT_PAYCHNL_ID) {
                Map<String,Object> cfgQmap = new HashMap<>(4);
                List<String> cfgKey_inList = new ArrayList<>();
                cfgKey_inList.add(PaycenterConstants.CFGKEY_SYSPAY_DFT_PAYCHNL_ID);
                cfgKey_inList.add(PaycenterConstants.CFGKEY_SYSPAY_YIBAO_DFT_PAYCHNL_CFGID);
                cfgKey_inList.add(PaycenterConstants.CFGKEY_SYSPAY_FUIOU_DFT_PAYCHNL_CFGID);
                cfgKey_inList.add(PaycenterConstants.CFGKEY_SYSPAY_OFLINE_DFT_PAYCHNL_CFGID);
                cfgQmap.put("cfgKey_inList", cfgKey_inList);
                List<DtPctConfigInf> cfgInfList = dtPctConfigInfService.selectByCriteria(cfgQmap, 0, 20);
                if(cfgInfList == null ) {
                    return cachedInf ;
                }
                Map<String,DtPctConfigInf> cfgInfMap = new HashMap<>();
                for (DtPctConfigInf tmp_configInf : cfgInfList) {
                    cfgInfMap.put(tmp_configInf.getCfgKey(), tmp_configInf);
                }
                cachedInf = new SyspayDftCfgInf();
                
                DtPctConfigInf syspayChnl = cfgInfMap.get(PaycenterConstants.CFGKEY_SYSPAY_DFT_PAYCHNL_ID);
                if(syspayChnl != null ) {
                	Integer tmpCfgId = Integer.valueOf(syspayChnl.getCfgVal()) ;
                	cachedInf.setSysPayChnlId(tmpCfgId); 
                }
                DtPctConfigInf yibaoCfgId = cfgInfMap.get(PaycenterConstants.CFGKEY_SYSPAY_YIBAO_DFT_PAYCHNL_CFGID);
                if(yibaoCfgId != null ) {
                	Long tmpCfgId = Long.valueOf(yibaoCfgId.getCfgVal()) ;
                	cachedInf.setSysYibaoPayChnlCfgId(tmpCfgId);
                }
                DtPctConfigInf fuiouCfgId = cfgInfMap.get(PaycenterConstants.CFGKEY_SYSPAY_FUIOU_DFT_PAYCHNL_CFGID);
                if(fuiouCfgId  != null ) {
                	Long tmpCfgId = Long.valueOf(fuiouCfgId.getCfgVal()) ;
                	cachedInf.setSysFuiouPayChnlCfgId(tmpCfgId); 
                }
                DtPctConfigInf oflineCfgId = cfgInfMap.get(PaycenterConstants.CFGKEY_SYSPAY_OFLINE_DFT_PAYCHNL_CFGID);
                if(oflineCfgId  != null ) {
                	Long tmpCfgId = Long.valueOf(oflineCfgId.getCfgVal()) ;
                	cachedInf.setSysOflinePayChnlCfgId(tmpCfgId);
                }
                sysPayCacheService.cacheSyspayDftCfgInf(cachedInf);
            }
        }
        return cachedInf;
    }
    
    /**
     * 根据枚举获取支付渠道服务.
     * @param chnlEnu
     * @return
     * @throws BaseServiceException 
     */
    public SysPayChnlService getSysPayChnlServiceByEnum(SyspayIfcChnlEnum chnlEnu){
        if(chnlEnu == null ) {
            throw new IllegalArgumentException("渠道枚举不能为空"); 
        }
        String serviceName = PaycenterConstants.SYSPAY_CHNLSERVICENAME_PRE + chnlEnu.getCode();
        SysPayChnlService sysPayChnlService = sysPayChnlServiceMap.get(serviceName);
        if(sysPayChnlService == null) {
            String errorMsg = "代付渠道("+chnlEnu.getDesc()+")未支持";
            throw new IllegalArgumentException(errorMsg); 
        }
        return sysPayChnlService ;
    }

    @Override
    public SysPayChnlServiceAndCfg getSysPayChnlServiceAndCfg(Integer payType , Integer chnlId , Long cfgId ) throws BaseServiceException {
    	SysPayChnlServiceAndCfg rtVal = null ;
    	
    	SyspayDftCfgInf dftCfgInf = null ;
    	SysPayChnlService sysPayChnlService = null ; 
    	DtPctIfcChnlCfg pctIfcChnkCfg = null ;
    	
    	if(cfgId != null ) {
    		pctIfcChnkCfg = getCachedPctIfcChnlCfg(cfgId);
    		Integer ifcChnlId = pctIfcChnkCfg.getIfcChnlId();
    		SyspayIfcChnlEnum chnlEnum = SyspayIfcChnlEnum.getByCode(ifcChnlId);
			sysPayChnlService = getSysPayChnlServiceByEnum(chnlEnum);
    	}else if (chnlId != null) {
    		SyspayIfcChnlEnum chnlEnum = SyspayIfcChnlEnum.getByCode(chnlId);
    		sysPayChnlService = getSysPayChnlServiceByEnum(chnlEnum);
    		pctIfcChnkCfg = sysPayChnlService.getDefaultChnlCfg();
    	}else {
    		if(dftCfgInf == null ) {
    			dftCfgInf = this.getSyspayDftCfgInf();
    		}
    		chnlId = dftCfgInf.getSysPayChnlId();
    		SyspayIfcChnlEnum chnlEnum = SyspayIfcChnlEnum.getByCode(chnlId);
    		sysPayChnlService = getSysPayChnlServiceByEnum(chnlEnum);
    		pctIfcChnkCfg = sysPayChnlService.getDefaultChnlCfg();
		}

		if(sysPayChnlService == null || pctIfcChnkCfg == null ) {
    		throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "支付服务渠道及配置获取错误");
    	}
    	rtVal = new SysPayChnlServiceAndCfg();
    	rtVal.setSysPayChnlService(sysPayChnlService);
    	rtVal.setPctIfcChnkCfg(pctIfcChnkCfg);
    	return rtVal ;
    }
    

}
