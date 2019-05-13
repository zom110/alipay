package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseRsp;
/**
 * 绑卡支付返回数据.
 * @author fangzhiping
 * @date 2018年4月18日
 */
public class BkPayRsp extends YibaodkBaseRsp{

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
     * 订单状态
     */
    public String status;
    /**
     * 是否发送短验
     */
    public String issms;
    /**
     * 还款金额
     */
    public String amount;
    
    /**
     * 短验码
     */
    public String smscode;
    /**
     * 短验发送方
     */
    public String codesender;
    /**
     * 实际短验发送类型
     */
    public String smstype;
    /**
     * 备注
     */
    public String remark;
   
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

    public String getIssms() {
        return issms;
    }

    public void setIssms(String issms) {
        this.issms = issms;
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

    public String getSmstype() {
        return smstype;
    }

    public void setSmstype(String smstype) {
        this.smstype = smstype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
