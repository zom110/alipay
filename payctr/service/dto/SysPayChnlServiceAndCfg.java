package com.sdhoo.pdloan.payctr.service.dto;

import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.service.SysPayChnlService;

public class SysPayChnlServiceAndCfg {

	/**
	 * 代付渠道服务.
	 */
	private SysPayChnlService sysPayChnlService ;
	
	/**
	 * 渠道配置
	 */
	private DtPctIfcChnlCfg pctIfcChnkCfg ;

	public SysPayChnlService getSysPayChnlService() {
		return sysPayChnlService;
	}

	public void setSysPayChnlService(SysPayChnlService sysPayChnlService) {
		this.sysPayChnlService = sysPayChnlService;
	}

	public DtPctIfcChnlCfg getPctIfcChnkCfg() {
		return pctIfcChnkCfg;
	}

	public void setPctIfcChnkCfg(DtPctIfcChnlCfg pctIfcChnkCfg) {
		this.pctIfcChnkCfg = pctIfcChnkCfg;
	}
	
	
}
