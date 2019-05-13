package com.sdhoo.pdloan.payctr.busi.yibaodf.dto.rsp;

import java.util.List;

import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YbdfTransferRst;

/**
 * 代付单笔出款接口
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-05 21:31:51
 *
 */
public class TransferQueryRsp extends YibaodfBaseQueryRsp {
    
    List<YbdfTransferRst> list ; // 数据列表 

    @Override
    public List<YbdfTransferRst> getList() {
        return list;
    }

    public void setList(List<YbdfTransferRst> list) {
        this.list = list;
    }

}
