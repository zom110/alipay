package com.sdhoo.pdloan.payctr.busi.fuioudk.rsp;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 绑卡消息响应数据
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 15:13:31
 *
 */
@XObject(value="RESPONSE")
public class NewpropayBindMsgRsp extends FuioudkBaseRsp {

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
	 * 协议号
	 */
	@XNode("PROTOCOLNO")
	private String protocolno ;

	/**
	 * 交易流水号
	 */
	@XNode("MCHNTSSN")
	private String mchntssn ;


	public String getProtocolno() {
		return protocolno;
	}

	public void setProtocolno(String protocolno) {
		this.protocolno = protocolno;
	}

	public String getMchntssn() {
		return mchntssn;
	}

	public void setMchntssn(String mchntssn) {
		this.mchntssn = mchntssn;
	}
	
}
