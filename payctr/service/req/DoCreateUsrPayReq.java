package com.sdhoo.pdloan.payctr.service.req;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.sdhoo.pdloan.payctr.service.UserPayService;

/**
 * 用户创建支付(代扣)请求.
 * @author SDPC_LIU
 *
 */
public class DoCreateUsrPayReq {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *  支付编号.
	 */
	@NotNull(message="支付编号不能为空" )
	private String payCode ; 

	@NotNull(message="用户ID不能为空"  )
	private Long userId ;

	@NotNull(message="标题不能为空" )
	private String title ;

	/**
	 *  支付类型, {@UserPayTypeEnum#getCode} 1银行卡代扣
	 */
	@NotNull(message="支付类型不能为空" )
	private Integer payType ; 

	/**
	 *  支付金额(元)
	 */
	@NotNull(message="支付金额不能为空" )
	private Double payamtYuan ; 

	/**
	 * 支付接口渠道ID,{@UserPayIfcChnlEnum}
	 */
	private Integer ifcChnlId ;
	
	/**
	 * 接口渠道配置ID,指定配置渠道,
	 */
	private Long ifcChnlCfgId ;

	/**
	 * 渠道接口支付类型,微信等需指定 MWEB 等作为交易类型,
	 */
	private String ifcChnlTradeType ;

	/**
	 * 接口渠道对应的uid,支付宝则为对应的openid,微信则为openid,新浪支付则为开户用户ID,
	 */
	private String ifcChnlUid ;
	
	/**
	 * 调用客户端的IP地址(用户主动还款时,该字段不为空)
	 */
	private String cltIp ; 

    /**
     *  关联业务信息
     */
    private String busiRelInf ; 

	
	/**
	 * 后台通知URL地址.
	 */
	private String notifyUrl ;

	/**
	 * 支付后返回地址(主动支付需要该值)
	 */
	private String returnUrl ;

	/**
	 *  最多重试次数(网络等未知原因失败时)
	 */
	private Integer maxErrorTryCount = 1  ; 

	/**
	 *  发起人用户类型. @{UsrTypeEnum}
	 */
	private Integer initUserType ; 

	/**
	 *  发起人用户ID.
	 */
	private String initUserId ;
	
	/**
	 * 发起人名称,即描述用户账户或名称,例: 管理员(admin)
	 */
	private String initUserName;

	/**
	 *  发起备注
	 */
	private String initMemo ; 

	/**
	 *  扩展信息(渠道特殊需要的信息,如走微信时,需要提供微信oauth信息等)
	 */
	private Map<String,Object> extraInf ;

	/**
	 * 订单交易类型详见枚举TransactionTypeEnum
	 */
	private Integer transactionType;


	public Integer getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getBusiRelInf() {
		return busiRelInf;
	}

	public void setBusiRelInf(String busiRelInf) {
		this.busiRelInf = busiRelInf;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getPayamtYuan() {
		return payamtYuan;
	}

	public void setPayamtYuan(Double payamtYuan) {
		this.payamtYuan = payamtYuan;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public Integer getMaxErrorTryCount() {
		return maxErrorTryCount;
	}

	public void setMaxErrorTryCount(Integer maxErrorTryCount) {
		this.maxErrorTryCount = maxErrorTryCount;
	}
	
	public Integer getInitUserType() {
		return initUserType;
	}

	public void setInitUserType(Integer initUserType) {
		this.initUserType = initUserType;
	}

	public String getInitUserId() {
		return initUserId;
	}

	public void setInitUserId(String initUserId) {
		this.initUserId = initUserId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

//	public String getPayMemo() {
//		return payMemo;
//	}
//
//	public void setPayMemo(String payMemo) {
//		this.payMemo = payMemo;
//	}

	public String getInitMemo() {
		return initMemo;
	}

	public void setInitMemo(String initMemo) {
		this.initMemo = initMemo;
	}

	public Map<String, Object> getExtraInf() {
		return extraInf;
	}

	public void setExtraInf(Map<String, Object> extraInf) {
		this.extraInf = extraInf;
	}

    public String getCltIp() {
        return cltIp;
    }

    public void setCltIp(String cltIp) {
        this.cltIp = cltIp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getIfcChnlUid() {
        return ifcChnlUid;
    }

    public void setIfcChnlUid(String ifcChnlUid) {
        this.ifcChnlUid = ifcChnlUid;
    }

    public Integer getIfcChnlId() {
        return ifcChnlId;
    }

    public void setIfcChnlId(Integer ifcChnlId) {
        this.ifcChnlId = ifcChnlId;
    }

    public String getIfcChnlTradeType() {
        return ifcChnlTradeType;
    }

    public void setIfcChnlTradeType(String ifcChnlTradeType) {
        this.ifcChnlTradeType = ifcChnlTradeType;
    }

    public Long getIfcChnlCfgId() {
        return ifcChnlCfgId;
    }

    public void setIfcChnlCfgId(Long ifcChnlCfgId) {
        this.ifcChnlCfgId = ifcChnlCfgId;
    }

    public String getInitUserName() {
        return initUserName;
    }

    public void setInitUserName(String initUserName) {
        this.initUserName = initUserName;
    }


}
