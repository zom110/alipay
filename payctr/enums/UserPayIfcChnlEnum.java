package com.sdhoo.pdloan.payctr.enums;

import com.sdhoo.pdloan.payctr.PaycenterConstants;

/**
 * 
 * 用户支付接口渠道
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年5月30日
 */
public enum UserPayIfcChnlEnum {

	
	
    SINAPAY_BINDINGDK(PaycenterConstants.USERPAY_CHNL_SINABINDDK,"新浪绑定代扣"), 

//    BAOFU_PAY_DK(PaycenterConstants.USERPAY_CHNL_BAOFU,"宝付银行卡代扣"),
    
    YIBAOPAY_BINDDK(PaycenterConstants.USERPAY_CHNL_YIBAOBINDDK,"易宝绑定代扣"),
    
    FUYOU_BINDDK(PaycenterConstants.USERPAY_CHNL_FUIOUBINDDK,"富友绑定代扣"),

    WEIXINPAY(PaycenterConstants.USERPAY_CHNL_WEIXIN,"微信支付"),

    ALIPAY(PaycenterConstants.USERPAY_CHNL_ALIPAY,"支付宝支付"),

    USRPAY_OFL(PaycenterConstants.USERPAY_CHNL_OFLINE,"线下还款"),

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

	UserPayIfcChnlEnum(int code , String desc ){
		this.code = code ;
		this.desc = desc;
	}
	
	/**
	 * 根据编号获取枚举
	 * @param code
	 * @return
	 */
	public static UserPayIfcChnlEnum getByCode(Integer code) {
	    if(code == null ) {
	        return null ;
	    }
	    for(UserPayIfcChnlEnum tmpEnum : values() ){
            if(tmpEnum.getCode() == code ){
                return tmpEnum ;
            }
        }
	    return null ;
	}

	public boolean isCodeValid(int code){
		for(UserPayIfcChnlEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == code ){
				return true ;
			}
		}
		return false ;
	}
	
}
