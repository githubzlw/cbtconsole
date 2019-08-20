package com.cbt.report.service;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.DataQueryBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.OrderProductSource;
import com.cbt.bean.Orderinfo;
import com.cbt.pojo.*;
import com.cbt.warehouse.pojo.Shipments;
import com.cbt.warehouse.pojo.ShippingPackage;
import com.cbt.website.bean.InventoryData;

import java.util.List;
import java.util.Map;

public interface TaobaoOrderService {
   public List<TaoBaoOrderInfo> getTaoBaoOrderList(String orderstatus, String orderSource, String orderdate, String procurementAccount, int start, int end, int DeliveryDate, String orderid, String isCompany, String myorderid, String startdate, String enddate, String paystartdate, String payenddate, int page);

   public List<TaoBaoOrderInfo> getAllCount(String orderstatus, String orderSource, String orderdate, String procurementAccount, int start, int end, int DeliveryDate, String orderid, String isCompany, String myorderid, String startdate, String paystartdate, String payenddate, String enddate);

   public List<TaoBaoOrderInfo> getTaoBaoOrderDetails(String orderid, String bforderid);

   public List isStorage(String orderid);

   public List<TaoBaoOrderInfo> queryBuyCount(String torderid, String opsorderid);

   public List<TaoBaoOrderInfo> associatedOrder(String orderid);

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
   public int enable(int id);

   /**
    * 查询优惠券主表状态
    * @param subsidiary_id
    * @return coupons_id
    * 2017-06-27 王宏杰
    */
   public int queryCouponsState(int subsidiary_id);

   /**
    * 批量启用停用优惠券详情
    * @param map
    * @return
    * 2017-06-28 王宏杰
    */
   public int AllenableDetails(Map<Object, Object> map);

   /**
    * 保存修改的优惠券详情
    * @param map
    * @return
    * 2017-06-27 王宏杰
    */
   public int addCoupusDetailsView(Map<Object, Object> map);

   /**
    * 批量启用、停用优惠券
    * @param map
    * @return
    * 2017-06-27 王宏杰
    */
   public int Allenable(Map<Object, Object> map);
   /**
    * 线下采购付款审核
    * @param map
    * @return
    */
   public int ThroughReview(Map<Object, Object> map);

   public String getUserName(int userid);

   /**
    * 批量更改优惠券详情状态
    * @param ids
    * @return
    * 2017-07-27 whj
    */
   public int enableForSubsidiary(int ids);

   public List<InOutDetailsInfo> getIinOutDetails(Map<String, String> map);

   public List<InOutDetailsInfo> getIinOutDetailsCount(Map<String, String> map);
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
    * 查询优惠券信息
    * @param map
    * @return
    */
   public List<Coupons> searchCoupusManagement(Map<Object, Object> map);

   /**
    * 查询优惠券详情
    * @param map
    * @return
    */
   public List<CouponSubsidiary> searchCoupusDetails(Map<Object, Object> map);


   /**
    * 获取优惠券详情明细
    * @param map
    * @return
    * 2017-06-27 王宏杰
    */
   public List<CouponSubsidiary> searchCoupusDetailsView(Map<Object, Object> map);

   /**
    * 根据选择的年月获取相应的支付宝数据
    * @param data
    * @return
    */
   public List<ZfuDate> getZfuDate(String data);

   /**
    * 修改/添加当月支付宝余额
    * @param map
    * @return
    */
   public int addZfbData(Map<Object, Object> map);

   /**
    * 将使用的库存放回到库存库位
    * @param map
    * @return
    */
   public int reductionInventory(Map<Object, Object> map);

   /**
    * 月统计采购对账报表
    * @param map
    * @return
    */
   public List<BuyReconciliationPojo> buyReconciliationReport(Map<Object, Object> map, String type);
   /**
    * 订单页面的销售额统计报表
    * @param map
    * @return
    */
   public List<OrderSalesAmountPojo> orderSalesAmount(Map<Object, Object> map);

   /**
    * 异步获取利润汇总数据
    * @param map
    * @return
    */
   public List<OrderSalesAmountPojo> getProfitSummaryData(Map<String, String> map);

   /**
    * 统计用户月利润数据
    * @param map
    * @return
    */
   public List<OrderSalesAmountPojo> getUserProfitByMonth(Map<String, String> map);

   /**
    * 删除当月用户利润数据
    * @param time
    * @return
    */
   public int deleteUserProfitByMonth(String time);

