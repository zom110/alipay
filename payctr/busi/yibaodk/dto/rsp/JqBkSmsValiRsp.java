package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseRsp;
/**
 * 鉴权绑卡短验确认返回数据
 * @author fangzhiping
 * @date 2018年4月18日
 */
public class JqBkSmsValiRsp extends YibaodkBaseRsp{

    private static final long serialVersionUID = 1L;

    /**
     * 绑卡请求号
     */
    public String requestno;
    /**
     * 易宝流水号
     */
    public String yborderid;
    /**
     * 银行编码
     */
    public String bankcode;
   
    /**
     * 卡号前六位
     */
    public String cardtop;
    /**
     * 卡号后四位
     */
    public String cardlast;
    /**
     * 订单状态
     */
    public String status;

    public String getRequestno() {
        return requestno;
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

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getCardtop() {
        return cardtop;
    }

    public void setCardtop(String cardtop) {
        this.cardtop = cardtop;
    }

    public String getCardlast() {
        return cardlast;
    }

    public void setCardlast(String cardlast) {
        this.cardlast = cardlast;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
