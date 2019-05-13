package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.JqBkSmsValiAgainRsp;

/**
 * 鉴权绑卡短验重发请求
 * @author Fangzhiping
 *
 */
public class JqBkSmsValiAgainReq extends YibaodkBaseReq<JqBkSmsValiAgainRsp>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 绑卡请求号.
     */
    private String requestno;
    
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
        return "/rest/v1.0/paperorder/auth/resend";
    }

    @Override
    public Class<JqBkSmsValiAgainRsp> doGetRespClass() {
        return JqBkSmsValiAgainRsp.class;
    }
}
