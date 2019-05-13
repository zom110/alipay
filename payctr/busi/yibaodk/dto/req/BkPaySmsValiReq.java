package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.BkPaySmsValiRsp;

/**
 * 绑卡支付短验确认请求.
 * @author Fangzhiping
 *
 */
public class BkPaySmsValiReq extends YibaodkBaseReq<BkPaySmsValiRsp>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *  充值请求号.
	 */
	private String requestno ; 

	/**
	 * 短验码
	 */
	private String validatecode ;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    public String getValidatecode() {
        return validatecode;
    }

    public void setValidatecode(String validatecode) {
        this.validatecode = validatecode;
    }
    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/paperorder/pay/confirm";
    }

    @Override
    public Class<BkPaySmsValiRsp> doGetRespClass() {
        return BkPaySmsValiRsp.class;
    }
}
