package com.sdhoo.pdloan.payctr.busi.fuioudk.enums;


/**
 * 
 * 支付错误码
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum FuiouPayErrorCodeEnum {


    SUCCESS("0000","成功"),
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

    FuiouPayErrorCodeEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static FuiouPayErrorCodeEnum getByCode(String code){
		for(FuiouPayErrorCodeEnum tmpEnum : values() ){
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
		for(FuiouPayErrorCodeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
