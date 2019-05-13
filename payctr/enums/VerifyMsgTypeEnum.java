package com.sdhoo.pdloan.payctr.enums;

/**
 * 
 * 校验信息类型
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年5月30日
 */
public enum VerifyMsgTypeEnum {


	SMS(1,"短信"), 

    VOICE(2,"语音"), 
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


	VerifyMsgTypeEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}

	
	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static VerifyMsgTypeEnum getByCode(int code){
		for(VerifyMsgTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public static boolean isCodeValid(int code){
		for(VerifyMsgTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
}
