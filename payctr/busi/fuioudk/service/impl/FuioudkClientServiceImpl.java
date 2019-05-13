package com.sdhoo.pdloan.payctr.busi.fuioudk.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.Md5SignUtils;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.busi.fuioudk.req.FuioudkBaseReq;
import com.sdhoo.pdloan.payctr.busi.fuioudk.rsp.FuioudkBaseRsp;
import com.sdhoo.pdloan.payctr.busi.fuioudk.service.FuioudkClientService;
import com.sdhoo.pdloan.payctr.busi.fuioudk.util.HttpPoster;
import com.sdhoo.pdloan.payctr.busi.fuioudk.util.RSAUtils;
import com.sdhoo.pdloan.payctr.busi.fuioudk.util.XMapUtil;

/**
 *  TODO 富友客户端实现类.未完成
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-22 21:05:07
 *
 */
@Service
public class FuioudkClientServiceImpl implements FuioudkClientService {

	
	private static final Logger logger = LoggerFactory.getLogger(FuioudkClientServiceImpl.class); 
	
	/**
	 * 客户端调用日志
	 */
	private static final Logger cltLogger = LoggerFactory.getLogger("FUIOUDKCLT_ACCESS");
	

	@Override
	public String generateTransSerialNo() {
		String rtStr = ""+System.currentTimeMillis();
		return rtStr;
	}
	
