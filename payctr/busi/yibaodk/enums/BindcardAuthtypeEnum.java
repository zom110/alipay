package com.sdhoo.pdloan.payctr.busi.yibaodk.enums;


/**
 * 
 * 绑卡鉴权类型
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum BindcardAuthtypeEnum {

    /**
     * 四要素校验
     */
    COMMON_FOUR("COMMON_FOUR","验四"),
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

    BindcardAuthtypeEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static BindcardAuthtypeEnum getByCode(String code){
		for(BindcardAuthtypeEnum tmpEnum : values() ){
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
		for(BindcardAuthtypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
