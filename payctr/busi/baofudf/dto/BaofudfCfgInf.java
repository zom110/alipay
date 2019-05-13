package com.sdhoo.pdloan.payctr.busi.baofudf.dto;

import org.springframework.util.Base64Utils;

/**
 * 宝付代扣配置信息
 * @author SDPC_LIU
 *
 */
public class BaofudfCfgInf {

    /**
     * 
     */
    private static final long serialVersionUID = 1L; 
    
    
    private Long bdcId ;
    
    private String terminalId ;
    
    private String memberId ;
    
    private String encPriPfxFileB64;

    private String encPriPfxFilePwd; 
    
    private String decPubCerFileTxt;
    
    private String gatewayUrl ; //网关地址,  http://paytest.baofoo.com/baofoo-fopay/pay  http://pay.baofoo.com/baofoo-fopay/pay
    
    /**
     * pfx文件字节数
     */
    public byte[] pfxFileBytes ;

    /**
     * 公角文件字节数
     */
    public byte[] pubkeyCerFileBytes;
    

    public Long getBdcId() {
        return bdcId;
    }

    public void setBdcId(Long bdcId) {
        this.bdcId = bdcId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEncPriPfxFileB64() {
        return encPriPfxFileB64;
    }

    public void setEncPriPfxFileB64(String encPriPfxFileB64) {
        this.encPriPfxFileB64 = encPriPfxFileB64;
    }

    public String getEncPriPfxFilePwd() {
        return encPriPfxFilePwd;
    }

    public void setEncPriPfxFilePwd(String encPriPfxFilePwd) {
        this.encPriPfxFilePwd = encPriPfxFilePwd;
    }

    public String getDecPubCerFileTxt() {
        return decPubCerFileTxt;
    }

    public void setDecPubCerFileTxt(String decPubCerFileTxt) {
        this.decPubCerFileTxt = decPubCerFileTxt;
    }

    public byte[] getPfxFileBytes() {
        if(pfxFileBytes == null && this.getEncPriPfxFileB64() != null ) {
            pfxFileBytes = Base64Utils.decodeFromString(this.getEncPriPfxFileB64());
        }
        return pfxFileBytes;
    }

    public void setPfxFileBytes(byte[] pfxFileBytes) {
        this.pfxFileBytes = pfxFileBytes;
    }

    public byte[] getPubkeyCerFileBytes() {
        if(pubkeyCerFileBytes == null && this.getDecPubCerFileTxt() != null ) {
            pubkeyCerFileBytes = this.getDecPubCerFileTxt().getBytes();
        }
        return pubkeyCerFileBytes;
    }

    public void setPubkeyCerFileBytes(byte[] pubkeyCerFileBytes) {
        this.pubkeyCerFileBytes = pubkeyCerFileBytes;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}
    
}
