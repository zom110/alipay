package com.sdhoo.pdloan.payctr.busi.baofudf.bens.rsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 交易状态查证接口
 * @author SDPC_LIU
 *
 */
public class BaofudfRspBF0040002 extends BaofudfBaseRsp {

    
    private List<Map<String, List<TransRespBF0040002>>> trans_reqDatas = new ArrayList<Map<String, List<TransRespBF0040002>>>() ; 

    
    public void setTrans_reqDatas(List<Map<String, List<TransRespBF0040002>>> trans_reqDatas) {
        this.trans_reqDatas = trans_reqDatas;
    }

    public List<Map<String, List<TransRespBF0040002>>> getTrans_reqDatas() {
        return trans_reqDatas;
    }

    public static class TransRespBF0040002 {

        private String trans_orderid;//宝付订单号
        private String trans_batchid;// 宝付批次号
        private String trans_no;// 商户订单号
        private String trans_money;// 转账金额
        private String to_acc_name;// 收款人姓名
        private String to_acc_no;// 收款人银行帐号
        private String to_acc_dept;// 收款人开户行机构名
        
        private String trans_fee;

        /**
         * 订单交易状态, 0转账中,1转账成功,-1转账失败,2转账退款
         */
        private String state;
        private String trans_remark;
        private String trans_starttime;
        private String trans_endtime;
        
        private String trans_summary;//摘要
        
        public String getTrans_orderid() {
            return trans_orderid;
        }

        public void setTrans_orderid(String trans_orderid) {
            this.trans_orderid = trans_orderid;
        }

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

        public String getTrans_money() {
            return trans_money;
        }

        public void setTrans_money(String trans_money) {
            this.trans_money = trans_money;
        }

        public String getTo_acc_name() {
            return to_acc_name;
        }

        public void setTo_acc_name(String to_acc_name) {
            this.to_acc_name = to_acc_name;
        }

        public String getTo_acc_no() {
            return to_acc_no;
        }

        public void setTo_acc_no(String to_acc_no) {
            this.to_acc_no = to_acc_no;
        }

        public String getTo_acc_dept() {
            return to_acc_dept;
        }

        public void setTo_acc_dept(String to_acc_dept) {
            this.to_acc_dept = to_acc_dept;
        }

        public String getTrans_fee() {
            return trans_fee;
        }

        public void setTrans_fee(String trans_fee) {
            this.trans_fee = trans_fee;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTrans_remark() {
            return trans_remark;
        }

        public void setTrans_remark(String trans_remark) {
            this.trans_remark = trans_remark;
        }

        public String getTrans_starttime() {
            return trans_starttime;
        }

        public void setTrans_starttime(String trans_starttime) {
            this.trans_starttime = trans_starttime;
        }

        public String getTrans_endtime() {
            return trans_endtime;
        }

        public void setTrans_endtime(String trans_endtime) {
            this.trans_endtime = trans_endtime;
        }

        public String getTrans_summary() {
            return trans_summary;
        }

        public void setTrans_summary(String trans_summary) {
            this.trans_summary = trans_summary;
        }

    }
    
}
