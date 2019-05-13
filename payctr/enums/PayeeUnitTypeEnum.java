package com.sdhoo.pdloan.payctr.enums;


/**
 * 
 * 收款单位类型
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年5月30日
 */
public enum PayeeUnitTypeEnum {

	/**
	 * 查询类型
	 */
	COMPANY(1,"公司"), 
	PERSON(2,"个人"), 
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


	PayeeUnitTypeEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}


	public static PayeeUnitTypeEnum getByCode(Integer code){
		if(code == null ) {
			return null ;
		}
		for(PayeeUnitTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public boolean isCodeValid(int code){
		for(PayeeUnitTypeEnum tmpEnum : values() ){
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
		if( codesStr == null ){
			return rtStr ;
		}
		String[] codesAry = codesStr.split(",");
		int idx = 0 ;
		String regx = "^-?\\d+$" ; // 正则 
		for(String code : codesAry){
			if( code != null && code.matches(regx) ){
				Integer codeInt = Integer.valueOf(code);
				PayeeUnitTypeEnum byCode = PayeeUnitTypeEnum.getByCode(codeInt);
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
