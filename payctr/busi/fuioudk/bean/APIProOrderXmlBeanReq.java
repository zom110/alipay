package com.sdhoo.pdloan.payctr.busi.fuioudk.bean;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject(value = "REQUEST")
public class APIProOrderXmlBeanReq extends ApiSdkH5Bean{
	
	@XNode("VERSION")
	private String version;//VERSION
	@XNode("MCHNTCD")
	private String mchntcd;//MCHNTCD
	@XNode("MCHNTORDERID")
	private String mchntorderid;//MCHNTORDERID
	@XNode("PROTOCOLNO")
	private String protocolno;//PROTOCOLNO
	@XNode("USERID")
	private String userid;//USERID
	@XNode("AMT")
	private String amt;//AMT
	@XNode("BACKURL")
	private String backurl;//BACKURL
	@XNode("REM1")
	private String rem1;//REM1
	@XNode("REM2")
	private String rem2;//REM2
	@XNode("REM3")
	private String rem3;//REM3
	@XNode("SIGNTP")
	private String signtp;//SIGNTP
	@XNode("SIGN")
	private String sign;//SIGN
	@XNode("TYPE")
	private String type;//TYPE
	@XNode("CVN")
	private String cvn;//CVN
	@XNode("USERIP")
	private String userip;//USERIP
	@XNode("ORDERID")
	private String orderid;
	@XNode("ORDERALIVETIME")
	private String orderalivetime;//ORDERALIVETIME
	
	private String name;
	private String bankcard;
	private String idno;
	private String idtype;
	private String mobile;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBankcard() {
		return bankcard;
	}
	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
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
	public String getMchntorderid() {
		return mchntorderid;
	}
	public void setMchntorderid(String mchntorderid) {
		this.mchntorderid = mchntorderid;
	}
	public String getProtocolno() {
		return protocolno;
	}
	public void setProtocolno(String protocolno) {
		this.protocolno = protocolno;
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCvn() {
		return cvn;
	}
	public void setCvn(String cvn) {
		this.cvn = cvn;
	}
	public String getUserip() {
		return userip;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOrderalivetime() {
		return orderalivetime;
	}
	public void setOrderalivetime(String orderalivetime) {
		this.orderalivetime = orderalivetime;
	}
	

	public String signStr(String key) {
		StringBuffer buffer = new StringBuffer();
				//TYPE+"|"+VERSION+"|"+MCHNTCD+"|"+MCHNTORDERID+"|"+USERID+"|"+PROTOCOLNO+"|"+AMT+"|"+BACKURL+"|"+USERIP+"|"+"商户key"
				buffer.append(type)
				.append("|")
				.append(version)
				.append("|")
				.append(mchntcd)
				.append("|")
				.append(mchntorderid)
				.append("|")
				.append(userid)
				.append("|")
				.append(protocolno)
				.append("|")
				.append(amt)
				.append("|")
				.append(backurl)
				.append("|")
				.append(userip)
				.append("|")
				.append(key);
				System.out.println("返回信息明文-----"+buffer.toString());
				return buffer.toString();
	}
	
	public String signStrMsgReSend(String key) {
		StringBuffer buffer = new StringBuffer();
				//TYPE+"|"+VERSION+"|"+MCHNTCD+"|"+MCHNTORDERID+"|"+USERID+"|"+PROTOCOLNO+"|"+AMT+"|"+BACKURL+"|"+USERIP+"|"+"商户key"
				buffer.append(type)
				.append("|")
				.append(version)
				.append("|")
				.append(mchntcd)
				.append("|")
				.append(orderid)
				.append("|")
				.append(userid)
				.append("|")
				.append(protocolno)
				.append("|")
				.append(amt)
				.append("|")
				.append(backurl)
				.append("|")
				.append(userip)
				.append("|")
				.append(key);
				System.out.println("返回信息明文-----"+buffer.toString());
				return buffer.toString();
	}
	
	public String md5StrPay(String key){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(mchntcd)
		.append("|")
		.append(mchntorderid)
		.append("|")
		.append(userid)
		.append("|")
		.append(protocolno)
		.append("|")
		.append(key);
		System.out.println("md5StrPay信息-----"+stringBuffer.toString());
		return stringBuffer.toString();
	}
}
