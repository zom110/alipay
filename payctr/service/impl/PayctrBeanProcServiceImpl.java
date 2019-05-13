package com.sdhoo.pdloan.payctr.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlCfg;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlUserAcc;
import com.sdhoo.pdloan.bcrud.model.DtPctIfcChnlUserExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctSysPayRcd;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayExtra;
import com.sdhoo.pdloan.bcrud.model.DtPctUserPayRcd;
import com.sdhoo.pdloan.bcrud.service.DtPctIfcChnlCfgService;
import com.sdhoo.pdloan.payctr.dto.SyspayRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserAccRecordInf;
import com.sdhoo.pdloan.payctr.dto.UserPayRecordInf;
import com.sdhoo.pdloan.payctr.service.PayctrBeanProcService;

/**
 * Bean处理服务
 * @author SDPC_LIU
 *
 */
@Service
public class PayctrBeanProcServiceImpl implements PayctrBeanProcService {

//    @Autowired
//    private DtPctBankInfoService pctBankInfoService;
    
    @Autowired
    private DtPctIfcChnlCfgService pctIfcChnlCfgService;
    
    /**
     * 创建交易记录信息
     * @param upr
     * @return
     */
    @Override
    public UserPayRecordInf genUsrPayRecordInfByModel(DtPctUserPayRcd upr , DtPctUserPayExtra upe ) {
        if(upr == null ) {
            throw new IllegalArgumentException("入参model不能为空");
        }
        UserPayRecordInf uprInf = new UserPayRecordInf();

        uprInf.setPayChnlFebtime(upr.getIfcChnlFebtime());
        uprInf.setPayChnlId(upr.getIfcChnlId());
        uprInf.setIfcChnlCfgId(upr.getIfcChnlCfgId()); 
        uprInf.setPayChnlName(upr.getIfcChnlName()); 
        uprInf.setPayCode(upr.getUprCode());
        uprInf.setPayRmbamt(upr.getPayRmbamt());
        uprInf.setPayStepStatus(upr.getStepStatus());
        uprInf.setPayTitle(upr.getPayTitle());
        uprInf.setPayType(upr.getPayType());
        uprInf.setUprCtime(upr.getUprCtime());
        uprInf.setUprId(upr.getUprId());
        uprInf.setUprMtime(upr.getUprMtime());
        uprInf.setUserId(upr.getUserId()); 

        if(upe != null ) {
            uprInf.setInitMemo(upe.getInitMemo());
            if(upe.getInitUid() != null) {
                uprInf.setInitUserId(upe.getInitUid().toString());
            }
            uprInf.setBusiRelInf(upe.getBusiRelInfo());
            uprInf.setInitUserType(upe.getInitUtype()); 
            uprInf.setNotifyUrl(upe.getUprNotifyUrl()); 
            uprInf.setPayChnlFebinfo(upe.getIfcFebInf());
            uprInf.setPayChnlRstcode(upe.getIfcChnlRspcode());
            uprInf.setPayChnlRstMsg(upe.getIfcChnlRspmsg());
            uprInf.setPayChnlSerial(upe.getIfcChnlSerial());
            uprInf.setPrepayInf(upe.getPrepayJsn());
        }
        return uprInf;
    }


