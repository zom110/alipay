package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.BkPaySmsValiAgainRsp;

/**
 * 绑卡支付短验重发请求.
 * @author Fangzhiping
 *
 */
public class BkPaySmsValiAgainReq extends YibaodkBaseReq<BkPaySmsValiAgainRsp>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  充值请求号.
	 */
	private String requestno ; 

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }
    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/paperorder/pay/resend";
    }

    @Override
    public Class<BkPaySmsValiAgainRsp> doGetRespClass() {
        return BkPaySmsValiAgainRsp.class;
    }
}
