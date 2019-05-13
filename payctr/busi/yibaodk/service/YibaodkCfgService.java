package com.sdhoo.pdloan.payctr.busi.yibaodk.service;

import java.security.PrivateKey;
import java.security.PublicKey;

import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.busi.yibaodk.dto.YibaodkCfgInf;

/**
 * 易宝配置服务
 * @author Fangzhiping
 *
 */
public interface YibaodkCfgService {


    /**
     * 根据配置ID获取配置信息
     * @param ifcCfgInf
     * @return
     */
    YibaodkCfgInf getCfgInfByCfgId(DtPctIfcChnlCfg ifcCfgInf);

    /**
     * 获取公钥 
     * @param cfg
     * @return
     */
    PublicKey getPubKey(YibaodkCfgInf cfg);

    /**
     * 获取私钥
     * @param cfg
     * @return
     */
    PrivateKey getPrivKey(YibaodkCfgInf cfg);
}
