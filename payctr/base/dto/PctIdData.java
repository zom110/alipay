package com.sdhoo.pdloan.payctr.base.dto;

import java.util.Date;

/**
 * ID数据类
 * @author LIUPC
 *
 */
public class PctIdData {

	public PctIdData(Long idVal, Date idDate) {
        super();
        this.idVal = idVal;
        this.idDate = idDate;
    }
    /**
     * ID的值.
     */
    Long idVal ;

    /**
     * ID对应的时间
     */
    Date idDate ;

    public Long getIdVal() {
        return idVal;
    }

    public void setIdVal(Long idVal) {
        this.idVal = idVal;
    }

    public Date getIdDate() {
        return idDate;
    }

    public void setIdDate(Date idDate) {
        this.idDate = idDate;
    }
}
