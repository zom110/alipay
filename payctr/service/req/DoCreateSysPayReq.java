package com.sdhoo.pdloan.payctr.service.req;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.service.SysPayService;

/**
 * 用户创建支付(代扣)请求.
 * @author SDPC_LIU
 *
 */
public class DoCreateSysPayReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 *  支付编号.
	 */
	@NotNull(message="支付编号不能为空" )
	private String sprCode ; 

    /**
     * 支付接口渠道ID,{@SyspayIfcChnlEnum}
     */
    private Integer ifcChnlId ;
    
    /**
     * 接口渠道配置ID.
     */
    private Long ifcChnlCfgId ;

    /**
     * 目标用户ID
     */
    private Long userId ;

	/**
	 * 标题
	 */
	@NotNull(message="标题不能为空" )
	private String payTitle ;

	/**
	 *  支付金额(元)
	 */
	@NotNull(message="支付金额不能为空" )
	private Double payamtYuan ; 

	/**
     * 收款渠道类型
     */
	@NotNull(message="收款渠道类型不能为空" )
	private Integer payeeChnlType ;

	/**
     * 收款渠道名称,银行名称
     */
	@NotNull(message="渠道名称不能为空" )
	private String payeeChnlName ;

	/**
	 * 收款渠道编码,银行编码
	 */
	private String payeeChnlCode ;

	/**
	 * 渠道全名,分行名称
	 */
	private String payeeChnlFullname ; 

    /**
     * 收款渠道省份名称
     */
    private String payeeChnlProvname;

    /**
    * 收款渠道地市名称
    */
    private String payeeChnlCityname ; 

	/**
	 * 收款单位类型, {@PayeeUnitTypeEnum}
	 */
	@NotNull(message="收款单位类型不能为空" ,groups={SysPayService.class} )
	private Integer payeeAccUnitType ;

	/**
	 * 收款账户号(银行卡号)
	 */
	@NotNull(message="收款账户号不能为空" ,groups={SysPayService.class} )
	private String payeeAccNo ; 

	/**
	 * 收款账户姓名,用户姓名
	 */
	@NotNull(message="收款账户名称不能为空" ,groups={SysPayService.class} )
	private String payeeAccFullName; 

	/**
	 * 身份证号
	 */
	private String payeeAccIdcard ; 
	
	/**
	 * 绑定电话
	 */
	private String payeeAccMobile ; 
	
	/**
	 *  业务信息
	 */
	private String busiRelInf ; 
	
	/**
	 *  发起人用户类型. @{UsrTypeEnum}
	 */
	private Integer initUserType ; 

	/**
	 *  发起人用户ID.
	 */
	private String initUserId ; 
	
	/**
	 * 发起人名称
	 */
	private String initUserName ;
	
	/**
	 *  发起备注
	 */
	private String initMemo ; 

	/**
	 *  扩展信息(渠道特殊需要的自定义内容)
	 */
	private Map<String,Object> extraInf ; 

    /**
     *  最多重试(网络等未知原因失败时)次数  
     */
    private Integer maxErrorTryCount = 1  ;
    
    /**
     * 通知地址url
     */
    private String statusNotifyUrl ; 
    
    
    
    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

    public Double getPayamtYuan() {
        return payamtYuan;
    }

    public void setPayamtYuan(Double payamtYuan) {
        this.payamtYuan = payamtYuan;
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

    public Integer getPayeeAccUnitType() {
        return payeeAccUnitType;
    }

    public void setPayeeAccUnitType(Integer payeeUnitType) {
        this.payeeAccUnitType = payeeUnitType;
    }

    public String getPayeeAccNo() {
        return payeeAccNo;
    }

    public void setPayeeAccNo(String payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }

    public String getPayeeAccFullName() {
        return payeeAccFullName;
    }

    public void setPayeeAccFullName(String payeeAccFullName) {
        this.payeeAccFullName = payeeAccFullName;
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

    public String getBusiRelInf() {
        return busiRelInf;
    }

    public void setBusiRelInf(String busiRelInf) {
        this.busiRelInf = busiRelInf;
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

    public Map<String, Object> getExtraInf() {
        return extraInf;
    }

    public void setExtraInf(Map<String, Object> extraInf) {
        this.extraInf = extraInf;
    }

    public Integer getMaxErrorTryCount() {
        return maxErrorTryCount;
    }

    public void setMaxErrorTryCount(Integer maxErrorTryCount) {
        this.maxErrorTryCount = maxErrorTryCount;
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

    public Integer getIfcChnlId() {
        return ifcChnlId;
    }

    public void setIfcChnlId(Integer ifcChnlId) {
        this.ifcChnlId = ifcChnlId;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Long getIfcChnlCfgId() {
        return ifcChnlCfgId;
    }

    public void setIfcChnlCfgId(Long ifcChnlCfgId) {
        this.ifcChnlCfgId = ifcChnlCfgId;
    }

    public String getStatusNotifyUrl() {
        return statusNotifyUrl;
    }

    public void setStatusNotifyUrl(String statusNotifyUrl) {
        this.statusNotifyUrl = statusNotifyUrl;
    }

    public String getPayeeChnlCode() {
        return payeeChnlCode;
    }

    public void setPayeeChnlCode(String payeeChnlCode) {
        this.payeeChnlCode = payeeChnlCode;
    }

    public String getInitUserName() {
        return initUserName;
    }

    public void setInitUserName(String initUserName) {
        this.initUserName = initUserName;
    }

    
}
