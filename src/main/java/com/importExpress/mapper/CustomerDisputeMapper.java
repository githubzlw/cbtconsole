package com.importExpress.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.importExpress.pojo.CustomerDisputeBean;

public interface CustomerDisputeMapper {

	/**插入数据
	 * @param bean
	 * @return
	 */
	int insert(CustomerDisputeBean bean);
	
	/**更新状态
	 * @param disputeId
	 * @param status
	 * @return
	 */
	int updateStatus(@Param("disputeID")String disputeId,@Param("status")String status);
	
	/**统计数量
	 * @param disputeId
	 * @param status
	 * @return
	 */
	Integer count(@Param("disputeId")String disputeId,@Param("status")String status);
	
	/**获取列表
	 * @param disputeid
	 * @param status
	 * @param startNum
	 * @param limitNum
	 * @return
	 */
	List<CustomerDisputeBean> confirmList(@Param("disputeid")String disputeid,@Param("status")String status,@Param("startNum")int startNum,@Param("limitNum")int limitNum);
	
	/**获取申诉退款信息
	 * @param disputeid
	 * @return
	 */
	CustomerDisputeBean getComfirmByDisputeID(@Param("disputeid")String disputeid);
	
	/**拒绝退款更新
	 * @param disputeid
	 * @param refuseReason
	 * @return
	 */
	Integer updateRefuseReason(@Param("disputeid")String disputeid,@Param("refuseReason")String refuseReason);
	
	
	/**退款成功
	 * @param refundedAmount
	 * @param state
	 * @return
	 */
	Integer updateRefund(@Param("disputeid")String disputeid,@Param("refundedAmount")String refundedAmount);
	
	/**插入数据
	 * @param disputeId
	 * @param admId
	 * @return
	 */
	Integer insertMessage(@Param("disputeId")String disputeId,@Param("admId")int admId);
	
	/**统计消息
	 * @param admId
	 * @return
	 */
	Integer countMessage(@Param("disputeId")String disputeId,@Param("admId")int admId);
	
}
