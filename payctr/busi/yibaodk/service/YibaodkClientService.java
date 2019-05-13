package com.sdhoo.pdloan.payctr.busi.yibaodk.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.YibaodkCfgInf;

/**
 * 易宝支付服务.
 * 
 * @author Fangzhiping
 *
 */
public interface YibaodkClientService {
    
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
     */
    <T extends YibaodkBaseRsp>T doExecuteBaseReq(YibaodkBaseReq<T> context, YibaodkCfgInf cfg ) throws BaseServiceException ;

    
}
