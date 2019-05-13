package com.sdhoo.pdloan.payctr.busi.yibaodk.dto.rsp;

import java.util.List;

import com.sdhoo.pdloan.payctr.busi.yibaodk.beans.YibaodkBaseRsp;
/**
 * 鉴权列表查询返回数据
 * @author fangzhiping
 * @date 2018年4月18日
 */
public class JqBkListRsp extends YibaodkBaseRsp {
    
    private static final long serialVersionUID = 1L; 
    
    /**
     * 用户标识
     */
    public String identityid;
    /**
     * 用户标识类型
     */
    public String identitytype;
    /**
     * 绑卡列表
     */
    public List<JqRecordRsp> cardlist;

    public List<JqRecordRsp> getCardlist() {
        return cardlist;
    }
    public void setCardlist(List<JqRecordRsp> cardlist) {
        this.cardlist = cardlist;
    }
    public String getIdentityid() {
        return identityid;
    }
    public void setIdentityid(String identityid) {
        this.identityid = identityid;
    }
    public String getIdentitytype() {
        return identitytype;
    }
    public void setIdentitytype(String identitytype) {
        this.identitytype = identitytype;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
