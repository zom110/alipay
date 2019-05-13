package com.sdhoo.pdloan.payctr.enums;

/**
 * 
 * 用户账户(开户)状态
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年5月30日
 */
public enum UserAccStepStatusEnum {

    CREATE(0,"数据创建"),

	SUCCESS(1,"开户成功"), 

    FAIL(2,"开户失败"), 

    WAITE_FOR_VERIFYMSG(3,"等待校验信息"),

    EXCEPTION(4,"开户异常"),

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


	UserAccStepStatusEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}

	
	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static UserAccStepStatusEnum getByCode(int code){
		for(UserAccStepStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public static boolean isCodeValid(int code){
		for(UserAccStepStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
}
