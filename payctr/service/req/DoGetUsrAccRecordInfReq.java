package com.sdhoo.pdloan.payctr.service.req;

import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.service.UserPayService;

/**
 * 获取用户开户账户信息
 * @author SDPC_LIU
 *
 */
public class DoGetUsrAccRecordInfReq {

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

    public Long getIfcChnlCfgId() {
        return ifcChnlCfgId;
    }

    public void setIfcChnlCfgId(Long ifcChnlCfgId) {
        this.ifcChnlCfgId = ifcChnlCfgId;
    }
    
    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
