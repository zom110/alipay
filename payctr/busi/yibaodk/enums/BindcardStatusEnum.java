package com.sdhoo.pdloan.payctr.busi.yibaodk.enums;


/**
 * 
 * 绑卡鉴权类型
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum BindcardStatusEnum {

    /**
     * 绑卡成功
     */
    BIND_SUCCESS("BIND_SUCCESS","绑卡成功"),
    TO_VALIDATE("TO_VALIDATE","待短验"),
    BIND_FAIL("BIND_FAIL","绑卡失败"),
    BIND_ERROR("BIND_ERROR","绑卡异常"), // 可重试
    TIME_OUT("TIME_OUT","超时失败"),
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

    BindcardStatusEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static BindcardStatusEnum getByCode(String code){
		for(BindcardStatusEnum tmpEnum : values() ){
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
		for(BindcardStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
