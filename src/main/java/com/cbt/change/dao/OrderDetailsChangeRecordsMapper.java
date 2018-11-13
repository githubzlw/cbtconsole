package com.cbt.change.dao;

import com.cbt.change.bean.OrderDetailsChangeRecords;
import com.cbt.change.bean.OrderDetailsChangeRecordsQuery;

import java.util.List;

public interface OrderDetailsChangeRecordsMapper {
	/**
	 * 根据条件查询订单详情更改记录数据
	 * 
	 * @param changeRecordsQuery
	 * @return
	 */
	public List<OrderDetailsChangeRecords> queryForList(OrderDetailsChangeRecordsQuery changeRecordsQuery);
	
	/**
	 * 根据条件查询符合条件的数据数量
	 * @param changeRecordsQuery
	 * @return
	 */
	public int queryForCount(OrderDetailsChangeRecordsQuery changeRecordsQuery);

	/**
	 * 插入订单详情变更记录信息
	 * 
	 * @param changeRecords
	 */
	public void insertChangeRecords(OrderDetailsChangeRecords changeRecords);

}
