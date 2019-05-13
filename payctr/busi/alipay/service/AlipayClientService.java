package com.sdhoo.pdloan.payctr.busi.alipay.service;

import com.alipay.api.AlipayClient;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;

public interface AlipayClientService {

	
	/**
	 * 
	 * @param cfgInf
	 * @return
	 */
	AlipayClient getAlipayClientByCfg(DtPctIfcChnlCfg cfgInf); 

}
