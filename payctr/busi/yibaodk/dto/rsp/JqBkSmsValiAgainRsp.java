package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseRsp;
/**
 * 鉴权绑卡短验重发返回数据
 * @author fangzhiping
 * @date 2018年4月18日
 */
public class JqBkSmsValiAgainRsp extends YibaodkBaseRsp{

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
     * 订单状态
     */
    public String status;
    /**
     * 短验码
     */
    public String smscode;
    /**
     * 实际短验发送方
     */
    public String codesender;

    public String getYborderid() {
        return yborderid;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
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

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
