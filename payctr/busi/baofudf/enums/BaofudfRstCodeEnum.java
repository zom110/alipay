package com.sdhoo.pdloan.payctr.busi.baofudf.enums;


/**
 * 宝付代付的返回码.
 * @author SDPC_LIU
 *
 */
public enum BaofudfRstCodeEnum {

	CODE_0000("0000","交易成功",1),
	CODE_200("200","代付交易成功",1),

	CODE_0300("0300","代付交易未明，请发起该笔订单查询",2),
	CODE_0401("0401","代付交易查证信息不存在",2),
	CODE_0999("0999","代付主机系统繁忙",2),

	CODE_0001("0001","商户代付公共参数格式不正确",3),
	CODE_0002("0002","商户代付证书无效",3),
	CODE_0003("0003","商户代付报文格式不正确",3),
	CODE_0004("0004","交易请求记录条数超过上限",3),
	CODE_0201("0201","商户未开通代付业务",3),
	CODE_0202("0202","商户不存在，请联系宝付技术支持",3),
	CODE_0203("0203","商户代付业务未绑定IP，请联系宝付技术支持",3),
	CODE_0204("0204","商户代付终端号不存在，请联系宝付技术支持",3),
	CODE_0205("0205","商户代付收款方账号{}被列入黑名单",3),
	CODE_0206("0206","商户代付交易受限",3),
	CODE_0207("0207","商户和委托商户不能相同",3),
	CODE_0208("0208","商户和委托商户绑定关系不存在",3),
	CODE_0301("0301","代付交易失败",3),
	CODE_0501("0501","代付白名单添加失败",3),
	CODE_0601("0601","代付(同卡进出)交易失败",3),
	;

    /**
     * 编号
     */
	private String code ;
	
	/**
	 * 名称
	 */
	private String name ;

	/**
	 *  类型, 1为交易成功类, 2为交易结果暂未知类(需查询), 3 为交易失败类,无需查询.
	 */
	private Integer type ; 

	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String desc) {
		this.name = desc;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	BaofudfRstCodeEnum(String code , String desc ,Integer type ){
		this.code = code ;
		this.name = desc ;
		this.type = type ;
	}

	public static BaofudfRstCodeEnum getByCode(String code){
		for(BaofudfRstCodeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	public boolean isCodeValid(String code){
		for(BaofudfRstCodeEnum tmpEnum : values() ){
			if(tmpEnum.getCode().equals(code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
