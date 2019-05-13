package com.sdhoo.pdloan.payctr.service;

import java.util.Map;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayRcd;
import com.sdhoo.pdloan.payctr.dto.UserAccRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserPayRecordInf;
import com.sdhoo.pdloan.payctr.enums.UserPayIfcChnlEnum;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccResendVerifyReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccVerifyCheckReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrPayReq;
import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;

/**
 * 用户支付渠道服务.
 * @author SDPC_LIU
 *
 */
public interface UserPayChnlService {
	

	/**
	 * 获取实现类的支付渠道枚举
	 * @return
	 */
	UserPayIfcChnlEnum getPayIfcChnlEnum();
	
	/**
	 * 获取默认的支付渠道配置
	 * @return
	 */
	DtPctIfcChnlCfg getDefaultChnlCfg(); 

	/**
	 * 核对用户是否可以使用该渠道.
	 * @param usrId
	 * @param ifcCfgInf 支付配置 
	 * @param extraInf 渠道需要的扩展信息
	 * @return 有值或不为空,则为错误,否则为正确可用
	 */
	String checkUsrCanUseChnl(Long usrId, DtPctIfcChnlCfg ifcCfgInf, Map<String, Object> extraInf) ; 
	
	/**
	 * 获取渠道的用户ID.
	 * @param userId
	 * @return
	 */
	String getChnlUserId(Long userId);


	/**
	 * 创建支付交易.
	 * @param req
	 * @param ifcCfgInf 
	 * @return 调用发起成功则返回发起后的最终状态数据,上层根据该数据判断是否发起检测任务或异步监听, 发起失败(所有不成功)则抛出业务异常. 
	 * @throws BaseServiceException
	 */
	UserPayRecordInf doCreatePayRecord(DoCreateUsrPayReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException ; 


	/**
	 * 校验并转换响应通知信息.
	 * @param formParam
	 * @return
	 * @throws BaseServiceException
	 */
	UserPayRecordInf verifyAndParseNotifyParams(Map<String,? extends Object> formParam ) throws BaseServiceException ; 


	/**
	 * 主动核对并更新支付状态
	 * @param ifcCfgInf 支付配置
	 * @param payRecordInf
	 * @return
	 * @throws BaseServiceException
	 */
    UserPayRecordInf checkAndUpdatePayStatus(DtPctUserPayRcd upr, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException; 
    
    /**
     * 被动核对并更新支付状态
     * @param fedbackInf
     * @param ifcCfgInf 支付配置
     * @return
     * @throws BaseServiceException
     */
    UserPayRecordInf payfedbackCheckAndDoBusi(DoPayFedbackBusiReq fedbackInf, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException ;

    /**
     * 渠道用户开户
     * @param usrAccInf
     * @param ifcCfgInf 支付配置
     * @return
     * @throws BaseServiceException
     */
    UserAccRecordInf openAccount(DoCreateUsrAccReq usrAccInf, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException;

//    /**
//     * 查询开户信息
//     * @param req
//     * @return
//     */
//    List<UserAccRecordInf> queryOpenAccount(DoGetUsrAccRecordInfReq req) throws BaseServiceException ;

    /**
     * 绑卡短信确认
     * @param req
     * @param ifcCfgInf 支付配置ID 
     * @return
     */
    UserAccRecordInf toBindCheck(DoCreateUsrAccVerifyCheckReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException;

    /**
     * 绑卡短信重发
     * @param req
     * @param ifcCfgInf 支付配置ID 
     * @return
     */
    UserAccRecordInf toCreateUserAccResendVerify(DoCreateUsrAccResendVerifyReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException;
    
    
    

}
