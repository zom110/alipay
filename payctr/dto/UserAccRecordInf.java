package com.sdhoo.pdloan.payctr.dto;

import java.util.Date;

/**
 * 
 * 用户账户(开户)记录对象
 * 
 * @author SDPC_LIU
 *
 */
public class UserAccRecordInf {

    /**
     * 开户信息ID
     */
    private Long uarId;

    /**
     *  用户ID
     */
    private Long userId; 

    /**
     * 支付渠道ID
     */
    private Integer payChnlId; 

    /**
     * 对应易宝配置表ID
     */
    private Long ifcChnlCfgId ; 

    /**
     * 支付渠道名称111
     */
    private String payChnlName; 

    /**
     * 账户状态 @{UserAccStepStatusEnum}
     */
    private Integer accStepStatus; 
    /**
     * 账户状态描述
     */
    private String accStepStatusMemo; 
    /**
     * 账户主卡状态
     */
    private Integer mainAccStatus; 

    /**
     * 创建时间
     */
    private Date uarCtime; 

    /**
     * 更新时间
     */
    private Date uarMtime;

    /**
     * 交易请求号
     */
    private String ifcRequestno ; 

    /**
     * 开户ID
     */
    private String openId ; 

    /**
     * 用户真实姓名
     */
    private String userFullname ; 

    /**
     * 用户身份证号
     */
    private String idcardNo ; 

    /**
     * 银行卡号
     */
    private String bankCardNo ; 
    
    /**
     * 银行编号
     */
    private String bankCode ; 
    
    /**
     * 银行名称
     */
    private String bankName ; 
    
    /**
     * 银行绑定手机号
     */
    private String bankCardMobile ; 

    public String getAccStepStatusMemo() {
        return accStepStatusMemo;
    }

    public void setAccStepStatusMemo(String accStepStatusMemo) {
        this.accStepStatusMemo = accStepStatusMemo;
    }

    public Integer getMainAccStatus() {
        return mainAccStatus;
    }

    public void setMainAccStatus(Integer mainAccStatus) {
        this.mainAccStatus = mainAccStatus;
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

    public Long getIfcChnlCfgId() {
        return ifcChnlCfgId;
    }

    public void setIfcChnlCfgId(Long ifcChnlCfgId) {
        this.ifcChnlCfgId = ifcChnlCfgId;
    }

    public String getIfcRequestno() {
        return ifcRequestno;
    }

    public void setIfcRequestno(String ifcRequestno) {
        this.ifcRequestno = ifcRequestno;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserFullname() {
        return userFullname;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankCardMobile() {
        return bankCardMobile;
    }

    public void setBankCardMobile(String bankCardMobile) {
        this.bankCardMobile = bankCardMobile;
    }

    public Long getUarId() {
        return uarId;
    }

    public void setUarId(Long uarId) {
        this.uarId = uarId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getPayChnlId() {
        return payChnlId;
    }

    public void setPayChnlId(Integer payChnlId) {
        this.payChnlId = payChnlId;
    }

    public String getPayChnlName() {
        return payChnlName;
    }

    public void setPayChnlName(String payChnlName) {
        this.payChnlName = payChnlName;
    }

    public Integer getAccStepStatus() {
        return accStepStatus;
    }

    public void setAccStepStatus(Integer accStepStatus) {
        this.accStepStatus = accStepStatus;
    }

    public Date getUarCtime() {
        return uarCtime;
    }

    public void setUarCtime(Date uarCtime) {
        this.uarCtime = uarCtime;
    }

    public Date getUarMtime() {
        return uarMtime;
    }

    public void setUarMtime(Date uarMtime) {
        this.uarMtime = uarMtime;
    } 

}
