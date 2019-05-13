package com.sdhoo.pdloan.payctr.busi.baofudf.service.impl;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sdhoo.common.base.util.StreamUtils;
import com.sdhoo.pdloan.payctr.busi.baofudf.dto.BaofudfCfgInf;
import com.sdhoo.pdloan.payctr.busi.baofudf.service.BaofudfCfgService;


/**
 * 宝付代扣配置服务服务
 * @author SDPC_LIU
 *
 */
@Service
public class BaofudfCfgServiceImpl implements BaofudfCfgService {
    
    
    /**
     * 商户号
     */
    @Value(value = "${paycenter.baofudf.member_id}")
    private String memberId;

    /**
     * 终端号
     */
    @Value(value = "${paycenter.baofudf.terminal_id}")
    private String terminalId;

    /**
     * 私钥证书资源地址
     */
    @Value(value = "${paycenter.baofudf.pfx_res_path}")
    private String pfxResPath;

    /**
     * 私钥证书密码
     */
    @Value(value = "${paycenter.baofudf.pfx_pwd}")
    private String pfxPwd;
    
    /**
     * 公钥证书资源地址
     */
    @Value(value = "${paycenter.baofudf.req_base_url}")
    private String gatewayUrl;

    /**
     * 公钥证书资源地址
     */
    @Value(value = "${paycenter.baofudf.pubkcer_res_path}")
    private String pubkcerResPath;

    /**
     *  公钥证书资源地址
     */
    @Value(value="${paycenter.baofudf.pubkcer_res_path}")
    private String pubkcer_res_path ; 


    private static BaofudfCfgInf defaultCfgInf  ;

    @Override
    public BaofudfCfgInf getCfgInfById(Long cfgId) {

        if(defaultCfgInf == null ) {
            initDefaultCfgInf();
        }
        return defaultCfgInf;
    }

    private synchronized void initDefaultCfgInf() {
        if(defaultCfgInf == null ) {
            try {
                defaultCfgInf = new BaofudfCfgInf();
                InputStream cerIs = this.getClass().getResourceAsStream(pubkcer_res_path);
                byte[] pubkeyCerFileBytes = StreamUtils.readByteArray(cerIs);
                InputStream pfxResIs = this.getClass().getResourceAsStream(pfxResPath);
                byte[] pfxFileBytes = StreamUtils.readByteArray(pfxResIs);
                defaultCfgInf.setBdcId(21L); 
                defaultCfgInf.setGatewayUrl(gatewayUrl);
                defaultCfgInf.setTerminalId(terminalId);
                defaultCfgInf.setMemberId(memberId);
                defaultCfgInf.setEncPriPfxFilePwd(pfxPwd);
                defaultCfgInf.setPfxFileBytes(pfxFileBytes);
                defaultCfgInf.setPubkeyCerFileBytes(pubkeyCerFileBytes);
                defaultCfgInf.setDecPubCerFileTxt(new String(pubkeyCerFileBytes)); 
            } catch (Exception e) {
                throw new RuntimeException("宝付代扣默认配置初始化时出现异常了");
            }
        }
    }


}
