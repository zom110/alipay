package com.sdhoo.pdloan.payctr.dto;

/**
 * 用户支付默认信息
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-05 18:15:30
 *
 */
public class SyspayDftCfgInf {

    /**
     * 代付渠道ID
     */
    private Integer sysPayChnlId ;
    
    /**
     * 易宝默认支付渠道配置ID
     */
    private Long sysYibaoPayChnlCfgId ;
    
    /**
     * 富友默认渠道配置ID
     */
    private Long sysFuiouPayChnlCfgId ;

    /**
     * 线下放款默认渠道配置ID
     */
    private Long sysOflinePayChnlCfgId ;
    
    public Integer getSysPayChnlId() {
        return sysPayChnlId;
    }

    public void setSysPayChnlId(Integer sysPayChnlId) {
        this.sysPayChnlId = sysPayChnlId;
    }

    public Long getSysYibaoPayChnlCfgId() {
        return sysYibaoPayChnlCfgId;
    }

    public void setSysYibaoPayChnlCfgId(Long sysPayChnlCfgId) {
        this.sysYibaoPayChnlCfgId = sysPayChnlCfgId;
    }

	public Long getSysFuiouPayChnlCfgId() {
		return sysFuiouPayChnlCfgId;
	}

	public void setSysFuiouPayChnlCfgId(Long sysFuiouPayChnlCfgId) {
		this.sysFuiouPayChnlCfgId = sysFuiouPayChnlCfgId;
	}

	public Long getSysOflinePayChnlCfgId() {
		return sysOflinePayChnlCfgId;
	}

	public void setSysOflinePayChnlCfgId(Long sysOflinePayChnlCfgId) {
		this.sysOflinePayChnlCfgId = sysOflinePayChnlCfgId;
	}
    
}
