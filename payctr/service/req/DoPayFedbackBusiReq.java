package com.sdhoo.pdloan.payctr.service.req;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * 
 * 支付响应请求.
 * @author SDPC_LIU
 *
 */
public class DoPayFedbackBusiReq {
	
	/**
	 * 支付渠道ID. @{UsrRepayChanelEnum}
	 */
	@NotNull(message="接口渠道ID不能为空"  )
	private Integer ifcChnlId ;
	
    /**
     * 接口渠道配置ID.
     */
	@NotNull(message="接口渠道配置ID不能为空" )
    private Long ifcChnlCfgId ;
	
	/**
	 * 
	 */
	private Integer payType ;

	/**
	 * 表单(请求)的参数信息Map,后期用于数据验证.
	 * 使用该接口后请求数据验证的口就不在前端(Ctrler)了.
	 * 
	 */
	private Map<String,? extends Object> formPramsMap = new HashMap<>() ;


	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getIfcChnlId() {
        return ifcChnlId;
    }

    public void setIfcChnlId(Integer ifcChnlId) {
        this.ifcChnlId = ifcChnlId;
    }

    public Map<String, ? extends Object > getFormPramsMap() {
		return formPramsMap;
	}

	public void setFormPramsMap(Map<String, ? extends Object> formPramsMap) {
		this.formPramsMap = formPramsMap;
	}

    public Long getIfcChnlCfgId() {
        return ifcChnlCfgId;
    }

    public void setIfcChnlCfgId(Long ifcChnlCfgId) {
        this.ifcChnlCfgId = ifcChnlCfgId;
    }


}