    /**
     * 根据Model创建Dto数据
     * @param model
     * @param extra
     * @return
     */
    @Override
    public SyspayRecordInf genSysPayRecordInfByModel(DtPctSysPayRcd model,DtPctSysPayExtra extra ) {
        if(model == null) {
            return null ;
        }
        SyspayRecordInf rtInf = new SyspayRecordInf();
        rtInf.setSprId(model.getSprId());
        rtInf.setSprCode(model.getSprCode());
        rtInf.setIfcChnlId(model.getIfcChnlId());
        
        rtInf.setSprId( model.getSprId() ) ;
        rtInf.setSprCode( model.getSprCode() );
        rtInf.setUserId( model.getUserId() );
        rtInf.setPayTitle( model.getPayTitle() );
        rtInf.setIfcChnlId( model.getIfcChnlId() );
        rtInf.setIfcChnlCfgId( model.getIfcChnlCfgId() );
        rtInf.setIfcChnlName( model.getIfcChnlName() );
        rtInf.setIfcChnlBatchNo( model.getIfcChnlBatchNo() );
        rtInf.setIfcChnlEndtime( model.getIfcChnlEndtime() );
        rtInf.setPayRmbamt( model.getPayRmbamt() );
        rtInf.setPayeeChnlId( model.getPayeeChnlId() );
        rtInf.setPayeeChnlType( model.getPayeeChnlType() );
        rtInf.setPayeeChnlName( model.getPayeeChnlName() );
//        rtInf.setPayeeChnlCode( model.getPayeeChnlCode() );
        rtInf.setPayeeChnlFullname( model.getPayeeChnlFullname() );
        rtInf.setPayeeChnlProvname( model.getPayeeChnlProvname() );
        rtInf.setPayeeChnlCityname( model.getPayeeChnlCityname() );
        rtInf.setPayeeAccUnitType( model.getPayeeAccUnitType() );
        rtInf.setPayeeAccNo( model.getPayeeAccNo() );
        rtInf.setPayeeAccFullname( model.getPayeeAccFullname() );
        rtInf.setPayeeAccIdcard( model.getPayeeAccIdcard() );
        rtInf.setPayeeAccMobile( model.getPayeeAccMobile() );
        rtInf.setStepStatus( model.getStepStatus() );
        rtInf.setStepMemo( model.getStepMemo() );
        rtInf.setSprCtime( model.getSprCtime() );
        rtInf.setSprMtime( model.getSprMtime() );
        
        if(extra != null ) {
            rtInf.setBusyRelInf(extra.getBusiRelInf());
            rtInf.setInitMemo(extra.getInitMemo());
        }
        return rtInf ;
    }


    @Override
    public UserAccRecordInf genUserAccRecordByModel(DtPctIfcChnlUserAcc currChnlUserAcc, DtPctIfcChnlUserExtra currUserExtra) {
        if(currChnlUserAcc == null ) {
            return null ;
        }
        UserAccRecordInf record = new UserAccRecordInf();
        record.setUarId(currChnlUserAcc.getIcuaId());
        record.setAccStepStatus(currChnlUserAcc.getStepStatus());
        record.setIfcChnlCfgId(currChnlUserAcc.getIfcChnlCfgId());
        record.setOpenId(currChnlUserAcc.getOpenId());
        record.setPayChnlId(currChnlUserAcc.getIfcChnlId());
        record.setUarCtime(currChnlUserAcc.getIcuaCtime());
        record.setUarMtime(currChnlUserAcc.getIcuaMtime());
        record.setUserId(currChnlUserAcc.getUserId());
        record.setPayChnlName(currChnlUserAcc.getIfcChnlName()); 

        if(currUserExtra != null ) {
            record.setAccStepStatusMemo(currUserExtra.getIfcChnlStatusMemo());
            record.setBankCardMobile(currUserExtra.getBankCardMobile());
            record.setBankCardNo(currUserExtra.getBankCardNo());
            record.setIdcardNo(currUserExtra.getIdcardNo());
            record.setIfcRequestno(currUserExtra.getIfcRequestno());
            record.setUserFullname(currUserExtra.getUserFullname());
            record.setBankCode(currUserExtra.getBankCode());
            record.setBankName(currUserExtra.getBankName()); 
        }
        record.setMainAccStatus(currChnlUserAcc.getMainAccStatus());
        DtPctIfcChnlCfg cfg = pctIfcChnlCfgService.getByPrimKey(currChnlUserAcc.getIfcChnlCfgId(), null);
        if( cfg != null && cfg.getMemberName()!=null && cfg != null ){
            record.setPayChnlName(cfg.getMemberName());
        }
        return record;
    }
    
    
    
}
