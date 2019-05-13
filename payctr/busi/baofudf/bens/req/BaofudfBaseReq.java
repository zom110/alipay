package com.sdhoo.pdloan.payctr.busi.baofudf.bens.req;

import java.util.List;

import com.sdhoo.pdloan.payctr.busi.baofudf.bens.TransHead;
import com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp.BaofudfBaseRsp;

public abstract class BaofudfBaseReq<T extends BaofudfBaseRsp > {

    private TransHead trans_head;

//    private List<? extends Object> trans_reqDatas;

    private String data_type;

	public TransHead getTrans_head() {
        return trans_head;
    }

    public void setTrans_head(TransHead trans_head) {
        this.trans_head = trans_head;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public abstract List<? extends Object> getTrans_reqDatas();

//    public void setTrans_reqDatas(List<? extends Object> trans_reqDatas) {
//        this.trans_reqDatas = trans_reqDatas;
//    }

    /**
     * 获取交易码
     * @return
     */
    public abstract String doGetTranCode();
    
    /**
     * 获取响应类
     * @return
     */
    public abstract Class<T> doGetRspClass();
    

}
