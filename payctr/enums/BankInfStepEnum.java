package com.sdhoo.pdloan.payctr.enums;


/**
 * 银行卡状态
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-14 20:04:14
 *
 */
public enum BankInfStepEnum {

	NONE(0,"未支持"), 
	AVALABLE(1,"可用"), // 有效的 
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


	BankInfStepEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}


	public static BankInfStepEnum getByCode(Integer code){
		if(code == null ) {
			return null ;
		}
		for(BankInfStepEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public boolean isCodeValid(int code){
		for(BankInfStepEnum tmpEnum : values() ){
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
				BankInfStepEnum byCode = BankInfStepEnum.getByCode(codeInt);
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
