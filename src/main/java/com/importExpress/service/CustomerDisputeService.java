package com.importExpress.service;

import java.util.Map;

public interface CustomerDisputeService {
	
	/**获取申诉列表
	 * @param disputeID
	 * @param startNum
	 * @param limitNum
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @return
	 */
	Map<String,Object> list(String disputeID,int startNum,int limitNum,String startTime,String endTime,String status);
	
	
	
}
