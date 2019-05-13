package com.sdhoo.pdloan.payctr.busi.yibaodf.beans;

import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.busi.yibaodk.service.YibaodkClientService;

/**
 * 易宝基本请求类.
 * @author SDPC_LIU
 *
 * @param <T>
 */
public abstract class YibaodfBaseReq <T extends YibaodfBaseRsp > {

    /**
     * 商户编号,
     */
    @NotNull(message = "商户编号不能为空", groups = { YibaodkClientService.class })
    private String  customerNumber ;
    

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public abstract String doGetCallUrl();

	public abstract Class<T> doGetRespClass();
}
