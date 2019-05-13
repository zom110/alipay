package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseRsp;
/**
 * 绑卡支付查询返回数据
 * @author fangzhiping
 * @date 2018年4月18日
 */
public class BkPayRecordRsp extends YibaodkBaseRsp{
    
    private static final long serialVersionUID = 1L; 
    /**
     * 支付请求号
     */
    public String requestno;
    /**
     * 易宝流水号
     */
    public String yborderid;
    /**
     * 订单状态
     */
    public String status;
    /**
     * 金额
     */
    public String amount;
    /**
     * 支付类型
     */
    public String paytype;
    /**
     * 卡号前六位
     */
    public String cardtop;
    /**
     * 卡号后四位
     */
    public String cardlast;
    /**
     * 银行编码
     */
    public String bankcode;
    /**
     * 交易成功时间
     */
    public String banksuccessdate;
    
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
    public static long getSerialversionuid() {
        return serialVersionUID;
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
    public String getBankcode() {
        return bankcode;
    }
    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getPaytype() {
        return paytype;
    }
    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }
    public String getBanksuccessdate() {
        return banksuccessdate;
    }
    public void setBanksuccessdate(String banksuccessdate) {
        this.banksuccessdate = banksuccessdate;
    }
}
