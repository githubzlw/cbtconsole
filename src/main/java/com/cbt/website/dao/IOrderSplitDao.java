package com.cbt.website.dao;

import com.cbt.bean.*;
import com.cbt.pojo.Admuser;
import com.cbt.warehouse.pojo.Dropshiporder;

import java.util.List;

public interface IOrderSplitDao {

	/**
	 * 获取订单详情和是否已有采购链接 ylm
	 * 
	 * @param orderBean原订单信息
	 * @param orderBean2拆出来的新订单信息
	 * @param odId_last拆出来的没有填写数量详情ID
	 * @param selSplitArr填写了数量的拆出来的详情ID
	 * @param 拆出来的支付信息
	 * @param state1分批，0退款另一个
	 * @param order_ac订单退还运费抵扣
	 * @param balance_pay订单退款至余额的金额
	 * @param gId_last拆出来的购物车ID
	 * @param splitGoodsId前台输入数量拆出来的购物车ID
	 */
	public int splitOrder(OrderBean orderBean, OrderBean orderBean2, String odId_last, String selSplitArr, Payment pay,
                          int state, double order_ac, double balance_pay, String gId_last, String adminUserName, String pay_time,
                          List<OrderDetailsBean> updateOrderDetaildList, List<OrderDetailsBean> insertOrderDetaildList,
                          String splitGoodsId);

	public boolean checkTestOrder(String odid);

	/**
	 * 获取订单详情和是否已有采购链接 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public List<OrderDetailsBean> getOrdersDetails_split(String orderNo);

	/**
	 * 获取订单详情 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public List<Object[]> getOrdersDetails(String[] orderNo);

	/**
	 * 获取订单对象 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public List<OrderBean> getOrders(String[] orderno);

	/**
	 * 获取订单对象 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public OrderBean getOrders(String orderno);
	/**
	 * 订单取消商品进去库存
	 * @Title addInventory
	 * @Description TODO
	 * @param odid
	 * @return 返回影响库存表的记录数
	 * @author 王宏杰 2018-05-04
	 * @return int
	 */
	public int addInventory(String odid, String remark);

	/**
	 * 添加拆分日志，记录原始订单 ylm
	 *
	 * @param userId
	 *            用户ID,订单状态
	 */
	public void addOrderInfoLog(String orderno, String orderinfo);

	/**
	 * 添加发送邮件的错误信息 ylm
	 *
	 * @param
	 */
	public void addMessage_error(String email, String error, String info);

	/**
	 * 获取发送邮件的错误信息 ylm
	 *
	 * @param
	 */
	public List<Object[]> getMessage_error(String time, String endtime, int page, int endpage);

	/**
	 * 获取发送邮件的错误数量 ylm
	 *
	 * @param
	 */
	public int getMessage_error(String time, String endtime);

	/**
	 * 获取汇率值 ylm
	 *
	 * @param userId用户ID
	 * @param currency订单货币单位
	 */
	// public double getExchangeRate(int userId, String currency);
	/**
	 * 拆分订单通过订单号和明细ids获取order_product_source zsl
	 */
	public List<OrderProductSource> getOrderProductSource(String orderid, String[] orderdtlIds);

	/**
	 * 通过order_detail的id获取order_detail采购状态 odids格式："111,222,333"
	 */
	public List<Integer> getOrderDetailPurchaseByOdids(String odids);

	/**
	 * 根据orderNo,goodsid查询出需要拆分的订单(DropShip拆单)
	 *
	 * @param orderNo
	 * @param goodsid
	 * @return
	 */
	public OrderDetailsBean getDropShipOrdersDetails(String orderNo, String goodsid);

	/**
	 * 根据orderNo查询订单的地址信息(DropShip拆单)
	 *
	 * @param orderNo
	 * @return
	 */
	public OrderAddress getOrderAddressByOrderNo(String orderNo);

	/**
	 * 添加订单地址信息(DropShip拆单)
	 *
	 * @param orderAddress
	 * @return
	 */
	public void addOrderAddress(OrderAddress orderAddress);

	/**
	 * 更新OrderDetails dropshipid(DropShip拆单)
	 *
	 * @param orderDetailsBean
	 */
	public void updateOrderDetails(OrderDetailsBean orderDetailsBean);

	/**
	 * 根据orderNo 获取DropShip Order信息
	 *
	 * @param parentOrderNo
	 * @return
	 */
	public List<Dropshiporder> getDropShipOrderList(String parentOrderNo);

	/**
	 * 新增dropshiporder记录(DropShip拆单)
	 *
	 * @param dropshiporder
	 */
	public void insertDropShiporder(Dropshiporder dropshiporder);

	/**
	 * 更新dropshiporder记录(DropShip拆单)
	 *
	 * @param dropshiporder
	 */
	public void updateDropShiporder(Dropshiporder dropshiporder);

