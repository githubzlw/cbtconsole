package com.cbt.website.dao2;

import com.cbt.website.bean.UserInfo;

import java.util.Date;
import java.util.List;

public interface IGoodsDao {

	//根据userid和session查询购物车总额
	public List<Object[]> getGoodsPrice(int type);
	
	//查询未注册购物车数量和已注册购物车
	public int getGoodsNumber(int type);
	
	//查询走至交期确认的用户和购物车信息
	
	
	//根据用户ID||走至交期确认,的购物车数据
	
	//查询用户信息
	 public List<UserInfo> getUserInfos(int userid, Date stateDate, Date endDate);

	//查询用户信息
	public List<UserInfo> getUserInfoForPrice(String conutry, String admUserId, String vip, int userid, Date stateDate, Date endDate, Date date, String name, String email, String recipients, String recipientsaddress, String paymentusername, String paymentid, String paymentemail);

	//查询用户信息
	public List<UserInfo> getUserInfo(Integer userid, String email);

	//修改用户类别
	public int updateUserCategory(Integer userid, String email, String category, String signkey);
	
	//查询支付状态为2的所有订单
	public List<Object[]> getOrdersPay();
	
	//修改支付状态和订单状态
	public int upOrders(String orderno);
	
	//修改支付状态和订单状态
	public int upPay(String orderno);
}
