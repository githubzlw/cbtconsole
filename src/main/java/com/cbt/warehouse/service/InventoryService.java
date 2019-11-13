package com.cbt.warehouse.service;

import java.util.List;
import java.util.Map;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.pojo.Inventory;
import com.cbt.website.bean.InventoryCheck;
import com.cbt.website.bean.InventoryCheckRecord;
import com.cbt.website.bean.InventoryCheckWrap;
import com.cbt.website.bean.InventoryData;
import com.cbt.website.bean.InventoryDetailsWrap;
import com.cbt.website.bean.InventoryWrap;
import com.cbt.website.bean.LossInventoryWrap;

public interface InventoryService {
	/**采购使用库存减少操作
	 * @param inventory
	 * @return
	 */
	int useInventory(Map<String,String> inventory);
	/**取消库存增加操作
	 * @param inventory
	 * @return
	 */
	int cancelInventory(Map<String,String> inventory);
	
	/**验货增加库存
	 * @param inventory
	 * @return
	 */
	int addInventory(Map<String,String> inventory);
	/**人工录入库存
	 * @param inventory
	 * @return
	 */
	int inputInventory(Map<String,String> inventory);
	/**
	 * 库存列表查询
	 * @param map
	 * @return
	 */
	public List<InventoryData> getIinOutInventory(Map<Object, Object> map);
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
	public int getIinOutInventoryCount(Map<Object, Object> map);

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
	
	/*************************************************************************/
	/**
	 * 损耗库存记录
	 * @Title recordLossInventory
	 * @Description TODO
	 * @param map
	 * @return
	 * @return int
	 */
	public int recordLossInventory(Map<String, Object> map);
	/**
	 * 库存管理页面统计最近30天新产生的库存
	 * @return
	 */
	public String getNewInventory();
	
	/**库存报损调整
	 * @param map
	 * @return
	 */
	public Map<String,Object> reportLossInventory(Map<String, Object> map);
	
	/**库存明细
	 * @param map
	 * @return
	 */
	List<InventoryDetailsWrap> inventoryDetails(Map<String, Object> map);
	
	/**库存明细数量
	 * @param map
	 * @return
	 */
	int inventoryDetailsCount(Map<String, Object> map);
	
	/**获取淘宝订单
	 * @param orderShipno
	 * @return
	 */
	List<Map<String,Object>> getTbGoods(String orderShipno);
	
	
	/**盘点列表
	 * @return
	 */
	List<InventoryCheckWrap> invetoryCheckList(Map<Object, Object> map);
	/**盘点列表所有
	 * @return
	 */
	List<InventoryCheckWrap> invetoryCheck();
	
	/**最近盘点
	 * @return
	 */
	InventoryCheck getLastInventoryCheck();
	/**获取最近一次有效盘点 inventory_sku_check
	 * @return
	 */
	List<InventoryCheck> getUnDoneInventoryCheck();
	
	/**开始盘点
	 * @param check
	 * @return
	 */
	int insertInventoryCheck(InventoryCheck check);
	/**撤销盘点
	 * @param check
	 * @return
	 */
	int updateInventoryCheckCancel(InventoryCheck check);
	
	/**获取库存表所有类别统计列表
	 * @return
	 */
	List<Map<String,Object>> getInventoryCatList();
	
	/**插入盘点记录 inventory_sku_check_record_temp
	 * @param record
	 * @return
	 */
	int insertInventoryCheckRecord(InventoryCheckRecord record);
	
	/**更新盘点记录inventory_sku_check_record_temp
	 * @param record
	 * @return
	 */
	int updateInventoryCheckRecord(InventoryCheckRecord record);
	
	/**完成盘点 将inventory_sku_check_record_temp 本次数据插入inventory_sku_check_record
	 * @param checkId
	 * @return
	 */
	List<InventoryCheckRecord> doneInventoryCheckRecord(int checkId,int admid);
	/**获取inventory_sku_check_record盘点历史数据
	 * @param checkId
	 * @return
	 */
	List<InventoryCheckRecord> getICRHistory(int inid,int page,String goodsPid);
	
	/**获取inventory_sku_check_record盘点历史数据数量
	 * @param inid
	 * @return
	 */
	int getICRHistoryCount(int inid,String goodsPid);
	
	/**取消订单进入库存
	 * @param orderNo
	 * @return
	 */
	int cancelOrderToInventory(String orderNo,int admid,String admName);
	/**拆单取消进入库存
	 * @param orderNo
	 * @return
	 */
	int cancelToInventory( String[] odidLst,int admid,String admName);
	/**移库列表
	 * @return
	 */
	List<InventoryWrap> inventoryBarcodeList(Map<String, Object> map);
	/**移库列表数量
	 * @return
	 */
	int inventoryBarcodeListCount(Map<String, Object> map);
	
	/**移库
	 * @param map
	 * @return
	 */
	int  moveBarcode(Map<String,Object> map);
	/**更新备注
	 * @param map
	 * @return
	 */
	int  updateRemark(Map<String,Object> map);
	
	/**报损列表
	 * @return
	 */
	List<LossInventoryWrap> inventoryLossList(Map<String,Object> map);
	/**报损列表数量
	 * @param map
	 * @return
	 */
	int inventoryLossListCount(Map<String,Object> map);
	/**未完成移库请求数量
	 * @return
	 */
	int getUnDoneInventoryBarcode();
	
	/**库位列表
	 * @return
	 */
	List<String> barcodeList();
	/**库存库位修改
	 * @param map
	 * @return
	 */
	int  updateBarcode(Map<String,Object> map);
	
}
