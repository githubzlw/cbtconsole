package com.cbt.website.server;

import com.cbt.website.bean.UserInfo;
import com.cbt.website.dao2.GoodsDao;
import com.cbt.website.dao2.IGoodsDao;

import java.util.Date;
import java.util.List;


public class GoodsServer implements IGoodsServer {

	private IGoodsDao dao = new GoodsDao();
	@Override
	public List<Object[]> getGoodsPrice(int type) {
		return dao.getGoodsPrice(type);
	}

	@Override
	public int getGoodsNumber(int type) {
		// TODO Auto-generated method stub
		return 0;
	}

	//查询用户信息
	public List<UserInfo> getUserInfoForPrice(String conutry,String admUserId,String vip,int userid,Date stateDate,Date endDate,Date date,String name ,String email,String recipients,String recipientsaddress,String paymentusername,String paymentid,String paymentemail){
		return dao.getUserInfoForPrice(conutry,admUserId,vip,userid, stateDate, endDate, date,  name,  email,recipients,recipientsaddress,paymentusername,paymentid,paymentemail);
	}

	@Override
	public List<Object[]> getOrdersPay() {
		return dao.getOrdersPay();
	}

	@Override
	public String upOrdersPay(String orderno) {
		int res = dao.upOrders(orderno);
		int res1 = dao.upPay(orderno);
		String resu = (res > 0 ? "修改订单状态成功" : "修改订单状态失败") +"&"+ (res1 > 0 ? "修改支付状态成功" : "修改支付状态失败");
		return resu;
	}

	@Override
	public int updateUserCategory(Integer userid,String email,String category,String signkey) {
		return dao.updateUserCategory(userid, email, category,signkey);
	}

	@Override
	public List<UserInfo> getUserInfo(Integer userid, String email) {
		return dao.getUserInfo(userid, email);
	}
}
