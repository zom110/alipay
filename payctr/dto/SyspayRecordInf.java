package com.sdhoo.pdloan.payctr.dto;

import java.util.Date;

/**
 * 
 * 系统支付(代付)记录对象(从系统角度考虑的对象)
 * 
 * @author SDPC_LIU
 *
 */
public class SyspayRecordInf {

    /**
     * 交易记录ID
     */
    private Long sprId;

    /**
     * 交易编号(商户订单号)
     */
    private String sprCode;
    
    /**
     * 目标用户ID,
     */
    private Long userId ;

    /**
     * 交易标题
     */
    private String payTitle;

    /**
     * 接口渠道ID,101宝付代付,
     */
    private Integer ifcChnlId;
    
    /**
     * 接口渠道配置ID
     */
    private Long ifcChnlCfgId ;
    
    /**
     * 接口渠道名称.
     */
    private String ifcChnlName ;

    /**
     *  渠道交易结束时间(成功时间或失败时间)
     */
    private Date ifcChnlEndtime; 
    
    /**
     * 接口渠道交易批次号,默认为交易编号,特殊情况下渠道实现类进行处理
     */
    private String ifcChnlBatchNo ; 
    
    /**
     * 代付金额
     */
    private Double payRmbamt;

    /**
     * 收款渠道id(对应一家银行一个ID):0未指定,2支付宝,3微信账号,xx某某银行
     */
    private Integer payeeChnlId;

    /**
     * 收款账户渠道类型:1银行卡,2网络支付通道
     */
    private Integer payeeChnlType;

    /**
     * 收款渠道名称(银行名称),例:工商银行
     */
    private String payeeChnlName;

    /**
     * 收款渠道全称(支行名称),例:工商银行福州湖前支行
     */
    private String payeeChnlFullname;

    /**
     * 收款渠道省份名称
     */
    private String payeeChnlProvname;

    /**
     * 收款渠道地市名称
     */
    private String payeeChnlCityname;

    /**
     * 收款单位类型(0未知,1企业,2个人)
     */
    private Integer payeeAccUnitType;

    /**
     * 收款人账号(卡号)
     */
    private String payeeAccNo;

    /**
     * 收款人姓名
     */
    private String payeeAccFullname;

    /**
     * 收款人身份证号
     */
    private String payeeAccIdcard;

    /**
     * 收款人(绑定)手机号
     */
    private String payeeAccMobile;

    /**
     * 业务关联信息,订单角度可以存储订单ID,账单角度可以存账单ID
     */
    private String busyRelInf;

    /**
     * 发起用户类型
     */
    private Integer initUserType;

    /**
     * 发起用户ID
     */
    private String initUserId;

    /**
     * 发起备注
     */
    private String initMemo;

    /**
     * 交易环节状态,@{SysPayStepStatusEnum},0创建,1处理成功,2失败,3等待渠道通知,4退单,5异常,6需要主动监测状态
     */
    private Integer stepStatus;

    /**
    * 状态备注(失败,则显示失败提示内容)
    */
    private String stepMemo ; 

    /**
     * 创建时间
     */
    private java.util.Date sprCtime;

    /**
     * 更新时间
     */
    private java.util.Date sprMtime;

    public Long getSprId() {
        return sprId;
    }

    public void setSprId(Long sprId) {
        this.sprId = sprId;
    }

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getIfcChnlEndtime() {
		return ifcChnlEndtime;
	}

	public void setIfcChnlEndtime(Date ifcChnlEndtime) {
		this.ifcChnlEndtime = ifcChnlEndtime;
	}

	public String getSprCode() {
        return sprCode;
    }

    public void setSprCode(String sprCode) {
        this.sprCode = sprCode;
    }

    public String getPayTitle() {
        return payTitle;
    }

    public void setPayTitle(String payTitle) {
        this.payTitle = payTitle;
    }

    public Integer getIfcChnlId() {
        return ifcChnlId;
    }

    public void setIfcChnlId(Integer ifcChnlId) {
        this.ifcChnlId = ifcChnlId;
    }

