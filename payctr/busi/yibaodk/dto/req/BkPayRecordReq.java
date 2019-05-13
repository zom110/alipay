package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.BkPayRecordRsp;

/**
 * 绑卡支付查询请求.
 * @author Fangzhiping
 *
 */
public class BkPayRecordReq extends YibaodkBaseReq<BkPayRecordRsp>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  绑卡请求号.
	 */
	private String requestno ; 

	/**
	 * 易宝流水号
	 */
	private String yborderid ;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
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

    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/paperorder/api/pay/query";
    }

    @Override
    public Class<BkPayRecordRsp> doGetRespClass() {
        return BkPayRecordRsp.class;
    }
}
