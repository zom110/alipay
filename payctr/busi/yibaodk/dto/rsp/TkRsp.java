package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseRsp;
/**
 * 退款返回数据
 * @author fangzhiping
 * @date 2018年4月18日
 */
public class TkRsp extends YibaodkBaseRsp{
    
    private static final long serialVersionUID = 1L; 
    /**
     * 退款请求号
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
     * 退款金额
     */
    public String amount;
    
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
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
   
}
