package com.sdhoo.pdloan.payctr.service;

import java.util.List;

import com.sdhoo.pdloan.bcrud.model.DtPctBankInfo;

/**
 * 银行信息服务.
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-14 20:32:49
 *
 */
public interface BankInfService {

	/**
	 * 
	 * @param bankName
	 * @return
	 */
	public DtPctBankInfo getBankInfByName(String bankName);
	
	
	/**
	 * 获取激活状态下的银行列表
	 * @return
	 */
	public List<DtPctBankInfo> getAvalidBankInfList();
	
	
}
