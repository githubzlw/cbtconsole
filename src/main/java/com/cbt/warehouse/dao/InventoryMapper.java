package com.cbt.warehouse.dao;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.pojo.Inventory;
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
	/**
	 * 手动录入库存
	 * @param map
	 * @return
	 */
	public int inventoryEntry(Map<Object, Object> map);
	/**
	 * 查询订单商品详情信息
	 * @param map
	 * @return
	 */
	public OrderDetailsBean findOrderDetails(Map<Object, Object> map);

	public int updateSources(@Param("flag") String flag, @Param("old_sku") String old_sku, @Param("goods_pid") String goods_pid, @Param("car_urlMD5") String car_urlMD5, @Param("new_barcode") String new_barcode, @Param("old_barcode") String old_barcode, @Param("new_remaining") int new_remaining, @Param("old_remaining") int old_remaining, @Param("remark") String remark, @Param("new_inventory_amount") double new_inventory_amount);

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
}
