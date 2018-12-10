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

	/**
	 * 手动录入库存
	 * @param map
	 * @return
	 */
	public int inventoryEntry(Map<Object, Object> map);

	public int updateSources(String flag, String old_sku, String goods_pid, String car_urlMD5, String new_barcode, String old_barcode, int new_remaining, int old_remaining, String remark, double new_inventory_amount);

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


}
