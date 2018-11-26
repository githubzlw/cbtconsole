package com.importExpress.service;

import java.util.Map;

import com.importExpress.pojo.CustomerDisputeBean;

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
	
	
	
	/**申诉详情
	 * @param disputeID
	 * @return
	 */
	String info(String disputeID);
	
	
	/**
	 * 确认财务去退款
	 * @param disputeId
	 * @param userid
	 * @param orderNo
	 * @param transctionID
	 * @param remark
	 * @return
	 */
	int confirm(CustomerDisputeBean customer);
	
	/**是否已经提醒退款
	 * @param disputeID
	 * @return
	 */
	int getConfim(String disputeID);
	
	
}
