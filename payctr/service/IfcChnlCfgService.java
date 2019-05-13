package com.sdhoo.pdloan.payctr.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.dto.SyspayDftCfgInf;
import com.sdhoo.pdloan.payctr.dto.UserPayDftCfgInf;
import com.sdhoo.pdloan.payctr.service.dto.SysPayChnlServiceAndCfg;
import com.sdhoo.pdloan.payctr.service.dto.UserPayChnlServiceAndCfg;

/**
 * 渠道配置服务.
 * @author Administrator
 *
 */
public interface IfcChnlCfgService {


//    /**
//     * 获取缓存的支付中心渠道配置.
//     * @param cfgId
//     * @return
//     */
//    DtPctIfcChnlCfg getCachedPctIfcChnlCfg(Long cfgId);


	/**
	 * 获取缓存的配置
	 * @param cfgId
	 * @return
	 */
	DtPctIfcChnlCfg getCachedPctIfcChnlCfg(Long cfgId);
	
	/**
	 * 用户支付默认配置
	 * @return
	 */
	UserPayDftCfgInf getUserPayDftCfgInf();
	
    /**
     * 获取支付渠道服务及对应的配置 
     * @param payType
     * @param chnlId
     * @param cfgId
     * @return
     * @throws BaseServiceException
     */
	UserPayChnlServiceAndCfg getUserPayChnlServiceAndCfg(Integer payType, Integer chnlId, Long cfgId) throws BaseServiceException;

	/**
	 * 代付默认配置
	 * @return
	 */
	SyspayDftCfgInf getSyspayDftCfgInf();

	/**
	 * 获取系统代付渠道服务及对应的配置
	 * @param payType
	 * @param chnlId
	 * @param cfgId
	 * @return
	 * @throws BaseServiceException
	 */
	SysPayChnlServiceAndCfg getSysPayChnlServiceAndCfg(Integer payType, Integer chnlId, Long cfgId) throws BaseServiceException;





    
}
