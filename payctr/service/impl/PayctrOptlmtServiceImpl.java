package com.sdhoo.pdloan.payctr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.service.BaseBytesCacheService;
import com.sdhoo.pdloan.payctr.service.PayctrOptlmtService;

/**
 * 操作限制服务实现类.
 * @author SDPC_LIU
 *
 */
@Service
public class PayctrOptlmtServiceImpl implements PayctrOptlmtService {


    /**
     *  操作支付单限制.
     *  key = pre+sprId
     */
    public String CACHE_KEY_OPT_SPRID_PRE = "pct_optlmt:sprid_"; 
    
    /**
     *  操作支付单限制.
     *  key = pre+sprId
     */
    public String CACHE_KEY_OPT_UPRID_PRE = "pct_optlmt:uprid_"; 
    
    /**
     * 通知ID前缀 
     */
    public String CACHEKEY_NOTIFYID_PRE = "pct_notify:ponrid_";


    /**
     *  默认缓存时间(秒).
     */
    private Integer defaultExpSeconds = 1200 ; 

    /**
     * 缓存服务. 
     */
//    @Resource(name=PaycenterConstants.SERVICENAME_BASEBYTECACHE)
    @Autowired
    private BaseBytesCacheService baseBytesCacheService ;


    @Override
    public boolean checkAndExpSyspayLmtById(Long sprId , String optCode ,String pwd, boolean throwExp ) throws BaseServiceException{ 
        // ID缓存.
        String cacheKey = CACHE_KEY_OPT_SPRID_PRE + sprId + "_" + optCode ; 
        // 密码缓存 
        boolean putRst = baseBytesCacheService.putAndExpireByteArrayNx(cacheKey, pwd.getBytes(), defaultExpSeconds);
        if( putRst ){ 
        	// put 成功了,则返回成功 
            return true ;
        }else{
            // 是否抛异常,
            if(throwExp) { 
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_MINI, "业务处理中,请忽重复操作");
            }
            return false;
        }
    }

    @Override
    public boolean releaseSyspayLmtById(Object sprId , String optCode ,String pwd){
        // 订单缓存.
        String cacheKey = CACHE_KEY_OPT_SPRID_PRE + sprId + "_" + optCode ; 
        // 解锁失败 
        if(pwd == null || pwd.length() < 1 ){ 
            return false ;
        }
        // 没有密码
        byte[] pwdByteArray = baseBytesCacheService.getByteArray(cacheKey); 
        if(pwdByteArray != null && pwdByteArray.length > 0 ) {
            if(!pwd.equals( new String(pwdByteArray))) { 
                // 密码不对,返回错误
                return false ;
            }
        }
        baseBytesCacheService.remove(cacheKey); 
        // 解锁成功 
        return true ; 
    }

    @Override
    public boolean checkAndExpUsrpayLmtById(Long uprId , String optCode ,String pwd, boolean throwExp ) throws BaseServiceException{ 
        // ID缓存.
        String cacheKey = CACHE_KEY_OPT_UPRID_PRE + uprId + "_" + optCode ; 
        boolean putRst = baseBytesCacheService.putAndExpireByteArrayNx(cacheKey, pwd.getBytes(), defaultExpSeconds);
        if( putRst  ){ 
            // put成功了,则返回true 
            return true ;
        }else{
            // 是否抛异常,
            if(throwExp) { 
                throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_MINI, "业务处理中,请忽重复操作");
            }
            return false;
        }
    }

    @Override
    public boolean releaseUsrpayLmtById(Long uprId , String optCode ,String pwd){
        // 订单缓存.
        String cacheKey = CACHE_KEY_OPT_UPRID_PRE + uprId + "_" + optCode ; 
        // 解锁失败 
        if(pwd == null || pwd.length() < 1 ){ 
            return false ;
        }
        // 没有密码
        byte[] pwdByteArray = baseBytesCacheService.getByteArray(cacheKey); 
        if(pwdByteArray != null && pwdByteArray.length > 0 ) {
            if(!pwd.equals( new String(pwdByteArray))) { 
                // 密码不对,返回错误
                return false ;
            }
        }
        baseBytesCacheService.remove(cacheKey); 
        // 解锁成功 
        return true ; 
    }
    
    
    @Override
    public boolean checkAndExpNotifyLmtById(Long notifyId , String optCode ,String pwd, boolean throwExp ) throws BaseServiceException{ 
    	// ID缓存.
    	String cacheKey = CACHEKEY_NOTIFYID_PRE + notifyId + "_" + optCode ; 
    	boolean putRst = baseBytesCacheService.putAndExpireByteArrayNx(cacheKey, pwd.getBytes(), defaultExpSeconds);
    	if( putRst  ){ 
    		// put成功了,则返回true 
    		return true ;
    	}else{
    		// 是否抛异常,
    		if(throwExp) { 
    			throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_MINI, "业务处理中,请忽重复操作");
    		}
    		return false;
    	}
    }
    
    @Override
    public boolean releaseNotifyLmtById(Long notifyId , String optCode ,String pwd){
    	// 订单缓存.
    	String cacheKey = CACHEKEY_NOTIFYID_PRE + notifyId + "_" + optCode ; 
    	// 解锁失败 
    	if(pwd == null || pwd.length() < 1 ){ 
    		return false ;
    	}
    	// 没有密码
    	byte[] pwdByteArray = baseBytesCacheService.getByteArray(cacheKey); 
    	if(pwdByteArray != null && pwdByteArray.length > 0 ) {
    		if(!pwd.equals( new String(pwdByteArray))) { 
    			// 密码不对,返回错误
    			return false ;
    		}
    	}
    	baseBytesCacheService.remove(cacheKey); 
    	// 解锁成功 
    	return true ; 
    }
    
    

}
