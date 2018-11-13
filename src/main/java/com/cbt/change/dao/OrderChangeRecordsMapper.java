package com.cbt.change.dao;

import com.cbt.change.bean.OrderChangeRecords;
import com.cbt.change.bean.OrderChangeRecordsQuery;

import java.util.List;

public interface OrderChangeRecordsMapper {

	/**
	 * 根据条件查询订单变更记录数据
	 * 
	 * @param changeRecordsQuery
	 * @return
	 */
	public List<OrderChangeRecords> queryForList(OrderChangeRecordsQuery changeRecordsQuery);
	
	/**
	 * 根据条件查询符合条件的数据数量
	 * @param changeRecordsQuery
	 * @return
	 */
	public int queryForCount(OrderChangeRecordsQuery changeRecordsQuery);

	/**
	 * 插入订单变更记录信息
	 * 
	 * @param changeRecords
	 */
	public void insertRecords(OrderChangeRecords changeRecords);
	
	

}
