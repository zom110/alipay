package com.sdhoo.pdloan.payctr.busi.fuioudk.req;


import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import com.sdhoo.pdloan.payctr.busi.fuioudk.rsp.CheckInfoCheckResultRsp;

/**
 * 商户订单号查询订单结果请求
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 12:02:46
 *
 */
@XObject(value = "ORDER")
public class CheckInfoCheckResultReq extends FuioudkBaseReq<CheckInfoCheckResultRsp>{

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
	 * 商户订单号
	 */
	@XNode("MCHNTORDERID")
	private String mchntorderid;


	public String getMchntorderid() {
		return mchntorderid;
	}


	public void setMchntorderid(String mchntorderid) {
		this.mchntorderid = mchntorderid;
	}




	public String doGetSignStr(String key){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getVersion());
		sb.append("|");
		sb.append(this.getMchntcd());
		sb.append("|");
		sb.append(this.getMchntorderid());
		sb.append("|");
		sb.append(key);
		return sb.toString();
	}

	
	public String doGetReqUrlPath() {
		return "/checkInfo/checkResult.pay";
	}

	@Override
	public Class<CheckInfoCheckResultRsp> doGetRspClass() {
		return CheckInfoCheckResultRsp.class;
	}

	@Override
	public String doGetDataParamName() {
		return "FM";
	}

	@Override
	public boolean hasEncrypt() {
		return false;
	}
}
