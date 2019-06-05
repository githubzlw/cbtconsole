package com.importExpress.service;

import java.util.List;

import com.importExpress.pojo.LoginErrorInfo;

public interface LoginErrorService {
	
	/**获取数据列表
	 * @param startNum
	 * @param limitNum
	 * @return
	 */
	List<LoginErrorInfo> getList(int startNum, int limitNum);

}
