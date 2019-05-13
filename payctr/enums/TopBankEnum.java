package com.sdhoo.pdloan.payctr.enums;


/**
 * 
 * 顶级银行枚举
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum TopBankEnum {

    ICBC("ICBC","工商银行"),
    BOC("BOC","中国银行"),
    CCB("CCB","建设银行"),
    PSBC("PSBC","邮政储蓄"),
    ECITIC("ECITIC","中信银行"),
    CEB("CEB","光大银行"),
    HX("HX","华夏银行"),
    CIB("CIB","兴业银行"),
    SPDB("SPDB","浦发银行"),
    SZPA("SZPA","平安银行"),
    CMBC("CMBC","民生银行"),
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

    TopBankEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static TopBankEnum getByCode(String code){
		for(TopBankEnum tmpEnum : values() ){
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
		for(TopBankEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
