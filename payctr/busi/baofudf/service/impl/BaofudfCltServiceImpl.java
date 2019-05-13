package com.sdhoo.pdloan.payctr.busi.baofudf.service.impl;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.WebOptUtils;
import com.sdhoo.common.base.util.WebOptUtils.WebRequest;
import com.sdhoo.common.base.util.WebOptUtils.WebResp;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.base.service.WebUtilService;
import com.sdhoo.pdloan.payctr.busi.baofudf.bens.req.BaofudfBaseReq;
import com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp.BaofudfBaseRsp;
import com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp.BaofudfRspObject;
import com.sdhoo.pdloan.payctr.busi.baofudf.dto.BaofudfCfgInf;
import com.sdhoo.pdloan.payctr.busi.baofudf.service.BaofudfCltService;
import com.sdhoo.pdloan.payctr.busi.baofudf.util.RsaCodingUtil;
import com.sdhoo.pdloan.payctr.busi.baofudf.util.RsaReadUtil;

/**
 * 宝付代付客户端服务.
 * 
 * @author SDPC_LIU
 * @param <E>
 *
 */
@Service(value = "baofudfCltServiceImpl")
public class BaofudfCltServiceImpl implements BaofudfCltService {

    private static final Logger logger = LoggerFactory.getLogger(BaofudfCltServiceImpl.class);

    /**
     * 业务级日志,宝付代付调用日志
     */
    private static final Logger baofudfclt_logger = LoggerFactory.getLogger("BAOFUDFCLT_ACCESS");

    /**
     * 版本号
     */
    @Value(value = "${paycenter.baofudf.version}")
    private String version;

    /**
     * 数据类型
     */
    @Value(value = "${paycenter.baofudf.data_type}")
    private String dataType;

    /**
     * 是否远程调用
     */
    @Value(value = "${paycenter.baofudf.isrmt}")
    private boolean isrmt;
    

    @Autowired
    private WebUtilService webUtilService;
    
    @Autowired
    private PctIdGenService pctIdGenService ;

    @Override
    public String generateTransSerialNo() {
        // 序号
        PctIdData nextRcdId = pctIdGenService.genAndCachePayctIdData();
        long generateIndex = nextRcdId.getIdVal() ;
        String rtVal = ""+generateIndex ;
        if(rtVal.length() > 20 ){
            rtVal = rtVal.substring(0, 20);
        }
        return rtVal ;
    }


	@Override
	public <T extends BaofudfBaseRsp> T doExecuteBaseReq(BaofudfBaseReq<T> baseReq, BaofudfCfgInf dfCfg) throws BaseServiceException {

		String createSerial = generateTransSerialNo();
		if (dfCfg == null) {
			throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "渠道配置信息不能为空");
		}
		String memberId = dfCfg.getMemberId();
		String logPre = "宝付代付(mer=" + memberId + ")发起(seq=" + createSerial + ")调用(txn_sub_type=" + "xxxx" + ")-->";

