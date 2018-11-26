package com.importExpress.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

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
	Map<String,Object> list(String disputeID,int startNum,int limitNum,
			String startTime,String endTime,String status,int admID);
	
	
	
	/**申诉详情
	 * @param disputeID
	 * @return
	 */
	String info(String disputeID);
	
	
	/**确认退款
	 * @param customer
	 * @return
	 */
	int confirm(CustomerDisputeBean customer);
	
	/**统计数量
	 * @param disputeID
	 * @param status
	 * @return
	 */
	int count(String disputeID,String status);
	
	/**获取列表
	 * @param disputeid
	 * @param status
	 * @param startNum
	 * @param limitNum
	 * @return
	 */
	List<CustomerDisputeBean> confirmList(String disputeid,String status,int startNum,int limitNum);
	
	/**更新状态
	 * @param disputeId
	 * @param status
	 * @return
	 */
	int updateStatus(String disputeId,String status);
	
}
