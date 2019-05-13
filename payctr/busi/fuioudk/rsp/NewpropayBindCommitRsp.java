package com.sdhoo.pdloan.payctr.busi.fuioudk.rsp;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 绑卡短验确认响应
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 15:13:31
 *
 */
@XObject(value="RESPONSE")
public class NewpropayBindCommitRsp extends FuioudkBaseRsp {

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
	 * 商户编号
	 */
	@XNode("MCHNTCD")
	private String mchntcd ;
	

	/**
	 * 商户流水号
	 */
	@XNode("MCHNTSSN")
	private String mchntssn ;
	
	/**
	 * 协议号
	 */
	@XNode("PROTOCOLNO")
	private String protocolno ;
	

	public String getProtocolno() {
		return protocolno;
	}

	public void setProtocolno(String protocolno) {
		this.protocolno = protocolno;
	}

	public String getMchntcd() {
		return mchntcd;
	}

	public void setMchntcd(String mchntcd) {
		this.mchntcd = mchntcd;
	}


	public String getMchntssn() {
		return mchntssn;
	}

	public void setMchntssn(String mchntssn) {
		this.mchntssn = mchntssn;
	}
	
}
