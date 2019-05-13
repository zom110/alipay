package com.sdhoo.pdloan.payctr.busi.fuioudf.rsp;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeList;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 代付交易响应
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-26 11:25:07
 *
 */
@XObject(value="qrytransrsp")
public class QrytransreqRsp extends FuioudfBaseRsp {
	
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
	 * 响应交易列表
	 */
	@XNodeList(type=ArrayList.class, componentType=QrytransreqRspTrans.class,value = "trans")
	List<QrytransreqRspTrans> trans = new ArrayList<QrytransreqRsp.QrytransreqRspTrans>() ;
	
	
	public List<QrytransreqRspTrans> getTrans() {
		return trans;
	}

	public void setTrans(List<QrytransreqRspTrans> trans) {
		this.trans = trans;
	}

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

	@XObject(value="trans")
	public static class QrytransreqRspTrans{

		@XNode("merdt")
		private String merdt ;  // 原请求日期
		
		/**
		 * 原订单流水号
		 */
		@XNode("trans")
		private String orderno ;
		
		/**
		 * 账号
		 */
		@XNode("accntno")
		private String accntno ;
		
		/**
		 * 账户名称
		 */
		@XNode("accntnm")
		private String accntnm ;
		
		/**
		 * 交易金额(分)
		 */
		@XNode("trans")
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
		 * 交易状态
		 */
		@XNode("state")
		private String state ;
		
		/**
		 * 交易结果
		 */
		@XNode("result")
		private String result ;
		
		/**
		 * 结果原因
		 */
		@XNode("reason")
		private String reason ;

		public String getMerdt() {
			return merdt;
		}

		public void setMerdt(String merdt) {
			this.merdt = merdt;
		}

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
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

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}
		
		
		
	}

	

}
