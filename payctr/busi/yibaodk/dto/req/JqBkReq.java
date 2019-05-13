package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.JqBkRsp;

/**
 * 鉴权绑卡请求.
 * 
 * @author Fangzhiping
 *
 */
public class JqBkReq extends YibaodkBaseReq<JqBkRsp>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 绑卡请求号.
     */
    private String requestno;
    /**
     * 用户标识
     */
    private String identityid;
    /**
     * 用户标识类型
     */
    private String identitytype;

    /**
     * 卡号.
     */
    private String cardno;
    /**
     * 证件号.
     */
    private String idcardno;
    /**
     * 证件类型.
     */
    private String idcardtype;
    /**
     * 用户姓名.
     */
    private String username;
    /**
     * 手机号.
     */
    private String phone;
    /**
     * 是否发送短信.
     */
    private Boolean issms;
    
    /**
     * 信息类型,
     */
    private String advicesmstype  ;
    
    /**
     * 
     */
    private String callbackurl ;
    
    /**
     * 鉴权类型,COMMON_FOUR(验四)，
允许商户在请求中指定是走验三还是
走验四
（注：商户在易宝需开通拥有鉴权验四
的鉴权类型）
@{BindcardAuthtypeEnum}
     */
    private String authtype ;
    
    /**
     * 备注内容.
     */
    private String remark ; 
    
   
    /**
     * 请求时间(格式yyyy-MM-dd HH:mm:ss)
     */
    private String requesttime;
    /**
     * 扩展信息
     */
    private String extinfos;
    
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

    public String getRequestno() {
        return requestno;
    }

    public void setRequestno(String requestno) {
        this.requestno = requestno;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getIdcardno() {
        return idcardno;
    }

    public void setIdcardno(String idcardno) {
        this.idcardno = idcardno;
    }

    public String getIdcardtype() {
        return idcardtype;
    }

    public void setIdcardtype(String idcardtype) {
        this.idcardtype = idcardtype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIssms() {
        return issms;
    }

    public void setIssms(Boolean issms) {
        this.issms = issms;
    }

    public String getRequesttime() {
        return requesttime;
    }

    public void setRequesttime(String requesttime) {
        this.requesttime = requesttime;
    }

    public String getExtinfos() {
        return extinfos;
    }

    public void setExtinfos(String extinfos) {
        this.extinfos = extinfos;
    }

    public String getAdvicesmstype() {
        return advicesmstype;
    }

    public void setAdvicesmstype(String advicesmstype) {
        this.advicesmstype = advicesmstype;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public String getAuthtype() {
        return authtype;
    }

    public void setAuthtype(String authtype) {
        this.authtype = authtype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/paperorder/unified/auth/request";
    }

    @Override
    public Class<JqBkRsp> doGetRespClass() {
        return JqBkRsp.class;
    }
}
