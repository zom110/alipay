package com.sdhoo.pdloan.payctr.busi.fuioudf.rsp;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 代付交易响应
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-26 11:25:07
 *
 */
@XObject(value="payforrsp")
public class PayforreqRsp extends FuioudfBaseRsp {
	
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
	
	/**
	 * 交易状态类别, 
        success 成功
        acceptSuccess 受理成功
        internalFail 富友失败
        channelFail 通道失败
        cardInfoError 卡信息错误
        unknowReasons 交易结果未知 
	 */
	@XNode("transStatusDesc")
	private String transStatusDesc ;
	
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

	public String getTransStatusDesc() {
		return transStatusDesc;
	}

	public void setTransStatusDesc(String transStatusDesc) {
		this.transStatusDesc = transStatusDesc;
	}

	

}
