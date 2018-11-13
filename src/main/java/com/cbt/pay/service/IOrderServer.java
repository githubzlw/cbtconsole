package com.cbt.pay.service;

import com.cbt.bean.Address;
import com.cbt.bean.Eightcatergory;
import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;

import java.util.List;
import java.util.Map;

public interface IOrderServer {
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
	public List<OrderBean> getOrderInfo(int userid, String order_no);
	public void updateOrderState(int userid, String orderid, String pay_price_three);
	public void updateOrderStatePayPrice(int userid, String orderid, String pay_price_three, String pryprice, String ipnAddressJson, double order_ac);
	/**
	 * 更新用户支付金额,状态
	 *
	 */
	public void updateOrderStatePayPrice(int userid, List<String[]> orderInfo, String ipnAddressJson);
	/*提交订单后修改购物车商品的状态--wanyang*/
	public int updateGoodscarState(int userid, String itemid);
	/*提交订单后修改购物车商品的状态--wanyang*/
	public int updateGoodscarState(String itemid);
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
	public int upOrderExpress(String orderno, String mode_transport, String actual_ffreight, String remaining_price, String pay_price_tow, String service_fee, double pay_price);

	/**
	 * 修改订单服务费
	 * author:ylm
	 * 2015-11-30
	 */
	public int upOrderService_fee(String orderno, String service_fee);
	
	public String initCheckData(String orderNos);
}
