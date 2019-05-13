package com.sdhoo.pdloan.payctr.base.service;

import com.sdhoo.common.base.util.WebOptUtils.WebRequest;
import com.sdhoo.common.base.util.WebOptUtils.WebResp;

public interface WebUtilService {

	
	/**
	 * 远程调用.
	 * @param inReq
	 * @return
	 */
	public WebResp doRmtPost(WebRequest inReq);

}
