package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.JqRecordRsp;

/**
 * 鉴权记录查询请求.
 * @author Fangzhiping
 *
 */
public class BkRecordReq extends YibaodkBaseReq<JqRecordRsp>{

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
	/**
	 * 用户标识
	 */
	private String identityid ;
	/**
	 * 用户标识类型
	 */
	private String identitytype ;
	

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
        return "/rest/v1.0/paperorder/auth/query";
    }

    @Override
    public Class<JqRecordRsp> doGetRespClass() {
        return JqRecordRsp.class;
    }
}
