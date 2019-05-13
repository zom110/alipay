package com.sdhoo.pdloan.payctr.busi.fuioudk.req;


import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import com.sdhoo.pdloan.payctr.busi.fuioudk.rsp.FindPayQueryOrderIdRsp;

/**
 * 渠道订单号查询订单结果请求
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 12:02:46
 *
 */
@XObject(value = "FM")
public class FindPayQueryOrderIdReq extends FuioudkBaseReq<FindPayQueryOrderIdRsp>{

	
	/**
	 * 商户代码
	 */
	@XNode("MchntCd")
	private String mchntCd;
	
	/**
	 * 摘要数据
	 */
	@XNode("Sign")
	private String sign;
	
	

	public String getMchntCd() {
		return mchntCd;
	}

	public void setMchntCd(String mchntCd) {
		this.mchntCd = mchntCd;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	/**
	 * 渠道订单号
	 */
	@XNode("OrderId")
	private String orderId;


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String doGetSignStr(String key){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getMchntCd());
		sb.append("|");
		sb.append(this.getOrderId());
		sb.append("|");
		sb.append(key);
		return sb.toString();
	}

	
	public String doGetReqUrlPath() {
		return "/findPay/queryOrderId.pay";
	}

	@Override
	public Class<FindPayQueryOrderIdRsp> doGetRspClass() {
		return FindPayQueryOrderIdRsp.class;
	}

	@Override
	public String doGetDataParamName() {
		return "FM";
	}

	@Override
	public void setMchntcd(String memberId) {
		this.mchntCd = memberId;
	}

	@Override
	public void setVersion(String version) {
		
	}

	@Override
	public String getVersion() {
		return "";
	}

	@Override
	public boolean hasEncrypt() {
		return false;
	}
}
