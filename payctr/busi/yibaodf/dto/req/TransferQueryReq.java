package com.sdhoo.pdloan.payctr.busi.yibaodf.dto.req;

import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YibaodfBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.rsp.TransferQueryRsp;

public class TransferQueryReq extends YibaodfBaseReq<TransferQueryRsp> {

    
    @NotNull
    private String groupNumber ;
    
    @NotNull
    private String batchNo ; // 出款批次号(交易号)
    
    private String orderId ; // 交易号 
    
    @NotNull
    private String product ; // 出款产品类型
    
    private Integer pageNo ; // 分页号码 
    
    private Integer pageSize ; // 每页条数 


    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/balance/transfer_query";
    }

    @Override
    public Class<TransferQueryRsp> doGetRespClass() {
        return TransferQueryRsp.class;
    }

}
