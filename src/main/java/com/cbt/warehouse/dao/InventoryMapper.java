package com.cbt.warehouse.dao;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.pojo.Inventory;
import com.cbt.website.bean.InventoryData;
import com.cbt.website.bean.PurchaseSamplingStatisticsPojo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface InventoryMapper {
	/**
	 * 库存列表查询
	 * @param map
	 * @return
	 */
	public List<Inventory> getIinOutInventory(Map<Object, Object> map);
	public int isExitBarcode(@Param("barcode") String barcode);
	/**
	 * 根据ID获取库存
	 */
	public Inventory queryInById(@Param("id") String id);
	public int updateIsStockFlag(@Param("goods_pid") String goods_pid);
	public int updateSourcesLog(@Param("in_id") int in_id, @Param("name") String name, @Param("old_sku") String old_sku, @Param("old_url") String old_url, @Param("new_barcode") String new_barcode, @Param("old_barcode") String old_barcode, @Param("new_remaining") int new_remaining, @Param("old_remaining") int old_remaining, @Param("remark") String remark);
	public int updateIsStockFlag1(@Param("goods_pid") String goods_pid);
	/**
	 * 根据亚马逊录入的pid去查询是否存在库存
	 * @param map
	 * @return
	 */
	public Inventory getInventoryByPid(Map<String,String> map);
	/**
	 * 录入的库存是新的亚马逊库存,做插入操作
	 * @param map
	 * @return
	 */
	public int insertInventoryYmx(Map<String,String> map);
	/**
	 * 损耗库存记录
	 * @Title recordLossInventory
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	public int recordLossInventory(Map<Object, Object> map);
	/**
	 * 手动录入库存
	 * @param map
	 * @return
	 */
	public int inventoryEntry(Map<Object, Object> map);
	/**
	 * 最近30天 产生的 库存损耗
	 * @return
	 */
	public PurchaseSamplingStatisticsPojo getLossInventory();
	/**
	 * 最近30天 产生的 库存删除
	 * @return
	 */
	public PurchaseSamplingStatisticsPojo getDeleteInventory();
	/**
	 * 最近30天销售掉的库存
	 * @return
	 */
	public PurchaseSamplingStatisticsPojo getSaleInventory();
	/**
	 * 查询订单商品详情信息
	 * @param map
	 * @return
	 */
	public OrderDetailsBean findOrderDetails(Map<Object, Object> map);

	public int updateSources(@Param("flag") String flag, @Param("old_sku") String old_sku, @Param("goods_pid") String goods_pid, @Param("car_urlMD5") String car_urlMD5, @Param("new_barcode") String new_barcode, @Param("old_barcode") String old_barcode, @Param("new_remaining") int new_remaining, @Param("old_remaining") int old_remaining, @Param("remark") String remark, @Param("new_inventory_amount") double new_inventory_amount);
	/**
	 * 库存管理页面统计最近30天新产生的库存
	 * @return
	 */
	public PurchaseSamplingStatisticsPojo getNewInventory();
	/**
	 * 根据ID删除库存数据
	 */
	public int deleteInventory(@Param("id") int id,@Param("dRemark") String dRemark);
	public int updateIsStockFlag2(@Param("goods_pid") String goods_pid);
	/**
	 * 库存列表查询
	 * @param map
	 * @return
	 */
	public List<Inventory> getIinOutInventoryCount(Map<Object, Object> map);

	public List<AliCategory> searchAliCategory(@Param("type") String type, @Param("cid") String cid);

	public Inventory queryInId(@Param("old_sku") String old_sku, @Param("goods_pid") String goods_pid, @Param("old_barcode") String old_barcode, @Param("car_urlMD5") String car_urlMD5, @Param("flag") String flag);

	/**
	 * 标记库存为问题库存
	 * @Title problem_inventory
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	public int problem_inventory(Map<Object, Object> map);
	/**
	 *盘点库存库位变更
	 * @param id
	 * @param old_barcode
	 * @param new_barcode
	 * @return
	 */
	public int insertChangeBarcode(@Param("id") int id, @Param("old_barcode") String old_barcode, @Param("new_barcode") String new_barcode);
	
	
	
	/****************************************************************************************/
	
	/**库存变更记录表
	 * @param inventory
	 * @return
	 */
	int addInventoryChangeRecord(Map<String,String> inventory);
	
	
	/**获取库存数量
	 * @param inventory
	 * @return
	 */
	Map<String,Object>  getInventory(Map<String,String> inventory);
	
	/**插入库存
	 * @return
	 */
	int addInventory(Map<String,String> inventory);
	
	/**更新库存
	 * @return
	 */
	int updateInventory(Map<String,String> inventory);
	
	
	/**是否存在库存
	 * @return
	 */
	Integer isExsisInventory(Map<String,String> inventory);
	
	
	/**记录库存存入记录，用来统计当月库存金额
	 * @param inventory
	 * @return
	int insertInventoryDetails(Map<String,String> inventory);
	 */
	
	/**
	 * @param inventory
	 * @return
	 */
	Map<String,String> getOrderDetails(Map<String,String> inventory);
	
	/**入库
	 * @param inventory
	 * @return
	 */
	int insertInventoryDetailsSku(Map<String,String> inventory);
	
//	int addStorageOutboundDetails(Map<String,String> inventory);
	
	
	
}
