package com.sdhoo.pdloan.payctr.busi.fuioudk.rsp;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 发起代扣
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 15:13:31
 *
 */
@XObject(value="RESPONSE")
public class NewpropayOrderRsp extends FuioudkBaseRsp {
	
	/**
	 * 版本号
	 */
	@XNode("VERSION")
	private String version ;
	
	/**
	 * 响应码
	 */
	@XNode("RESPONSECODE")
	private String responsecode ;
	
	/**
	 * 响应信息
	 */
	@XNode("RESPONSEMSG")
	private String responsemsg ;
	
	
	/**
	 * 签名数据
	 */
	@XNode("SIGN")
	private String sign ;

	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getResponsecode() {
		return responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	public String getResponsemsg() {
		return responsemsg;
	}

	public void setResponsemsg(String responsemsg) {
		this.responsemsg = responsemsg;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	/**
	 * 类型
	 */
	@XNode("TYPE")
	private String type ;
	
	
	/**
	 * 商户号
	 */
	@XNode("MCHNTCD")
	private String mchntcd ;
	
	
	/**
	 * 用户编号
	 */
	@XNode("USERID")
	private String userid ;
	
	
	/**
	 * 商户订单号
	 */
	@XNode("MCHNTORDERID")
	private String mchntorderid ;
	
	/**
	 * 富友订单号
	 */
	@XNode("ORDERID")
	private String orderid ;
	
	
	/**
	 * 协议号
	 */
	@XNode("PROTOCOLNO")
	private String protocolno ;
	
	
	/**
	 * 银行卡号
	 */
	@XNode("BANKCARD")
	private String bankcard ;
	
	
	/**
	 * 交易金额(分)
	 */
	@XNode("AMT")
	private String amt ;
	
	
	/**
	 * 保留字段 1
	 */
	@XNode("REM")
	private String rem ;
	
	
	/**
	 * 保留字段 2
	 */
	@XNode("REM2")
	private String rem2 ;
	
	
	/**
	 * 保留字段 3
	 */
	@XNode("REM3")
	private String rem3 ;
	
	
	/**
	 * 签名类型,MD5或RSA
	 */
	@XNode("SIGNTP")
	private String signtp ;


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMchntcd() {
		return mchntcd;
	}


	public void setMchntcd(String mchntcd) {
		this.mchntcd = mchntcd;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
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


	public String getBankcard() {
		return bankcard;
	}


	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}


	public String getAmt() {
		return amt;
	}


	public void setAmt(String amt) {
		this.amt = amt;
	}


	public String getRem() {
		return rem;
	}


	public void setRem(String rem) {
		this.rem = rem;
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


	public String getOrderid() {
		return orderid;
	}


	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}


	public String getSigntp() {
		return signtp;
	}


	public void setSigntp(String signtp) {
		this.signtp = signtp;
	}

	
}
