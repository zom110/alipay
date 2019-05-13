package com.sdhoo.pdloan.payctr.base.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.base.util.WebOptUtils;
import com.sdhoo.common.base.util.WebOptUtils.WebRequest;
import com.sdhoo.common.base.util.WebOptUtils.WebResp;
import com.sdhoo.pdloan.payctr.base.service.WebUtilService;

/**
 * Web调用工具.
 * @author SDPC_LIU
 *
 */
@Service
public class WebUtilServiceImpl implements WebUtilService {

	private static final Logger logger = LoggerFactory.getLogger(WebUtilServiceImpl.class); // 日志. 

	/**
	 * webPost请求路径地址.
	 * 
	 */
	@Value(value="${paycenter.base.webutil.rmtposturl}")
	private String websitePostUrl = "";
	
	/**
	 * 远程调用.
	 * @param inReq
	 * @return
	 */
    @Override
	public WebResp doRmtPost(WebRequest inReq){
		if(inReq == null ){
			return null ;
		}
		WebResp resp = null ;
		
		String requestUrl = websitePostUrl ;
		String logStr = "远程("+requestUrl+")调用地址:" + inReq.getUrl()  ;
		if( logger.isDebugEnabled()){
			logger.debug(logStr); 
		}
		System.out.println(logStr);

		Map<String,String> reqParam = new HashMap<>();
		reqParam.put("webreqJsn", JSONObject.toJSONString(inReq));
		Integer inTimeOut = inReq.getReadTimeout();
		WebRequest webreq = new WebRequest();
		Integer readTimeout = webreq.getReadTimeout();
		if(readTimeout < inTimeOut ){
			webreq.setReadTimeout(inTimeOut);
		}
		Map<String,String> rqHeadMap = new HashMap<String,String>();
		webreq.setUrl(requestUrl);
		webreq.setHeadMap(rqHeadMap);
		webreq.setParamsMap(reqParam);
		try {
			WebResp postResp = WebOptUtils.doPost(webreq);
			String responseStr = postResp.getResponseStr();
			JSONObject parseObject = JSONObject.parseObject(responseStr);
			String webrespJsnStr = parseObject.getString("webrespJsn");
			resp = JSONObject.parseObject(webrespJsnStr, WebResp.class);
			return resp;
		} catch (IOException e) {
			logger.error("调用Webutil的do_post请求出现异常了");
			return resp ;
		}
	}
}
