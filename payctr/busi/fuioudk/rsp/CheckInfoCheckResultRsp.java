package com.sdhoo.pdloan.payctr.busi.fuioudk.rsp;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 商户订单号查询订单结果响应
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 15:13:31
 *
 */
@XObject(value="RESPONSE")
public class CheckInfoCheckResultRsp extends FuioudkBaseRsp {
	
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
	 * 商户订单号
	 */
	@XNode("MCHNTORDERID")
	private String mchntorderid ;
	
	
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
	 * 交易日期
	 */
	@XNode("ORDERDATE")
	private String orderdate ;


	public String getMchntorderid() {
		return mchntorderid;
	}

	public void setMchntorderid(String mchntorderid) {
		this.mchntorderid = mchntorderid;
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

	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}
	

	
}
