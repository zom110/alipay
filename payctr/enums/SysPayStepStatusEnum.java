package com.sdhoo.pdloan.payctr.enums;


/**
 * 
 * 代付支付环节状态枚举.
 * @author SDPC_LIU
 *
 */
public enum SysPayStepStatusEnum {

	CREATE(0,"创建"), // 
	SUCCESS(1,"交易成功"), //  
	FAIL(2,"失败"), // 
	WAITE4NTF(3,"交易中"), // 等待进一步核对 
	REFORDER(4,"退单"), // 
	EXCEPTION(5,"异常"), // 
	NOT_EXISTS(6,"渠道交易不存在"), // 
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


	SysPayStepStatusEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}


	public static SysPayStepStatusEnum getByCode(int code){
		for(SysPayStepStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public boolean isCodeValid(int code){
		for(SysPayStepStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
}
