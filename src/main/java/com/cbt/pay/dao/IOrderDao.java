package com.cbt.pay.dao;

import com.cbt.bean.Address;
import com.cbt.bean.Eightcatergory;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;

import java.util.List;
import java.util.Map;

public interface IOrderDao {
	
	
	/**
	 * 获取订单信息
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 *//*
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage,int endpage);*/
	/*添加订单详细信息*/
	public int addOrderDetail(List<OrderDetailsBean> orderdetails);
	/*获取最大订单号*/
	public String getMaxOrderno(String userid);
	/*获取订单内所有商品的信息*/
	public List<OrderDetailsBean> getOrderDetail(int userid, String orderid);
	/*添加新地址*/
	public int addAddress(com.cbt.bean.Address add);
	/*获取用户地址*/
	public List<Address> getUserAddr(int userid);
	/*查询用户地址是否存在*/
	public int existUserAddr(int userid);
	/*用户添加订单记录
	 * wanyang*/
	public int addOrderInfo(List<OrderBean> OrderBean, int address, int odcount);
	/*获取用户的订单记录
	 * wanyang*/
	public List<OrderBean> getOrderInfo(int userid, String orderid);
	/*获取用户的订单记录
	 * zlw*/
	public List<OrderBean> getOrders(int userid);
	public int updateOrderShowFlag(int userid);
	public int updateUnpaidOrderShowFlag(int userid);
	/*生成订单时更新购物车的商品状态
	 * wanyang*/
	public int updateGoodscarState(int userid, String itemid);
	/*生成订单时更新购物车的商品状态
	 * wanyang*/
	public int updateGoodscarState(String itemid);
	/*付款时再次更新购物车的商品状态
	 * wanyang*/
	public void updateGoodscarStateAgain(int userid, String itemid);
	/*确认付款后修改订单和订单详情状态
	 * wanyang*/
	public void updateOrderState(int userid, String orderid, String pay_price_three);
	/*更新用户地址
	 * wanyang*/
	public int updateUserAddress(int id, String address, String country, String phonenumber, String zipcode, String address2, String statename, String recipients, String street);
	/*获取homefurniture页面内容
	 * wanyang
	 */
	public List<Eightcatergory> getHomefurnitureProduct(String catergory);

	/**
	 * ylm
	 * 根据商品ID查询总支付费用
	 * @param cancel_obj 取消订单对象
	 */
	public float getTotalPrice(String goodsids);

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
	public void mergeOrder(String orderid, int userid);//合并订单

	/**
	 * 更新用户支付金额
	 *
	 */
	public void updateOrderPayPrice(int userid, String order_no, String pay_price, String ipnAddressJson, double discount_amount, double product_cost, double remaining_price);
	/**
	 * 更新用户支付金额,状态
	 *
	 */
	public void updateOrderStatePayPrice(int userid, String orderid, String pay_price_three, String pryprice, String ipnAddressJson, double order_ac);
	/**
	 * 更新用户支付金额,状态
	 *
	 */
	public void updateOrderStatePayPrice(int userid, List<String[]> orderInfo, String ipnAddressJson);
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
	public int addOrderAddress(List<Map<String, Object>> maps);
	/**
	 * 获取订单地址
	 * author:wanyang
	 * 2015-08-27
	 */
	public Address getOrderAddress(String orderid);
	/**
	 * 更新订单地址
	 * author:wanyang
	 * 2015-08-27
	 */
	public int updateOrderAddress(Address address, String orderid);
	public String getOrderProductcost(String orderid);
	public void updateOrderinfo(String product_cost, String service_fee, String orderid);
	public int delGoods(String goodsid, String orderid);

	/**
	 * 获取订单号
	 * author:ylm
	 * 2015-09-16
	 */
	public String getOrderNo();
	/**
	 * 获取实时汇率
	 * @return
	 */
	public String getExchangeRate();

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
	public int upOrderExpress(String orderno, String mode_transport, String actual_ffreight, String remaining_price, String pay_price_tow, String service_fee, double pay_price);
	/**
	 * 修改订单服务费
	 * author:ylm
	 * 2015-11-30
	 */
	public int upOrderService_fee(String orderno, String service_fee);

	public String initCheckData(String orderNos);

	/**
	 * 降价优惠查询
	 */
	public int getPriceReduction(int userid);

	/**
	 * 更新替換表刪除flag
	 *
	 */
	public void updateChangeDelFlag(String order_no, int goodsCarid);
	
	
	/**取消后台销售生成的订单
	 * @date 2016年11月21日
	 * @author abc
	 * @param orderid
	 * @return  
	 */
	public int cancelOrder(String orderid);
	public void updateOnlineOrderAddress(Address addr, String orderid);
	
}
