package com.sdhoo.pdloan.payctr.enums;


/**
 * 
 * 用户支付环节状态枚举.
 * @author SDPC_LIU
 *
 */
public enum UserPayStepStatusEnum {

	CREATE(0,"创建"), 
	SUCCESS(1,"支付成功"), 
	FAIL(2,"失败"), 
	WAITE4NTF(3,"等待进一步核查"), 
	NEEDPUSH(4,"需要推进交易"), 
	EXCEPTION(5,"异常"), 
//	REFORDER(4,"退单"), // 
//	NEED_MONITOR(6,"需要监测状态"), // 
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


	UserPayStepStatusEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}


	public static UserPayStepStatusEnum getByCode(int code){
		for(UserPayStepStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public boolean isCodeValid(int code){
		for(UserPayStepStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
}
