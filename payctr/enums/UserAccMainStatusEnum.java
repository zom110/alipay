package com.sdhoo.pdloan.payctr.enums;

/**
 * 
 * 用户账户主户状态
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年5月30日
 */
public enum UserAccMainStatusEnum {

	MAIN(1,"主账户"), 

    SUBACC(2,"副账户"), 

    FORUPDATE_MAIN(3,"待更新为主账户"),

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


	UserAccMainStatusEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}

	
	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static UserAccMainStatusEnum getByCode(int code){
		for(UserAccMainStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public static boolean isCodeValid(int code){
		for(UserAccMainStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
}
