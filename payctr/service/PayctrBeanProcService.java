package com.sdhoo.pdloan.payctr.service;

import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlUserAcc;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlUserExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayRcd;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayRcd;
import com.sdhoo.pdloan.payctr.dto.SyspayRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserAccRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserPayRecordInf;

/**
 * 支付中心Bean处理服务.
 * @author SDPC_LIU
 *
 */
public interface PayctrBeanProcService {

    /**
     * 根据Model创建Dto数据
     * @param model
     * @param extra
     * @return
     */
    SyspayRecordInf genSysPayRecordInfByModel(DtPctSysPayRcd model, DtPctSysPayExtra extra);

    
    /**
     * 创建交易记录信息
     * @param upr
     * @return
     */
    UserPayRecordInf genUsrPayRecordInfByModel(DtPctUserPayRcd upr, DtPctUserPayExtra upe);


    /**
     * 创建用户开户记录信息
     * @param currChnlUserAcc
     * @param currUserExtra
     * @return
     */
    UserAccRecordInf genUserAccRecordByModel(DtPctIfcChnlUserAcc currChnlUserAcc, DtPctIfcChnlUserExtra currUserExtra);

}
