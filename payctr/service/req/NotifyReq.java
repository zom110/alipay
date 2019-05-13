package com.sdhoo.pdloan.payctr.service.req;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 状态通知表单信息
 *
 */
public class NotifyReq{
    
	@NotEmpty(message="业务编号不能为空")
    private String busiCode;
    
    @NotNull(message="业务类型不能为空")
    private Integer busiType;
    
    @NotNull(message="业务状态不能为空")
    private Integer busiStatus;

    @NotEmpty(message="通知地址不能为空")
    private String busiUrl;
    
    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getBusiUrl() {
        return busiUrl;
    }

    public void setBusiUrl(String busiUrl) {
        this.busiUrl = busiUrl;
    }

    public Integer getBusiType() {
        return busiType;
    }

    public void setBusiType(Integer busiType) {
        this.busiType = busiType;
    }

    public Integer getBusiStatus() {
        return busiStatus;
    }

    public void setBusiStatus(Integer busiStatus) {
        this.busiStatus = busiStatus;
    }
}
