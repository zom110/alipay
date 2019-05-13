package com.sdhoo.pdloan.payctr.service.dto;

import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.service.UserPayChnlService;

public class UserPayChnlServiceAndCfg {
	
	/**
	 * 渠道服务实现类 
	 */
	private UserPayChnlService userPayChnlService ;
	
	/**
	 * 渠道配置
	 */
	private DtPctIfcChnlCfg pctIfcChnkCfg ;


	public UserPayChnlService getUserPayChnlService() {
		return userPayChnlService;
	}

	public void setUserPayChnlService(UserPayChnlService userPayChnlService) {
		this.userPayChnlService = userPayChnlService;
	}

	public DtPctIfcChnlCfg getPctIfcChnkCfg() {
		return pctIfcChnkCfg;
	}

	public void setPctIfcChnkCfg(DtPctIfcChnlCfg pctIfcChnkCfg) {
		this.pctIfcChnkCfg = pctIfcChnkCfg;
	}

}
