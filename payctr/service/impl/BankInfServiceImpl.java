package com.sdhoo.pdloan.payctr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctBankInfo;
import com.sdhoo.pdloan.bcrud.service.DtPctBankInfoService;
import com.sdhoo.pdloan.payctr.enums.BankInfStepEnum;
import com.sdhoo.pdloan.payctr.service.BankInfService;

/**
 * 银行信息服务.实现
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-14 20:33:11
 *
 */
@Service
public class BankInfServiceImpl implements BankInfService {

	@Autowired
	private DtPctBankInfoService dtPctBankInfoService ;

	@Override
	public DtPctBankInfo getBankInfByName(String bankName) {
		if(StringUtils.isEmpty(bankName)) {
			return null ;
		}
		Map<String,Object> qmap = new HashMap<>(2);
		qmap.put("biName", bankName);
		List<DtPctBankInfo> bankInfList = dtPctBankInfoService.selectByCriteria(qmap, 0, 2);
		if(bankInfList.size() != 1 ) {
			return null ;
		}else {
			return bankInfList.get(0);
		}
	}

	@Override
	public List<DtPctBankInfo> getAvalidBankInfList() {
		
		Map<String,Object> qmap = new HashMap<>(2);
		qmap.put("stepStatus", BankInfStepEnum.AVALABLE.getCode());
		List<DtPctBankInfo> bankInfList = dtPctBankInfoService.selectByCriteria(qmap, 0, 2);
		return bankInfList;
	}

}