    public Double getPayRmbamt() {
        return payRmbamt;
    }

    public void setPayRmbamt(Double payRmbamt) {
        this.payRmbamt = payRmbamt;
    }

    public Integer getPayeeChnlId() {
        return payeeChnlId;
    }

    public void setPayeeChnlId(Integer payeeChnlId) {
        this.payeeChnlId = payeeChnlId;
    }

    public Integer getPayeeChnlType() {
        return payeeChnlType;
    }

    public void setPayeeChnlType(Integer payeeChnlType) {
        this.payeeChnlType = payeeChnlType;
    }

    public String getPayeeChnlName() {
        return payeeChnlName;
    }

    public void setPayeeChnlName(String payeeChnlName) {
        this.payeeChnlName = payeeChnlName;
    }

    public String getPayeeChnlFullname() {
        return payeeChnlFullname;
    }

    public void setPayeeChnlFullname(String payeeChnlFullname) {
        this.payeeChnlFullname = payeeChnlFullname;
    }

    public String getPayeeChnlProvname() {
        return payeeChnlProvname;
    }

    public void setPayeeChnlProvname(String payeeChnlProvname) {
        this.payeeChnlProvname = payeeChnlProvname;
    }

    public String getPayeeChnlCityname() {
        return payeeChnlCityname;
    }

    public void setPayeeChnlCityname(String payeeChnlCityname) {
        this.payeeChnlCityname = payeeChnlCityname;
    }

    public Integer getPayeeAccUnitType() {
        return payeeAccUnitType;
    }

    public void setPayeeAccUnitType(Integer payeeAccUnitType) {
        this.payeeAccUnitType = payeeAccUnitType;
    }

    public String getPayeeAccNo() {
        return payeeAccNo;
    }

    public void setPayeeAccNo(String payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }

    public String getPayeeAccFullname() {
        return payeeAccFullname;
    }

    public void setPayeeAccFullname(String payeeAccFullname) {
        this.payeeAccFullname = payeeAccFullname;
    }

    public String getPayeeAccIdcard() {
        return payeeAccIdcard;
    }

    public void setPayeeAccIdcard(String payeeAccIdcard) {
        this.payeeAccIdcard = payeeAccIdcard;
    }

    public String getPayeeAccMobile() {
        return payeeAccMobile;
    }

    public void setPayeeAccMobile(String payeeAccMobile) {
        this.payeeAccMobile = payeeAccMobile;
    }

    public String getBusyRelInf() {
        return busyRelInf;
    }

    public void setBusyRelInf(String busyRelInf) {
        this.busyRelInf = busyRelInf;
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

    public String getInitMemo() {
        return initMemo;
    }

    public void setInitMemo(String initMemo) {
        this.initMemo = initMemo;
    }

    public Integer getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(Integer stepStatus) {
        this.stepStatus = stepStatus;
    }

    public java.util.Date getSprCtime() {
        return sprCtime;
    }

    public void setSprCtime(java.util.Date sprCtime) {
        this.sprCtime = sprCtime;
    }

    public java.util.Date getSprMtime() {
        return sprMtime;
    }

    public void setSprMtime(java.util.Date sprMtime) {
        this.sprMtime = sprMtime;
    }

    public String getStepMemo() {
        return stepMemo;
    }

    public void setStepMemo(String stepMemo) {
        this.stepMemo = stepMemo;
    }

    public Long getIfcChnlCfgId() {
        return ifcChnlCfgId;
    }

    public void setIfcChnlCfgId(Long ifcChnlCfgId) {
        this.ifcChnlCfgId = ifcChnlCfgId;
    }

    public String getIfcChnlName() {
        return ifcChnlName;
    }

    public void setIfcChnlName(String ifcChnlName) {
        this.ifcChnlName = ifcChnlName;
    }

	public String getIfcChnlBatchNo() {
		return ifcChnlBatchNo;
	}

	public void setIfcChnlBatchNo(String ifcChnlBatchNo) {
		this.ifcChnlBatchNo = ifcChnlBatchNo;
	}
    
    

}
