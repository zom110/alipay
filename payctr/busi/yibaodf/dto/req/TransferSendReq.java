package com.sdhoo.pdloan.payctr.busi.yibaodf.dto.req;

import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YibaodfBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.rsp.TransferSendRsp;

public class TransferSendReq extends YibaodfBaseReq<TransferSendRsp> {

    
    @NotNull
    private String groupNumber ;
    
    @NotNull
    private String batchNo ;
    
    @NotNull
    private String orderId ; // 交易号 
    
    @NotNull
    private String amount ; // 金额 
    
    private String product ; // 出款产品类型
    
    private String urgency; // 是否加急. 0.非加急, 1加急

    @NotNull
    private String accountName; // 收款银行账户名称
    
    @NotNull
    private String accountNumber; // 收款银行账户卡号
    
    @NotNull
    private String bankCode; // 收款银行编号 《 易宝银行编号表-易宝.xls》
    
    private String bankName; // 收款银行全称 见 附 件 《 出款银行支持情况表.xlsx》
    
    private String bankBranchName; // 收款银行支行名称 , 《出款银行支持情况表.xlsx》
    
    private String provinceCode; // 收款银行省编码 详见附件《省市区.xlsx》
    
    private String cityCode; // 收款银行城市编码 详见附件《省市区.xlsx》
    
    @NotNull
    private String feeType; // 手续费方式 “SOURCE” 商户承担 , “TARGET” 用户承担
    
    private String desc; // 描述(打款原因)
    
    private String leaveWord; // 留言
    
    private String abstractInfo; // 摘要
    
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLeaveWord() {
        return leaveWord;
    }

    public void setLeaveWord(String leaveWord) {
        this.leaveWord = leaveWord;
    }

    public String getAbstractInfo() {
        return abstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo;
    }

    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/balance/transfer_send";
    }

    @Override
    public Class<TransferSendRsp> doGetRespClass() {
        return TransferSendRsp.class;
    }

}
