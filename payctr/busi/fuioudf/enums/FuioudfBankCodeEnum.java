package com.sdhoo.pdloan.payctr.busi.fuioudf.enums;


/**
 * 
 * 银行编号枚举
 * @author SDPC_LIU(LiuJianbin)
 * @date 2018年4月20日
 */
public enum FuioudfBankCodeEnum {

    ICBC("0102","中国工商银行"),
    ABC("0103","中国农业银行"),
    BOC("0104","中国银行"),
    CCB("0105","中国建设银行"),
    COMM("0301","交通银行"),
    CMB("0308","招商银行"),
    CIB("0309","兴业银行"),
    CMBC("0305","中国民生银行"),
    GDB("0306","广发银行"),
    PAB("0307","平安银行"),
    SPDB("0310","浦发银行"),
    HXB("0304","华夏银行"),
    BOB("0313","北京银行"),
    SHB("0313","上海银行"),
    RCB("0314","农商银行"),
    HSBC("0315","恒丰银行"),
    PSBC("0403","中国邮政储蓄银行"),
    CEB("0303","光大银行"),
    CITIC("0302","中信银行"),
    CZB("0316","浙商银行"),
    CBHB("0318","渤海银行"),
    HSB("0319","徽商银行"),
	BCOM("0301","中国交通银行"),
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

    FuioudfBankCodeEnum(String code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static FuioudfBankCodeEnum getByCode(String code){
		for(FuioudfBankCodeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	/**
	 * 根据名称获取枚举
	 * @param code
	 * @return
	 */
	public static FuioudfBankCodeEnum getByName(String name){
		for(FuioudfBankCodeEnum tmpEnum : values() ){
			if(tmpEnum.getName().equals(name) ){
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
		for(FuioudfBankCodeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
