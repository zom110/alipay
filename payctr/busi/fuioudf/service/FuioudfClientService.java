package com.sdhoo.pdloan.payctr.busi.fuioudf.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.busi.fuioudf.req.FuioudfBaseReq;
import com.sdhoo.pdloan.payctr.busi.fuioudf.rsp.FuioudfBaseRsp;

/**
 * 富友代付客户端服务.
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-24 21:04:39
 *
 */
public interface FuioudfClientService {

	
	
	
	
	/**
	 * 调用请求
	 * @param beanReq
	 * @param cfg
	 * @return
	 * @throws BaseServiceException
	 */
	<T extends FuioudfBaseRsp> T doExecuteBaseReq(FuioudfBaseReq<T> beanReq, DtPctIfcChnlCfg cfg) throws BaseServiceException;

	
	
	
	
	
}
