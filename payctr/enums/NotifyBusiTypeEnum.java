package com.sdhoo.pdloan.payctr.enums;


/**
 * 
 * 通知业务类型枚举.
 * @author Fangzhiping
 * 
 */
public enum NotifyBusiTypeEnum {

	/**
	 * 查询类型
	 */
	SYS_PAY_STATUS("1","系统代付状态通知"), // 
	USER_PAY_STATUS("2","用户支付状态通知"), // 
	;

	private String code ;
	private String desc ;
	
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	NotifyBusiTypeEnum(String code , String desc ){
		this.code = code ;
		this.desc = desc;
	}

	public boolean isCodeValid(String code){
		for(NotifyBusiTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
