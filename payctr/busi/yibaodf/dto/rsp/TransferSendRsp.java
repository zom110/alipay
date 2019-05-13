package com.sdhoo.pdloan.payctr.busi.yibaodf.dto.rsp;

import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YibaodfBaseRsp;


/**
 * 代付单笔出款接口
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-05 21:31:51
 *
 */
public class TransferSendRsp extends YibaodfBaseRsp {
    
    /**
     * 
     */
    private String orderId ;
    
    /**
     * 
     */
    private String batchNo ;
    
    /**
     * 
     */
    private String transferStatusCode;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTransferStatusCode() {
        return transferStatusCode;
    }

    public void setTransferStatusCode(String transferStatusCode) {
        this.transferStatusCode = transferStatusCode;
    }
    

}
