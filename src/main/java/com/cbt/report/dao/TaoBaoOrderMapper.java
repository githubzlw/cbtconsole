package com.cbt.report.dao;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.DataQueryBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.bean.Orderinfo;
import com.cbt.pojo.*;
import com.cbt.warehouse.pojo.Shipments;
import com.cbt.warehouse.pojo.ShippingPackage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TaoBaoOrderMapper {
	List<TaoBaoOrderInfo> selectTaoBaoOrder(@Param("orderstatus") String orderstatus, @Param("orderSource") String orderSource, @Param("orderdate") String orderdate, @Param("procurementAccount") String procurementAccount,
                                            @Param("start") Integer start, @Param("end") Integer end, @Param("DeliveryDate") Integer DeliveryDate, @Param("orderid") String orderid, @Param("isCompany") String isCompany, @Param("myorderid") String myorderid, @Param("startdate") String startdate, @Param("enddate") String enddate, @Param("paystartdate") String paystartdate, @Param("payenddate") String payenddate, @Param("page") Integer page);

	public List<TaoBaoOrderInfo> selectAllCount(@Param("orderstatus") String orderstatus, @Param("orderSource") String orderSource, @Param("orderdate") String orderdate, @Param("procurementAccount") String procurementAccount,
                                                @Param("start") Integer start, @Param("end") Integer end, @Param("DeliveryDate") Integer DeliveryDate, @Param("orderid") String orderid, @Param("isCompany") String isCompany, @Param("myorderid") String myorderid, @Param("startdate") String startdate, @Param("enddate") String enddate, @Param("paystartdate") String paystartdate, @Param("payenddate") String payenddate);

    public List<TaoBaoOrderInfo> getTaoBaoOrderDetails(@Param("orderid") String orderid, @Param("bforderid") String bforderid);

    public List isStorage(@Param("orderid") String orderid);

    public List<TaoBaoOrderInfo> associatedOrder(@Param("orderid") String orderid);

    public List<String> selectStatus();
    public List<com.cbt.pojo.Admuser> getAllBuyer();
	/**
	 * 添加实际采购不在分配采购备注
	 * @param map
	 * @return
	 */
	public int saveRemark(Map<String, String> map);
    /**
     * 优惠券启用
     * @param id
     * @return
     */
    public int enable(@Param("id") int id);

    /**
	    * 批量更改优惠券详情状态
	    * @param
	    * @return
	    * 2017-07-27 whj
	    */
	public int enableForSubsidiary(@Param("id") int id);


    /**
	    * 查询优惠券主表状态
	    * @param subsidiary_id
	    * @return coupons_id
	    * 2017-06-27 王宏杰
	    */
	public int queryCouponsState(@Param("subsidiary_id") int subsidiary_id);

	/**
	    * 批量启用停用优惠券详情
	    * @param map
	    * @return
	    * 2017-06-28 王宏杰
	    */
	public int AllenableDetails(Map<Object, Object> map);

    /**
	    * 查询当天最后生成的批次
	    * time 当天日期格式
	    * @return
	    */
	public String queryOldBatch(@Param("time") String time);

	 /**
	    * 批量启用、停用优惠券
	    * @param map
	    * @return
	    * 2017-06-27 王宏杰
	    */
	public int Allenable(Map<Object, Object> map);
	/**
	 * 线下采购付款审核
	 */
	public int ThroughReview(Map<Object, Object> map);

	/**
	    * 保存修改的优惠券详情
	    * @param map
	    * @return
	    * 2017-06-27 王宏杰
	    */
	public int addCoupusDetailsView(Map<Object, Object> map);


	public String getUserName(@Param("id") int id);

	/**
	    * 创建优惠券
	    * @param map
	    * @return
	    * 2017-06-26 whj
	  */
	public int createCoupons(Map<Object, Object> map);

	/**
	 * 删除优惠券信息
	 * @param id
	 * @return
	 * 2017-07-06 王宏杰
	 */
	public int delCoupons(@Param("id") int id);

	/**
	 * 查询优惠券可使用的类别
	 * @return
	 */
	public List<AliCategory> queryUsingRange();

	public int getCouponsId(@Param("batch") String batch);

	 /**
	    * 获取优惠券详情明细
	    * @param map
	    * @return
	    * 2017-06-27 王宏杰
	    */
	public List<CouponSubsidiary> searchCoupusDetailsView(
            Map<Object, Object> map);

	/**
	    * 月统计采购对账报表
	    * @param map
	    * @return
	    */
	public List<BuyReconciliationPojo> buyReconciliationReport(Map<Object, Object> map);
	/**
	 * 订单页面的销售额统计报表
	 * @param map
	 * @return
	 */
	public List<OrderSalesAmountPojo> orderSalesAmount(Map<Object, Object> map);

	public OrderSalesAmountPojo getMonthlyFinancialStatistics(@Param("times") String times);
	/**
	 * 异步获取利润汇总数据
	 * @param map
	 * @return
	 */
	public List<OrderSalesAmountPojo> getProfitSummaryData(Map<String, String> map);

	/**
	 * 删除当月用户利润数据
	 * @param time
	 * @return
	 */
	public int deleteUserProfitByMonth(String time);

	/**
	 * 统计用户月利润数据
	 * @param map
	 * @return
	 */
	public List<OrderSalesAmountPojo> getUserProfitByMonth(Map<String, String> map);

	/**
	 * 用户月利润数量总数
	 * @param map
	 * @return
	 */
	public List<OrderSalesAmountPojo> getUserProfitByMonthCount(Map<String, String> map);

	/**
	 * 获取用户当月订单实际运费
	 * @return
	 */
	public String getActualFreight(@Param("userId") String userId, @Param("s_time") String s_time, @Param("e_time") String e_time);

	/**
	 * 获取月用户利润中下单的的订单信息
	 * @param userId
	 * @param s_time
	 * @param e_time
	 * @return
	 */
	public List<Map<String,String>> getAllOrderNo(@Param("userId") String userId, @Param("s_time") String s_time, @Param("e_time") String e_time);

	/**
	 * 付款审批的相关财务统计报表
	 * @param map
	 * @return
	 */
	public List<OrderDetetailsSalePojo> orderDetetailsSale(Map<Object, Object> map);
	/**
	 * 付款审批的相关财务统计报表数量
	 * @param map
	 * @return
	 */
	public List<OrderDetetailsSalePojo> orderDetetailsSaleCount(Map<Object, Object> map);
	/**
	 * 付款审批的相关财务统计明细报表
	 * @param map
	 * @return
	 */
	public List<OrderDetetailsSalePojo> orderDetetailsSaleDetails(Map<Object, Object> map);
	/**
	 * 批量插入月用户利润数据
	 * @param list
	 * @return
	 */
	public int insertUserProfitBatch(List<OrderSalesAmountPojo> list);
	/**
	 * 查询当月用户利润数据
	 * @param map
	 */
	public List<OrderSalesAmountPojo> getUserProfitByMonthData(Map<String, String> map);

	/**
	 * 查询当月用户利润数据
	 * @param map
	 */
	public List<OrderSalesAmountPojo> getUserProfitByMonthCountData(Map<String, String> map);
	/**
	 * 付款审批的相关财务统计报表明细数量
	 * @param map
	 * @return
	 */
	public List<OrderDetetailsSalePojo> orderDetetailsSaleDetailsCount(Map<Object, Object> map);
	/**
	 * 将使用的库存放回到库存库位
	 * @param map
	 * @return
	 * whj 2017-08-22
	 */
	public int reductionInventory(Map<Object, Object> map);

	/**
	 * 商品取消后使用的库存还原
	 * @param map
	 * @return
	 */
	public List<CancelGoodsInventory> searchCancelGoodsInventory(Map<Object, Object> map);
	   /**
	    * 录入广东直发快递号
	    * @Title straightShipnoEntry
	    * @Description TODO
	    * @param map
	    * @return
	    * @return int
	    */
	   public int straightShipnoEntry(Map<Object, Object> map);
	   /**
	    * 线下采购申请明细
	    * @param map
	    * @return
	    */
	public List<OfflinePaymentApplicationPojo> getOfflinePaymentApplication(
            Map<Object, Object> map);
	   /**
	    * 线下采购申请明细数量
	    * @param map
	    * @return
	    */
	public List<OfflinePaymentApplicationPojo> getOfflinePaymentApplicationCount(
            Map<Object, Object> map);
	/**
	 * 无订单匹配采购订单明细
	 * @param map
	 * @return
	 */
	public List<TaoBaoOrderInfo> getNoMatchingOrder(Map<Object, Object> map);
	/**
	 * 无订单匹配采购订单明细数量
	 * @param map
	 * @return
	 */
	public List<TaoBaoOrderInfo> getNoMatchingOrderCount(Map<Object, Object> map);
	/**
	 * 商品取消后使用的库存还原记录数量
	 * @param map
	 * @return
	 */
	public int searchCancelGoodsInventoryCount(Map<Object, Object> map);
	/**
	 * 无订单匹配采购订单明细
	 */
	public List<TaoBaoOrderInfo> getNoStorage(Map<Object, Object> map);
	/**
	 *发货三天未入库订单详情
	 * @param map
	 * @return
	 */
	public List<TaoBaoOrderInfo> getNoStorageDetails(Map<String, String> map);
	/**
	 *发货三天未入库订单详情
	 * @param map
	 * @return
	 */
	public int getNoStorageDetailsCount(Map<String, String> map);
	/**
	 * 无订单匹配采购订单明细数量
	 */
	public List<TaoBaoOrderInfo> getNoStorageCount(Map<Object, Object> map);
	/**
	 * 当月产生库存明细
	 */
	public List<InventoryDetailsPojo> getInventoryAmount(Map<Object, Object> map);
	/**
	 * Importexpress-GA数据统计
	 *
	 */
	public List<DataQueryBean> getOrderQuery(Map<Object, Object> map);
	   /**
	    * 查询建议或确定广东直发的列表
	    */
	 public List<StraightHairPojo> StraightHairList(Map<Object, Object> map);

	   /**
	    * 查询建议或确定广东直发的列表数量
	    */
	 public List<StraightHairPojo> StraightHairListCount(Map<Object, Object> map);
	   /**
	    * 查询损耗库存数据
	    * @Title searchLossInventory
	    * @Description TODO
	    * @return
	    * @return List<LossInventoryPojo>
	    */
	   public List<LossInventoryPojo> searchLossInventory(Map<Object, Object> map);
	   /**
	    * 查询损耗库存数据条数
	    * @Title searchLossInventoryCount
	    * @Description TODO
	    * @return
	    * @return List<LossInventoryPojo>
	    */
	   public List<LossInventoryPojo> searchLossInventoryCount(Map<Object, Object> map);
	/**
	 * 当月产生库存明细数量
	 */
	public List<InventoryDetailsPojo> getInventoryAmountCount(Map<Object, Object> map);
	   /**
	    * 支付失败订单查看
	    * @Title getPayFailureOrder
	    * @Description TODO
	    * @param map
	    * @return
	    * @return List<PayFailureOrderPojo>
	    */
	   public List<PayFailureOrderPojo> getPayFailureOrder(Map<Object, Object> map);
	/**
	 * 客户商品评价管理
	 * @param map
	 * @return
	 */
	public List<ReviewManage> reviewManagerment(Map<String, String> map);
	/**
	 * 客户商品评价管理
	 * @param map
	 * @return
	 */
	public List<ReviewManage> reviewManagermentCount(Map<String, String> map);
	   /**
	    * 支付失败订单数量统计
	    * @Title getPayFailureOrderCount
	    * @Description TODO
	    * @param map
	    * @return
	    * @return List<PayFailureOrderPojo>
	    */
	   public List<PayFailureOrderPojo> getPayFailureOrderCount(Map<Object, Object> map);
	/**
	 * 当月销售库存明细
	 */
	public List<InventoryDetailsPojo> getSaleInventory(Map<Object, Object> map);
	public int setDisplay(Map<String, String> map);
	/**
	 * 当月销售库存明细数量
	 */
	public List<InventoryDetailsPojo> getSaleInventoryCount(
            Map<Object, Object> map);
	/**
	 * 采购订单对应上月销售订单明细
	 */
	public List<TaoBaoOrderInfo> getCancelAmount(Map<Object, Object> map);
	/**
	 * 采购订单对应上月销售订单明细数量
	 */
	public List<TaoBaoOrderInfo> getCancelAmountCount(Map<Object, Object> map);
	/**
	 * 采购订单对应取消销售订单明细
	 */
	public List<TaoBaoOrderInfo> getLastAmount(Map<Object, Object> map);
	/**
	 * 采购订单对应取消销售订单明细数量
	 */
	public List<TaoBaoOrderInfo> getLastAmountCount(Map<Object, Object> map);
	/**
	 * 抓取正常采购订单明细
	 */
	public List<TaoBaoOrderInfo> getGrabNormalAmount(Map<Object, Object> map);
	/**
	 * 抓取正常采购订单明细数量
	 */
	public List<TaoBaoOrderInfo> getGrabNormalAmountCount(Map<Object, Object> map);
	/**
	 * 预估订单运费明细
	 */
	public List<ShippingPackage> getforecastAmount(Map<Object, Object> map);
	/**
	 * 预估订单运费明细数量
	 */
	public List<ShippingPackage> getforecastAmountCount(Map<Object, Object> map);
	/**
	 *  实际支付运费运单明细
	 */
	public List<Shipments> getPayFreight(Map<Object, Object> map);
	/**
	 *  实际支付运费运单明细数量
	 */
	public List<Shipments> getPayFreightCount(Map<Object, Object> map);
	   /**
	    * 修改/添加当月支付宝余额
	    * @param map
	    * @return
	    */
	public int addZfbData(Map<Object, Object> map);
	/**
	 * 根据选择的年月获取相应的支付宝数据
	 * data 日期  2017-04
	 */
	public List<ZfuDate> getZfuDate(@Param("data") String data);
	  /**
	    * 根据类别ID获取类别名称
	    * @param ids 类别id
	    * @return 类别名称
	    * @author 王宏杰 2017-07-27
	    */
	   public String queryUsingRangeForIds(@Param("ids") String ids);
	/**
	 * 批量生成优惠券详情
	 * @param list
	 * @return
	 */
	public int createCouponSubsidiary(List<Map<String, Object>> list);

	/**
	 * 库存删除记录查询
	 * @Title searchGoodsInventoryDeleteInfo
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<Inventory>
	 */
	public List<Inventory> searchGoodsInventoryDeleteInfo(
            Map<Object, Object> map);

	/**
	 * 库存删除数量
	 * @Title searchGoodsInventoryDeleteInfoCount
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<Inventory>
	 */
	public List<Inventory> searchGoodsInventoryDeleteInfoCount(
            Map<Object, Object> map);
	   /**
	    * 查询工厂实际采样的订单情况
	    * @Title searchSampleProduct
	    * @Description TODO
	    * @param map
	    * @return
	    * @return List<Orderinfo>
	    */
	   public List<Orderinfo> searchSampleProduct(Map<Object, Object> map);
	   /**
	    * 查询工厂实际采样的订单数量
	    * @Title searchSampleProductCount
	    * @Description TODO
	    * @param map
	    * @return
	    * @return List<Orderinfo>
	    */
	   public List<Orderinfo> searchSampleProductCount(Map<Object, Object> map);
	/**
	 * 查询库存盘点记录
	 * @Title searchGoodsInventoryUpdateInfo
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<Inventory>
	 */
	public List<Inventory> searchGoodsInventoryUpdateInfo(Map<Object, Object> map);

	/**
	 * 查询库存盘点记录总数
	 * @Title searchGoodsInventoryUpdateInfoCount
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<Inventory>
	 */
	public List<Inventory> searchGoodsInventoryUpdateInfoCount(Map<Object, Object> map);

	public int searchCoupusManagementCount(Map<Object, Object> map);

	/**
	 * 查询优惠券详情
	 */
	public List<CouponSubsidiary> searchCoupusDetails(Map<Object, Object> map);

	/**
	 * 查询优惠券详情记录数
	 * @param map
	 * @return
	 */
	public int searchCoupusDetailsCount(Map<Object, Object> map);

    /**
	    * 查询优惠券信息
	    * @param map
	    * @return
	    */
	public List<Coupons> searchCoupusManagement(Map<Object, Object> map);

    public List<InOutDetailsInfo> getIinOutDetails(Map<String, String> map);

    public List<InOutDetailsInfo> getIinOutDetailsCount(Map<String, String> map);

    public List<Inventory> getIinOutInventory(Map<Object, Object> map);

    public List<Map<String,String>> getPidValidState(@Param("pids") String pids);

    public List<Inventory> getIinOutInventoryCount(Map<Object, Object> map);

    public List<TaoBaoOrderInfo> queryBuyCount(@Param("torderid") String torderid, @Param("opsorderid") String opsorderid);

    public List<AliCategory> searchAliCategory(@Param("type") String type, @Param("cid") String cid);
	/**
	 * 获取库存产品所有类别
	 * @return
	 */
	public List<Inventory> getAllInventory();

	/**
     * 库存商品对应的出入库明细
     * @Title searchInOutDetails
     * @Description TODO
     * @param map
     * @return
     * @return List<StorageOutboundDetailsPojo>
     */
    public List<StorageOutboundDetailsPojo> searchInventoryInOutDetails(Map<Object, Object> map);
    /**
     * 库存商品对应的出入库明细数量
     * @Title searchInOutDetailsCount
     * @Description TODO
     * @param map
     * @return
     * @return List<StorageOutboundDetailsPojo>
     */
    public List<StorageOutboundDetailsPojo> searchInventoryInOutDetailsCount(Map<Object, Object> map);
	/**
	 * 添加未入库采购订单备注
	 */
	public int updateReply(Map<Object, Object> map);
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
	 * 强制入库
	 */
	public int inStorage(Map<Object, Object> map);
	public String getOperationRemark(Map<Object, Object> map);
    public List<String> getNewBarcode();
	/**
	 * 根据ID删除库存数据
	 */
	public int deleteInventory(@Param("id") int id,@Param("dRemark") String dRemark);
    /**
     *盘点库存库位变更
     * @param id
     * @param old_barcode
     * @param new_barcode
     * @return
     */
    public int insertChangeBarcode(@Param("id") int id, @Param("old_barcode") String old_barcode, @Param("new_barcode") String new_barcode);
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
     * 获取采购订单备注信息
     * @param id
     * @return
     */
    public String getTbOrderRemark(@Param("id") String id);

    /**
     * 更新采购更新备注信息
     * @param id
     * @param new_remark
     * @return
     */
    public int addTbOrderRemark(@Param("id") String id, @Param("new_remark") String new_remark);

    public List<OrderProductSource> getSourceValidation(@Param("buyer") String buyer, @Param("account") String account, @Param("page") int page, @Param("startdate") String startdate, @Param("enddate") String enddate);

    public List<TaoBaoOrderInfo> getSourceValidationForTb(@Param("buyer") String buyer, @Param("account") String account, @Param("page") int page, @Param("startdate") String startdate, @Param("enddate") String enddate, @Param("isTrack") String isTrack);

	/**
	 * 根据1688订单的itemid和下单日期匹配货源
	 * @param buyer
	 * @param time
	 * @return
	 */
    public List<OrderProductSource> getOrderProductSource(@Param("itemid") String buyer, @Param("time") String time);
    public int getSourceValidationForTbCount(@Param("buyer") String buyer, @Param("account") String account, @Param("page") int page, @Param("startdate") String startdate, @Param("enddate") String enddate, @Param("isTrack") String isTrack);

    public int getCount(@Param("buyer") String buyer, @Param("startdate") String startdate, @Param("enddate") String enddate);
	/**
	 * 列出所有交易
	 * @param map
	 * @return
	 */
	public List<TaoBaoOrderInfo> getAllBuyerOrderInfo(Map<String, String> map);

	/**
	 * 列出所有交易总数
	 * @param map
	 * @return
	 */
	public List<TaoBaoOrderInfo> getAllBuyerOrderInfoCount(Map<String, String> map);
    public String getBuyerAmountByMouth(@Param("startdate") String startdate, @Param("enddate") String enddate, @Param("account") String account);
	/**
	 * 查询1688订单没有入库记录信息
	 * @return
	 */
	public List<TaoBaoOrderInfo> getTaoBaoNoInspection(Map<String, String> map);

	/**
	 * 查询实际采购未在确认采购中的商品验货状态
	 * @param orderid
	 * @param itmeid
	 * @return
	 */
	public String getInspectionResult(@Param("orderid") String orderid, @Param("itemid") String itmeid);
	/**
	 * 查看1688订单是否入库记录
	 * @param orderid
	 * @return
	 */
	public int getIdState(@Param("orderid") String orderid);
	/**
	 * 查询1688订单没有入库记录数量
	 * @return
	 */
	public List<TaoBaoOrderInfo> getTaoBaoNoInspectionCount(Map<String, String> map);
    public int getCounts(@Param("account") String account, @Param("startdate") String startdate, @Param("enddate") String enddate);

    public int getSourceValidationCount(@Param("buyer") String buyer, @Param("account") String account, @Param("page") int page, @Param("startdate") String startdate, @Param("enddate") String enddate);

    public int isExitBarcode(@Param("barcode") String barcode);
	/**
	 * 根据ID获取库存
	 */
	public Inventory queryInById(@Param("id") String id);
    /**
     * 查询订单商品详情信息
     * @param map
     * @return
     */
    public OrderDetailsBean findOrderDetails(Map<Object, Object> map);
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
	 * 分配给采购的商品实际没有采购
	 * @param map
	 * @return
	 */
	public List<OrderProductSource> getNopurchaseDistribution(Map<String, String> map);
	/**
	 * 分配给采购的商品实际没有采购数量
	 * @param map
	 * @return
	 */
	public List<OrderProductSource> getNopurchaseDistributionCount(Map<String, String> map);
    /**
     * 手动录入库存
     * @param map
     * @return
     */
    public int inventoryEntry(Map<Object, Object> map);
	/**
	 * 查询一个商品采购了多次
	 * @return
	 */
	public List<OrderProductSource> getOneMathchMoreOrderInfo(Map<String, String> map);
	/**
	 * 查询一个商品采购了多次的数量
	 * @return
	 */
	public List<OrderProductSource> getOneMathchMoreOrderInfoCount(Map<String, String> map);

	/**
	 * 根据入库记录的运单号查询1688订单号
	 * @param shipnos
	 * @return
	 */
	public String getBuyOrderId(@Param("shipnos") String shipnos);
    public int updateIsStockFlag(@Param("goods_pid") String goods_pid);

    public int updateIsStockFlag1(@Param("goods_pid") String goods_pid);

    public int updateIsStockFlag2(@Param("goods_pid") String goods_pid);

	   /**
	    * 更新库存
	    * @param map
	    * @return
	    */
	public int updateInventory(Map<Object, Object> map);


    public Inventory queryInId(@Param("old_sku") String old_sku, @Param("goods_pid") String goods_pid, @Param("old_barcode") String old_barcode, @Param("car_urlMD5") String car_urlMD5, @Param("flag") String flag);

    public int updateSources(@Param("flag") String flag, @Param("old_sku") String old_sku, @Param("goods_pid") String goods_pid, @Param("car_urlMD5") String car_urlMD5, @Param("new_barcode") String new_barcode, @Param("old_barcode") String old_barcode, @Param("new_remaining") int new_remaining, @Param("old_remaining") int old_remaining, @Param("remark") String remark, @Param("new_inventory_amount") double new_inventory_amount);

    public int updateSourcesLog(@Param("in_id") int in_id, @Param("name") String name, @Param("old_sku") String old_sku, @Param("old_url") String old_url, @Param("new_barcode") String new_barcode, @Param("old_barcode") String old_barcode, @Param("new_remaining") int new_remaining, @Param("old_remaining") int old_remaining, @Param("remark") String remark);
 }