	/**
	 * 根据OrderNo获取订单列表信息(用来判断Drop Ship订单拆分是否只有一个)
	 *
	 * @param orderNo
	 * @return
	 */
	public List<OrderDetailsBean> getOrdersDetailsList(String orderNo, int noCancel);

	/**
	 * 更新DropShipOrder 表的状态
	 *
	 * @param dropShipOrderId
	 * @return
	 */
	public int updateDropShipOrderStates(String dropShipOrderId);

	/**
	 * 查询 商品数量
	 *
	 * @param orderid
	 * @return
	 */
	public int getOrderdetailYouorder(String orderId);

	/**
	 * 使用存储过程进行拆单
	 *
	 * @param userid
	 *            :用户id
	 * @param orderNoOld
	 *            :老订单号
	 * @param orderNoNew
	 *            : 新订单号
	 * @param updateGoodsId
	 *            ：拆分的goodsid
	 * @param updateGoodsNum
	 *            : 拆分的goods数量
	 * @return
	 */
	public boolean orderSplitByOrderNo(int userId, String orderNoOld, String orderNoNew, int updateGoodsId,
                                       int updateGoodsNum);

	/**
	 * 插入新订单的支付记录
	 *
	 * @param userid
	 *            :用户id
	 * @param nwOrderNo
	 * @param oldOrderNo
	 * @return
	 */
	public int insertIntoPayment(int userId, String nwOrderNo, String oldOrderNo);

	/**
	 * 取消新订单
	 *
	 * @param userid
	 *            :用户id
	 * @param nwOrderNo
	 * @return
	 */
	public int cancelNewOrder(int userId, String nwOrderNo);

	/**
	 * 查询本地订单信息
	 *
	 * @param orderNo
	 * @return
	 */
	public OrderBean getOrderInfo(String orderNo);

	/**
	 * 获取本地订单详情数据
	 *
	 * @param orderNo
	 * @return
	 */
	public List<Object[]> queryLocalOrderDetails(String orderNo);

	/**
	 * 使用存储过程，判断订单状态和更新订单状态
	 *
	 * @param oldOrder
	 *            : 原订单
	 * @param newOrder
	 *            : 新订单
	 */
	public boolean checkAndUpdateOrderState(String oldOrder, String newOrder);

	/**
	 * 更新新订单商品的入库信息
	 *
	 * @param oldOrder
	 *            : 原订单
	 * @param newOrder
	 *            : 新订单
	 * @param goodsIds
	 *            : 新订单的商品id
	 * @return
	 */
	public int updateWarehouseInfo(String oldOrder, String newOrder, List<Integer> goodsIds);

	/**
	 * 拆单取消后取消的商品如果有使用库存则还原库存
	 *
	 * @param ids
	 */
	public void cancelInventory(String[] ids);

	/**
	 * 根据订单号获取订单详情信息
	 *
	 * @param orderNo
	 * @return
	 */
	public List<Object[]> getOrderDetails(String[] orderNos);

	/**
	 * 拆单之前进行订单和支付信息保存
	 *
	 * @param orderNo
	 * @param admuser
	 * @param flag
	 *            :新老订单标识 0老订单 1新订单
	 */
	public boolean addOrderInfoAndPaymentLog(String orderNo, Admuser admuser, int flag);

	/**
	 * 使用正常程序拆单
	 *
	 * @param orderBean
	 *            :订单信息
	 * @param orderNoOld
	 *            :老订单号
	 * @param orderNoNew
	 *            : 新订单号
	 * @param goodsIds
	 *            ：拆分的goodsIds
	 * @return
	 */
	public boolean orderSplitNomal(OrderBean orderBean, String orderNoOld, String orderNoNew, List<Integer> goodsIds);

	/**
	 * 保存预期结果到日志中
	 *
	 * @param orderBeans
	 * @param admuser
	 * @return
	 */
	public boolean saveOrderInfoLogByList(List<OrderBean> orderBeans, Admuser admuser);

	/**
	 * 新的程序拆单函数
	 * @param orderBeanTemp
	 * @param odbeanNew
	 * @param nwOrderDetails
	 * @return
	 */
	public boolean newOrderSplitFun(OrderBean orderBeanTemp, OrderBean odbeanNew, List<OrderDetailsBean> nwOrderDetails,
									String state, int isSplitNum);

	/**
	 * 根据订单哈查询线上存在通知消息
	 * @param orderNo
	 * @return
	 */
	int isExistsMessageByOrderNo(String orderNo);

	/**
	 * 更新新老订单的重量，运费、商品总价和支付金额信息
	 * @param oldDropShopId
	 * @param newDropShopId
	 * @param parentOrderNo
	 * @return
	 */
	boolean updateDropShipDate(String oldDropShopId, String newDropShopId, String parentOrderNo, float orderAc, float extraFreight);

	/**
	 * 更新商品备注信息
	 * @param oldOrder
	 * @param newOrder
	 * @param odIds
	 * @return
	 */
	int updateGoodsCommunicationInfo(String oldOrder, String newOrder, List<Integer> odIds);

}
