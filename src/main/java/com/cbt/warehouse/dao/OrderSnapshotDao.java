package com.cbt.warehouse.dao;

import com.cbt.warehouse.pojo.OrderSnapshot;

import java.util.List;

public interface OrderSnapshotDao {

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
	 * @Title insertOrderSnapshot
	 * @Description 单条插入订单快照信息
	 * @return 是否插入成功
	 * @return boolean
	 */
	public boolean insertOrderSnapshot(OrderSnapshot orderSnapshot);

	/**
	 * 
	 * @Title updateOrderSnapshot
	 * @Description 更新订单快照信息
	 * @param orderSnapshot
	 * @return 是否更新成功
	 * @return boolean
	 */
	public boolean updateOrderSnapshot(OrderSnapshot orderSnapshot);
	
	/**
	 * 
	 * @Title queryExistsImgUrlByPidAndType 
	 * @Description 根据pid和规格数据查询是否存在已经下载的图片
	 * @param pid
	 * @param type
	 * @return 图片链接
	 * @return String
	 */
	public String queryExistsImgUrlByPidAndType(String pid, String type);

}
