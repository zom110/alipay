package com.sdhoo.pdloan.payctr.busi.fuioudf.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.Md5SignUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.busi.fuioudf.req.FuioudfBaseReq;
import com.sdhoo.pdloan.payctr.busi.fuioudf.req.PayforreqReq;
import com.sdhoo.pdloan.payctr.busi.fuioudf.rsp.FuioudfBaseRsp;
import com.sdhoo.pdloan.payctr.busi.fuioudf.rsp.PayforreqRsp;
import com.sdhoo.pdloan.payctr.busi.fuioudf.service.FuioudfClientService;
import com.sdhoo.pdloan.payctr.busi.fuioudk.service.impl.FuioudkClientServiceImpl;
import com.sdhoo.pdloan.payctr.busi.fuioudk.util.HttpPoster;
import com.sdhoo.pdloan.payctr.busi.fuioudk.util.XMapUtil;

/**
 * 富友代付客户端实现类
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-26 10:51:18
 *
 */
@Service
public class FuioudfClientServiceImpl implements FuioudfClientService{

	
	private static final Logger logger = LoggerFactory.getLogger(FuioudkClientServiceImpl.class); 

	/**
	 * 客户端调用日志
	 */
	private static final Logger cltLogger = LoggerFactory.getLogger("FUIOUDFCLT_ACCESS");
	
	
	
	@Override
    public <T extends FuioudfBaseRsp> T doExecuteBaseReq(FuioudfBaseReq<T> beanReq, DtPctIfcChnlCfg cfg) throws BaseServiceException {

        if (cfg == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "渠道配置信息不能为空");
        }
        if(beanReq == null ) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "请求不能为空");
        }
        
        // 网关地址 
        String chnlApiUrl = cfg.getChnlApiUrl();
        // 调用地址
        String doGetCallUrl = beanReq.doGetReqUrlPath();
        // 请求地址 
        String reqUrl = chnlApiUrl + doGetCallUrl ;
        
        // 商户编号 
        String merid = cfg.getMemberId(); 
        String merpwd = cfg.getAppKey() ; //
        String charset = "utf-8";

        String reqtype = beanReq.doGetReqtype() ; 

        String logPre = "富友代付[mct="+merid+"]接口发起调用time:"+System.currentTimeMillis()+",接口地址:"+reqUrl+"-->";

        cltLogger.debug(logPre + "入参信息:{}" ,JSONObject.toJSONString(beanReq)); 

        try {
			String xmlStr = XMapUtil.toXML(beanReq, charset );
			String toSignStr = merid + "|" + merpwd + "|" + reqtype + "|" + xmlStr ;
			logger.debug(logPre+"待签名信息"+toSignStr);
			String signRstStr = Md5SignUtils.md5EncryptTo32BitHex(toSignStr).toUpperCase(); // 签名结果 
			logger.debug(logPre+"待签结果"+signRstStr);
			Map<String,String> paramsMap = new HashMap<>() ;
			paramsMap.put("merid",merid); 
			paramsMap.put("reqtype",reqtype); 
			paramsMap.put("xml",xmlStr); 
			paramsMap.put("mac",signRstStr); 
			logger.debug(logPre+"请求的xml内容:"+xmlStr);
			logger.debug(logPre+"请求参数Map:" + paramsMap ); 
			String result = new HttpPoster(reqUrl).postStr(paramsMap); 
			String decResult ; // 解密密文
			if(result.indexOf("<?xml") >= 0 ) {
				decResult = result;
			}else if (result.indexOf("<") == 0) {
//				decResult = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" + result; 
				decResult =  result; 
			}else {
				decResult = result ; // DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(mchntKey)); 
			}
			logger.debug(logPre + "请求数据解密后内容:{}", decResult ); 

			// 数据转换为Bean
			Class<T> rspClass = beanReq.doGetRspClass();
			T parseObj = XMapUtil.parseStr2Obj(rspClass, decResult);
//			logger.debug(logPre+"响应数据JSON:"+JSONObject.toJSONString(parseObj));
			return parseObj ;
			
        } catch (Exception e2) {
            logger.warn(logPre+"调用出异常了..",e2); 
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_BUSI_UNKNOWN,"渠道接口调用异常");
        }

    }
	

}
