package com.sdhoo.pdloan.payctr.busi.fuioudf.rsp;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 富友代付-交易响应
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 15:09:53
 *
 */
@XObject(value="payforreq")
public class FuioudfBaseRsp {
	
	/**
	 * 响应码
	 */
	@XNode("ret")
	private String ret ;
	
	/**
	 * 响应描述
	 */
	@XNode("memo")
	private String memo ;
	
	
	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}


	/**
	 * 是否成功调用
	 * @return
	 */
	public Boolean hasSuccess() {
		return "000000".equals(this.getRet());
	}
}
