package com.importExpress.service;

import java.util.Map;

public interface LoginErrorService {
	
	/**获取数据列表
	 * @param startNum
	 * @param limitNum
	 * @return
	 */
	Map<String,Object> getList(int startNum, int limitNum);

}