	@Override
    public <T extends FuioudkBaseRsp> T doExecuteBaseReq(FuioudkBaseReq<T> beanReq, DtPctIfcChnlCfg cfg) throws BaseServiceException {

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
        String memberId = cfg.getMemberId(); 

        // 商户编号
        String version = beanReq.getVersion();
        if(version == null ) {
        	version = "3.0";
        }
        String doGetDataParamName = beanReq.doGetDataParamName();
        if(doGetDataParamName == null ) {
        	doGetDataParamName = "APIFMS";
        }
        
        boolean hasEncrypt = beanReq.hasEncrypt();
        
		String charset = "utf-8";

		String mchntcd = memberId;
		String mchntKey = cfg.getAppKey();
		String appPriFileTxt = cfg.getAppPriFileTxt(); // 私钥 

        String logPre = "富友代扣[mct="+mchntcd+"]接口发起调用time:"+System.currentTimeMillis()+",接口地址:"+reqUrl+"-->";

        cltLogger.debug(logPre + "入参信息:{}" ,JSONObject.toJSONString(beanReq)); 

        try {
        	beanReq.setVersion(version);
        	beanReq.setMchntcd(memberId);
        	String signStr = getSign(beanReq.doGetSignStr(mchntKey), "MD5", appPriFileTxt);
			beanReq.setSign(signStr);
			String xmlStr = XMapUtil.toXML(beanReq, charset );
			Map<String,String> paramsMap = new HashMap<>() ;
			String paramData ; 
			if(hasEncrypt) {
				paramData = DESCoderFUIOU.desEncrypt(xmlStr, DESCoderFUIOU.getKeyLength8(mchntKey));
			}else {
				paramData = xmlStr;
			}
			paramsMap.put(doGetDataParamName,paramData); 
			paramsMap.put("MCHNTCD", mchntcd); 
//			logger.debug("请求地址:" + reqUrl); 
			logger.debug(logPre+"请求的xml内容:{}",xmlStr);
//			logger.debug(logPre+"请求加密的paramData:{}",paramData);
//			logger.debug(logPre + "请求参数Map:" + paramsMap ); 
			String result = new HttpPoster(reqUrl).postStr(paramsMap); 
			String decResult ; // 解密密文
			if(result.indexOf("<?xml") >= 0 ) {
				decResult = result;
			}else if (result.indexOf("<") == 0) {
//				decResult = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>" + result; 
				decResult =  result; 
			}else {
				decResult = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(mchntKey)); 
			}
			logger.debug(logPre + "请求数据解密后内容{}", decResult ); 

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
	
	
	/**
	 * 获取签名
	 * @param signStr  签名串
	 * @param signtp   签名类型
	 * @param key      密钥
	 * @return
	 * @throws Exception
	 */
	private static String getSign(String signStr,String signtp,String key) throws  Exception{
		String sign = "";
		if ("md5".equalsIgnoreCase(signtp)) {
			sign =  Md5SignUtils.md5EncryptTo32BitHex(signStr).toUpperCase();
		} else {
			sign =	RSAUtils.sign(signStr.getBytes("utf-8"), key);
		}
		return sign;
	}

	
//	
//	public static void main(String[] args) throws BaseServiceException {
//		
////		test_main1();
//		test_main2();
//	}
//
//	
//	public static void test_main2() throws BaseServiceException {
//		
//		String mchntcd = "0003910F1935362";
//		String mchntKey = "6sf9alvs1zxvrrpk7ida1hlm5abveq52";
//		String appPriFileTxt = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJzGUZO3faCtVhwVfN9eHEtOOga9Q/1FfAd8nM2xsIZMWQqdM1faUx3b2pMu6KuhMaafIpHcdvNTUbrg1EDyw2NYMSveoLVVk4wNiVw8pSe5Q1J8cb7PeIgYzZPY+Lylwz9ViwYJ2NQsp/O98UehF0d9juRPriJKuQ1XeeOED0R9AgMBAAECgYEAlccSoOGo8B0qK2KMDorG1VtDLLUdg5ciy7RNymZ8k5HVb8KIDuLSjbBBvmQOfgpeeSWHzXqnWgi8CKJLplFIQNNZi5Gj6GlDbfWcUPpNy9bStODNQbgKStDFym8fbV9d9XH99oRalmIGGXpAfvr0I442Qg6ytJwpShDuK6IpNbECQQD+uN/1Zn1k4Qt69KjxY7nqLRm+NyL1mNohs9lxe6w73Sq7kznT/B7yTnVonDe/amK/GAAtzqoaF4U0qrGv/9LDAkEAnY+ny4eI/UIh3+mzk+AcGS30Gr0b4vu3Vx3Nv4SqwgEUKPnOp3USymZVfoWMdR3UKsYtdn0uPRoSt3CYx6CXvwJAc50R+d5k9861WT+HFtk+3y1NS+rWsUaJa1aoUTvi9uDxrmWqj68VlMFLxQlyvCzbWQTZv/DXgDRAv6DiOZIR5QJAMtLn+Usa8Tan3qZv+r9L0QN1vjpKCInRthQzDhgpTLM0YZNTCclF6mvgIBPMHuskSTfr6lCu3Z7PcsSrQ5mV2QJARUxIcRH0VJeHjHSc1Jct5CaokudEgg4uBvB+AaHrdn8AKxE46WcmgjccPKfYurH8MAMceeF95FrJ8vw7vtBJig==";
//		String gatewayUrl = "https://mpay.fuiou.com";
//		
//		gatewayUrl = "http://www-1.fuiou.com:18670/mobile_pay";
//		mchntcd = "0002900F0096235";
//		mchntKey = "5old71wihg2tqjug9kkpxnhx9hiujoqj";
//		appPriFileTxt = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCd8u/u4oV8VRV4cdwi8b3B+BDsDR2Jp3FFJh8ON3HN1fPCcAuMwgJWmwmYzSA186acg1/Ully6sSjcR4FdPub3+tPlF3H9ulVjeXTsJ3N2WQCY3Zc0AmuF4EIU5dx/DsWTCe0V5uZsueunlgXdz0bOisTjfHPiQQGRxKZ4cS8l0qSbLII5ardX3yyEFk9FsBxK1GEjqdUpc6DOyD4NpKSXY75HEAZELRMZurF+sIbdoxbKgxwL8/ckzm7RyQrc55g609qcqLGUsYohlj2MtuMV7cQ29weHNDBXMsbDkMWAP9KlxeunXHeYhpbC1KN4tS9st3W7srTJU7lCe3zXnYyjAgMBAAECggEAVt+d1BhHLyFlE8AcQiQQKLWWKR71lhbAmDXlct5P+9Sno9QYpiy3OIdBegeopcQDaaZgeP7SteRnAvFZV74XL2GIVkLU8acg+/t7sb/pI7jEOU/3sCV8mj/EWNYstr2bMcqN2jQLnNWNpblANMWivktZC34SBumWQWSV4zztn7Eh+AsgBnVBfMQiDlORIuAiKgA8Y25AIlzjaAHTxR0/CB8/QvYM1aGOq5WmAvdDO8+ZahUjOnUb8Ah3ObsYI7ek9JXF8agYGISJ819jix8V740TRZghqpkVb9cZm68glhIpOqncKsc847vWHiazf8Mtto9iJSh7HHIs519gttX2MQKBgQDqx2UEA7FIn2DbYLXmbxU81LKaaM/39fnYMeM31W0eBUCLkn0q+Q6fqnMjJk+Rqt0y6P2eVG5LzvG/0VvVHu3qDxPlbDpopn8xnPZfiV1/45P2VyVFSc1bvx+wj/j0iX5G/mXlPB0G/F0cjn+2WAmg31zLVYmy0Ttlo56o/Ze1bwKBgQCsOcG5xT9VrcIB+cLnhXK16eDKNlYyBgBP4tdGvfSJyROtabHWT2CcgTeNCLo6nsZdSRCyJaE1SpwLz0c6qkaa1mgWbkZqcp3hqLUfzX8/f1DDulMpwQLj7Ylh1Si9DjOflykXjwg3gLNM76Y0V/KUt/VmV+/9mhVVjjajp90KDQKBgQDQCyTua787VU/k1kwaCDFPmEM0JNC1L5jYBU0NeuiYg5rwkzyrScu1nLsuWJKFyFz+8gtbtAGQ167NhV3U+vVkRTwiPsd7WJ65fhesDPN3kljZH9v9DCsXs8MHGG1JdFUb3+63NY+bUyHHXla5ZEYT8houbHv6owH5TQYZRa6brQKBgET9mgId5sIeqeH6uD3gclQ3DSpT2/GL/+IAf7ZB78ymNPC5r1inUY9AIA2qGlfTNKk31K1Yb5yWd64vaVwxy7jI/MdHg4OuyFoWKABGZJv+F09gu2G1hL/jbq43ImuhXcKnXfmm/Wamcqz2L0E2neE/U9mcjnGroLI2FuM2IlCdAoGAYyD4s+gWGuJM/kZrU6I1y65PlvsOlA6wauyzbW3L1maic2aRyDYUJYFL9P/gx6D99F09dif93Z1/hrPo8LQxu2PGabwyuOpbNqde3AmywMh3vZdrXd04ojmveNQfzA4PxeuOIdH886NahxxF8BwY+QZXGMSLtAk6YbjtCvkexCI=";
//		
//		
//		
//		DtPctIfcChnlCfg cfg = new DtPctIfcChnlCfg(); 
//		cfg.setIccId(1701L); 
//		cfg.setIfcChnlId(UserPayIfcChnlEnum.FUYOU_BINDDK.getCode()); 
//		cfg.setChnlApiUrl(gatewayUrl);
//		cfg.setMemberId(mchntcd);
//		cfg.setAppKey(mchntKey);
//		cfg.setAppPriFileTxt(appPriFileTxt);
//		
//		
//		SimpleDateFormat tradeDateSdfmt = TimeCalcUtils.getThreadLocalDateFormat("yyyyMMdd");
//		String ssn; // 交易流水号 
//		String userid; // 开户用户ID 
//		String account;
//		String cardNo;
//		String idType = "0";
//		String idCard;
//		String mobileNo;
//		String tradeDate ;
//		Date curTime = new Date();
//		{
//			ssn = "18112220270001"; // 交易号 
//			userid = "1001";
//			account = "刘剑斌";
//			cardNo = "6222021402011427881";
//			idCard = "350321198710070711";
//			mobileNo = "15980261863";
//			tradeDate = tradeDateSdfmt.format(curTime);
//		}
//		NewpropayBindMsgReq beanReq = new NewpropayBindMsgReq(); 
//		beanReq.setUserId(userid);
//		beanReq.setAccount(account);
//		beanReq.setCardNo(cardNo);
//		beanReq.setIdType(idType);
//		beanReq.setIdCard(idCard);
//		beanReq.setMobileNo(mobileNo);
//		beanReq.setMchntSsn(ssn);
//		beanReq.setTradeDate(tradeDate); 
//		
//		FuiouDkClientService clientService = new FuiouDkClientServiceImpl();
//		NewpropayBindMsgRsp rspData = clientService.doExecuteBaseReq(beanReq, cfg);
//		
//		System.err.println(JSONObject.toJSONString(rspData)); 
//
//	}
//
//
//	private static void test_main1() {
//		String gatewayUrl = "https://mpay.fuiou.com";
//		String version = "3.0";
//		String charset = "utf-8";
//		
//		String mchntcd = "0003910F1935362";
//		String mchntKey = "6sf9alvs1zxvrrpk7ida1hlm5abveq52";
//		String appPriFileTxt = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJzGUZO3faCtVhwVfN9eHEtOOga9Q/1FfAd8nM2xsIZMWQqdM1faUx3b2pMu6KuhMaafIpHcdvNTUbrg1EDyw2NYMSveoLVVk4wNiVw8pSe5Q1J8cb7PeIgYzZPY+Lylwz9ViwYJ2NQsp/O98UehF0d9juRPriJKuQ1XeeOED0R9AgMBAAECgYEAlccSoOGo8B0qK2KMDorG1VtDLLUdg5ciy7RNymZ8k5HVb8KIDuLSjbBBvmQOfgpeeSWHzXqnWgi8CKJLplFIQNNZi5Gj6GlDbfWcUPpNy9bStODNQbgKStDFym8fbV9d9XH99oRalmIGGXpAfvr0I442Qg6ytJwpShDuK6IpNbECQQD+uN/1Zn1k4Qt69KjxY7nqLRm+NyL1mNohs9lxe6w73Sq7kznT/B7yTnVonDe/amK/GAAtzqoaF4U0qrGv/9LDAkEAnY+ny4eI/UIh3+mzk+AcGS30Gr0b4vu3Vx3Nv4SqwgEUKPnOp3USymZVfoWMdR3UKsYtdn0uPRoSt3CYx6CXvwJAc50R+d5k9861WT+HFtk+3y1NS+rWsUaJa1aoUTvi9uDxrmWqj68VlMFLxQlyvCzbWQTZv/DXgDRAv6DiOZIR5QJAMtLn+Usa8Tan3qZv+r9L0QN1vjpKCInRthQzDhgpTLM0YZNTCclF6mvgIBPMHuskSTfr6lCu3Z7PcsSrQ5mV2QJARUxIcRH0VJeHjHSc1Jct5CaokudEgg4uBvB+AaHrdn8AKxE46WcmgjccPKfYurH8MAMceeF95FrJ8vw7vtBJig==";
//		
//		gatewayUrl = "http://www-1.fuiou.com:18670/mobile_pay";
//		mchntcd = "0002900F0096235";
//		mchntKey = "5old71wihg2tqjug9kkpxnhx9hiujoqj";
//		appPriFileTxt = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCd8u/u4oV8VRV4cdwi8b3B+BDsDR2Jp3FFJh8ON3HN1fPCcAuMwgJWmwmYzSA186acg1/Ully6sSjcR4FdPub3+tPlF3H9ulVjeXTsJ3N2WQCY3Zc0AmuF4EIU5dx/DsWTCe0V5uZsueunlgXdz0bOisTjfHPiQQGRxKZ4cS8l0qSbLII5ardX3yyEFk9FsBxK1GEjqdUpc6DOyD4NpKSXY75HEAZELRMZurF+sIbdoxbKgxwL8/ckzm7RyQrc55g609qcqLGUsYohlj2MtuMV7cQ29weHNDBXMsbDkMWAP9KlxeunXHeYhpbC1KN4tS9st3W7srTJU7lCe3zXnYyjAgMBAAECggEAVt+d1BhHLyFlE8AcQiQQKLWWKR71lhbAmDXlct5P+9Sno9QYpiy3OIdBegeopcQDaaZgeP7SteRnAvFZV74XL2GIVkLU8acg+/t7sb/pI7jEOU/3sCV8mj/EWNYstr2bMcqN2jQLnNWNpblANMWivktZC34SBumWQWSV4zztn7Eh+AsgBnVBfMQiDlORIuAiKgA8Y25AIlzjaAHTxR0/CB8/QvYM1aGOq5WmAvdDO8+ZahUjOnUb8Ah3ObsYI7ek9JXF8agYGISJ819jix8V740TRZghqpkVb9cZm68glhIpOqncKsc847vWHiazf8Mtto9iJSh7HHIs519gttX2MQKBgQDqx2UEA7FIn2DbYLXmbxU81LKaaM/39fnYMeM31W0eBUCLkn0q+Q6fqnMjJk+Rqt0y6P2eVG5LzvG/0VvVHu3qDxPlbDpopn8xnPZfiV1/45P2VyVFSc1bvx+wj/j0iX5G/mXlPB0G/F0cjn+2WAmg31zLVYmy0Ttlo56o/Ze1bwKBgQCsOcG5xT9VrcIB+cLnhXK16eDKNlYyBgBP4tdGvfSJyROtabHWT2CcgTeNCLo6nsZdSRCyJaE1SpwLz0c6qkaa1mgWbkZqcp3hqLUfzX8/f1DDulMpwQLj7Ylh1Si9DjOflykXjwg3gLNM76Y0V/KUt/VmV+/9mhVVjjajp90KDQKBgQDQCyTua787VU/k1kwaCDFPmEM0JNC1L5jYBU0NeuiYg5rwkzyrScu1nLsuWJKFyFz+8gtbtAGQ167NhV3U+vVkRTwiPsd7WJ65fhesDPN3kljZH9v9DCsXs8MHGG1JdFUb3+63NY+bUyHHXla5ZEYT8houbHv6owH5TQYZRa6brQKBgET9mgId5sIeqeH6uD3gclQ3DSpT2/GL/+IAf7ZB78ymNPC5r1inUY9AIA2qGlfTNKk31K1Yb5yWd64vaVwxy7jI/MdHg4OuyFoWKABGZJv+F09gu2G1hL/jbq43ImuhXcKnXfmm/Wamcqz2L0E2neE/U9mcjnGroLI2FuM2IlCdAoGAYyD4s+gWGuJM/kZrU6I1y65PlvsOlA6wauyzbW3L1maic2aRyDYUJYFL9P/gx6D99F09dif93Z1/hrPo8LQxu2PGabwyuOpbNqde3AmywMh3vZdrXd04ojmveNQfzA4PxeuOIdH886NahxxF8BwY+QZXGMSLtAk6YbjtCvkexCI=";
//		
//		SimpleDateFormat tradeDateSdfmt = TimeCalcUtils.getThreadLocalDateFormat("yyyyMMdd");
//		String ssn; // 交易流水号 
//		String userid; // 开户用户ID 
//		String account;
//		String cardNo;
//		String idType = "0";
//		String idCard;
//		String mobileNo;
//		String tradeDate ;
//		Date curTime = new Date();
//		{
//			ssn = "18112220270001"; // 交易号 
//			userid = "1001";
//			account = "刘剑斌";
//			cardNo = "6222021402011427881";
//			idCard = "350321198710070711";
//			mobileNo = "15980261863";
//			tradeDate = tradeDateSdfmt.format(curTime);
//		}
//		NewpropayBindMsgReq beanReq = new NewpropayBindMsgReq(); 
//		beanReq.setVersion(version);
//		beanReq.setMchntCd(mchntcd);
//		beanReq.setUserId(userid);
//		beanReq.setAccount(account);
//		beanReq.setCardNo(cardNo);
//		beanReq.setIdType(idType);
//		beanReq.setIdCard(idCard);
//		beanReq.setMobileNo(mobileNo);
//		beanReq.setMchntSsn(ssn);
//		beanReq.setTradeDate(tradeDate); 
//		
//		String reqUrlPath = beanReq.doGetReqUrlPath();
//		String xmlStr;
//		try {
//			String signStr = getSign(beanReq.doGetSignStr(mchntKey), "MD5", appPriFileTxt);
//			beanReq.setSign(signStr);
//			xmlStr = XMapUtil.toXML(beanReq, charset );
//			String reqUrl = gatewayUrl + reqUrlPath ;
//			Map<String,String> paramsMap = new HashMap<>() ;
//			String apiFms = DESCoderFUIOU.desEncrypt(xmlStr, DESCoderFUIOU.getKeyLength8(mchntKey));
//			paramsMap.put("APIFMS",apiFms); 
//			paramsMap.put("MCHNTCD", mchntcd); 
//			System.err.println("请求地址:" + reqUrl); 
//			System.err.println("请求的xml内容:"+xmlStr);
//			System.err.println("请求参数Map:" + paramsMap ); 
//			String result = new HttpPoster(reqUrl).postStr(paramsMap); 
//			String decResult ; // 解密密文
//			if(result.indexOf("<?xml") >= 0) {
//				decResult = result;
//			}else {
//				decResult = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(mchntKey)); 
//			}
//			System.err.println("请求结果:" + result ); 
//			System.err.println("请求结果解密内容:" + decResult ); 
//			
//			NewpropayBindMsgRsp parseObj = XMapUtil.parseStr2Obj(NewpropayBindMsgRsp.class, decResult);
//
//			System.err.println(JSONObject.toJSONString(parseObj));
//	
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
