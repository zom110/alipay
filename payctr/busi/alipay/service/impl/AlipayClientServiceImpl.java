package com.sdhoo.pdloan.payctr.busi.alipay.service.impl;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.payctr.busi.alipay.service.AlipayClientService;

/**
 * 支付宝客户端服务.
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-17 14:23:43
 *
 */
@Service
public class AlipayClientServiceImpl implements AlipayClientService {

//	@Value(value="${paycenter.alipay.gatewayurl}")
//	private String gatewayUrl ;

//	private Map<Long,AlipayClient> alipayClientMap = new HashMap<>(4);

	@Override
	public AlipayClient getAlipayClientByCfg(DtPctIfcChnlCfg  cfgInf) {

		String charset = "utf-8";
//		Long iccId = cfgInf.getIccId();
		String appId = cfgInf.getAppKey();
		String appPrivateKey = cfgInf.getAppPriFileTxt();
		String alipayPublicKey = cfgInf.getChnlPubFileTxt(); 
		String gatewayUrl = cfgInf.getChnlApiUrl();

		String format = "json";
		String signType = "RSA2";

		AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, appPrivateKey, format, charset, alipayPublicKey, signType);
		return alipayClient ;
	}

	
	public static void main(String[] args) {
		
		
		String gatewayUrl = "https://openapi.alipay.com/gateway.do"; // 调用地址 
		
		String appId ; // appId
		
		String appPrivateKey ; // 应用私钥 
		
		String charset = "UTF-8";
		
		String alipayPublicKey ; // 支付宝公钥 

		{
			appId = "2018110261963939";
			appPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDTi72KvhBRNGjGVyFDJwltrSCVI8XxWVNy5vKkOwOndg481yz8GaaJe2Jvz5GPOaNqQvOzHWoWbUMSY49TxTSIOex/p0ByFqZtbIigHv7gEUxzE7EtNWkpj/CT5UcFhCfRqEr2HFYD9MZUso4kTgX/6EdxkU3/pVmFuZ2TkJnB/7+1K6KIYAuTCbFMs8PtiF99PWtm/ZrPS/+Xv3UNfzOlYnpJkne2Oo+DX2WIEz5k30m+vYVS/Uuih/c1VetnQdLbBrp0UazpU+v3w8u4xfHxgUHTjgnQ7tpgdMV6L6WjvF6/l3kfRZFGLqjIyD2Ox6KJ1QcZNjs5Z5fNjY1nDN0PAgMBAAECggEAKYygxcde3RQ2H/6tAcuBBpGifKhyEF1DDqZdsfQFW7bMRRbeNwWyt/4L73oNVNw1RIGx69QzKgR7z/jOBd4N21PFJ61p8v6P4Z4Xl5t+4/OB2HhdrEt5RDiJQAMgV+0FBSDQ2FXobw58hdYsJOVOOF0e6ydm8nc/hgxW7szP5lJBfFKndttHOGj/tiOiqjhESDC9zW7sVVaMjqMx4AMO7gkTCWAT7FPy0S/+2bGwJXtVRZCSbdJMkFsRJJKUxnGH01MDJRdrAHcEaz/UyonhCKP3ovtgUKOpOrGYM32yaV4sTCH0dJ7wrmg3wjHMrYkr5bOOximWlKR2/CQAc++twQKBgQD2cHCGUSFlCfdxKvaqHCTakcJwnwN9cIKz6RyQUOmwcHOn5l89ysSW2Kxb8ojx2Oy6CwwIoRDCHkbIldQnBIvWAO4p6ZfnNFU9PB4zc5JPwYJGTVzMO+XSKdvvxJCSgXDKecTwSdrb6Yn63O3Eo4d9vPwC9TdeIOk6mRwOmfQSTQKBgQDbwMAgUo5mgHmqlxd03mCGqTFamUPnggzgqRKTNgrLQnTtEYRsbXPCyYKCeGnOEx4Z8kO0aeoaaJPjDiuBYco6fPaL7QBpvyMt+NFZDvkrw2MN15gdrmm7FH4MLmyFY9W6kjpCYOJoudxnElNqIqlxWiTR/QHzkI6uBKcFuTTCywKBgEP2unGwij8tQnqJWMGNRXSp4i/hCrtbfhBT57a9OdWZDaxuUjHea90WIGKzPqVEJ/Ue+tCeatq+MtM6zul7zXzVgNk6PNRckG102PdjliQ8v8rXpxIEKtuNA8TaNuzQmeSdNHz4QU2I2K7Aoyop10IMDoxaSEzDUs8ATsaQGsm5AoGAd6+ddH1CTZV/rmeoQOC1FeKknMGI+Fgeh2XOVhtC+b1yTYQS6bMbL2XtDP5kMfa7mEoYkfR1qnh2w11+WHNYX06cQU2Nwgp2MJ2p96VuFGXQV0Oq9898ioX4uM24EVZ0m670yB5Ycc14uzKB9/Z07uW31B7Gp09+Rk0GYtmiYM0CgYEA2Mm6IDp2kLfJg1TOzkr78CocMZL5tLm1s5wjmwmRxWaOs4zfpJFVjrBsW8t0YbkyXFPwfgAmowP/At1uIi145T78LlCDIDnligwH1nZ7M7gGD0Od/L5lZP/VVeACMU0gIq2kqrpi5fkyUEd8VFec9GPRDlXjkRXBM5MAX649mRw=";
			alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkY860kXScdQCm5zBNPIlg03RmPCwTCrc1/DEJL2IWq5yoAj8jQS6/s7Gv2u31v/A5+i87XI9eNdd6mBYfkuUSKwPymGurQlmRyr0aX2IPsqhbNpsn2YwiNosBoWJNY1skiyp3R+pZY6qJPogddBxzFlbYSg0HCwe5Ual8GJkhD2Z+zgpWDX0pc5/sG331JF6bCFmMjlw+l4IsbNN0CZ4u9sJZRBbDaUg9DopgmtnYI+XqCdBLxhF8WxZgrKFbBIeOE1Nhesh8ZouBSvpSS7OMlKbetv6fvoWYkAEVNicDFi9bRTiCSD4/2npnT+IEdTOrGrUaqbfqD2u0KKfbHfbPQIDAQAB";

		}
		
		//实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, appPrivateKey, "json", charset, alipayPublicKey, "RSA2");
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify 
		//SDK已经封装掉了公共参数，这里只需要传入业务参数
		//此次只是参数展示，未进行字符串转义，实际情况下请转义
		
		String outTradeNo; // 商户订单号
		Double totalAmt ;
		String totalAmount; // 订单金额 
		
		DecimalFormat dcfmt = new DecimalFormat("#0.00");
		{
			totalAmt = 0.1 ;
			outTradeNo = "1811171458001";
			totalAmount = dcfmt.format(totalAmt); 
		}
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest(); 
		
		Map<String,Object> bizContentMap = new HashMap<>(16); 
		bizContentMap.put("body", "支付宝app交易");
		bizContentMap.put("subject", "测试的商品名称");
		bizContentMap.put("out_trade_no", outTradeNo );
		bizContentMap.put("total_amount", totalAmount );

		String binzContentStr = JSONObject.toJSONString(bizContentMap); 

//		String tmpStr = "  {" +
//		"    \"primary_industry_name\":\"IT科技/IT软件与服务\"," +
//		"    \"primary_industry_code\":\"10001/20102\"," +
//		"    \"secondary_industry_code\":\"10001/20102\"," +
//		"    \"secondary_industry_name\":\"IT科技/IT软件与服务\"" +
//		" }";
		request.setBizContent(binzContentStr);
		AlipayTradeAppPayResponse response;
		try {
			response = alipayClient.execute(request);
			
			System.err.println(JSONObject.toJSONString(response)); 
			//调用成功，则处理业务逻辑
			if(response.isSuccess()){
				//.....
			}

		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	
	
	
}
