package com.sdhoo.pdloan.payctr.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.payctr.dto.SyspayRecordInf;
import com.sdhoo.pdloan.payctr.service.req.DoCreateSysPayReq;
import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;

/**
 * 系统支付中心服务.用于通过系统给指定用户或企业支付金额
 * @author SDPC_LIU
 *
 */
public interface SysPayService {
    

    /**
     * 创建代付支付编号
     * @param ifcEnum
     * @return
     */
    public String genSysPayCode();

    /**
     * 创建发起代付.
     * @param req
     * @return
     * @throws BaseServiceException
     */
    SyspayRecordInf doCreateSysPay(DoCreateSysPayReq req) throws BaseServiceException;
    
    /**
     * 获取代付记录交易号
     * @param sprId
     * @return
     */
    public SyspayRecordInf doGetSysPayRcd(String sprCode);

    /**
     * 核对最新支付状态的支付信息.
     * @param sprCode
     * @return
     * @throws BaseServiceException
     */
    SyspayRecordInf doPayRcdCheckAndDoBusi(String sprCode) throws BaseServiceException;

    /**
     * 核对反馈的支付信息并处理业务.
     * @param req 
     * @return 
     * @throws BaseServiceException 
     */
    SyspayRecordInf payfedbackCheckAndDoBusi(DoPayFedbackBusiReq req) throws BaseServiceException;
    


}
