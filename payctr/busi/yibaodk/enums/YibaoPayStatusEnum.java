package com.sdhoo.pdloan.payctr.busi.yibaodk.enums;


/**
 * 
 * 支付状态枚举
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum YibaoPayStatusEnum {

    PAY_FAIL("PAY_FAIL","支付失败"),
    PROCESSING("PROCESSING","处理中"),
    PAY_SUCCESS("PAY_SUCCESS","支付成功"),
    TIME_OUT("TIME_OUT","支付超时"),
    ACCEPT("ACCEPT","已接收"),
    TO_VALIDATE("TO_VALIDATE","待短验确认"),
    FAIL("FAIL","系统异常"),
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

    YibaoPayStatusEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static YibaoPayStatusEnum getByCode(String code){
		for(YibaoPayStatusEnum tmpEnum : values() ){
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
		for(YibaoPayStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
