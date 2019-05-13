package com.sdhoo.pdloan.payctr.busi.baofudf.bens;

import java.util.List;


public class TransContent<T>{

	private TransHead trans_head;

	private List<T> trans_reqDatas;

	private String data_type;
	
	public TransHead getTrans_head() {
		return trans_head;
	}
	public void setTrans_head(TransHead trans_head) {
		this.trans_head = trans_head;
	}
	public List<T> getTrans_reqDatas() {
		return trans_reqDatas;
	}
	public void setTrans_reqDatas(List<T> trans_reqDatas) {
		this.trans_reqDatas = trans_reqDatas;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

}