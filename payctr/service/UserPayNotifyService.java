package com.sdhoo.pdloan.payctr.service;

/**
 * 用户支付通知处理服务.
 * @author SDPC_LIU
 *
 */
public interface UserPayNotifyService {

  /**
   * 定时推送
   */
  void toNotifyAccordTime();

  Integer getTimeByCount(Integer fail);
  
}
