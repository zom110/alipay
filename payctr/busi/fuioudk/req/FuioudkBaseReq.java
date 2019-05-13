package com.sdhoo.pdloan.payctr.busi.fuioudk.req;

import com.sdhoo.pdloan.payctr.busi.fuioudk.rsp.FuioudkBaseRsp;

/**
 * 富友基本请求
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-23 15:09:53
 *
 */
public abstract class FuioudkBaseReq<T extends FuioudkBaseRsp> {

	
	/**
	 * 设置版本数据
	 * @param version
	 */
	public abstract void setVersion(String version);
	
	/**
	 * 获取版本号
	 * @param version
	 */
	public abstract String getVersion();

	/**
	 * 设置商编号
	 * @param memberId
	 */
	public abstract void setMchntcd(String memberId) ;

	/**
	 * 设置签名值
	 * @param signStr
	 */
	public abstract void setSign(String signStr) ;


	/**
	 * 获取签名内容
	 * @param key
	 * @return
	 */
	public abstract String doGetSignStr(String key) ;
	
	
	/**
	 * 获取请求地址
	 * @return
	 */
	public abstract String doGetReqUrlPath() ;

	/**
	 * 获取数据参数名称
	 * @return
	 */
	public abstract String doGetDataParamName();
	
	/**
	 * 是否加密
	 * @return
	 */
	public abstract boolean hasEncrypt();
	
	
	/**
	 * 获取返回类型
	 * @return
	 */
	public abstract Class<T> doGetRspClass();


	
}
