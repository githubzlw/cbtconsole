package com.importExpress.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.importExpress.pojo.CustomerDisputeBean;

public interface CustomerDisputeMapper {

	/**
	 * @param disputeId
	 * @param userid
	 * @param orderNo
	 * @param transctionID
	 * @param remark
	 * @return
	 */
	int insert(CustomerDisputeBean bean);
	
	/**
	 * @param disputeId
	 * @param userid
	 * @param orderNo
	 * @param transctionID
	 * @param remark
	 * @return
	 */
	int update(CustomerDisputeBean bean);
	
	/**
	 * @param disputeId
	 * @return
	 */
	Integer count(@Param("disputeId")String disputeId);
	
	List<CustomerDisputeBean> list();
}
