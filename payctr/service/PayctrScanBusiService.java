package com.sdhoo.pdloan.payctr.service;

/**
 * 支付中心扫描类业务服务.
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-20 15:55:18
 *
 */
public interface PayctrScanBusiService {

	
	/**
	 * 扫描并核对所有待核对的支付记录
	 */
	void doScanAndCheckAllForCheckUserPayRcd();


	/**
     * 扫描并发起待核对的代付记录进行主动核对.
     */
    void doScanAndCheckAllForCheckSysPayRcd();


	/**
	 * 扫描待通知数据
	 */
	void doScanAndCallToNotifyDataAndNotify();


}
