package com.sdhoo.pdloan.payctr.busi.fuioudk.bean;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

@XObject(value = "REQUEST")
public class APIProPayXmlBeanReq extends ApiSdkH5Bean{
	
	@XNode("VERSION")
	private String version;//VERSION
	@XNode("MCHNTCD")
	private String mchntcd;//MCHNTCD
	@XNode("ORDERID")
	private String orderid;//ORDERID
	@XNode("MCHNTORDERID")
	private String mchntorderid;//MCHNTORDERID
	@XNode("USERID")
	private String userid;//USERID
	@XNode("VERCD")
	private String vercd;//VERCD
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
	@XNode("SIGNPAY")
	private String signpay;//SIGNPAY
	@XNode("PROTOCOLNO")
	private String protocolno;
	@XNode("USERIP")
	private String userip;//USERIP
	
	
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getMchntorderid() {
		return mchntorderid;
	}
	public void setMchntorderid(String mchntorderid) {
		this.mchntorderid = mchntorderid;
	}
	public String getVercd() {
		return vercd;
	}
	public void setVercd(String vercd) {
		this.vercd = vercd;
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
	public String getSignpay() {
		return signpay;
	}
	public void setSignpay(String signpay) {
		this.signpay = signpay;
	}
	public String getUserip() {
		return userip;
	}
	public void setUserip(String userip) {
		this.userip = userip;
	}
	public String getProtocolno() {
		return protocolno;
	}
	public void setProtocolno(String protocolno) {
		this.protocolno = protocolno;
	}

	public String signStr(String key){
		//TYPE+"|"+VERSION+"|"+MCHNTCD+"|"+ORDERID+"|"+MCHNTORDERID+"|"+PROTOCOLNO+"|"+USERID+"|"+VERCD+"|"+"盐值"
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(type)
				.append("|")
				.append(version)
				.append("|")
				.append(mchntcd)
				.append("|")
				.append(orderid)
				.append("|")
				.append(mchntorderid)
				.append("|")
				.append(protocolno)
				.append("|")
				.append(userid)
				.append("|")
				.append(vercd)
				.append("|")
				.append(key);
				System.out.println("返回信息明文-----"+stringBuffer.toString());
				return stringBuffer.toString();
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