   /**
    * 用户月利润数量总数
    * @param map
    * @return
    */
   public List<OrderSalesAmountPojo> getUserProfitByMonthCount(Map<String, String> map);
   /**
    * 付款审批的相关财务统计报表
    * @param map
    * @return
    */
   public List<OrderDetetailsSalePojo> orderDetetailsSale(Map<Object, Object> map);

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
    * 付款审批的相关财务统计明细报表数量
    * @param map
    * @return
    */
   public List<OrderDetetailsSalePojo> orderDetetailsSaleDetailsCount(Map<Object, Object> map);

   /**
    * 商品取消后使用的库存还原
    * @param map
    * @return
    * whj 2017-08-22
    */
   public List<CancelGoodsInventory> searchCancelGoodsInventory(Map<Object, Object> map);
   /**
    * 线下采购申请明细
    * @param map
    * @return
    */
   public List<OfflinePaymentApplicationPojo> getOfflinePaymentApplication(Map<Object, Object> map);
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
    * 线下采购申请明细数量
    * @param map
    * @return
    */
   public List<OfflinePaymentApplicationPojo> getOfflinePaymentApplicationCount(Map<Object, Object> map);

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
    * 未入库采购订单明细
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getNoStorage(Map<Object, Object> map);

   /**
    * 采购订单详情查询
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getTbOrderDetails(Map<String,String> map);
   /**
    * 采购订单详情查询
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getTbOrderDetailsCount(Map<String,String> map);

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
    * 未入库采购订单明细数量
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getNoStorageCount(Map<Object, Object> map);
   /**
    * 当月产生库存明细
    * @param map
    * @return
    */
   public List<InventoryDetailsPojo> getInventoryAmount(Map<Object, Object> map);
   /**
    * Importexpress-GA数据统计
    * @Title getOrderQuery
    * @Description TODO
    * @param map
    * @return
    * @return List<DataQueryBean>
    */
   public List<DataQueryBean> getOrderQuery(Map<Object, Object> map);
   /**
    * 查询建议或确定广东直发的列表
    * @Title StraightHairList
    * @Description TODO
    * @param map
    * @return
    * @return List<StraightHairPojo>
    */
   public List<StraightHairPojo> StraightHairList(Map<Object, Object> map);

   /**
    *采购订单/销售订单匹配查询
    * @param map
    * @return
    */
   public List<StraightHairPojo> getSaleBuyInfo(Map<String, String> map);
   /**
    *采购订单/销售订单匹配查询
    * @param map
    * @return
    */
   public List<StraightHairPojo> getSaleBuyInfoCount(Map<String, String> map);
   /**
    * 查询损耗库存数据
    * @Title searchLossInventory
    * @Description TODO
    * @return
    * @return List<LossInventoryPojo>
    */
   public List<LossInventoryPojo> searchLossInventory(Map<Object, Object> map);
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
    * 查询损耗库存数据条数
    * @Title searchLossInventoryCount
    * @Description TODO
    * @return
    * @return List<LossInventoryPojo>
    */
   public List<LossInventoryPojo> searchLossInventoryCount(Map<Object, Object> map);
   /**
    * 查询建议或确定广东直发的列表数量
    * @Title StraightHairListCount
    * @Description TODO
    * @param map
    * @return
    * @return List<StraightHairPojo>
    */
   public List<StraightHairPojo> StraightHairListCount(Map<Object, Object> map);
   /**
    *当月产生库存明细数量
    * @param map
    * @return
    */
   public List<InventoryDetailsPojo> getInventoryAmountCount(Map<Object, Object> map);
   /**
    * 当月销售库存明细
    * @param map
    * @return
    */
   public List<InventoryDetailsPojo> getSaleInventory(Map<Object, Object> map);
   /**
    *当月销售库存明细数量
    * @param map
    * @return
    */
   public List<InventoryDetailsPojo> getSaleInventoryCount(Map<Object, Object> map);
   public int setDisplay(Map<String, String> map);
   /**
    * 采购订单对应上月销售订单明细
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getLastAmount(Map<Object, Object> map);

   /**
    * 采购订单对应上月销售订单明细数量
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getLastAmountCount(Map<Object, Object> map);
   /**
    * 抓取正常采购订单明细
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getGrabNormalAmount(Map<Object, Object> map);

   /**
    * 抓取正常采购订单明细数量
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getGrabNormalAmountCount(Map<Object, Object> map);
   /**
    * 采购订单对应取消销售订单明细
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getCancelAmount(Map<Object, Object> map);
   /**
    * 采购订单对应取消销售订单明细数量
    * @param map
    * @return
    */
   public List<TaoBaoOrderInfo> getCancelAmountCount(Map<Object, Object> map);
   /**
    * 预估订单运费明细
    * @param map
    * @return
    */
   public List<ShippingPackage> getforecastAmount(Map<Object, Object> map);
   /**
    * 预估订单运费明细数量
    * @param map
    * @return
    */
   public List<ShippingPackage> getforecastAmountCount(Map<Object, Object> map);
   /**
    * 实际支付运费运单明细
    * @param map
    * @return
    */
   public List<Shipments> getPayFreight(Map<Object, Object> map);
   /**
    * 实际支付运费运单明细数量
    * @param map
    * @return
    */
   public List<Shipments> getPayFreightCount(Map<Object, Object> map);

