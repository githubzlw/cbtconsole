package com.cbt.warehouse.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.pojo.Inventory;
import com.cbt.website.bean.InventoryBarcodeRecord;
import com.cbt.website.bean.InventoryCheck;
import com.cbt.website.bean.InventoryCheckRecord;
import com.cbt.website.bean.InventoryData;
import com.cbt.website.bean.InventoryDetails;
import com.cbt.website.bean.InventoryLock;
import com.cbt.website.bean.InventoryLog;
import com.cbt.website.bean.InventorySku;
import com.cbt.website.bean.InventoryWrap;
import com.cbt.website.bean.LossInventoryRecord;
import com.cbt.website.bean.PurchaseSamplingStatisticsPojo;

public interface InventoryMapper {
	/**锁定库存
	 * @param map
	 * @return
	 */
	int insertLockInventory(InventoryLock lock);
	/**关联order_details_sku_match inventory_sku获取用户下单从规格id,库存数量
	 * @param od_id
	 * @return
	 */
	Map<String,Object> getInventoryByOrderDetialsId(String od_id);
	
	/**库存关联入库记录 插入storage_outbound_details记录
	 * @param inventory
	 * @return
	 */
	int insertStorageOutboundDetails(Map<String,String> inventory);
	
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
	public int recordLossInventory(Map<String, Object> map);
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
	public int getIinOutInventoryCount(Map<Object, Object> map);

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
	
	
	
	
	
	/*****************************************新库存系统**********start*************************************/
	/**
	 * 库存列表查询inventory_sku
	 * @param map
	 * @return
	 */
	List<InventoryData> getIinOutInventory(Map<Object, Object> map);
	/**获取库存列表
	 * @param list
	 * @return
	 */
	List<InventoryData> getInventoryByIds(List<Integer> list);
	
	/**库存变更记录表(关联库存表插入)
	 * @param inventory
	 * @return
	 */
	int addInventoryLogByInventoryid(Map<String,String> inventory);
	
	/**获取订单产品详细数据
	 * @param inventory
	 * @return
	 */
	Map<String,String> getOrderDetails(Map<String,String> map);
	
	/**插入库存明细表(关联库存表插入)inventory_details_sku
	 * @param inventory
	 * @return
	 */
	int addInventoryDetailsSku(Map<String,String> inventory);
	
	
	/**获取库存明细表的货源产品详细数据inventory_details_sku
	 * @param itemid
	 * @param specid
	 * @param skuid
	 * @return
	 */
	Map<String,Object> getInventoryDetailSku(String inventory_sku_id);
	
	/**采购入库关联
	 * @param map
	 * @return
	 */
	int addIdRelationTable(Map<String,String> map);
	
	/**库存报损记录loss_inventory_record
	 * @param record
	 * @return
	 */
	int addLossInventoryRecord(LossInventoryRecord record);
	/**库存明细列表inventory_details_sku
	 * @param map
	 * @return
	 */
	List<InventoryDetails> inventoryDetails(Map<String, Object> map);
	/**库存明细数量inventory_details_sku
	 * @param map
	 * @return
	 */
	int inventoryDetailsCount(Map<String,Object> map);
	
	/**库存inventory_sku
	 * @param item
	 * @return
	 */
	int insertInventory(InventorySku item);
	
	
	/**获取库存inventory_sku
	 * @param item
	 * @return
	 */
	InventorySku getInventory(InventorySku item);
	
	/**更新库存inventory_sku
	 * @param item
	 * @return
	 */
	int updateInventory(InventorySku item);
	
	/**库存变更表inventory_sku_log
	 * @param log
	 * @return
	 */
	int insertInventoryLog(InventoryLog log);
	
	/**库存明细inventory_details_sku
	 * @param detail
	 * @return
	 */
	int insertInventoryDetailsSku(InventoryDetails detail);
	
	/**获取淘宝订单 taobao_1688_history
	 * @param orderShipno
	 * @return
	 */
	List<Map<String,Object>> getTbGoods(String orderShipno);
	
	
	/**开始库存盘点  inventory_sku_check
	 * @param check
	 * @return
	 */
	int insertInventoryCheck(InventoryCheck check);
	
