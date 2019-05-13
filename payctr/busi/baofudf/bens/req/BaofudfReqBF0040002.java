package com.sdhoo.pdloan.payctr.busi.baofudf.bens.req;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp.BaofudfRspBF0040002;

/**
 * 
 * 交易状态查询接口
 * @author SDPC_LIU
 *
 */
public class BaofudfReqBF0040002 extends BaofudfBaseReq<BaofudfRspBF0040002> {


    private List<TransReqBF0040002> transReqInfs = new ArrayList<>() ;
    public void appendTransReq(TransReqBF0040002 req) {
        transReqInfs.add(req); 
    }

    @Override
    public String doGetTranCode() {
        return "BF0040002";
    }

    /**
     * 获取交易请求数据
     */
    @Override
    public List<Map<String, List<TransReqBF0040002>>> getTrans_reqDatas() {
        List<Map<String,List<TransReqBF0040002>>> transReqDatas = new ArrayList<>();
        Map<String,List<TransReqBF0040002>> trans_reqData = new HashMap<>();
        List<TransReqBF0040002> reqDataList = new ArrayList<>();
        for(TransReqBF0040002 transReq : transReqInfs ) {
            reqDataList.add(transReq);
        }
        trans_reqData.put("trans_reqData", reqDataList); 
        transReqDatas.add(trans_reqData); 
        return transReqDatas ;
    }

    @Override
    public Class<BaofudfRspBF0040002> doGetRspClass() {
        return BaofudfRspBF0040002.class;
    }

    /**
     * 代付交易
     * 
     * @author Administrator
     *
     */
    public static class TransReqBF0040002 {

        private String trans_batchid; //宝付批次号
        private String trans_no; // 商户订单号
        
        public String getTrans_batchid() {
            return trans_batchid;
        }

        public void setTrans_batchid(String trans_batchid) {
            this.trans_batchid = trans_batchid;
        }

        public String getTrans_no() {
            return trans_no;
        }

        public void setTrans_no(String trans_no) {
            this.trans_no = trans_no;
        }
    }

}
