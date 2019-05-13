package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.TkRsp;

/**
 * 退款请求.
 * @author Fangzhiping
 *
 */
public class TkReq extends YibaodkBaseReq<TkRsp>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 退款请求号.
     */
    private String requestno;
    /**
     * 原交易易宝流水号
     */
    private String paymentyborderid;

    /**
     * 退款金额
     */
    private String amount;
    /**
     * 请求时间
     */
    private String requesttime;
   

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getPaymentyborderid() {
        return paymentyborderid;
    }


    public void setPaymentyborderid(String paymentyborderid) {
        this.paymentyborderid = paymentyborderid;
    }


    public String getRequesttime() {
        return requesttime;
    }


    public void setRequesttime(String requesttime) {
        this.requesttime = requesttime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/paperorder/api/refund/request";
    }

    @Override
    public Class<TkRsp> doGetRespClass() {
        return TkRsp.class;
    }

}
