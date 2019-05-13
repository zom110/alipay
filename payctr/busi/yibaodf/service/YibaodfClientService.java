package com.sdhoo.pdloan.payctr.busi.yibaodf.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YibaodfBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YibaodfBaseRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.YibaodfCfgInf;

/**
 * 易宝支付服务.
 * 
 * @author Fangzhiping
 *
 */
public interface YibaodfClientService {
    
    /**
     * 创建一个交易序列号.
     * @return
     */
    String generateTransSerialNo();
    
    /**
     * 执行内容.
     * @param context
     * @return
     * @throws BaseServiceException 
     * @throws Exception 
     */
    <T extends YibaodfBaseRsp>T doExecuteBaseReq(YibaodfBaseReq<T> context, YibaodfCfgInf cfg ) throws BaseServiceException, Exception ;

    
}
