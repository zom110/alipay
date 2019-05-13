package com.sdhoo.pdloan.payctr.busi.yibaodk.dto;

/**
 * 易宝配置信息
 * @author Fangzhiping
 *
 */
public class YibaodkCfgInf{

    /**
     * 序列.
     */
    private static final long serialVersionUID = 1L; 
    
    /**
     * 配置ID
     */
    private Long cfgId ; 
    
    /**
     * 网关地址
     */
    private String gatewayUrl ;
    
    /**
     * 商户号
     */
    private String merchantAccount ;

    /**
     * 接口渠道名称
     */
    private String ifcChnlName ;
    
    /**
     * appKey
     */
    private String merchantAppKey ;

    /**
     * 商户终端号:
     */
    private String merchantTermilalno ;
    
    /**
     * 商户私钥
     */
    private String merchantPrivateKey ;
    
    /**
     * 解密公钥
     */
    private String decPubFileTxt ;
    

    public Long getCfgId() {
        return cfgId;
    }


    public void setCfgId(Long cfgId) {
        this.cfgId = cfgId;
    }


    public String getMerchantAppKey() {
        return merchantAppKey;
    }


    public void setMerchantAppKey(String merchantAppKey) {
        this.merchantAppKey = merchantAppKey;
    }


    public String getMerchantAccount() {
        return merchantAccount;
    }


    public void setMerchantAccount(String merchantAccount) {
        this.merchantAccount = merchantAccount;
    }



    public String getMerchantPrivateKey() {
        return merchantPrivateKey;
    }


    public void setMerchantPrivateKey(String merchantPrivateKey) {
        this.merchantPrivateKey = merchantPrivateKey;
    }

    public String getMerchantTermilalno() {
        return merchantTermilalno;
    }

    public void setMerchantTermilalno(String merchantTermilalno) {
        this.merchantTermilalno = merchantTermilalno;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getIfcChnlName() {
        return ifcChnlName;
    }

    public void setIfcChnlName(String ifcChnlName) {
        this.ifcChnlName = ifcChnlName;
    }


    public String getDecPubFileTxt() {
        return decPubFileTxt;
    }


    public void setDecPubFileTxt(String decPubFileTxt) {
        this.decPubFileTxt = decPubFileTxt;
    }


	public String getGatewayUrl() {
		return gatewayUrl;
	}


	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}
    
    
    
}
