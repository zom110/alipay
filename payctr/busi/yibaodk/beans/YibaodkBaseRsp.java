package com.sdhoo.pdloan.payctr.busi.yibaodk.beans;

/**
 * 返回内容
 * @author Fangzhiping
 */
public class YibaodkBaseRsp {

    /**
     * 商户编号
     */
    public String merchantno;
    
    /**
     * 错误码
     */
    public String errorcode;
    /**
     * 错误信息
     */
    public String errormsg;
    public String getMerchantno() {
        return merchantno;
    }
    public void setMerchantno(String merchantno) {
        this.merchantno = merchantno;
    }
    public String getErrorcode() {
        return errorcode;
    }
    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }
    public String getErrormsg() {
        return errormsg;
    }
    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }
}
