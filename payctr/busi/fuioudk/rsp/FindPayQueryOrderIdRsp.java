package com.sdhoo.pdloan.payctr.busi.fuioudk.rsp;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 商户订单号查询订单结果响应
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 15:13:31
 *
 */
@XObject(value="FM")
public class FindPayQueryOrderIdRsp extends FuioudkBaseRsp {
	
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
	@XNode("Sign")
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
	 * 响应代码
	 */
	@XNode("Rcd")
	private String rcd ;
	
	
	/**
	 * 中文描述
	 */
	@XNode("RDesc")
	private String rdesc ;


	public String getRcd() {
		return rcd;
	}

	public void setRcd(String rcd) {
		this.rcd = rcd;
	}

	public String getRdesc() {
		return rdesc;
	}

	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
	}
	
	
}
