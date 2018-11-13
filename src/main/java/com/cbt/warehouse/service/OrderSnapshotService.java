package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.OrderSnapshot;

import java.util.List;

public interface OrderSnapshotService {

	/**
	 * 
	 * @Title syncOfflineToOnline
	 * @Description 同步线下订单快照数据到线上
	 * @return 是否同步成功
	 * @return boolean
	 */
	public boolean syncOfflineToOnline();

	/**
	 * 
	 * @Title queryMaxIdFromOrderSnapshot
	 * @Description 查询订单快照表的最大id
	 * @return 最大id
	 * @return int
	 */
	public int queryMaxIdFromOrderSnapshot();

	/**
	 * 
	 * @Title queryForListByMaxId
	 * @Description 查询order_details表id大于order_snapshot表的最大id的数据
	 * @param maxId
	 * @return 订单快照需要的数据的集合
	 * @return List<OrderSnapshot>
	 */
	public List<OrderSnapshot> queryForListByMaxId(int maxId);

	/**
	 * 
	 * @Title batchInsertOrderSnapshot
	 * @Description 批量插入订单快照信息
	 * @return 是否插入成功
	 * @return boolean
	 */
	public boolean batchInsertOrderSnapshot(List<OrderSnapshot> orderSnapshots);

	/**
	 * 
	 * @Title updateOrderSnapshot
	 * @Description 更新订单快照信息
	 * @param orderSnapshot
	 * @return 是否更新成功
	 * @return boolean
	 */
	public boolean updateOrderSnapshot(OrderSnapshot orderSnapshot);

}
