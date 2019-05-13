package com.sdhoo.pdloan.payctr.service.req;

import javax.validation.constraints.NotNull;

/**
 * 创建支付渠道用户账户验证重发请求.
 * @author SDPC_LIU
 *
 */
public class DoCreateUsrAccResendVerifyReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * 账户ID
     */
    @NotNull(message="账户ID不能为空"  )
    private Long icuaId ;
    
//	/**
//	 * 商户ID
//	 */
//    @NotNull(message="商户ID不能为空",groups={UserPayService.class} )
//    private Long shId ;

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

    public Long getIcuaId() {
        return icuaId;
    }

    public void setIcuaId(Long icuaId) {
        this.icuaId = icuaId;
    }

//    public Long getShId() {
//        return shId;
//    }
//
//    public void setShId(Long shId) {
//        this.shId = shId;
//    }

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
