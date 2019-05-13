package com.sdhoo.pdloan.payctr.busi.fuioudf.enums;


/**
 * 
 * 代付状态码
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum FuiouDfPayStatusEnum {

    SUCCESS("success","成功"),
    ACCEPT_SUCCESS("acceptSuccess","受理成功"),
    INTERNAL_FAIL("internalFail","富友失败"),
    UNKNOW_REASONS("unknowReasons","交易结果未知"),
    CHANNEL_FAIL("channelFail","通道失败"),
    CARDINFO_ERROR("cardInfoError","卡信息错误"),
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

    FuiouDfPayStatusEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static FuiouDfPayStatusEnum getByCode(String code){
		for(FuiouDfPayStatusEnum tmpEnum : values() ){
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
		for(FuiouDfPayStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