		Map<String, Object> reqDataMap = new HashMap<>(16);
		reqDataMap.put("trans_content", baseReq);
		String reqJsonStr = JSONObject.toJSONString(reqDataMap);
		// 交易码
		String tranCode = baseReq.doGetTranCode();
		Class<T> rspClass = baseReq.doGetRspClass();
		String encByPrikey;
		try {
			encByPrikey = encriptBasereqDataContent(reqJsonStr, dfCfg);
			if (logger.isDebugEnabled()) {
				logger.debug(logPre + "加密后的请求数据密文=" + encByPrikey);
			}
		} catch (Exception e1) {
			logger.error(logPre + "加密数据异常", e1);
			throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_KNOWN, "代付数据加密异常");
		} // 数据加密
		String dataContent = encByPrikey;
		Map<String, String> headMap = new HashMap<>(16);
		Map<String, String> paramsMap = new HashMap<>(16);
		paramsMap.put("version", version);
		paramsMap.put("member_id", memberId);
		paramsMap.put("terminal_id", dfCfg.getTerminalId());
		paramsMap.put("data_type", dataType);
		paramsMap.put("data_content", dataContent);
		baofudfclt_logger.debug(logPre + "data_content的JSON明文:{}", reqJsonStr);
		WebRequest webreq = new WebRequest();
		String reqBaseUrl = dfCfg.getGatewayUrl();
		String requestUrl = reqBaseUrl + "/" + tranCode + ".do";
		webreq.setUrl(requestUrl);
		webreq.setHeadMap(headMap);
		webreq.setParamsMap(paramsMap);
		WebResp resp;
		try {
			// 是否远程调用
			if (isrmt) {
				resp = webUtilService.doRmtPost(webreq);
			} else {
				resp = WebOptUtils.doPost(webreq);
			}
		} catch (IOException e) {
			logger.error(logPre + "网络调用异常", e);
			throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "网络调用异常");
		}
		String responseStr = resp.getResponseStr();
		logger.debug(logPre + "Http响应数据密文:{}", responseStr);

		// 是否16进制内容.
		boolean checkIsHexStr = checkIsHexStr(responseStr);
		JSONObject rspCttJsonObj = null;
		// 明文内容
		String decContent;
		if (!checkIsHexStr) {
			// 不是16进制,则认为数据格式为json内容.则直接返回Json内容.
			baofudfclt_logger.debug(logPre + "响应Http的JSON明文(将直接作为错误对象返回):{}", responseStr);
			decContent = responseStr;
		} else {
			try {
				// 数据解密
				decContent = decryptRespContent(responseStr, dfCfg);
			} catch (Exception e) {
				logger.error(logPre + "密文数据解密异常,对应的请求明文content_data的JSON为:" + reqJsonStr, e);
				throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN, "调用数据异常");
			}
			baofudfclt_logger.debug(logPre + "响应解析后的JSON明文:{}", decContent);
		}
		rspCttJsonObj = JSONObject.parseObject(decContent);

		String transContentStr = rspCttJsonObj.get("trans_content").toString();
		BaofudfRspObject obj = JSONObject.parseObject(transContentStr, BaofudfRspObject.class);
		List<Map<String, Object>> transReqDatas = obj.getTrans_reqDatas();
		if (transReqDatas.size() > 0) {
			Map<String, Object> transReqData0Map = transReqDatas.get(0);
			Object object = transReqData0Map.get("trans_reqData");
			if (object != null) {
				if (object instanceof List) {

				} else {
					List<Object> objList = new ArrayList<>();
					objList.add(object);
					transReqData0Map.put("trans_reqData", objList);
				}
			}
		}
		String fnlTransCttStr = JSONObject.toJSONString(obj);
		T rspData = JSONObject.parseObject(fnlTransCttStr, rspClass);
		return rspData;
	}


    /**
     * 核对是否Hex字符串
     * 
     * @param hexStr
     */
    private static boolean checkIsHexStr(String hexStr) {
        boolean rtVal = false;
        String validStr = hexStr;
        int checkMaxLength = 100;
        if (hexStr.length() > checkMaxLength) {
            validStr = hexStr.substring(0, checkMaxLength);
        }
        validStr = validStr.toUpperCase();
        String hexRegex = "([0-9a-hA-H])*";
        if (validStr.matches(hexRegex)) {
            rtVal = true;
        }
        return rtVal;
    }

    /**
     * 解密数据包.
     * 
     * @param responseStr
     * @return
     * @throws Exception
     */
    @Override
    public String decryptRespContent(String responseStr, BaofudfCfgInf dfCfg) throws Exception {
        try {
            String pubKeyText =  dfCfg.getDecPubCerFileTxt();
            PublicKey publicKey = RsaReadUtil.getPublicKeyByText(pubKeyText);
            String decB64Str = RsaCodingUtil.decryptByPublicKey(responseStr, publicKey);
            byte[] decdeB64Bytes = Base64.getDecoder().decode(decB64Str);
            String decContent = new String(decdeB64Bytes);
            return decContent;
        } catch (Exception e) {
            logger.error("数据解密出现异常", e);
            throw e;
        } finally {

        }
    }

    /**
     * 加密数据.
     * 
     * @param jsonStr
     * @return
     * @throws Exception
     */
    @Override
    public String encriptBasereqDataContent(String jsonStr, BaofudfCfgInf dfCfg) throws Exception {
        String encodeToB64Str = Base64.getEncoder().encodeToString(jsonStr.getBytes());
        try {
            byte[] pfxBytes = dfCfg.getPfxFileBytes();
            String pfxPwd = dfCfg.getEncPriPfxFilePwd();
            String encByPrikey = RsaCodingUtil.encryptByPriPfxStream(encodeToB64Str, pfxBytes, pfxPwd);
            return encByPrikey;
        } catch (Exception e) {
            logger.error("数据加密出现异常", e);
            throw e;
        } finally {
           
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isIsrmt() {
        return isrmt;
    }

    public void setIsrmt(boolean isrmt) {
        this.isrmt = isrmt;
    }


    public WebUtilService getWebUtilService() {
        return webUtilService;
    }

    public void setWebUtilService(WebUtilService webUtilService) {
        this.webUtilService = webUtilService;
    }
    

}
