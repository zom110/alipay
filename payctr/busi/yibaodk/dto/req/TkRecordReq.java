package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.TkRecordRsp;

/**
 * 退款记录查询请求.
 * @author Fangzhiping
 *
 */
public class TkRecordReq extends YibaodkBaseReq<TkRecordRsp>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 退款请求号.
     */
    private String requestno;
    /**
     * 易宝流水号
     */
    private String yborderid;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getYborderid() {
        return yborderid;
    }

    public void setYborderid(String yborderid) {
        this.yborderid = yborderid;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/paperorder/api/refund/query";
    }

    @Override
    public Class<TkRecordRsp> doGetRespClass() {
        return TkRecordRsp.class;
    }

}
