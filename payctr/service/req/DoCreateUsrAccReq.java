package com.sdhoo.pdloan.payctr.service.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.service.UserPayService;

/**
 * 创建支付渠道用户账户请求(支付渠道开户)
 * @author SDPC_LIU
 *
 */
public class DoCreateUsrAccReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    /**
     * 支付接口渠道ID,{@UserPayIfcChnlEnum}
     */
    private Integer ifcChnlId ;
    
    /**
     * 接口渠道配置ID
     */
    private Long ifcChnlCfgId ;

    /**
     * 用户ID
     */
    @NotNull(message="用户ID不能为空" )
    private Long userId ;

    /**
     * 用户身份证号
     */
    @NotNull(message="用户身份证号不能为空" )
    private String userIdcard ;

    /**
     * 用户真实姓名.
     */
    @NotNull(message="用户姓名不能为空" )
    private String userFullName ; 

    /**
     * 绑定的(银行)卡号
     */
    private String bindCardno ; 
    
    /**
     * 绑定的银行编号
     */
    private String bankCode ; 
    
    /**
     * 绑定的银行名称
     */
    @NotNull(message="银行名称不能为空")
    @NotEmpty(message="银行名称不能为空.")
    private String bankName ;

    /**
     * 校验信息(通知)类型, 1短信,2语音,默认1,
     * @{VerifyMsgTypeEnum}
     * 
     */
    private Integer verifyMsgType ;
    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 是否短验
     */
    private Boolean issms;
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Boolean getIssms() {
        return issms;
    }

    public void setIssms(Boolean issms) {
        this.issms = issms;
    }

    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getIfcChnlId() {
        return ifcChnlId;
    }

    public void setIfcChnlId(Integer ifcChnlId) {
        this.ifcChnlId = ifcChnlId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserIdcard() {
        return userIdcard;
    }

    public void setUserIdcard(String userIdcard) {
        this.userIdcard = userIdcard;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getBindCardno() {
        return bindCardno;
    }

    public void setBindCardno(String bindCardno) {
        this.bindCardno = bindCardno;
    }

    public Integer getVerifyMsgType() {
        return verifyMsgType;
    }

    public void setVerifyMsgType(Integer verifyMsgType) {
        this.verifyMsgType = verifyMsgType;
    }

    public Long getIfcChnlCfgId() {
        return ifcChnlCfgId;
    }
    public void setIfcChnlCfgId(Long ifcChnlCfgId) {
        this.ifcChnlCfgId = ifcChnlCfgId;
    }
    

}
