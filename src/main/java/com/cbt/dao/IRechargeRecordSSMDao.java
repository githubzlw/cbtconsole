package com.cbt.dao;

import com.cbt.bean.RechargeRecord;
import com.cbt.common.BaseDao;

import java.util.List;

public interface IRechargeRecordSSMDao extends BaseDao<RechargeRecord> {
	
	public List<RechargeRecord> findRecordByUid(Integer uid);

	public List<RechargeRecord> findRefundRecordsByUid(Integer uid);
	
	
	/**余额变更记录列表
	 * @date 2017年3月29日
	 * @author abc
	 * @param userid 用户id
	 * @param page  分页
	 * @return  
	 */
	List<RechargeRecord> getRecordList(Integer uid, int page);
	
	/**
	 * 
	 * @Title queryBalancePayRecords 
	 * @Description 根据客户id查询余额支付的变更记录
	 * @param userId
	 * @return List<RechargeRecord>
	 */
	List<RechargeRecord> queryBalancePayRecords(int userId);
	
}
