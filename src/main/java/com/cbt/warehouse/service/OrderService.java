package com.cbt.warehouse.service;

import com.cbt.bean.*;
import com.cbt.pojo.page.Page;
import com.cbt.warehouse.pojo.ClassDiscount;
import com.cbt.warehouse.pojo.IdRelationTable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface OrderService {
	
	/**
	 * 获取订单信息
	 * 
	 * @param userId，state,startpage
	 * 		用户ID,订单状态,页码
	 */
	public List<OrderBean> getOrders(int userID, int state, int startpage, String orderNo, String timeFrom, String timeTo);

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
	public int getOrderdNumber(int userID, int state, String orderNo, String timeFrom, String timeTo);

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
	 * 保存确定价格商品的咨询内容
	 * @param goodId
	 * @param orderNo
	 * @param zixun
	 * @return
	 */
	public String savectpoOrderZiXun(int goodId, String orderNo, String zixun);
	/**
	 * 删除订单内的商品
	 * @param orderNo
	 * @param goodId
	 * @return {删除标志，订单状态}
	 */
	public Integer[] deleteCtpoOrderGoods(String orderNo, int goodId, int userid, int purchase_state, List<ClassDiscount> cdList, HttpServletResponse response);

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
	 * 重新提交订单
	 * @param orderNo
	 * @param orderInfo
	 * @return
	 */
	public String updateOrderInfo(int userId, String value, int type, int goodsid, String orderNo);


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
	 * 修改订单表返单优惠折扣
	 */
	public int upCouponPrice(String orderNo);

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

	/**
	 * 根据订单号查询订单金额
	 * @param orderNo
	 * @return
	 */
	public Map<String, Object> getTotalPriceFormOrderNo(String orderNo);

	public void updateOrderState(int userid, String orderid);

	/**
	 * 获取订单信息
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public AdvanceOrderBean getOrders(String orderNo);



	public OrderBean getOrderByorderNo(String orderNo);



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


/**********原com.cbt.pay.dao下************************************************/
	/**
	 * 获取订单详细信息 wy
	 *
	 */
	public void add(List<OrderDetailsBean> orderdetails);
	public List<OrderDetailsBean> getOrder(int userid, String orderid);
	public int addAddress(Address add);
	public List<Address> getUserAddr(int userid);
	public int existUserAddr(int userid);
	/**
	 * 获取订单信息
	 *
	 * @param userId，state,startpage
	 * 		用户ID,订单状态,页码
	 *//*
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage);*/
	/**
	 * 修改个人中心的地址
	 *
	 * @param
	 *
	 */
	public int updateIndvidualAddress(int userid, int id, String address,
                                      String country, String phonenumber, String zipcode, String address2) ;
	public void addOrderInfo(List<OrderBean> orderinfo, int addressid, int odcount);
	public int queryPreferential(int userid);
	public int updatePreferential(int userid, String orderNo);
	public void addOrderInfo(OrderBean orderBean);
	public List<OrderBean> getOrderInfo(int userid, String order_no);
	public void updateOrderState(int userid, String orderid, String pay_price_three);
	public void updateOrderStatePayPrice(int userid, String orderid, String pay_price_three, String pryprice, String ipnAddressJson, double order_ac, double owe);
	/**
	 * 更新用户支付金额,状态
	 *
	 */
	public void updateOrderStatePayPrice(int userid, List<String[]> orderInfo, String ipnAddressJson);
	/*提交订单后修改购物车商品的状态--wanyang*/
	public int updateGoodscarState(int userid, String itemid);
	/*提交订单后修改购物车商品的状态--wanyang*/
	public int updateGoodscarStateByItemid(String itemid);
	/*订单付款后修改购物车商品的状态--wanyang*/
	public void updateGoodscarStateAgain(int userid, String itemid);
	/*更新用户地址--wanyang*/
	public void updateUserAddress(int id, String address, String country, String phonenumber, String zipcode, String address2, String statename, String recipients, String street);
	/*获取homefurniture*/
	public List<Eightcatergory> getHomefurnitureProduct(String catergory);

	/**
	 * ylm
	 * 根据商品ID查询总支付费用
	 * @param cancel_obj 取消订单对象
	 */
	//public float getTotalPrice(String goodsids);

	/**
	 * 根据id删除收件地址
	 * @param id
	 */
	public void delUserAddressByid(int id);

	/**
	 * 设置默认地址
	 * @param id
	 */
	public void setDefault(int id, int userid);

	public Address getUserAddrById(int id);

	public int getAddressCountByUserId(int userid);

	public void updateOrderPayPrice(int userid, String order_no, String pay_price, String ipnAddressJson);

	public int addOrderAddress(Map<String, Object> map);

	/**
	 * 新增订单地址
	 *
	 */
	public int addOrderAddress(List<Map<String, Object>> maps);

	/**
	 * 保存订单折扣信息
	 * author:ylm
	 * 2015-10-27
	 */
	public int saveOrder_discount(String orderno, int discounttype, double price, String discountinfo);

	/**
	 * 修改订单需支付运费金额，运输方式
	 * author:ylm
	 * 2015-10-27
	 */
	public int upOrderExpress(String orderno, String mode_transport, String actual_ffreight);

	/**
	 * 修改订单需支付运费金额，运输方式,运费，剩余支付金额，已支付运费
	 * author:ylm
	 * 2015-11-30
	 */
	public int upOrderExpress(String orderno, String mode_transport, String actual_ffreight, String remaining_price, String pay_price_tow, double service_fee, double pay_price);

	/**
	 * 修改订单服务费
	 * author:ylm
	 * 2015-11-30
	 */
	public int upOrderService_fee(String orderno, String service_fee);

	public String initCheckData(String orderNos);

	public List<OrderBean> getOrderByUid(Integer uid);


	public int updateOrderShowFlag(int userid);
	public int updateUnpaidOrderShowFlag(int userid);
	/**
	 * 获取用户的订单记录
	 * zlw*/
	public List<OrderBean> getOrdersByUserid(int userid);
	/**
	 * 降价优惠查询
	 */
	public int getPriceReduction(int userid);

	/**
	 * 获得历史订单数量
	 */
	public int getHistoryCount(int userid);

	/**
	 * 更新订单服务器flg
	 *
	 */
	public void updateServerUpdate(String order_no);

	public List<OrderDetailsBean> getIndividualOrdersDetails(String orderNo, int state);
	/**
	 * 获取订单库存表信息
	 *
	 * @param orderNo
	 * @return  List<IdRelationtable>
	 */
	public List<IdRelationTable> getIdRelationtable(String orderNo);
	/**
	 * 更新替換表刪除flag
	 */
	public void updateChangeDelFlag(String order_no, int goodsCarid);
	/**
	 * 获取订单号
	 * author:ylm
	 * 2015-09-16
	 */
	public String getOrderNo();
	/*获取最大订单号*/
	public String getMaxOrderno(String userid);

	/**
	 * 合并订单
	 * @param orderid
	 */
	public void mergeOrder(String orderid);

	public Integer existOrderNo(String orderNo);

	//取消订单更新订单表状态
	public void updateOrderInfoState(String orderNo);

	public void updateOrderInfopr(String orderNo, int userId, String productCost, String remainingPrice);

	public void updateChangeGood(String orderNo, String goodId);

	public void updateOrderinfo2Freight(String orderNo);

	public int saveGoodsCar(SpiderBean spider);

	public void updateOrderDetail(String orderNo, int userId, String goodsId, String goodSprice, String name, int goodsCarId, int id, String remark, String goodsUrl, String goodsImg, String goodsType);

	public void updateProductSource(String goodsid, String goodsdataid, String goodsUrl, String goodsImgUrl, String goodsPrice, String name, String orderNo, int goodCarKey);


	public Page<OrderBean> getOrderByUid(int uid, Page<OrderBean> page);

	/**
	 * 余额支付成功后更新订单表
	 * @param userId
	 * @param orderNo
	 * @param payPrice
	 * @param payPriceThree
	 * @param payPriceTow
	 * @return
	 */
	public int updateOrderinfoByBalancePayOnSuccess(int userId, String orderNo, String payPrice, String payPriceThree, String payPriceTow);

	/**
	 * 余额支付成功后更新订单表赠送运费
	 */
	public int updateOrderAc(int userId, String orderNo, double orderAc);

	/**
	 * 修改未支付金额
	 * @param userId
	 * @param orderNo
	 * @param remainingPrice
	 * @return
	 */
	public int updateRemainingPrice(int userId, String orderNo, double remainingPrice);

	//取得国家名
	public String getZoneBy(int id);

	//yqy
	public List<OrderDetailsBean> getDelOrderByDropshipIdAndStatus(String orderNo, int goodId);

	/**
		 * 跟据订单号在orderaddress表中查找国家id
		 * @param orderNo
		 * @return
		 */
		public String getCountryIdFromOrderNo(String orderNo);


		/**
		 * 将orderdetail信息状态更新为2
		 * @param orderNo
		 * @param goodid
		 * @return
		 */
		public int updateOrderDetails2StateByGoodId(String orderNo, int goodid);

		//yqy
		public int updateOrderDetailsStateByDropshipNoAndGoodId(String dropshipNo, int goodid);

		/**
		 * 取消dropship商品后更新orderinfo表信息
		 * @param map
		 * @return
		 */
		int updateOrderInfoFormCancelProduct(Map<String, Object> map);

		/**
		 * 订单生成日志记录，用来记录该订单对应的商品信息
		 *	用户ID  订单号，运输日期，地址ID，商品列表
		 * author:ylm
		 * 2016-11-29
		 */
		public int addOrderNoLog(int userId, String orderNo, String days, int addressId, String itemInfo, double pay_product);

		/**根据订单查询需要客户取消的order_change数量
		 * @author jxw
		 * @date 2017-3-25
		 * @param orderNo
		 * @return
		 */
		public int getOrderCgWithCancle(String orderNo);

		/**原有的mapper方法，设置OrderChange的State
		 * @author jxw
		 * @date 2017-3-25
		 * @param orderNo
		 * @param goodId
		 * @param ropType
		 * @return
		 */
		public int updateOrderChange2State(String orderNo, int goodId, Integer ropType);

		public int upUserPrice(int userId, double price);

		public List<Map<String,Object>> getNeedComfirmSourceGoods();
}
