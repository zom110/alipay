package com.sdhoo.pdloan.payctr.enums;

import com.sdhoo.pdloan.payctr.PaycenterConstants;

/**
 * 
 * 支付类型
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年5月30日
 */
public enum UserPayTypeEnum {


	DUDC(PaycenterConstants.USERPAY_IFC_TRADETYPE_DUDC,"银行卡代扣"), 

    JSAPI(PaycenterConstants.USERPAY_IFC_TRADETYPE_JSAPI,"公众号支付"), 
    
    NATIVE(PaycenterConstants.USERPAY_IFC_TRADETYPE_NATIVE,"本地扫码支付"), 
    
    APP(PaycenterConstants.USERPAY_IFC_TRADETYPE_APP,"商户APP支付"), 
    
    MWEB(PaycenterConstants.USERPAY_IFC_TRADETYPE_MWEB,"手机网页支付"), 
    
    PCWEB(PaycenterConstants.USERPAY_IFC_TRADETYPE_PCWEB,"PC网页支付"), 
    
    OFLINE(PaycenterConstants.USERPAY_IFC_TRADETYPE_OFLINE,"线下支付"), 
	;

	private int code ;
	private String desc ;
	
	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	UserPayTypeEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}

	
	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static UserPayTypeEnum getByCode(Integer code){
		for(UserPayTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public static boolean isCodeValid(int code){
		for(UserPayTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
}
