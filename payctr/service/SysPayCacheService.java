package com.sdhoo.pdloan.payctr.service;

import com.sdhoo.pdloan.payctr.dto.SyspayDftCfgInf;

/**
 * 支付中心缓存服务.
 * @author SDPC_LIU
 *
 */
public interface SysPayCacheService {


	/**
	 * 缓存支付码关联支付ID 
	 * @param payCode
	 * @param payId
	 */
	void cachePaycodeRelPayId(String payCode, Long payId);


	/**
	 * 根据支付码从缓存获取支付ID 
	 * @param payCode
	 * @return
	 */
	Long getCachedPayIdByPaycode(String payCode);


	/**
	 * 缓存支付码至支付码池.
	 * @param payCode
	 */
	void doAddPayCodeToPool(String payCode);


	/**
	 * 从支付码池核对并提取支付码.提取后,池中就没有该支付码.
	 * @param payCode
	 * @return
	 */
	boolean checkAndGetPayCodeFromPool(String payCode);

	/**
	 * 缓存支付码锁缓存
	 * @param payCode
	 */
	void cacheLockPayCode(String payCode);

	/**
	 * 释放支付码锁缓存
	 * @param payCode
	 */
	void freeCacheLockPayCode(String payCode);

	/**
	 * 核对缓存锁是否已被释放
	 * @param payCode
	 * @return 是否已被释放
	 */
	boolean checkCacheLockPayCodeIsfree(String payCode);


	/**
	 * 获取代付默认配置
	 * @return
	 */
    SyspayDftCfgInf getCachedSyspayDftCfgInf();

    /**
     * 代付默认配置缓存
     * @param cachedInf
     */
    void cacheSyspayDftCfgInf(SyspayDftCfgInf cachedInf);


//	/**
//	 * 添加用户指定渠道当天(自然天)的支付发起次数
//	 * @param chnlEnum
//	 * @param userId
//	 */
//	void incUserCudayPaycreatCnt(UserPayChanelEnum chnlEnum, Long userId);
//
//	/**
//	 * 获取指定渠道用户当天(自然天)的支付发起次数.
//	 * @param chnlEnum
//	 * @param userId
//	 * @return
//	 */
//	int getUserCudayPaycreateCnt(UserPayChanelEnum chnlEnum, Long userId);



}
