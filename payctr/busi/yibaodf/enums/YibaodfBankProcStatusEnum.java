package com.sdhoo.pdloan.payctr.busi.yibaodf.enums;


/**
 * 
 * 易宝代付银行处理状态
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum YibaodfBankProcStatusEnum {

	CODE_S("S","已成功"),
	CODE_I("I","银行处理中"),
	CODE_F("F","出款失败"),
	CODE_W("W","未出款"),
	CODE_U("U","未知"),
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

    YibaodfBankProcStatusEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static YibaodfBankProcStatusEnum getByCode(String code){
		for(YibaodfBankProcStatusEnum tmpEnum : values() ){
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
		for(YibaodfBankProcStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
