package com.sdhoo.pdloan.payctr.busi.yibaodk.enums;


/**
 * 
 * 绑卡用户身份类型
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum BindcardIdentityTypeEnum {

    /**
     * 四要素校验
     */
    MAC("MAC","网卡地址"),
    ID_CARD("ID_CARD","用户身份证号"),
    USER_ID("USER_ID","用户ID"),
    EMAIL("EMAIL","邮箱"),
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

    BindcardIdentityTypeEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static BindcardIdentityTypeEnum getByCode(String code){
		for(BindcardIdentityTypeEnum tmpEnum : values() ){
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
		for(BindcardIdentityTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
