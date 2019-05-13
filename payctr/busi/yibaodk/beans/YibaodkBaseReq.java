package com.sdhoo.pdloan.payctr.busi.yibaodk.beans;

import javax.validation.constraints.NotNull;

/**
 * 易宝基本请求类.
 * @author SDPC_LIU
 *
 * @param <T>
 */
public abstract class YibaodkBaseReq <T extends YibaodkBaseRsp > {

    /**
     * 商户编号,
     */
    @NotNull(message = "商户编号不能为空")
    private String  merchantno ;
    

	public String getMerchantno() {
        return merchantno;
    }

    public void setMerchantno(String merchantno) {
        this.merchantno = merchantno;
    }

    public abstract String doGetCallUrl();

	public abstract Class<T> doGetRespClass();
}
