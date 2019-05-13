package com.sdhoo.pdloan.payctr.service.impl;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sdhoo.common.base.RegexConstants;
import com.sdhoo.common.base.service.BaseBytesCacheService;
import com.sdhoo.pdloan.payctr.dto.UserPayDftCfgInf;
import com.sdhoo.pdloan.payctr.enums.UserPayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.service.UserPayCacheService;

/**
 * 缓存实现类
 * @author SDPC_LIU
 *
 */
@Service
public class UserPayCacheServiceImpl implements UserPayCacheService {

	private static final Logger logger = LoggerFactory.getLogger(UserPayCacheServiceImpl.class);

	/**
	 *  支付编号关联支付ID缓存,
	 */
	private static final String PAYCODERELPAYIDCACHE_KEYPRE = "pct_usrpcdRelId:"; 

	/**
	 *  支付编号关联支付ID缓存时间(86400秒/24小时)(7200秒/2小时),
	 */
	private static final int PAYCODERELPAYIDCACHE_TIMESECS = 86400; 

	/**
	 *  支付订单号缓存,只有在该缓存里的支付码才可以发起支付.缓存时间看配置. 
	 */
	private static final String PAYCODEPOOLCACHE_KEYPRE = "pct_usrpcd4CrtPool:"; 
	
	/**
	 *  正常缓存时间为3600秒(1小时)
	 */
	private static final int PAYCODEPOOLCACHE_TIMESECS = 3600; 
	
	/**
	 *  支付创建中的缓存.
	 */
	private static final String PAYCREATELOCKCACHE_KEYPRE = "pct_usrpcdInCreate:"; 
	/**
	 *  支付创建中缓存时间(60秒)
	 */
	private static final int PAYCREATELOCKCACHE_TIMESECS = 60 ; 
	
	/**
	 *  指定渠道用户当天发起支付次数, :渠道id:用户ID
	 */
	private static final String USRCUDAYCREATEPAYCNTCACHE_KEYPRE = "pct_usrCudayCreatepayCnt:"; 

	
	/**
	 * 用户支付配置默认值
	 */
	private static final String CACHEKEY_USERPAYDFTCFG = "pct_userpaydftcfg";
	
	/**
	 * 用户支付默认值缓存时间
	 */
	private static final int CACHEKEY_USERPAYDFTCFG_EXPSEC = 60;
	
	
	/**
	 * 缓存服务. 
	 */
    @Autowired
	private BaseBytesCacheService baseBytesCacheService ;  

	/**
	 * 缓存支付码关联支付ID 
	 * @param payCode
	 * @param payId
	 */
	@Override
	public void cachePaycodeRelPayId(String payCode , Long payId) {
		String cacheKey = PAYCODERELPAYIDCACHE_KEYPRE + payCode ;
		String payIdStr = payId.toString();
		baseBytesCacheService.putAndExpireByteArray(cacheKey, payIdStr.getBytes(), PAYCODERELPAYIDCACHE_TIMESECS);
	}

	/**
	 * 根据支付码从缓存获取支付ID 
	 * @param payCode
	 * @return
	 */
	@Override
	public Long getCachedPayIdByPaycode(String payCode) {
		String logPre = "根据支付码("+payCode+")从缓存获取支付ID-->";
		Long rtVal = null ;
		
		String cacheKey = PAYCODERELPAYIDCACHE_KEYPRE + payCode ;
		
		byte[] byteArray = baseBytesCacheService.getByteArray(cacheKey);
		if(byteArray != null && byteArray.length > 0 ) {
			String idStr = new String(byteArray);
			if(idStr.matches(RegexConstants.regexStr_long)) {
				rtVal = Long.valueOf(idStr);
			}
		}
		logger.debug(logPre+"取到的支付ID:" + rtVal );
		return rtVal ;
	}

	/**
	 * 缓存支付码至码池中
	 * @param payCode
	 */
	@Override
	public void doAddPayCodeToPool(String payCode) {
	    // KEY值.
		String key = PAYCODEPOOLCACHE_KEYPRE + payCode ; 
		baseBytesCacheService.putAndExpireByteArray(key, payCode.getBytes(), PAYCODEPOOLCACHE_TIMESECS);
	}
	
	/**
	 * 
	 * 核对支付码池中是否含有指定支付码.
	 * @param payCode
	 * @return
	 * 
	 */
	@Override
	public boolean checkAndGetPayCodeFromPool(String payCode){ 
	    // KEY值. 
		String key = PAYCODEPOOLCACHE_KEYPRE + payCode ; 
		// 获取缓存值.
		byte[] byteArray = baseBytesCacheService.getByteArray(key); 
		// 如果缓存值是空的,则认为已过期不可用.
		if(byteArray == null){ 
			return false; 
		}else{
			baseBytesCacheService.remove(key); 
			return true ;
		}
	}
	
	
	/**
	 * 缓存支付码锁缓存
	 * @param payCode
	 */
	@Override
	public void cacheLockPayCode(String payCode) {
		String createLockKey=PAYCREATELOCKCACHE_KEYPRE+payCode; 
		baseBytesCacheService.putAndExpireByteArray(createLockKey, payCode.getBytes(), PAYCREATELOCKCACHE_TIMESECS);
	}

