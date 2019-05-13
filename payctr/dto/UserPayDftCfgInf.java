package com.sdhoo.pdloan.payctr.dto;

/**
 * 用户支付默认信息
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-05 18:15:30
 *
 */
public class UserPayDftCfgInf {

    /**
     * 代扣渠道ID
     */
    private Integer dudcPayChnlId ;
    
    /**
     * 代扣配置ID
     */
    private Long dudcYibaoPayChnlCfgId ;
    
    /**
     * 代扣富友默认配置ID
     */
    private Long dudcFuiouPayChnlCfgId ;
    
    /**
     * 支付宝配置ID
     */
    private Long alipayChnlCfgId ;

    /**
     * 线下支付配置ID
     */
    private Long oflineChnlCfgId ;
    
    

    public Long getAlipayChnlCfgId() {
		return alipayChnlCfgId;
	}

	public void setAlipayChnlCfgId(Long alipayChnlCfgId) {
		this.alipayChnlCfgId = alipayChnlCfgId;
	}

	public Integer getDudcPayChnlId() {
        return dudcPayChnlId;
    }

    public void setDudcPayChnlId(Integer dudcPayChnlId) {
        this.dudcPayChnlId = dudcPayChnlId;
    }

    public Long getDudcYibaoPayChnlCfgId() {
        return dudcYibaoPayChnlCfgId;
    }

    public void setDudcYibaoPayChnlCfgId(Long dudcPayChnlCfgId) {
        this.dudcYibaoPayChnlCfgId = dudcPayChnlCfgId;
    }

	public Long getDudcFuiouPayChnlCfgId() {
		return dudcFuiouPayChnlCfgId;
	}

	public void setDudcFuiouPayChnlCfgId(Long dudcFuiouPayChnlCfgId) {
		this.dudcFuiouPayChnlCfgId = dudcFuiouPayChnlCfgId;
	}

	public Long getOflineChnlCfgId() {
		return oflineChnlCfgId;
	}

	public void setOflineChnlCfgId(Long oflineChnlCfgId) {
		this.oflineChnlCfgId = oflineChnlCfgId;
	}

    
}
