package com.sdhoo.pdloan.payctr.busi.fuioudk.rsp;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 富友基本响应信息
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 15:09:53
 *
 */
@XObject(value="RESPONSE")
public class FuioudkBaseRsp {

	
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
	 * 是否成功调用
	 * @return
	 */
	public Boolean hasSuccess() {
		return "0000".equals(this.getResponsecode());
	}
}
