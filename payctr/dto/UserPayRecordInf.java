package com.sdhoo.pdloan.payctr.dto;

import java.util.Date;

/**
 * 
 * 用户支付记录对象(从用户角度考虑的属性).
 * 
 * @author SDPC_LIU
 *
 */
public class UserPayRecordInf {

    /**
     * 交易ID
     */
    private Long uprId;
    
    /**
     *  支付码(商户订单号)
     */
    private String payCode; 

    /**
     *  用户ID
     */
    private Long userId; 

    /**
     *  关联业务信息.
     */
    private String busiRelInf; 

    /**
     *  交易类型, 1支出,2收入
     */
    private Integer payType; 
    /**
     * 支付标题
     */
    private String payTitle;

    /**
     *  交易(人民币元)金额,
     */
    private Double payRmbamt; 

    /**
     * 支付渠道ID
     */
    private Integer payChnlId;
    
    /**
     * 支付渠道配置ID(对应渠道配置表的配置ID),0为未配置或无配置或走系统配置文件的配置.其它值为对应表的配置
     */
    private Long ifcChnlCfgId ; 

    /**
     * 支付渠道名称
     */
    private String payChnlName; 

    /**
     * 后台通知地址URL.
     */
    private String notifyUrl; 
    /**
     * 渠道交易序列号(64位)
     */
    private String payChnlSerial; 
    /**
     * 渠道反馈交易结果码(渠道方反馈)
     */
    private String payChnlRstcode; 
    /**
     * 渠道反馈结果(错误)信息(64字符)
     */
    private String payChnlRstMsg; 

    /**
     * 发起人用户类型 @{UsrTypeEnum}
     */
    private Integer initUserType; 

    /**
     * 发起人用户ID
     */
    private String initUserId; 

    /**
     * 发起备注
     */
    private String initMemo; 

    /**
     * 渠道反馈时间
     */
    private Date payChnlFebtime; 

    /**
     * 渠道反馈内容(JSON)(考虑迁移至详情).
     */
    private String payChnlFebinfo; 

    /**
     * 交易环节状态 @{UprPayStepStatusEnum}
     */
    private Integer payStepStatus; 

    /**
     * 创建时间
     */
    private Date uprCtime; 

    /**
     * 更新时间
     */
    private Date uprMtime; 
    
    /**
     * 支付准备信息(支付宝,微信等主动付款时,创建成功后该字段有值)
     */
    private String prepayInf ;

    
    public Long getUprId() {
        return uprId;
    }

    public void setUprId(Long uprId) {
        this.uprId = uprId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getBusiRelInf() {
        return busiRelInf;
    }

    public void setBusiRelInf(String busiRelInf) {
        this.busiRelInf = busiRelInf;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getPayTitle() {
        return payTitle;
    }

    public void setPayTitle(String payTitle) {
        this.payTitle = payTitle;
    }

    public Double getPayRmbamt() {
        return payRmbamt;
    }

    public void setPayRmbamt(Double payRmbamt) {
        this.payRmbamt = payRmbamt;
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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPayChnlSerial() {
        return payChnlSerial;
    }

    public void setPayChnlSerial(String payChnlSerial) {
        this.payChnlSerial = payChnlSerial;
    }

    public String getPayChnlRstcode() {
        return payChnlRstcode;
    }

    public void setPayChnlRstcode(String payChnlRstcode) {
        this.payChnlRstcode = payChnlRstcode;
    }

    public String getPayChnlRstMsg() {
        return payChnlRstMsg;
    }

    public void setPayChnlRstMsg(String payChnlRstMsg) {
        this.payChnlRstMsg = payChnlRstMsg;
    }

    public Integer getInitUserType() {
        return initUserType;
    }

    public void setInitUserType(Integer initUserType) {
        this.initUserType = initUserType;
    }

    public String getInitUserId() {
        return initUserId;
    }

    public void setInitUserId(String initUserId) {
        this.initUserId = initUserId;
    }

    public Date getPayChnlFebtime() {
        return payChnlFebtime;
    }

    public void setPayChnlFebtime(Date date) {
        this.payChnlFebtime = date;
    }

    public Integer getPayStepStatus() {
        return payStepStatus;
    }

    public void setPayStepStatus(Integer payStepStatus) {
        this.payStepStatus = payStepStatus;
    }

    public Date getUprCtime() {
        return uprCtime;
    }

    public void setUprCtime(Date uprCtime) {
        this.uprCtime = uprCtime;
    }

    public Date getUprMtime() {
        return uprMtime;
    }

    public void setUprMtime(Date uprMtime) {
        this.uprMtime = uprMtime;
    }

    public String getPayChnlFebinfo() {
        return payChnlFebinfo;
    }

    public void setPayChnlFebinfo(String payChnlFebinfo) {
        this.payChnlFebinfo = payChnlFebinfo;
    }

    public String getInitMemo() {
        return initMemo;
    }

    public void setInitMemo(String initMemo) {
        this.initMemo = initMemo;
    }

    public Long getIfcChnlCfgId() {
        return ifcChnlCfgId;
    }

    public void setIfcChnlCfgId(Long ifcChnlCfgId) {
        this.ifcChnlCfgId = ifcChnlCfgId;
    }

	public String getPrepayInf() {
		return prepayInf;
	}

	public void setPrepayInf(String prepayInf) {
		this.prepayInf = prepayInf;
	}


}
