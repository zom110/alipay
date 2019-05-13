package com.sdhoo.pdloan.payctr.busi.fuioudk.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.busi.fuioudk.req.FuioudkBaseReq;
import com.sdhoo.pdloan.payctr.busi.fuioudk.rsp.FuioudkBaseRsp;

/**
 * 富友代扣客户端服务.
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-22 16:55:09
 *
 */
public interface FuioudkClientService {

	
	String generateTransSerialNo();
	
	/**
	 * 调用请求
	 * @param beanReq
	 * @param cfg
	 * @return
	 * @throws BaseServiceException
	 */
	<T extends FuioudkBaseRsp> T doExecuteBaseReq(FuioudkBaseReq<T> beanReq, DtPctIfcChnlCfg cfg) throws BaseServiceException;
	
	

}
