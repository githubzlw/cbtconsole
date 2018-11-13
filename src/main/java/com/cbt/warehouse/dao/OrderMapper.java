package com.cbt.warehouse.dao;

import com.cbt.bean.*;
import com.cbt.warehouse.pojo.IdRelationTable;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderMapper {
	
	/**
	 * 获取订单信息
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public List<OrderBean> getOrders(@Param("userid") int userid, @Param("state") int state,
                                     @Param("startpage") int startpage, @Param("endpage") int endpage,
                                     @Param("orderNo") String orderNo, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

	/**
	 * 获取商品信息
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public List<OrderDetailsBean> getProductDetail(@Param("userID") int userID, @Param("state") int state,
                                                   @Param("startpage") int startpage, @Param("endpage") int endpage, @Param("orderNo") String orderNo);

	public List<Map<String,Object>> getNeedComfirmSourceGoods();
	/**
	 * 获取订单信息的总数量
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int getCntByUseridAndState(@Param("userid") int userid, @Param("state") int state,
                                      @Param("orderNo") String orderNo, @Param("timeFrom") String timeFrom, @Param("timeTo") String timeTo);

	/**
	 * 获取历史订单数量
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public int getCntByUserid(int userid);
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
	public List<Map<String, Object>> getOrdersIndividual(int userid);

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
	public List<Map<String, Object>> getConfirmThePriceOf(int userId);
	//
	public List<Map<String, Object>> getCtpoOrderInfo(@Param("orderNo") String orderNo, @Param("userId") int userId);
	public List<Map<String, Object>> getOrderChanges(@Param("orderNo") String orderNo, @Param("goodId") int goodId);

	public int saveOrderZiXun(@Param("orderNo") String orderNo, @Param("goodId") int goodId, @Param("ropType") int ropType, @Param("oldValue") String oldValue,
                              @Param("newValue") String newValue, @Param("status") int status);

	public int updateClientUpdateByOrderNo(@Param("orderNo") String orderNo);

	public int updateOrderDetails2StateByGoodId(@Param("orderNo") String orderNo, @Param("goodId") int goodId);

	//yqy
	public int updateOrderDetailsStateByDropshipNoAndGoodId(@Param("dropshipNo") String dropshipNo, @Param("goodId") int goodId);

	public int updateOrderChange2State(@Param("orderNo") String orderNo, @Param("goodId") int goodId, @Param("ropType") Integer ropType);


	public int updateOrderDetails2Price(@Param("userid") int userid, @Param("goodsid") int goodsid, @Param("orderid") String orderid, @Param("goodsprice") String goodsprice);

	public int updateUserAvailableM(@Param("amount_u") double amount_u, @Param("userId") int userId);

	public int insertRechargeRecord(@Param("userId") int userId, @Param("price") double price, @Param("type") int type, @Param("remark") String remark, @Param("remarkId") String remarkId,
                                    @Param("usesign") int usesign, @Param("currency") String currency);

	public int updateOrderinfo2PayPrice(@Param("updatedtotalprice") BigDecimal updatedtotalprice, @Param("newProductCost") String newProductCost, @Param("discount_amount") double discount_amount,
                                        @Param("userId") int userId, @Param("orderNo") String orderNo);

	public int updateOrderinfo2RemainingPrice(@Param("amount") double amount, @Param("newProductCost") String newProductCost, @Param("discount_amount") double discount_amount,
                                              @Param("userId") int userId, @Param("orderNo") String orderNo);

	public int updateOrderDetails2DeliveryTime(@Param("delivery_time") String delivery_time, @Param("userid") int userid, @Param("goodsid") int goodsid, @Param("orderid") String orderid);

	public int updateOrderinfo2DeliveryTime(@Param("delivery_time") String delivery_time, @Param("orderNo") String orderNo);

	public int updateOrderDetails2Yourorder(@Param("yourorder") int yourorder, @Param("userid") int userid, @Param("goodsid") int goodsid, @Param("orderid") String orderid);

	public int updateOrderDetails2Freight(@Param("goodsfreight") String goodsfreight, @Param("userid") int userid, @Param("goodsid") int goodsid, @Param("orderid") String orderid);


	public int updateOrderDetailsChange(@Param("userid") int userid, @Param("orderNo") String orderNo);

	public int updateOrderChange2StateByRopTypeIn(@Param("orderNo") String orderNo, @Param("ropTypes") List<Integer> ropTypes);


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
	public int upOrderState(@Param("orderNo") String orderNo, @Param("state") int state);

	public int upCouponPrice(@Param("orderNo") String orderNo);

	/**
	 * ylm
	 * 取消订单
	 * @param cancel_obj 取消订单对象
	 */
	public int cancelOrder(@Param("orderNo") String orderNo, @Param("cancel_obj") int cancel_obj);

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
	public List<Map<String, Object>> getOrderChangeState(@Param("orderNos") String[] orderNos);


	public List<Map<String, String>> getPaymentState(@Param("orderNos") String[] orderNos);

	public List<Map<String, String>> getOrderDetailsState(@Param("orderNos") String[] orderNos);


	/**
	 * ylm
	 * 根据商品ID查询总支付费用
	 * @param cancel_obj 取消订单对象
	 */
	public Float getTotalPrice(String goodsids);


	public void updateOrderinfo2State(@Param("userid") int userid, @Param("orderid") String orderid);

	public void updateOrderDetails2StateByUserid(@Param("userid") int userid, @Param("orderid") String orderid);


	/**
	 * ylm
	 * 保存预订单的问题
	 * @param
	 */
	public int saveQuestions(@Param("orderNo") String orderNo, @Param("questions") String questions);


	public List<SpiderBean> getSpidersByOrderid(String orderNo);

	public AdvanceOrderBean getAdvanceOrderByOrderNo(String orderNo);


	/**
	 * 查询购物车商品
	 *
	 * @param userId
	 * 		用户ID
	 */
	public  List<SpiderBean> getSpiders(String orderNo);


	public List<Map<String, Object>> getProductChangeInfoByOrderDetails(@Param("orderNo") String orderNo, @Param("flag") String flag);

	public List<Map<String, Object>> getProductChangeInfoByGoodsdata(@Param("aliurl") String aliurl, @Param("goodscarid") String goodscarid);


	public List<ProductChangeBean> getPriceReductionOffer(int userId);

	/**
	 * ylm
	 * 修改预订单内容表
	 * 订单号，回答，运费，关税
	 * @param
	 */
	public int upQuestions(@Param("orderid") String orderid, @Param("questions") String questions);

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
	public int addAdvance(@Param("orderid") String orderid, @Param("questions") String questions);


	public Map<String, Object> getOrderinfoByOrderNo(String orderid);

	public List<OrderDetailsBean> getDelOrderByOrderNoAndStatus(@Param("orderNo") String orderNo, @Param("goodId") int goodId);


	public List<OrderDetailsBean> getDelOrderByDropshipIdAndStatus(@Param("orderNo") String orderNo, @Param("goodId") int goodId);

	/**
	 * ylm
	 * 修改订单表的状态和已采购数量和总采购数量
	 *
	 * @param
	 */
	public int upOrderPurchase(@Param("purchase_state") int purchase_state, @Param("orderno") String orderno, @Param("state") int state);

	/**
	 * ylm
	 * 更新订单状态
	 */
	public Integer checkUpOrderState(String orderNo);
	/**
	 * ylm
	 * 查询订单状态
	 */
	public Integer getOrderState(String orderNo);
	/**
	 * ylm
	 * 获取订单优惠变更
	 */
	public Map<String, Object> getOrderDiscount(String orderNo);


	public List<Map<String, Object>> getIndividualOrdersDetails(@Param("orderNo") String orderNo, @Param("state") int state);
	public List<Map<String, Object>> getDropshipIndividualOrdersDetails(@Param("orderNo") String orderNo, @Param("state") int state);

	public Map<String, Object> getIndividualOrdersChange(@Param("orderNo") String orderNo, @Param("goodId") int goodId);


	public List<Double> getRemainingPrice(@Param("orderNo") String orderNo, @Param("userId") int userId);

	public void updateOrderInfopr(@Param("orderNo") String orderNo, @Param("userId") int userId, @Param("productCost") String productCost,
                                  @Param("remainingPrice") String remainingPrice);

	public void updateChangeGood(@Param("orderNo") String orderNo, @Param("goodId") String goodId);

	/**
	 * 更新product_cost，remaining_price
	 * @param orderNo
	 * @return
	 */
	public void updateOrderDetail(@Param("orderNo") String orderNo, @Param("userId") int userId, @Param("goodsId") String goodsId,
                                  @Param("goodSprice") String goodSprice, @Param("name") String name, @Param("goodsCarId") int goodsCarId, @Param("id") int id,
                                  @Param("remark") String remark, @Param("goodsUrl") String goodsUrl, @Param("goodsImg") String goodsImg, @Param("goodsType") String goodsType);


	public int updateChangeGoodData(@Param("orderNo") String orderNo, @Param("goodId") String goodId);

	public int updateOrderinfo2Freight(@Param("orderNo") String orderNo);


	public int saveGoodsCar(SpiderBean spider);

	/**
	 * 更新order_product_source:goods_p_url,purchase_state
	 * @param orderNo,goodId
	 * @return
	 */
	public void updateProductSource(@Param("goodsid") String goodsid, @Param("goodsdataid") String goodsdataid, @Param("goodsUrl") String goodsUrl,
                                    @Param("goodsImgUrl") String goodsImgUrl, @Param("goodsPrice") String goodsPrice, @Param("name") String name, @Param("orderNo") String orderNo,
                                    @Param("goodCarKey") int goodCarKey);


	/**
	 * 获取订单库存表信息
	 *
	 * @param orderNo
	 * @return  List<IdRelationtable>
	 */
	public List<IdRelationTable> getIdRelationtable(String orderNo);

	public int updatePreshoppingcarInfo(@Param("userId") int userId, @Param("goodsDataId") int goodsDataId, @Param("goodsCarId") int goodsCarId);

	public int updateGoodsCar(@Param("userId") int userId, @Param("goodsDataId") int goodsDataId, @Param("goodsCarId") int goodsCarId);

	public int updateGoodsCarByUserId(int userId);

	public int upOrderDiscount(int id);
	/**
	 * 更新订单服务器flg
	 *
	 */
	public void updateServerUpdate(String order_no);




	/**
	 * 获取订单信息
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 *//*
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage,int endpage);*/
	/*添加订单详细信息*/
	public void addOrderDetail(OrderDetailsBean orderdetails);
	/*获取最大订单号*/
	public String getMaxOrderno(String userid);
	/*获取订单内所有商品的信息*/
	public List<OrderDetailsBean> getOrderDetail(@Param("userid") int userid, @Param("orderid") String orderid);
	/*添加新地址*/
	public int addAddress(Address add);
	/*获取用户地址*/
	public List<Address> getUserAddr(int userid);
	/*查询用户地址是否存在*/
	public int existUserAddr(int userid);
	/*用户添加订单记录,int address,int odcount
	 * wanyang*/
	public void addOrderInfo(OrderBean orderBean);
	//获取汇率
	public String getExchangeRate(String type);
	/**
	 * 获取用户的订单记录通过userid和order_no
	 * wanyang*/
	public List<OrderBean> getOrderInfoByOrder_noAndUserid(@Param("userId") int userid, @Param("order_no") String[] order_no);
	/**
	 * 获取用户的订单记录通过userid和orderNo
	 * ylm*/
	public OrderBean getOrderInfoByOrderNoAndUserId(@Param("orderNo") String orderNo, @Param("userId") String userId);
	/**
	 * 获取用户的订单记录
	 * zlw*/
	public List<Map<String, Object>> getOrdersByUserid(int userid);
	//根据用户id查询payment的最大createtime
	public String selectOrderShowFlag(int userid);
	public int updateOrderShowFlag(@Param("userid") int userid, @Param("paymentTime") String paymentTime);
	//根据用户id查询orderinfo的最大createtime
	public String selectUnpaidOrderShowFlag(int userid);
	public int updateUnpaidOrderShowFlag(@Param("userid") int userid, @Param("paymentTime") String paymentTime);
	/*生成订单时更新购物车的商品状态
	 * wanyang*/
	public int updateGoodscarState(@Param("userid") int userid, @Param("itemid") String itemid);
	/*生成订单时更新购物车的商品状态
	 * wanyang*/
	public int updateGoodscarStateByItemid(String[] itemIdArray);
	/*付款时再次更新购物车的商品状态
	 * wanyang*/
	public void updateGoodscarStateAgain(@Param("userid") int userid, @Param("itemid") String itemid);
	/*确认付款后修改订单和订单详情状态
	 * wanyang*/
	public void updateOrderPayPriceThree(@Param("userid") int userid, @Param("orderid") String orderid, @Param("pay_price_three") String pay_price_three);
	public void updateOrderState(@Param("userid") int userid, @Param("orderid") String orderid);
	/*更新用户地址
	 * wanyang*/
	public int updateUserAddress(@Param("id") int id, @Param("address") String address, @Param("country") String country, @Param("phonenumber") String phonenumber, @Param("zipcode") String zipcode,
                                 @Param("address2") String address2, @Param("statename") String statename, @Param("recipients") String recipients, @Param("street") String street);
	/*获取homefurniture页面内容
	 * wanyang
	 */
	public List<Eightcatergory> getHomefurnitureProduct(String catergory);

	/**
	 * 根据id删除收件地址
	 * @param id
	 */
	public void delUserAddressByid(int id);

	/**
	 * 设置默认地址
	 * @param id
	 */
	public void setDefault(@Param("id") int id, @Param("userid") int userid);
	/**
	 * 通过id查询地址
	 */
	public Address getUserAddrById(int id);
	/**
	 * 通过userid查询地址count
	 */
	public int getAddressCountByUserId(int userid);
	//合并订单
	public void mergeOrder_insert(@Param("orderid") String orderid, @Param("newOrderNo") String newOrderNo);
	public void mergeOrder_update(@Param("orderid") String orderid, @Param("newOrderNo") String newOrderNo);


	/**
	 * 更新用户支付金额
	 *
	 */
	public void updateOrderPayPrice(@Param("userid") int userid, @Param("order_no") String order_no, @Param("pay_price") String pay_price, @Param("ipnAddress") String ipnAddress,
                                    @Param("discount_amount") double discount_amount, @Param("product_cost") double product_cost, @Param("remaining_price") double remaining_price, @Param("cashback") double cashback);
	/**
	 * 更新用户支付金额,状态
	 * 需要调用updateOrderState
	 */
	public void updateOrderStatePayPrice(@Param("userid") int userid, @Param("orderid") String orderid, @Param("pay_price_three") String pay_price_three, @Param("pryprice") String pryprice,
                                         @Param("ipnAddressJson") String ipnAddressJson, @Param("order_ac") double order_ac, @Param("owe") double owe);
	/**
	 * 更新用户支付金额,状态
	 *
	 */
	public void updateOrderStatePayPrice_1(@Param("userid") int userid, @Param("orderInfo") List<String[]> orderInfo, @Param("ipnAddressJson") String ipnAddressJson);
	/**
	 * 获取订单自增id
	 *
	 */
	public int getOrderid();

	/**
	 * 新增地址
	 *
	 */
	public int addOrderAddress(Map<String, Object> map);

	/**
	 * 新增订单地址
	 *
	 */
	public int addOrderAddresss(@Param("map") Map<String, Object> map);

	/**
	 * 获取订单地址
	 * author:wanyang
	 * 2015-08-27
	 */
	public Address getOrderAddress(String orderid);
	public String getZoneBy(int id);
	/**
	 * 更新订单地址
	 * author:wanyang
	 * 2015-08-27
	 */
	public int updateOrderAddress(Address address, @Param("orderid") String orderid);
	public String getOrderProductcost(String orderid);
	public void updateOrderinfo(@Param("product_cost") String product_cost, @Param("service_fee") String service_fee, @Param("orderid") String orderid);
	public int delGoods(@Param("goodsid") String goodsid, @Param("orderid") String orderid);
	public int updateOrderinfoByOrderNo(String orderid);

	/**
	 * 获取订单号
	 * author:ylm
	 * 2015-09-16
	 */
	public String getOrderNo();

	/**
	 * 保存订单折扣信息
	 * author:ylm
	 * 2015-10-27
	 */
	public int saveOrder_discount(@Param("orderno") String orderno, @Param("discounttype") int discounttype, @Param("price") double price, @Param("discountinfo") String discountinfo);

	/**
	 * 修改订单需支付运费金额，运输方式
	 * author:ylm
	 * 2015-10-27
	 */
	public int upOrderExpress(@Param("orderno") String orderno, @Param("mode_transport") String mode_transport, @Param("actual_ffreight") String actual_ffreight);

	/**
	 * 修改订单需支付运费金额，运输方式,运费，剩余支付金额，已支付运费
	 * author:ylm
	 * 2015-11-30
	 */
	public int upOrderExpress_1(@Param("orderno") String orderno, @Param("mode_transport") String mode_transport, @Param("actual_freight") String actual_ffreight,
                                @Param("remaining_price") String remaining_price, @Param("pay_price_tow") String pay_price_tow, @Param("service_fee") double service_fee, @Param("pay_price") double pay_price);
	/**
	 * 修改订单服务费
	 * author:ylm
	 * 2015-11-30
	 */
	public int upOrderService_fee(@Param("orderno") String orderno, @Param("service_fee") String service_fee);

	public String initCheckData(String orderNos);

	/**
	 * 降价优惠查询
	 */
	public int getPriceReduction(int userid);


	/**
	 * 获得历史订单数量
	 */
	public int getHistoryCount(int userid);

	/**
	 * 更新替換表刪除flag
	 *
	 */
	public void updateChangeDelFlag(@Param("order_no") String order_no, @Param("goodsCarid") int goodsCarid);

	public List<OrderBean> getOrderByUid(Integer uid);

	public int insertOrderInfo(@Param("oldOrderId") String oldOrderId, @Param("newOrderId") String newOrderId);

	public int updateOrderId(@Param("oldOrderId") String oldOrderId, @Param("newOrderId") String newOrderId);

	public Integer existOrderNo(String orderNo);

	public void updateOrderInfoState(@Param("orderNo") String orderNo);

	public List<OrderBean> getOrderByUidPage(@Param("uid") int uid, @Param("start") int start, @Param("pageSize") int pageSize);

	public int getOrderByUidPageCount(int uid);

	/**
	 * 余额支付成功后更新订单表
	 * @param userId
	 * @param orderNo
	 * @param payPrice
	 * @param payPriceThree
	 * @param payPriceTow
	 * @return
	 */
	public int updateOrderinfoByBalancePayOnSuccess(@Param("userId") int userId, @Param("orderNo") String orderNo, @Param("payPrice") String payPrice,
                                                    @Param("payPriceThree") String payPriceThree, @Param("payPriceTow") String payPriceTow);

	public int updateOrderAc(@Param("userId") int userId, @Param("orderNo") String orderNo, @Param("orderAc") double orderAc);

	/**
	 * 修改未支付金额
	 * @param userId
	 * @param orderNo
	 * @param remainingPrice
	 * @return
	 */
	public int updateRemainingPrice(@Param("userId") int userId, @Param("orderNo") String orderNo, @Param("remainingPrice") double remainingPrice);


	/**
	 * yqy
	 * 添加订单信息,空字段排除
	 * @param record
	 * @return addOrderInfoBean
	 */
	int addOrderSelective(OrderBean record);

	/**
	 * yqy
	 * 根据订单号查询订单金额
	 * @param goodsids
	 * @return
	 */
	public Map<String, Object> getTotalPriceFormOrderNo(String orderNo);


	public String getCountryIdFromOrderNo(String orderNo);


	int updateOrderInfoFormCancelProduct(Map<String, Object> map);

	/**
	 * 订单生成日志记录，用来记录该订单对应的商品信息
	 *	用户ID  订单号，运输日期，地址ID，商品列表
	 * author:ylm
	 * 2016-11-29
	 */
	public int addOrderNoLog(@Param("userId") int userId, @Param("orderNo") String orderNo, @Param("days") String days, @Param("addressId") int addressId, @Param("itemInfo") String itemInfo, @Param("pay_product") double pay_product);

	public int queryPreferential(@Param("userid") int userid);

	public int updatePreferential(@Param("userid") int userid, @Param("orderNo") String orderNo);

	public OrderBean getOrderByorderNo(String orderNo);


	/**
	 * 取消商品,更新对应的金额
	 * @param userid
	 * @param orderNo
	 * @param pay_price
	 * @param object
	 * @param discount_amount
	 * @param product_cost
	 * @param d
	 * @param cashback
	 */
	public void updateOrderPayPrice1(@Param("userid") int userid, @Param("order_no") String order_no, @Param("pay_price") String pay_price, @Param("ipnAddress") String ipnAddress,
                                     @Param("discount_amount") double discount_amount, @Param("share_discount") double share_discount, @Param("product_cost") double product_cost, @Param("remaining_price") double remaining_price, @Param("cashback") double cashback);

	/**根据订单查询需要客户取消的order_change数量
	 * @author jxw
	 * @date 2017-3-25
	 * @param orderNo
	 * @return
	 */
	public int getOrderCgWithCancle(String orderNo);
	/**退钱
	 * @author cjc
	 * @date 2017-4-14
	 * @param userId price
	 * @return
	 */
	public int upPrice(@Param("userId") int userId, @Param("price") double price);

}
