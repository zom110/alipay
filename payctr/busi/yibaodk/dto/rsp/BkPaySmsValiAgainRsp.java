package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseRsp;
/**
 * 绑卡支付短验重发返回数据
 * @author fangzhiping
 * @date 2018年4月18日
 */
public class BkPaySmsValiAgainRsp extends YibaodkBaseRsp{

    private static final long serialVersionUID = 1L;

    /**
     * 还款请求号
     */
    public String requestno;
    /**
     * 易宝流水号
     */
    public String yborderid;
    /**
     * 还款金额
     */
    public String amount;
    /**
     * 订单状态
     */
    public String status;
    /**
     * 短验码
     */
    public String smscode;
    /**
     * 短验发送方
     */
    public String codesender;

    public String getRequestno() {
        return requestno;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

    public String getCodesender() {
        return codesender;
    }

    public void setCodesender(String codesender) {
        this.codesender = codesender;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    public String getYborderid() {
        return yborderid;
    }

    public void setYborderid(String yborderid) {
        this.yborderid = yborderid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
