package com.sdhoo.pdloan.payctr.busi.baofudf.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.payctr.busi.baofudf.bens.req.BaofudfBaseReq;
import com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp.BaofudfBaseRsp;
import com.sdhoo.pdloan.payctr.busi.baofudf.dto.BaofudfCfgInf;

/**
 * 宝付客户端服务.
 * @author SDPC_LIU
 *
 */
public interface BaofudfCltService {
	
	
	/**
	 * 执行内容.
	 * @param context
	 * @return
	 * @throws BaseServiceException 
	 */
//	<T extends BaofudfBaseRsp>T doExecuteBaseReq(BaofudfBaseReq<T> context) throws BaseServiceException ;

//	/**
//	 * 获取 配置的终端号 
//	 * @return
//	 */
//	String getTerminalId();

//	/**
//	 * 获取 配置的商户号 
//	 * @return
//	 */
//	String getMemberId();

	/**
	 * 创建一个交易序列号.
	 * @return
	 */
	String generateTransSerialNo();

	/**
	 * 解密数据.
	 * @param responseStr
	 * @param dfCfg 代付配置信息
	 * @return
	 * @throws Exception
	 */
	String decryptRespContent(String responseStr, BaofudfCfgInf dfCfg) throws Exception;
	
	/**
	 * 加密数据.
	 * @param jsonStr
	 * @param dfCfg 代付配置信息
	 * @return
	 * @throws Exception
	 */
	String encriptBasereqDataContent(String jsonStr, BaofudfCfgInf dfCfg) throws Exception;
	
	/**
	 * 执行请求
	 * @param baseReq
	 * @param dfCfg 代付配置信息
	 * @return
	 * @throws BaseServiceException
	 */
    <T extends BaofudfBaseRsp> T doExecuteBaseReq(BaofudfBaseReq<T> baseReq, BaofudfCfgInf dfCfg) throws BaseServiceException; 

//	/**
//	 * 是否测试环境
//	 * @return
//	 */
//	boolean getIsTestenv();


}
