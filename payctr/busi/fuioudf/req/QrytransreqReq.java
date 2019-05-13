package com.sdhoo.pdloan.payctr.busi.fuioudf.req;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

import com.sdhoo.pdloan.payctr.busi.fuioudf.rsp.QrytransreqRsp;

@XObject(value="qrytransreq")
public class QrytransreqReq extends FuioudfBaseReq<QrytransreqRsp> {
	
	
	/**
	 * 版本号
	 */
	@XNode("ver")
	private String ver = "1.0" ;  
	
	/**
	 * 业务代码
	 */
	@XNode("busicd")
	private String busicd = "AC01" ; 

	/**
	 * 请求流水号
	 */
	@XNode("orderno")
	private String orderno ; 

	/**
	 * 开始日期,yyyyMMdd
	 */
	@XNode("startdt")
	private String startdt ; 
	
	/**
	 * 结束日期,yyyyMMdd
	 */
	@XNode("enddt")
	private String enddt ; 
	
	
	/**
	 * 交易状态
	 */
	@XNode("transst")
	private String transst ; 
	
	/**
	 * 交易来源
	 */
	@XNode("srcModuleCd")
	private String srcModuleCd ; 
	
	

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getBusicd() {
		return busicd;
	}

	public void setBusicd(String busicd) {
		this.busicd = busicd;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getStartdt() {
		return startdt;
	}

	public void setStartdt(String startdt) {
		this.startdt = startdt;
	}

	public String getEnddt() {
		return enddt;
	}

	public void setEnddt(String enddt) {
		this.enddt = enddt;
	}

	public String getTransst() {
		return transst;
	}

	public void setTransst(String transst) {
		this.transst = transst;
	}

	public String getSrcModuleCd() {
		return srcModuleCd;
	}

	public void setSrcModuleCd(String srcModuleCd) {
		this.srcModuleCd = srcModuleCd;
	}

	@Override
	public String doGetReqtype() {
		return "qrytransreq";
	}

	@Override
	public String doGetReqUrlPath() {
		return "/req.do";
	}

	@Override
	public Class<QrytransreqRsp> doGetRspClass() {
		return QrytransreqRsp.class;
	}


	
	
			
}
