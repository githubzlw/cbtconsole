package com.cbt.processes.dao;

import com.cbt.bean.*;

import java.util.List;
import java.util.Map;


/**
 * @author ylm
 * 订单操作
 *
 */
public interface IOrderuserDao {
	
	/**
	 * 获取订单信息
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage, int endpage);

	/**
	 * 获取商品信息
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public List<OrderDetailsBean> getProductDetail(int userID, int state, int startpage, int endpage, String orderNo);


	/**
	 * 获取订单信息的总数量
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int getOrderdNumber(int userID, int state);

	/**
	 * 获取历史订单数量
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int getOrderNumber(int userID);
	/**
	 * 获取订单详情
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 *//*
	public List<SpiderBean> getOrdersDetails(String orderNo);*/

	/**
	 * ylm
	 * 显示个人中心订单的数量
	 *
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public List<Integer[]> getOrdersIndividual(int userid);

	/**
	 * 增加货代相关信息ylm
	 *
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int addForwarder(Forwarder forwarder);

	/**
	 * 查询已到仓库的商品数量
	 *
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int warehouse(int userid);

	/**
	 * 获取货代相关信息ylm
	 *
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public Forwarder getForwarder(String orderNo);

	/**
	 * 获取正在确认价格中的订单名称
	 * @param userId
	 * @return
	 */
	public Map<String, List<String>> getConfirmThePriceOf(int userId);

	/**
	 * 获取 正在确认价格中的订单的详情
	 * @param orderNo
	 * @return
	 */
	public Map<String, Object> getCtpoOrderInfo(String orderNo, int userId);

	/**
	 * ylm
	 * 保存用户对订单评价信息
	 * 用户评价
	 */
	public int saveEvaluate(Evaluate evaluate);

	/**
	 * ylm
	 * 修改订单表状态
	 */
	public int upOrderState(String orderNo, int state);

	/**
	 * ylm
	 * 取消订单
	 * @param cancel_obj 取消订单对象
	 */
	public int cancelOrder(String orderNo, int cancel_obj);

	/**
	 * ylm
	 * 查询该订单号中的订单详情中是否存在数据
	 * @param cancel_obj 取消订单对象
	 */
	public int getDelOrder(String orderNo);

	/**
	 * ylm
	 * 查询确认价格中的有变动的数量和状态
	 * @param cancel_obj 取消订单对象
	 */
	public Map<String, String> getOrderChangeState(String[] orderNo);
	/**
	 * ylm
	 * 查询确认价格中的有变动的数量和状态
	 * @param cancel_obj 取消订单对象
	 */
	public Map<String, String> getOrderChangeState1(String[] orderNo);

	/**
	 * ylm
	 * 根据商品ID查询总支付费用
	 * @param cancel_obj 取消订单对象
	 */
	public float getTotalPrice(String goodsids);

	/*确认付款后修改订单和订单详情状态
	 * wanyang*/
	public void updateOrderState(int userid, String orderid);

	/**
	 * ylm
	 * 保存预订单的问题
	 * @param
	 */
	public int saveQuestions(String orderid, String questions);

	/**
	 * 获取订单信息
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public AdvanceOrderBean getOrders(String orderNo);

	/**
	 * 查询购物车商品
	 *
	 * @param userId
	 * 		用户ID
	 */
	public  List<SpiderBean> getSpiders(String orderNo);

	/**
	 * 取得替换商品信息
	 *
	 * @param orderNo
	 * 		订单no
	 */
	public List<ProductChangeBean> getProductChangeInfo(String orderNo, String flag);

	public List<ProductChangeBean> getPriceReductionOffer(String userId);

	/**
	 * ylm
	 * 修改预订单内容表
	 * 订单号，回答，运费，关税
	 * @param
	 */
	public int upQuestions(String orderid, String questions);

	/**
	 * ylm
	 * 查询预订单内容表是否存在
	 *
	 * @param
	 */
	public String getAdvance(String orderid);

	/**
	 * ylm
	 * 添加预订单内容表
	 * @param
	 */
	public int addAdvance(String orderid, String questions);

	public Map<String, Object> getDelOrderByOrderNoAndStatus(String orderNo, int goodId, int status);

	/**
	 * ylm
	 * 修改订单表的状态和已采购数量和总采购数量
	 *
	 * @param
	 */
	public int upOrderPurchase(int purchase_state, String orderno, int state);

	/**
	 * ylm
	 * 查询订单状态
	 */
	public int getOrderState(String orderNo);
	/**
	 * ylm
	 * 获取订单优惠变更
	 */
	/*public Object[] getOrderDiscount(String orderNo);*/
	/*个人中心获取订单详情
	 * wanyang
	 * 2015-11-11*/
	public List<OrderDetailsBean> getIndividualOrdersDetails(String orderNo);

	/**
	 * 更新product_cost，remaining_price
	 * @param orderNo
	 * @return
	 */
	public int updateOrderInfopr(String orderNo, int userId, String productCost, String remainingPrice);

	/**
	 * 更新product_cost，remaining_price
	 * @param orderNo
	 * @return
	 */
	public int updateOrderDetail(String orderNo, int userId, String goodsId, String goodSprice, String name, int goodsCarId, int id, String remark, String goodsUrl, String goodsImg, String goodsType);

	/**
	 * 更新changegooddata:changeFlag
	 * @param orderNo
	 * @return
	 */
	public int updateChangeGood(String orderNo, String goodId);

	public int saveGoodsCar(String goodsUrl, String goodsName, String goodsImg, String goodsType, int goodsId, String remark);

	/**
	 * 更新order_product_source:goods_p_url,purchase_state
	 * @param orderNo,goodId
	 * @return
	 */
	public int updateProductSource(String goodsid, String goodsdataid, String goodsUrl, String goodsImgUrl, String goodsPrice, String name, String orderNo, int goodCarKey);
	
	
}
