package com.sdhoo.pdloan.payctr.busi.yibaodf.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.BeanProcUtils;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.pdloan.payctr.base.dto.PctIdData;
import com.sdhoo.pdloan.payctr.base.service.PctIdGenService;
import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YibaodfBaseReq;
import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YibaodfBaseRsp;
import com.sdhoo.pdloan.payctr.busi.yibaodf.dto.YibaodfCfgInf;
import com.sdhoo.pdloan.payctr.busi.yibaodf.service.YibaodfClientService;
import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.error.YopError;
import com.yeepay.g3.sdk.yop.error.YopSubError;

/**
 * 易宝支付服务.实现.
 * @author Fangzhiping
 *
 */
@Service
public class YibaodfClientServiceImpl implements YibaodfClientService {

    private static Logger logger = LoggerFactory.getLogger(YibaodfClientServiceImpl.class);

    /**
     * 客户端日志.
     */
    private static Logger yibaodkclt_logger = LoggerFactory.getLogger("YIBAODFCLT_ACCESS");
    

    @Autowired
    private PctIdGenService pctIdGenService;
    

    /**
     * 创建唯一序列号
     * @return 
     */
    @Override
    public String generateTransSerialNo() {
        PctIdData nextRcdId = pctIdGenService.genAndCachePayctIdData();
        long generateIndex = nextRcdId.getIdVal();
        String rtVal = "" + generateIndex;
        if (rtVal.length() > 20) {
            rtVal = rtVal.substring(0, 20);
        }
        return rtVal;
    }
    
    @Override
    public <T extends YibaodfBaseRsp> T doExecuteBaseReq(YibaodfBaseReq<T> baseReq, YibaodfCfgInf cfg) throws Exception {
        if (cfg == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "渠道配置信息不能为空");
        }
        if(baseReq == null ) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "请求不能为空");
        }

        // 调用地址
        String doGetCallUrl = baseReq.doGetCallUrl();
        
        // 商户编号
        String merchantno = cfg.getMerchantAccount();
        String merchantPrivateKey = cfg.getMerchantPrivateKey();
        String merchantAppKey = cfg.getMerchantAppKey();
        String logPre = "易宝代付["+merchantno+"]接口发起调用time:"+System.currentTimeMillis()+",接口地址:"+doGetCallUrl+"-->";
        
        Map<String, Object> reqParamsMap = BeanProcUtils.transBean2Map(baseReq);
        // 补充固定参数.
        reqParamsMap.put("merchantno", merchantno);

        if(yibaodkclt_logger.isDebugEnabled()) {
            yibaodkclt_logger.debug(logPre + "入参信息:" + JSONObject.toJSONString(reqParamsMap)); 
        }
        String rstJson = null ;
        String baseUrl = cfg.getGatewayUrl() ;
        try {
			YopRequest yopRequest = new YopRequest(merchantAppKey, merchantPrivateKey,baseUrl );

            Set<String> reqParamKeyset = reqParamsMap.keySet();
            for (String tmpkey : reqParamKeyset) {
                Object paramVal = reqParamsMap.get(tmpkey);
                yopRequest.addParam(tmpkey, paramVal);
            }
            YopResponse yopResp = YopClient3.postRsa(doGetCallUrl, yopRequest);
            String rspJson = null ;
            if(yibaodkclt_logger.isDebugEnabled()) {
                rspJson = JSONObject.toJSONString(yopResp) ;
                yibaodkclt_logger.debug(logPre + "响应信息:" + rspJson ); 
            }

            rstJson = yopResp.getStringResult();
            if(StringUtils.isEmpty(rstJson)) {
                yibaodkclt_logger.warn(logPre+"调用发起出错了,错误信息:" + rspJson );
                YopError error = yopResp.getError();
                String errorCode = error.getCode();
                String errorMsg = error.getMessage();
                List<YopSubError> subErrors = error.getSubErrors();
                if(subErrors != null && subErrors.size() > 0 ) {
                    YopSubError yopSubError = subErrors.get(0);
                    String subMessage = yopSubError.getMessage();
                    if(StringUtils.isNotEmpty(subMessage)) {
                        errorMsg = subMessage;
                    }
                }
                YibaodfBaseRsp rsp = new YibaodfBaseRsp();
                rsp.setErrorCode(errorCode);
                rsp.setErrorMsg(errorMsg);
                rstJson = JSONObject.toJSONString(rsp);
            }
        } catch (IOException e2) {
            logger.warn(logPre+"调用出异常了..",e2); 
            throw e2 ;
        }
        // 数据转换为Bean
        T respContext = JSONObject.parseObject(rstJson, baseReq.doGetRespClass());
        return respContext;
    }

    


}


