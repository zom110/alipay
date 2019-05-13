package com.sdhoo.pdloan.payctr.base.service;

import com.sdhoo.pdloan.payctr.base.dto.PctIdData;

/**
 * 支付中心ID生成服务.
 * @author SDPC_LIU
 *
 */
public interface PctIdGenService {

    /**
     * 创建记录ID
     * @return
     */
    PctIdData genAndCachePayctIdData();

//    /**
//     * 重新创建一个订单编号
//     * @return
//     */
//    String genOrderCode();


}
