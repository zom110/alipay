package com.sdhoo.pdloan.payctr.service.req;

import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.service.UserPayService;

/**
 * 创建支付渠道用户账户验证码确认
 * @author SDPC_LIU
 *
 */
public class DoCreateUsrAccVerifyCheckReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 账户ID
	 */
    @NotNull(message="账户ID不能为空"  )
    private Long icuaId ;
    
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
     * 确认码
     */
    @NotNull(message="确认码不能为空" )
    private String verifyCode ;


    public Long getIcuaId() {
        return icuaId;
    }

    public void setIcuaId(Long icuaId) {
        this.icuaId = icuaId;
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

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
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


}
