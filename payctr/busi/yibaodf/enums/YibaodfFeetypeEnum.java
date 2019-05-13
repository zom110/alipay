package com.sdhoo.pdloan.payctr.busi.yibaodf.enums;


/**
 * 易宝代付,手续费类型枚举,
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-06 19:45:29
 *
 */
public enum YibaodfFeetypeEnum {

	/**
	 * 查询类型
	 */
	SOURCE("SOURCE","商户承担"), // 
	TARGET("TARGET","用户承担"), // 
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


	YibaodfFeetypeEnum(String code , String desc ){
		this.code = code ;
		this.desc = desc;
	}

	public boolean isCodeValid(String code){
		for(YibaodfFeetypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
