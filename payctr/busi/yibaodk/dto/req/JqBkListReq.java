package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.JqBkListRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodk.service.YibaodkClientService;

/**
 * 鉴权列表查询请求.
 * 
 * @author Fangzhiping
 *
 */
public class JqBkListReq extends YibaodkBaseReq<JqBkListRsp>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户标识
     */
    @NotNull(message = "用户标识不能为空", groups = { YibaodkClientService.class })
    private String identityid;

    /**
     * 用户标识类型
     */
    @NotNull(message = "用户标识类型不能为空", groups = { YibaodkClientService.class })
    private String identitytype;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


    public String getIdentityid() {
        return identityid;
    }

    public void setIdentityid(String identityid) {
        this.identityid = identityid;
    }

    public String getIdentitytype() {
        return identitytype;
    }

    public void setIdentitytype(String identitytype) {
        this.identitytype = identitytype;
    }

    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/paperorder/auth/bindcard/list";
    }

    @Override
    public Class<JqBkListRsp> doGetRespClass() {
        return JqBkListRsp.class;
    }
}
