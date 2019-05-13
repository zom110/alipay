package com.sdhoo.pdloan.payctr.enums;


/**
 * 对外通知状态枚举
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-20 13:08:38
 *
 */
public enum PctOutNotifyStepEnum {

	UN_NOTIFY(0,"未通知"), 
	SUCCESS(1,"已成功响应"), 
	NOTIFY_FAIL(2,"未成功"), 
	NOTIFY_IN(3,"正在通知"), 
	CANCLED(4,"已取消"), 
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


	PctOutNotifyStepEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}

	public static PctOutNotifyStepEnum getByCode(int code){
		for(PctOutNotifyStepEnum tmpEnum : values() ){
			if(tmpEnum.getCode()== code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	public static boolean isCodeValid(int code){
		for(PctOutNotifyStepEnum tmpEnum : values() ){
			if(tmpEnum.getCode()== code ){
				return true ;
			}
		}
		return false ;
	}
	
}
