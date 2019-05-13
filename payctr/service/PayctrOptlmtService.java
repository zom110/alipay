package com.sdhoo.pdloan.payctr.service;

import com.sdhoo.common.base.exception.BaseServiceException;

/**
 * 支付中心操作限制服务.
 * @author SDPC_LIU
 *
 */
public interface PayctrOptlmtService {

    /**
     * 核对并锁操作限制.
     * @param sprId
     * @param optCode
     * @param pwd
     * @param throwExp
     * @return
     * @throws BaseServiceException
     */
    boolean checkAndExpSyspayLmtById(Long sprId, String optCode, String pwd, boolean throwExp) throws BaseServiceException;

    /**
     * 释放锁操作限制
     * @param sprId
     * @param optCode
     * @param pwd
     * @return
     */
    boolean releaseSyspayLmtById(Object sprId, String optCode, String pwd);

    
    /**
     * 用户支付限制.
     * @param uprId
     * @param optCode
     * @param pwd
     * @param throwExp
     * @return
     * @throws BaseServiceException
     */
    boolean checkAndExpUsrpayLmtById(Long uprId, String optCode, String pwd, boolean throwExp) throws BaseServiceException;

    /**
     * 用户支付限制.
     * @param uprId
     * @param optCode
     * @param pwd
     * @return
     */
    boolean releaseUsrpayLmtById(Long uprId, String optCode, String pwd);

    /**
     * 对外交易通知操作限制.
     * @param notifyId
     * @param optCode
     * @param pwd
     * @param throwExp
     * @return
     * @throws BaseServiceException
     */
	boolean checkAndExpNotifyLmtById(Long notifyId, String optCode, String pwd, boolean throwExp) throws BaseServiceException;

	/**
	 * 对外交易通知操作限制释放.
	 * @param notifyId
	 * @param optCode
	 * @param pwd
	 * @return
	 */
	boolean releaseNotifyLmtById(Long notifyId, String optCode, String pwd);

    
    

}
