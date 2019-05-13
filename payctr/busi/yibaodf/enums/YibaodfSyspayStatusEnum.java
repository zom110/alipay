package com.sdhoo.pdloan.payctr.busi.yibaodf.enums;


/**
 * 
 * 易宝代付系统返回码
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum YibaodfSyspayStatusEnum {

	CODE_0025("0025","已接收"),
	CODE_0026("0026","已汇出"),
	CODE_0027("0027","已接收"),
	CODE_0028("0028","已拒绝"),
	CODE_0029("0029","待复核"),
	CODE_0030("0030","未知"),
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

    YibaodfSyspayStatusEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static YibaodfSyspayStatusEnum getByCode(String code){
		for(YibaodfSyspayStatusEnum tmpEnum : values() ){
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
		for(YibaodfSyspayStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
