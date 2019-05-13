package com.sdhoo.pdloan.payctr.busi.yibaodf.dto.rsp;

import java.util.List;

import com.sdhoo.pdloan.payctr.busi.yibaodf.beans.YibaodfBaseRsp;

/**
 * 
 * 发基础查询返回信息
 * @author Administrator(LiuJianbin)
 * @param <T>
 * @data 2018-09-10 19:40:21
 *
 */
public abstract class YibaodfBaseQueryRsp extends YibaodfBaseRsp {
	
	private Integer pageNo ; // 页码,从0开始 
	private Integer totalCount ; // 总数 
	private Integer pageSize ; // 每页条数
	private Integer totalPageSize ; // 总页数

//	private List<T> list ; // 数据列表 
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotalPageSize() {
		return totalPageSize;
	}
	public void setTotalPageSize(Integer totalPageSize) {
		this.totalPageSize = totalPageSize;
	}
	
	public abstract List<? extends Object> getList();

}
