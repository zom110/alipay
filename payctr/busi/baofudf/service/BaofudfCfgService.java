package com.sdhoo.pdloan.payctr.busi.baofudf.service;

import com.sdhoo.pdloan.payctr.busi.baofudf.dto.BaofudfCfgInf;

/**
 * 宝付代扣配置服务
 * @author SDPC_LIU
 *
 */
public interface BaofudfCfgService {


    /**
     * 根据商户ID获取配置内容.
     * @param shId
     * @return
     */
    BaofudfCfgInf getCfgInfById(Long shId);
    
    
    
}
