package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.req;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp.BkPayRsp;

/**
 * 绑卡支付请求.
 * 
 * @author Fangzhiping
 *
 */
public class BkPayReq extends YibaodkBaseReq<BkPayRsp>{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * 还款请求号.
     */
    private String requestno;
    /**
     * 是否发送短信.
     */
    private Boolean issms;
    /**
     * 用户标识
     */
    private String identityid;
    /**
     * 用户标识类型
     */
    private String identitytype;

    /**
     * 卡号前六位
     */
    private String cardtop;

    /**
     * 卡号后四位
     */
    private String cardlast;
    /**
     * 还款金额
     */
    private String amount;

    /**
     * 商品名称
     */
    private String productname;

    /**
     * 请求时间(格式yyyy-MM-dd HH:mm:ss)
     */
    private String requesttime;

    /**
     * 终端标识码
     */
    private String terminalno;
    /**
     * 扩展信息
     */
    private String extinfos;
    
    /**
     * 回调地址.
     */
    private String callbackurl ;
    
    
    /**
     * 有效期时间(分钟)
     */
    private String avaliabletime ;
    
    /**
     * 备注
     */
    private String  remark  ;

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getCardtop() {
        return cardtop;
    }

    public void setCardtop(String cardtop) {
        this.cardtop = cardtop;
    }

    public String getCardlast() {
        return cardlast;
    }

    public void setCardlast(String cardlast) {
        this.cardlast = cardlast;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getTerminalno() {
        return terminalno;
    }

    public void setTerminalno(String terminalno) {
        this.terminalno = terminalno;
    }

    public String getExtinfos() {
        return extinfos;
    }

    public void setExtinfos(String extinfos) {
        this.extinfos = extinfos;
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

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvaliabletime() {
        return avaliabletime;
    }

    public void setAvaliabletime(String avaliabletime) {
        this.avaliabletime = avaliabletime;
    }

    @Override
    public String doGetCallUrl() {
        return "/rest/v1.0/paperorder/unified/pay";
    }

    @Override
    public Class<BkPayRsp> doGetRespClass() {
        return BkPayRsp.class;
    }

}
