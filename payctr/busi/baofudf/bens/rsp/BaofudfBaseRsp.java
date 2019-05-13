package com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp;

import com.sdhoo.pdloan.payctr.busi.baofudf.bens.TransHead;

/**
 * 基础响应信息
 * @author SDPC_LIU(LiuJianbin)
 * @date 2016年11月18日
 */
public class BaofudfBaseRsp {

    private TransHead trans_head;

//    private List<? extends Object> trans_reqDatas;

    private String data_type;

    public TransHead getTrans_head() {
        return trans_head;
    }

    public void setTrans_head(TransHead trans_head) {
        this.trans_head = trans_head;
    }

//    public List<? extends Object> getTrans_reqDatas() {
//        return trans_reqDatas;
//    }
//
//    public void setTrans_reqDatas(List<? extends Object> trans_reqDatas) {
//        this.trans_reqDatas = trans_reqDatas;
//    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }
    
}
