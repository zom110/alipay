package com.sdhoo.pdloan.payctr.busi.fuioudk.req;


import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import com.sdhoo.pdloan.payctr.busi.fuioudk.rsp.NewpropayBindCommitRsp;

/**
 * 绑卡确认
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 12:02:46
 *
 */
@XObject(value = "REQUEST")
public class NewpropayBindCommitReq extends FuioudkBaseReq<NewpropayBindCommitRsp>{
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
	 * 用户编号
	 */
	@XNode("USERID")
	private String userid;
	
	/**
	 * 交易请求日期
	 */
	@XNode("TRADEDATE")
	private String tradedate ;

	
	/**
	 * 商户流水号
	 */
	@XNode("MCHNTSSN")
	private String mchntssn;
	
	/**
	 * 账户名称
	 */
	@XNode("ACCOUNT")
	private String account;

	/**
	 * 账户名称
	 */
	@XNode("CARDNO")
	private String cardno;
	
	
	
	/**
	 * 证件类型
	 */
	@XNode("IDTYPE")
	private String idtype;
	
	/**
	 * 证件号码
	 */
	@XNode("IDCARD")
	private String idcard;
	
	/**
	 * 手机号码
	 */
	@XNode("MOBILENO")
	private String mobileno;

	/**
	 * 手机验证码
	 */
	@XNode("MSGCODE")
	private String msgcode;
	

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTradedate() {
		return tradedate;
	}

	public void setTradedate(String tradedate) {
		this.tradedate = tradedate;
	}

	public String getMchntssn() {
		return mchntssn;
	}

	public void setMchntssn(String mchntssn) {
		this.mchntssn = mchntssn;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getMsgcode() {
		return msgcode;
	}

	public void setMsgcode(String msgcode) {
		this.msgcode = msgcode;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String doGetSignStr(String key){
		StringBuilder sb = new StringBuilder();
		sb.append(version);
		sb.append("|");
		sb.append(mchntssn);
		sb.append("|");
		sb.append(mchntcd);
		sb.append("|");
		sb.append(userid);
		sb.append("|");
		sb.append(account);
		sb.append("|");
		sb.append(cardno);
		sb.append("|");
		sb.append(idtype);
		sb.append("|");
		sb.append(idcard);
		sb.append("|");
		sb.append(mobileno);
		sb.append("|");
		sb.append(msgcode);
		sb.append("|");
		sb.append(key);
		return sb.toString();
	}
	
	
	public String doGetReqUrlPath() {
		return "/newpropay/bindCommit.pay";
	}

	@Override
	public Class<NewpropayBindCommitRsp> doGetRspClass() {
		return NewpropayBindCommitRsp.class;
	}

	@Override
	public String doGetDataParamName() {
		return "APIFMS";
	}

	@Override
	public boolean hasEncrypt() {
		return true;
	}
}
