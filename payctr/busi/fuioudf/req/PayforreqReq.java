package com.sdhoo.pdloan.payctr.busi.fuioudf.req;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import com.sdhoo.pdloan.payctr.busi.fuioudf.rsp.PayforreqRsp;

@XObject(value="payforreq")
public class PayforreqReq extends FuioudfBaseReq<PayforreqRsp> {
	
	
	/**
	 * 版本号
	 */
	@XNode("ver")
	private String ver = "1.0" ;  

	/**
	 * 请求日期,yyyyMMdd
	 */
	@XNode("merdt")
	private String merdt ; 
	
	
	/**
	 * 请求流水号
	 */
	@XNode("orderno")
	private String orderno ; 
	
	/**
	 * 总行代码
	 */
	@XNode("bankno")
	private String bankno ; 
	
	/**
	 * 城市代码
	 */
	@XNode("cityno")
	private String cityno ; 
	
	/**
	 * 支行名称
	 */
	@XNode("branchnm")
	private String branchnm ; 

	/**
	 * 账号(银行卡号)
	 */
	@XNode("accntno")
	private String accntno ; 

	/**
	 * 账户名称(用户姓名,企业名称等)
	 */
	@XNode("accntnm")
	private String accntnm ; 

	/**
	 * 金额(分)
	 */
	@XNode("amt")
	private String amt ; 
	
	/**
	 * 企业流水号
	 */
	@XNode("entseq")
	private String entseq ; 
	
	/**
	 * 备注
	 */
	@XNode("memo")
	private String memo ; 

	/**
	 * 手机号(短信通知使用手机号)
	 */
	@XNode("mobile")
	private String mobile ; 

	/**
	 * 是否返回交易状态类别
	 */
	@XNode("addDesc")
	private String addDesc = "1" ; 
	

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getMerdt() {
		return merdt;
	}

	public void setMerdt(String merdt) {
		this.merdt = merdt;
	}

	public String getBankno() {
		return bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getCityno() {
		return cityno;
	}

	public void setCityno(String cityno) {
		this.cityno = cityno;
	}

	public String getBranchnm() {
		return branchnm;
	}

	public void setBranchnm(String branchnm) {
		this.branchnm = branchnm;
	}

	public String getAccntno() {
		return accntno;
	}

	public void setAccntno(String accntno) {
		this.accntno = accntno;
	}

	public String getAccntnm() {
		return accntnm;
	}

	public void setAccntnm(String accntnm) {
		this.accntnm = accntnm;
	}

	public String getAmt() {
		return amt;
	}

	public void setAmt(String amt) {
		this.amt = amt;
	}

	public String getEntseq() {
		return entseq;
	}

	public void setEntseq(String entseq) {
		this.entseq = entseq;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddDesc() {
		return addDesc;
	}

	public void setAddDesc(String addDesc) {
		this.addDesc = addDesc;
	}

	@Override
	public String doGetReqtype() {
		return "payforreq";
	}

	@Override
	public String doGetReqUrlPath() {
		
		return "/req.do";
	}

	@Override
	public Class<PayforreqRsp> doGetRspClass() {
		return PayforreqRsp.class;
	}


	
	
			
}
