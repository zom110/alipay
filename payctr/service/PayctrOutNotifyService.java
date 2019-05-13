package com.sdhoo.pdloan.payctr.service;

import com.sdhoo.pdloan.bcrud.model.DtPctOutNotifyRcd;

/**
 * 支付中心对外通知服务
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-20 12:41:45
 *
 */
public interface PayctrOutNotifyService {
	
	
	/**
	 * 创建并发起通知
	 * @param ntfRcd
	 * @return
	 */
	public DtPctOutNotifyRcd createAndStartNotifyPlan(DtPctOutNotifyRcd ntfRcd);

	/**
	 * 异步发起通知
	 * @param ntfRcd
	 * @return 
	 */
	Runnable asynvCheckAndStartNotify(DtPctOutNotifyRcd ntfRcd); 
	
	
	

}
