package com.sdhoo.pdloan.payctr.busi.fuioudk.req;


import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import com.sdhoo.pdloan.payctr.busi.fuioudk.rsp.NewpropayOrderRsp;

/**
 * 代扣下单请求
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 12:02:46
 *
 */
@XObject(value = "REQUEST")
public class NewpropayOrderReq extends FuioudkBaseReq<NewpropayOrderRsp>{

	/**
	 * 版本号
	 */
	@XNode("VERSION")
	private String version;
	
	/**
	 * 商户代码
	 */
	@XNode("MCHNTCD")
	private String mchntcd;
	
	/**
	 * 摘要数据
	 */
	@XNode("SIGN")
	private String sign;
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMchntcd() {
		return mchntcd;
	}

	public void setMchntcd(String mchntcd) {
		this.mchntcd = mchntcd;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * 客户 IP
	 */
	@XNode("USERIP")
	private String userip;

	/**
	 * 交易类型
	 */
	@XNode("TYPE")
	private String type;
	
	/**
	 * 商户订单号
	 */
	@XNode("MCHNTORDERID")
	private String mchntorderid;

	/**
	 * 用户编号
	 */
	@XNode("USERID")
	private String userid;
	
	/**
	 * 交易金额(分)
	 */
	@XNode("AMT")
	private String amt;
	
	/**
	 * 协议号
	 */
	@XNode("PROTOCOLNO")
	private String protocolno;
	
	/**
	 * 是否需要发送短信
	 */
	@XNode("NEEDSENDMSG")
	private String needsendmsg = "0";

	/**
	 * 后台通知 URL
	 */
	@XNode("BACKURL")
	private String backurl;
	
	/**
	 * 保留字段 1
	 */
	@XNode("REM1")
	private String rem1;
	
	/**
	 * 保留字段 2
	 */
	@XNode("REM2")
	private String rem2;
	
	/**
	 * 保留字段 3
	 */
	@XNode("REM3")
	private String rem3;
	
	/**
	 * 签名类型
	 */
	@XNode("SIGNTP")
	private String signtp;
	
	

	public String getUserip() {
		return userip;
	}


	public void setUserip(String userip) {
		this.userip = userip;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMchntorderid() {
		return mchntorderid;
	}


	public void setMchntorderid(String mchntorderid) {
		this.mchntorderid = mchntorderid;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public String getAmt() {
		return amt;
	}


	public void setAmt(String amt) {
		this.amt = amt;
	}


	public String getProtocolno() {
		return protocolno;
	}


	public void setProtocolno(String protocolno) {
		this.protocolno = protocolno;
	}


	public String getNeedsendmsg() {
		return needsendmsg;
	}


	public void setNeedsendmsg(String needsendmsg) {
		this.needsendmsg = needsendmsg;
	}


	public String getBackurl() {
		return backurl;
	}


	public void setBackurl(String backurl) {
		this.backurl = backurl;
	}


	public String getRem1() {
		return rem1;
	}


	public void setRem1(String rem1) {
		this.rem1 = rem1;
	}


	public String getRem2() {
		return rem2;
	}


	public void setRem2(String rem2) {
		this.rem2 = rem2;
	}


	public String getRem3() {
		return rem3;
	}


	public void setRem3(String rem3) {
		this.rem3 = rem3;
	}


	public String getSigntp() {
		return signtp;
	}


	public void setSigntp(String signtp) {
		this.signtp = signtp;
	}


	public String doGetSignStr(String key){
		StringBuilder sb = new StringBuilder();
		sb.append(type);
		sb.append("|");
		sb.append(this.getVersion());
		sb.append("|");
		sb.append(this.getMchntcd());
		sb.append("|");
		sb.append(this.getMchntorderid());
		sb.append("|");
		sb.append(this.getUserid());
		sb.append("|");
		sb.append(this.getProtocolno());
		sb.append("|");
		sb.append(this.getAmt());
		sb.append("|");
		sb.append(this.getBackurl());
		sb.append("|");
		sb.append(this.getUserip());
		sb.append("|");
		sb.append(key);
		return sb.toString();
	}

	public String doGetReqUrlPath() {
		return "/newpropay/order.pay";
	}

	@Override
	public Class<NewpropayOrderRsp> doGetRspClass() {
		return NewpropayOrderRsp.class;
	}

	@Override
	public String doGetDataParamName() {
		return "APIFMS";
	}
	@Override
	public boolean hasEncrypt() {
		return true;
	}

}
