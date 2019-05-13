package com.sdhoo.pdloan.payctr.busi.yibaodf.service;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.YibaodfCfgInf;

/**
 * 易宝配置服务
 * @author Fangzhiping
 *
 */
public interface YibaodfCfgService {


//    /**
//     * 根据配置ID获取配置信息
//     * @param ifcCfgInf
//     * @return
//     */
//    YibaodfCfgInf genCfgInf(DtPctIfcChnlCfg ifcCfgInf);
    
    
    YibaodfCfgInf getYibaoCfgInfByModel(DtPctIfcChnlCfg dbcfg);

    /**
     * 获取公钥 
     * @param cfg
     * @return
     */
    PublicKey getPubKey(YibaodfCfgInf cfg);

    /**
     * 获取私钥
     * @param cfg
     * @return
     */
    PrivateKey getPrivKey(YibaodfCfgInf cfg);
}
