package com.sdhoo.pdloan.payctr.busi.yibaodf.beans;

/**
 * 返回内容
 * @author Fangzhiping
 */
public class YibaodfBaseRsp {

    /**
     * 商户编号
     */
    public String merchantno;
    
    /**
     * 错误码
     */
    public String errorCode;
    /**
     * 错误信息
     */
    public String errorMsg;
    public String getMerchantno() {
        return merchantno;
    }
    public void setMerchantno(String merchantno) {
        this.merchantno = merchantno;
    }
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
