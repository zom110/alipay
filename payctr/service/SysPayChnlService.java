package com.sdhoo.pdloan.payctr.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayRcd;
import com.sdhoo.pdloan.payctr.dto.SyspayRecordInf;
import com.sdhoo.pdloan.payctr.service.req.DoCreateSysPayReq;
import com.sdhoo.pdloan.payctr.service.req.DoPayFedbackBusiReq;

/**
 * 系统支付接口渠道服务
 * @author SDPC_LIU
 *
 */
public interface SysPayChnlService {
	
	/**
	 * 获取默认的支付渠道配置
	 * @return
	 */
	DtPctIfcChnlCfg getDefaultChnlCfg(); 

    
    /**
     * 创建并发起一个系统支付记录
     * @param req
     * @param ifcCfgInf
     * @return
     * @throws BaseServiceException 
     */
    SyspayRecordInf doCreateSysPayRecord(DoCreateSysPayReq req, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException;


    /**
     * 获取或核对代付最新状态
     * @param payRcd
     * @param ifcCfgInf
     * @return
     * @throws BaseServiceException 
     */
    SyspayRecordInf checkAndUpdatePayStatus(DtPctSysPayRcd payRcd, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException;
    

    /**
     * 被动核对并更新支付状态
     * @param fedbackInf
     * @param ifcCfgInf
     * @return
     * @throws BaseServiceException
     */
    SyspayRecordInf fedbackCheckAndDoBusi(DoPayFedbackBusiReq fedbackInf, DtPctIfcChnlCfg ifcCfgInf) throws BaseServiceException ;

}
