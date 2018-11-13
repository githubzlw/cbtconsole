package com.cbt.website.server;

import com.cbt.website.bean.UserInfo;

import java.util.Date;
import java.util.List;

public interface IGoodsServer {
	
		//根据userid和session查询购物车总额
		public List<Object[]> getGoodsPrice(int type);
		
		//查询未注册购物车数量和已注册购物车
		public int getGoodsNumber(int type);
		
		//查询用户信息
		public List<UserInfo> getUserInfoForPrice(String conutry, String admUserId, String vip, int userid, Date stateDate, Date endDate, Date date, String name, String email, String recipients, String recipientsaddress, String paymentusername, String paymentid, String paymentemail);

		//查询支付状态为2的所有订单
		public List<Object[]> getOrdersPay();

		//修改支付状态和订单状态
		public String upOrdersPay(String orderno);

		//查询用户信息
		public List<UserInfo> getUserInfo(Integer userid, String email);

		//修改用户类型
		public int updateUserCategory(Integer userid, String email, String category, String signkey);
}