	/**获取最近一次有效盘点 inventory_sku_check
	 * @return
	 */
	InventoryCheck getLastInventoryCheck();
	
	/**撤销盘点 inventory_sku_check
	 * @param check
	 * @return
	 */
	int updateInventoryCheckCancel(InventoryCheck check);
	
	/**更新盘点完成标志 inventory_sku_check
	 * @param id
	 * @return
	 */
	int updateInventoryCheckDone(int id);
	
	/**批量更新盘点记录
	 * @param list
	 * @return
	 */
	int iBatchInventoryCheckRecord(List<InventoryCheckRecord> list);
	/**获取库存表所有类别统计列表inventory_sku & 1688_category
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
	int doneInventoryCheckRecord(int checkId);
	
	/**获取inventory_sku_check_record_temp本次盘点数据
	 * @param checkId
	 * @return
	 */
	List<InventoryCheckRecord> getInventoryCheckRecord(int checkId);
	/**获取inventory_sku_check_record盘点历史数据
	 * @param checkId
	 * @return
	 */
	List<InventoryCheckRecord> getICRHistory(@Param("inid")int inid,@Param("page")int page,@Param("goodsPid")String goodsPid);
	
	/**获取inventory_sku_check_record盘点历史数据数量
	 * @param inid
	 * @return
	 */
	int getICRHistoryCount(@Param("inid")int inid,@Param("goodsPid")String goodsPid);
	
	/**清空inventory_sku_check_record_temp本次盘点数据
	 * @param checkId
	 * @return
	 */
	int deleteInventoryCheckRecord(int checkId);
	
	/**盘点更新库存
	 * @param item
	 * @return
	 */
	int updateInventoryCheckFlag(InventorySku item);
	
	/**完全使用库存更新订单详情状态
	 * @param odid
	 * @return
	 */
	int updateOrderDetailsState(int odid);
	
	/**获取使用了库存订单详情列表
	 * @param orderid
	 * @return
	 */
	List<Map<String,Object>> getInventoryUsedByOrderno(String orderid);
	/**获取使用了库存订单详情列表
	 * @param orderid
	 * @return
	 */
	List<Map<String,Object>> getInventoryUsedByOdid(String odid);
	/**获取已经验货的订单详情列表
	 * @param orderid
	 * @return
	 */
	List<Map<String,Object>> getCheckedOrderDetailsByOrderno(String orderid);
	/**获取已经验货的订单详情列表
	 * @param orderid
	 * @return
	 */
	List<Map<String,Object>> getCheckedOrderDetailsByOdid(String odid);
	
	/**取消库存锁定
	 * @param id
	 * @return
	 */
	int cancelLockInventory(int id);
	
	/**使用库存，标记库位移动
	 * @return
	 */
	int insertInventoryBarcodeRecord(InventoryBarcodeRecord record);
	
	/**更新状态
	 * @param id
	 * @param state
	 * @return
	 */
	int updateBarcodeRecord(@Param("id")int id,@Param("state")int state);
	
	/**获取库位变更
	 * @param id
	 * @return
	 */
	InventoryWrap getInventoryBarcode(int id);
	
	/**移库位
	 * @return
	 */
	List<InventoryWrap> inventoryBarcodeList(Map<String, Object> map);
	/**移库位
	 * @return
	 */
	int inventoryBarcodeListCount(Map<String, Object> map);
	
	/**订单取消进入库存的数据
	 * @param odid
	 * @return
	 */
	Map<String,Object> getAddInventory(int odid);
	/**更新库位变换
	 * @param map
	 * @return
	 */
	int  updateBarcode(Map<String,Object> map);
	/**更新库位变换
	 * @param map
	 * @return
	 */
	int  updateRemark(Map<String,Object> map);
	
	/**获取库存变更备注
	 * @param list
	 * @return
	 */
	List<Map<String,Object>> getInventoryRemark(int skuId);
}
