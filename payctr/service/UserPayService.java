package com.sdhoo.pdloan.payctr.service;

import java.util.List;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.payctr.dto.UserAccRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserPayRecordInf;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccResendVerifyReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrAccVerifyCheckReq;
import com.sdhoo.pdloan.payctr.service.req.DoCreateUsrPayReq;
import com.sdhoo.pdloan.payctr.service.req.DoGetUsrAccRecordInfReq;
import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;

/**
 * 用户支付服务
 * @author SDPC_LIU
 *
 */
public interface UserPayService {
    

    /**
     * 支付渠道开户请求
     * @param req
     * @return
     */
    UserAccRecordInf createUserAcc(DoCreateUsrAccReq req ) throws BaseServiceException; 

    /**
     * 支付渠道开户验证信息重发
     * @param req
     * @return
     */
    UserAccRecordInf createUserAccResendVerify(DoCreateUsrAccResendVerifyReq req ) throws BaseServiceException; 

    /**
     * 支付渠道开户验证处理.
     * @param req
     * @return
     * @throws BaseServiceException 
     */
    UserAccRecordInf createUserAccVerifyCheck(DoCreateUsrAccVerifyCheckReq req ) throws BaseServiceException; 

    /**
     * 获取用户在渠道的开户主卡信息.
     * @param req
     * @return
     * @throws BaseServiceException 
     */
    UserAccRecordInf getIfcChnlUserMainAcc(DoGetUsrAccRecordInfReq req ) throws BaseServiceException;
    
    /**
     * 根据ID获取(代扣渠道)开户信息
     * @param icuaId
     * @return
     * @throws BaseServiceException
     */
    UserAccRecordInf getUserAccRecordById(Long icuaId) throws BaseServiceException ;


    /**
     * 创建代扣支付编号
     * @param userId
     * @param chanEnum
     * @return
     */
    String genUserPayCode(Long userId);

    /**
     * 发起代扣(创建支付).
     * @param req
     * @return
     * @throws BaseServiceException 
     */
    UserPayRecordInf createAndStartUserPay(DoCreateUsrPayReq req) throws BaseServiceException;

    /**
     * 获取交易记录信息
     * @param tranCode 交易号
     * @return
     */
    UserPayRecordInf getUserPayRecordInf(String tranCode);

    /**
     * 核对反馈的支付信息并处理业务.
     * @param req
     * @return 
     * @throws BaseServiceException 
     */
    UserPayRecordInf payfedbackCheckAndDoBusi(DoPayFedbackBusiReq req) throws BaseServiceException;


    /**
     * 主动核对支付状态并处理对应的业务.用于补偿性的核对支付状态.
     * @param uprCode 
     * @return 返回支付状态
     * @throws BaseServiceException 
     */
    UserPayRecordInf payRecordCheckAndDoBusi(String uprCode) throws BaseServiceException;

    /**
     * 查询用户开户信息
     * @param req
     * @return
     * @throws BaseServiceException
     */
    List<UserAccRecordInf> queryIfcChnlUserAcc(DoGetUsrAccRecordInfReq req) throws BaseServiceException;

//    /**
//     * 根据支付配置ID获取配置信息
//     * @param paycfgId
//     * @return
//     */
//    DtPctIfcChnlCfg getPayCfgById(Long paycfgId) ;


//    /**
//     * 根据枚举获取支付渠道服务实现类.
//     * @param chnlEnu
//     * @return
//     * @throws BaseServiceException 
//     */
//    UserPayChnlService getUserPayChnlServiceByEnum(UserPayIfcChnlEnum chnlEnu);

}