	/**
	 * 释放支付码锁缓存
	 * @param payCode
	 */
	@Override
	public void freeCacheLockPayCode(String payCode) {
		String createLockKey=PAYCREATELOCKCACHE_KEYPRE+payCode; 
		// 解锁支付创建操作.
		baseBytesCacheService.remove(createLockKey); 
	}

	/**
	 * 核对缓存锁是否已被释放
	 * @param payCode
	 * @return 是否已被释放
	 */
	@Override
	public boolean checkCacheLockPayCodeIsfree(String payCode) {
		String createLockKey=PAYCREATELOCKCACHE_KEYPRE+payCode; 
		byte[] byteArray = baseBytesCacheService.getByteArray(createLockKey);

		if(byteArray == null ||byteArray.length < 1 ) {
			return true ;
		}else {
			return false ;
		}
	}

	/**
	 * 添加用户指定渠道当天(自然天)的支付发起次数
	 * @param chnlEnum
	 * @param userId
	 */
	@Override
	public void incUserCudayPaycreatCnt(UserPayIfcChnlEnum chnlEnum , Long userId) {
		String cacheKey = USRCUDAYCREATEPAYCNTCACHE_KEYPRE + chnlEnum.getCode() + ":" + userId ;
		byte[] byteArray = baseBytesCacheService.getByteArray(cacheKey);

		// 计算过期时间
		Calendar cld = Calendar.getInstance();
		// 加一天 
		cld.add(Calendar.DATE, 1); 
		cld.set(Calendar.HOUR_OF_DAY,0);
		cld.set(Calendar.MINUTE,0);
		cld.set(Calendar.SECOND,0);
		cld.set(Calendar.MILLISECOND,0);
		Date currTime = new Date();

		// 秒数 
		int expSecond = Long.valueOf( ( cld.getTimeInMillis() - currTime.getTime() )/1000).intValue() ; 
		if(expSecond < 1 ){ 
		    // 至少缓存1秒 
			expSecond = 1 ;
		}
		if(byteArray == null ){
			byteArray = new byte[1];
			byteArray[0] = 1 ;
			baseBytesCacheService.putAndExpireByteArray(cacheKey, byteArray, expSecond);
		}else{
			int incVal = ( (int)byteArray[0] + 1 );
			byteArray[0] = (byte)incVal;
			baseBytesCacheService.putAndExpireByteArray(cacheKey, byteArray, expSecond);
		}
	}

	/**
	 * 获取指定渠道用户当天(自然天)的支付发起次数.
	 * @param chnlEnum
	 * @param userId
	 * @return
	 */
	@Override
	public int getUserCudayPaycreateCnt(UserPayIfcChnlEnum chnlEnum , Long userId) {

		String cacheKey = USRCUDAYCREATEPAYCNTCACHE_KEYPRE + chnlEnum.getCode() + ":" + userId ; 

		byte[] byteArray = baseBytesCacheService.getByteArray(cacheKey);
		if(byteArray == null ){ 
		    // 没有值,则添加一次并返回
			return 0 ; 
		}
		// 有值,则判断次数
		int currVal = (int)byteArray[0];
		return currVal ;
	}
	
	
	@Override
	public UserPayDftCfgInf getCachedUserpayDftCfgInf() {
	    String cacheKey = CACHEKEY_USERPAYDFTCFG ;
	    byte[] byteArray = baseBytesCacheService.getByteArray(cacheKey);
	    if(byteArray == null || byteArray.length < 5  ) {
	        return null ;
	    }else {
	        return JSONObject.parseObject(byteArray, UserPayDftCfgInf.class, Feature.IgnoreNotMatch);
	    }
	}
	
	@Override
	public void cacheUserpayDftCfgInf(UserPayDftCfgInf cfgInf) {
	    String cacheKey = CACHEKEY_USERPAYDFTCFG ;
	    byte[] jsonBytes = JSONObject.toJSONBytes(cfgInf, SerializerFeature.IgnoreErrorGetter);
	    if( jsonBytes != null && jsonBytes.length > 5 ) {
	        baseBytesCacheService.putAndExpireByteArray(cacheKey, jsonBytes, CACHEKEY_USERPAYDFTCFG_EXPSEC);
	    }
	}
	
	
	
}
