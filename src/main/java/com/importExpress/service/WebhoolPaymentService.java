package com.importExpress.service;

import java.util.Map;

public interface WebhoolPaymentService {
	
	/**列表
	 * @param startNum
	 * @param limitNum
	 * @param type
	 * @return
	 */
	Map<String,Object> list(int startNum, int limitNum, String type);

}
