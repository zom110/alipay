package com.sdhoo.pdloan.payctr.busi.yibaodf.service.impl;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import org.springframework.stereotype.Service;

import com.sdhoo.common.base.util.Base64Utils;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.YibaodfCfgInf;
import com.sdhoo.pdloan.payctr.busi.yibaodf.service.YibaodfCfgService;


/**
 * 易宝配置服务服务
 * @author Fangzhiping
 *
 */
@Service
public class YibaodfCfgServiceImpl implements YibaodfCfgService {


//    /**
//     * 根据配置ID获取配置信息
//     * @param yibaoUsrpayCfgId
//     * @return
//     */
//    @Override
//    public YibaodfCfgInf genCfgInf(DtPctIfcChnlCfg ifcCfgInf) {
//        if(ifcCfgInf == null ) {
//            return null;
//        }
//        return getYibaoCfgInfByModel(ifcCfgInf);
//    }

    @Override
    public YibaodfCfgInf getYibaoCfgInfByModel(DtPctIfcChnlCfg dbcfg) {
        if(dbcfg == null ) {
            return null ;
        }
        YibaodfCfgInf cfg = new YibaodfCfgInf();
        Long cfgId = dbcfg.getIccId();
        cfg.setCfgId(cfgId); 
        cfg.setMerchantAccount(dbcfg.getMemberId());
        cfg.setGatewayUrl(dbcfg.getChnlApiUrl()); 
        cfg.setMerchantAppKey(dbcfg.getAppKey());
        cfg.setMerchantPrivateKey(dbcfg.getAppPriFileTxt()); // app私钥 
        cfg.setMerchantTermilalno(dbcfg.getTerminalNo()); // 终端号 
        cfg.setDecPubFileTxt(dbcfg.getChnlPubFileTxt()); // 渠道公钥 
        cfg.setIfcChnlName(dbcfg.getMemberName()); 
        return cfg ;
    }
    
    
    @Override
    public PublicKey getPubKey( YibaodfCfgInf cfg ) {
        if(cfg == null ) {
            return null;
        }
        return createPubKey(cfg.getDecPubFileTxt());
    }
    
    @Override
    public PrivateKey getPrivKey(YibaodfCfgInf cfg) {
        if(cfg == null) {
            return null ;
        }
        return createPrivateKey(cfg.getMerchantPrivateKey());
    }
    
    public PublicKey getPubKey(DtPctIfcChnlCfg ifcCfgInf) {
        if(ifcCfgInf == null ) {
            return null;
        }
        return createPubKey(ifcCfgInf.getChnlPubFileTxt());
    }
    
    public PrivateKey getPrivKey(DtPctIfcChnlCfg ifcCfgInf) {
        if(ifcCfgInf == null) {
            return null ;
        }
        return createPrivateKey(ifcCfgInf.getAppPriFileTxt());
    }

    
    /**
     * 创建公钥
     * @param publickey
     * @return
     */
    private static PublicKey createPubKey(String publickey) {
        PublicKey publicKey = null;
        try {
            // 自己的公钥(测试)
            byte[] decodeBuffer = Base64Utils.getDecoder().decode(publickey);
            java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(decodeBuffer);
            // RSA对称加密算法
            java.security.KeyFactory keyFactory;
            keyFactory = java.security.KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            publicKey = keyFactory.generatePublic(bobPubKeySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    /**
     * 创建私钥
     * @param priKey
     * @return
     */
    private static PrivateKey createPrivateKey(String priKey) {
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec priPKCS8;
        try {
            byte[] b64DctBytes = Base64Utils.getDecoder().decode(priKey);
            priPKCS8 = new PKCS8EncodedKeySpec(b64DctBytes);
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            privateKey = keyf.generatePrivate(priPKCS8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }
    

}
