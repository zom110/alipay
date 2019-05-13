package com.sdhoo.pdloan.payctr.enums;

import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.pdloan.payctr.PaycenterConstants;

/**
 * 
 * 系统支付渠道接口层枚举
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年5月30日
 */
public enum SyspayIfcChnlEnum {

	/**
	 * 放款通道枚举
	 */
	BAOFUDF(PaycenterConstants.SYSPAY_CHNL_BAOFUDF,"宝付代付"),  // 101 
	YIBAODF(PaycenterConstants.SYSPAY_CHNL_YIBAODF,"易宝代付"),  // 103 
	FUIOUDF(PaycenterConstants.SYSPAY_CHNL_FUIOU,"富友代付"), // 107 
	OFLINE(PaycenterConstants.SYSPAY_CHNL_OFLINE,"线下放款"), // 108 
	EXTENSION(PaycenterConstants.SYSPAY_CHNL_EXTENSION,"展期放款"), // 109
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


	SyspayIfcChnlEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}


	public static SyspayIfcChnlEnum getByCode(Integer code){
		if(code == null ) {
			return null ;
		}
		for(SyspayIfcChnlEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public boolean isCodeValid(int code){
		for(SyspayIfcChnlEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
	
	/**
	 * 获取名称.
	 * @param codesStr
	 * @return
	 */
	public static String getDescsByCodes(String codesStr ){
		String rtStr = "";
		if(StringUtils.isEmpty(codesStr)){
			return rtStr ;
		}
		String[] codesAry = codesStr.split(",");
		int idx = 0 ;
		String regx = "^-?\\d+$" ; // 正则 
		for(String code : codesAry){
			if(StringUtils.isNotEmpty(code)&& code.matches(regx) ){
				Integer codeInt = Integer.valueOf(code);
				SyspayIfcChnlEnum byCode = SyspayIfcChnlEnum.getByCode(codeInt);
				if(byCode != null ){
					if(idx > 0 ){
						rtStr += ",";	
					}
					rtStr += byCode.getDesc();
				}
				
			}
		}
		return rtStr ;
	}
	
	
	
}
