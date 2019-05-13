package com.sdhoo.pdloan.payctr.busi.yibaodk.enums;


/**
 * 
 * 绑卡证件类型
 * @author Fangzhiping
 */
public enum BindcardIdCardTypeEnum {

    ID("ID","身份证"),
	;

	private String code ;
	private String name ;

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    BindcardIdCardTypeEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static BindcardIdCardTypeEnum getByCode(String code){
		for(BindcardIdCardTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	/**
	 * CODE是否符合
	 * @param code
	 * @return
	 */
	public static boolean isCodeValid(String code){
		for(BindcardIdCardTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
