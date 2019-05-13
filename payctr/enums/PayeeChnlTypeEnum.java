package com.sdhoo.pdloan.payctr.enums;

/**
 * 
 * 收款渠道类型
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年5月30日
 */
public enum PayeeChnlTypeEnum {

	BANKCARD(1,"银行卡"), 
	NETPAY(2,"网络支付"), 
	;

	private int code ;
	private String desc ;

	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	PayeeChnlTypeEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}


	public static PayeeChnlTypeEnum getByCode(Long code){
		if(code == null ) {
			return null ;
		}
		for(PayeeChnlTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public boolean isCodeValid(int code){
		for(PayeeChnlTypeEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
	
    
    /**
     * 获取名称.
     * @param codesStr
     * @return
     */
    public static String getDescsByCodes(String codesStr ){
        String rtStr = "";
        if(codesStr == null ){
            return rtStr ;
        }
        String[] codesAry = codesStr.split(",");
        int idx = 0 ;
        String regx = "^-?\\d+$" ; // 正则 
        for(String code : codesAry){
            if(code != null && code.matches(regx) ){
                Long codeInt = Long.valueOf(code);
                PayeeChnlTypeEnum byCode = PayeeChnlTypeEnum.getByCode(codeInt);
                if(byCode != null ){
                    if(idx > 0 ){
                        rtStr += ",";   
                    }
                    rtStr += byCode.getDesc();
                }
                
            }
        }
        return rtStr ;
    }
	
	
}
