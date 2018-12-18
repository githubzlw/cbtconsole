package com.cbt.warehouse.service;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.pojo.Inventory;

import java.util.List;
import java.util.Map;

public interface InventoryService {
	/**
	 * 库存列表查询
	 * @param map
	 * @return
	 */
	public List<Inventory> getIinOutInventory(Map<Object, Object> map);
	public int updateSourcesLog(int in_id, String name, String old_sku, String old_url, String new_barcode, String old_barcode, int new_remaining, int old_remaining, String remark);
	/**
	 * 手动录入库存
	 * @param map
	 * @return
	 */
	public int inventoryEntry(Map<Object, Object> map);
	/**
	 * 最近30天销售掉的库存
	 * @return
	 */
	public String getSaleInventory();

	/**
	 * 最近30天 产生的 库存损耗
	 * @return
	 */
	public String getLossInventory();

	/**
	 * 最近30天 产生的 库存删除
	 * @return
	 */
	public String getDeleteInventory();
	/**
	 * 根据id删除库存数据
	 * @Title deleteInventory
	 * @Description TODO
	 * @param id
	 * @return
	 * @return int
	 */
	public int deleteInventory(int id,String dRemark);
	public int updateSources(String flag, String old_sku, String goods_pid, String car_urlMD5, String new_barcode, String old_barcode, int new_remaining, int old_remaining, String remark, double new_inventory_amount);
	/**
	 *盘点库存库位变更
	 * @param id
	 * @param old_barcode
	 * @param new_barcode
	 * @return
	 */
	public int insertChangeBarcode(int id, String old_barcode, String new_barcode);
	/**
	 * 标记库存为问题库存
	 * @param map
	 * @return
	 */
	public int problem_inventory(Map<Object, Object> map);
	public Inventory queryInId(String old_sku, String old_url, String old_barcode, String car_urlMD5, String flag);
	/**
	 * 查询order_details
	 * @param map
	 * @return
	 */
	public OrderDetailsBean findOrderDetails(Map<Object, Object> map);

	/**
	 * 库存列表查询
	 * @param map
	 * @return
	 */
	public List<Inventory> getIinOutInventoryCount(Map<Object, Object> map);

	/**
	 * 查询分类数据
	 * @param type
	 * @param cid
	 * @return
	 */
	public List<AliCategory> searchAliCategory(String type, String cid);

	public int isExitBarcode(String barcode);
	/**
	 * 获取库存信息
	 * @Title queryInById
	 * @Description TODO
	 * @param id
	 * @return
	 * @return Inventory
	 */
	public Inventory queryInById(String id);
	public int updateIsStockFlag(String goods_pid);
	public int updateIsStockFlag2(String goods_pid);
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
	public int updateIsStockFlag1(String goods_pid);
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
	 * 库存管理页面统计最近30天新产生的库存
	 * @return
	 */
	public String getNewInventory();

}