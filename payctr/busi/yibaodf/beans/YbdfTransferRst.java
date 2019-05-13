package com.sdhoo.pdloan.payctr.busi.yibaodf.beans;

/**
 * 交易结果(一笔)
 * @author Administrator(LiuJianbin)
 * @data 2018-09-10 19:49:56
 *
 */
public class YbdfTransferRst {

	private String bankCode ; // ICBC",
	private String batchNo ; // 180910110601004",
	private String urgencyType ; // 加急类型, MD_URGENCY",
	private String orderId ; // DC180910110601004",
	private String accountName ; // ",
	private String provinceCode ; // 01",
	private String cityCode ; // 0101",
	private String leaveWord ; // 代付",
	private String transferStatusCode ; // 0026",
	private String bankName ; // 工商银行",
	private String accountNumber ; // ",
	private String feeType ; // SOURCE",
	private String bankTrxStatusCode ; // S",
	private String bankMsg ; // 银行信息 
	private String finishDate ; // 2018-09-10 19:05:52"
	private Double amount ; // 金额, 1.0,
	private Double fee ; // 服务费用 0.5,
	private Boolean urgency ; // 是否加急 true,
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getUrgencyType() {
		return urgencyType;
	}
	public void setUrgencyType(String urgencyType) {
		this.urgencyType = urgencyType;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getLeaveWord() {
		return leaveWord;
	}
	public void setLeaveWord(String leaveWord) {
		this.leaveWord = leaveWord;
	}
	public String getTransferStatusCode() {
		return transferStatusCode;
	}
	public void setTransferStatusCode(String transferStatusCode) {
		this.transferStatusCode = transferStatusCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getBankTrxStatusCode() {
		return bankTrxStatusCode;
	}
	public void setBankTrxStatusCode(String bankTrxStatusCode) {
		this.bankTrxStatusCode = bankTrxStatusCode;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public Boolean getUrgency() {
		return urgency;
	}
	public void setUrgency(Boolean urgency) {
		this.urgency = urgency;
	}
	public String getBankMsg() {
		return bankMsg;
	}
	public void setBankMsg(String bankMsg) {
		this.bankMsg = bankMsg;
	}

	
}
