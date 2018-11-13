package com.cbt.processes.service;

import com.cbt.bean.*;

import java.util.List;
import java.util.Map;

public interface IOrderServer {
	/**
	 * 获取订单信息
	 * 
	 * @param userId，state,startpage
	 * 		用户ID,订单状态,页码
	 */
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage);
	
	//zlw add start
	/**
	 * 获取商品信息
	 * 
	 * @param userId，state,startpage,orderNo
	 * 		用户ID,订单状态,页码,订单号
	 */
	public List<OrderDetailsBean> getProductDetail(int userID, int state, int startpage, String orderNo);
	//zlw add end
	
	/**
	 * 获取订单信息的总数量
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int getOrderdNumber(int userID, int state);
	
	/**
	 * 显示个人中心订单的数量
	 * 
	 * @param userId
	 * 		用户ID
	 */
	public int[] getOrdersIndividual(int userid);

	/**
	 * 获取正在确认价格中的订单
	 * @param userId
	 * @return
	 */
	public Map<String, List<String>> getConfirmThePriceOf(int userId);
	/**
	 * 获取正在确认价格的订单
	 * @param orderNo
	 * @return
	 */
	public Map<String, Object> getCtpoOrderInfo(String orderNo, int userId);

	/**
	 * 删除降价优惠的商品
	 * @param userId
	 * @param goodsDataId
	 * @param goodsCarId
	 * @return
	 */
	public int updatePriceReductionOffer(int userId, int goodsDataId, int goodsCarId);

	/**
	 * 删除购物车无降价优惠的商品
	 * @param userId
	 * @return
	 */
	public int updateGoosCar(int userId);


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
	 * 查询确认价格中的有变动的数量和状态
	 * @param cancel_obj 取消订单对象
	 */
	public Map<String, String> getOrderChangeState(String[] orderNo);

	/**
	 * ylm
	 * 根据商品ID查询总支付费用
	 * @param cancel_obj 取消订单对象
	 */
	public float getTotalPrice(String goodsids);

	public void updateOrderState(int userid, String orderid);

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

	/**
	 * 取得降价优惠产品
	 *
	 * @param userId
	 * 		用户ID
	 */
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
	 * 查询订单状态
	 */
	public int getOrderState(String orderNo);
	 
}
