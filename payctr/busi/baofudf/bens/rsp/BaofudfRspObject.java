package com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基础内容.
 * @author SDPC_LIU
 *
 */
public class BaofudfRspObject extends BaofudfBaseRsp {

    private List<Map<String, Object >> trans_reqDatas = new ArrayList<Map<String, Object >>() ;

    public List<Map<String, Object>> getTrans_reqDatas() {
        return trans_reqDatas;
    }

    public void setTrans_reqDatas(List<Map<String, Object>> trans_reqDatas) {
        this.trans_reqDatas = trans_reqDatas;
    }
    
}