   /**
    * 查询商品取消后使用的库存还原记录数量
    * @param map
    * @return
    * whj 2017-08-22
    */
   public int searchCancelGoodsInventoryCount(Map<Object, Object> map);

   /**
    * 根据类别ID获取类别名称
    * @param ids 类别id
    * @return 类别名称
    * @author 王宏杰 2017-07-27
    */
   public String queryUsingRangeForIds(String ids);


   public int searchCoupusDetailsCount(Map<Object, Object> map);

   public int searchCoupusManagementCount(Map<Object, Object> map);

   /**
    * 查询当天最后生成的批次
    * time 当天日期格式  如：20170622
    * @return
    */
   public String queryOldBatch(String time);

   /**
    * 创建优惠券
    * @param map
    * @return
    * 2017-06-26 whj
    */
   public int createCoupons(Map<Object, Object> map);

   /**
    * 删除优惠券
    * @param id
    * @return
    */
   public int delCoupons(int id);

   /**
    * 查询优惠券可使用的类别
    * @return
    */
   public List<AliCategory> queryUsingRange();

   public int getCouponsId(String batch);

   /**
    * 批量生成优惠券详情
    * @param list
    * @return
    */
   public int createCouponSubsidiary(List<Map<String, Object>> list);

   /**
    * 获取库存产品所有类别
    * @return
    */
   public List<InventoryData> getAllInventory();

   public List<Inventory> searchGoodsInventoryDeleteInfo(Map<Object, Object> map);

   public List<Inventory> searchGoodsInventoryDeleteInfoCount(Map<Object, Object> map);
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
    * 查询库存盘点记录总数
    * @Title searchGoodsInventoryUpdateInfo
    * @Description TODO
    * @param map
    * @return
    * @return List<Inventory>
    */
   public List<Inventory> searchGoodsInventoryUpdateInfoCount(Map<Object, Object> map);


   public List<OrderProductSource> getSourceValidation(String buyer, String account, int page, String startdate, String enddate);

   /**
    * 获取采购订单备注信息
    * @param id
    * @return
    */
   public String getTbOrderRemark(String id);

   /**
    * 更新采购订单备注信息
    * @param id
    * @param new_remark
    * @return
    */
   public int addTbOrderRemark(String id, String new_remark);

   public List<TaoBaoOrderInfo> getSourceValidationForTb(String buyer, String account, int page, String startdate, String enddate, String isTrack);

   public int getSourceValidationForTbCount(String buyer, String account, int page, String startdate, String enddate, String isTrack);

   public String getBuyerAmountByMouth(String startdate, String enddate, String account);

   /**
    * 当月实际采购未在确认采购中
    * @return
    */
   public List<TaoBaoOrderInfo> getTaoBaoNoInspection(Map<String, String> map);
   /**
    * 查询1688订单没有入库记录数量
    * @return
    */
   public List<TaoBaoOrderInfo> getTaoBaoNoInspectionCount(Map<String, String> map);

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
   public int getCount(String buyer, String startdate, String enddate);

   public int getCounts(String account, String startdate, String enddate);

   public int getSourceValidationCount(String buyer, String account, int page, String startdate, String enddate);

   public List<String> getNewBarcode();

   /**
    * 更新库存
    * @param map
    * @return
    */
   public int updateInventory(Map<Object, Object> map);
   /**
    * 添加未入库采购订单备注
    * @param map
    * @return
    */
   public int updateReply(Map<Object, Object> map);

   /**
    * 强制入库
    * @param map
    * @return
    */
   public int inStorage(Map<Object, Object> map);

   public String getOperationRemark(Map<Object, Object> map);

}
