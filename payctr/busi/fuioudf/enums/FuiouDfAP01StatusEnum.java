package com.sdhoo.pdloan.payctr.busi.fuioudf.enums;


/**
 * 
 * 代付返回状态码
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum FuiouDfAP01StatusEnum {

	UN_SEND(0,"交易未发送"),
    SUCCESS(1,"交易已发送且成功"),
    SEND_FAIL(2,"交易已发送且失败"),
    SEND_IN(3,"交易发送中"),
    SEND_TIMEOUT(7,"交易已发送且超时"),
	;

	private int code ;
	private String name ;

	public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    FuiouDfAP01StatusEnum(int code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static FuiouDfAP01StatusEnum getByCode(int code){
		for(FuiouDfAP01StatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
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
	public static boolean isCodeValid(int code){
		for(FuiouDfAP01StatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
}
