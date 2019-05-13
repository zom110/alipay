package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.JqBkSmsValiRsp;

/**
 * 鉴权绑卡短验确认
 * @author Fangzhiping
 *
 */
public class JqBkSmsValiReq extends YibaodkBaseReq<JqBkSmsValiRsp>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 绑卡请求号.
     */
    private String requestno;
    /**
     * 短验码
     */
    private String validatecode;
    
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
        return "/rest/v1.0/paperorder/auth/confirm";
    }

    @Override
    public Class<JqBkSmsValiRsp> doGetRespClass() {
        return JqBkSmsValiRsp.class;
    }

}
