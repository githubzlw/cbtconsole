package com.cbt.processes.service;

import com.cbt.bean.*;
import com.cbt.jdbc.DBHelper;
import com.cbt.processes.dao.IInquiry;
import com.cbt.processes.dao.IOrderuserDao;
import com.cbt.processes.dao.InquiryDao;
import com.cbt.processes.dao.OrderuserDao;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderServer implements IOrderServer {
 
	IOrderuserDao dao = new OrderuserDao();
	@Override
	public List<OrderDetailsBean> getOrders(int userID, int state, int startpage) {
		List<OrderDetailsBean> list = dao.getOrders(userID, state, (startpage-1)*8,8);
		return list;
	}
	@Override
	public List<OrderDetailsBean> getProductDetail(int userID, int state, int startpage, String orderNo) {
		List<OrderDetailsBean> list = dao.getProductDetail(userID, state,(startpage-1)*8,8, orderNo);
		return list;
	}
	
	public int getOrderdNumber(int userID, int state){
		return dao.getOrderdNumber(userID, state);
	}
	
	@Override
	public int[] getOrdersIndividual(int userid) {
		List<Integer[]> ls = dao.getOrdersIndividual(userid);
		int waitPay = 0;//-1等待付款状态
		int ing = 0;//2确认价格
		int onck =0; //1已到仓库
		int cy =0; //3出运中
		int history =  0;//历史订单	
		int purchase =  0;//购买中	
		int xj = 0;//询价结果数量
		for (int i = 0; i < ls.size(); i++) {
			 if(ls.get(i)[0] == 0){
				 waitPay = ls.get(i)[1];
			 }else if(ls.get(i)[0] == 5){
				 ing = ls.get(i)[1];
			 }else if(ls.get(i)[0] == 3){
				 cy = ls.get(i)[1];
			 }else if(ls.get(i)[0] == 4){
				 history = ls.get(i)[1];
			 }else if(ls.get(i)[0] == 1){
				 purchase = ls.get(i)[1];
			 }
		}
		onck =  dao.warehouse(userid);//3已到仓库
		IInquiry inquiry = new InquiryDao();
		xj = inquiry.getInquiryNumber(userid);//询价结果数量
		//等待付款状态,确认价格,购买中,已到仓库,出运中,历史订单,询价结果数量
		int[] oi = {waitPay,ing,purchase,onck,cy,history,xj};
		return oi;
	}
	@Override
	public Map<String, List<String>> getConfirmThePriceOf(int userId){
		Map<String, List<String>> confirmThePriceOf = dao.getConfirmThePriceOf(userId);
		return confirmThePriceOf;
	}
	@Override
	public Map<String, Object> getCtpoOrderInfo(String orderNo,int userId){
		Map<String, Object> ctpoOrderInfo = dao.getCtpoOrderInfo(orderNo,userId);
		//查询该订单支付总金额
		IOrderwsDao orderwsDao = new OrderwsDao();
		ctpoOrderInfo.put("totalProductCost", orderwsDao.getOrdersPay(orderNo)[0]);
		return ctpoOrderInfo;
	}
	
	@Override
	public int updatePriceReductionOffer(int userId,int goodsDataId,int goodsCarId) {
		
		String sql = "update tbl_preshoppingcar_info set flag=1  where flag = 0 and userid = ? and goods_data_id=? and goods_car_id=? ";
		String sql1 = "update goods_car set state=9  where state=0 and userid = ? and goodsdata_id=? and id=? ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null,stmt1=null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			stmt.setInt(2, goodsDataId);
			stmt.setInt(3, goodsCarId);
			res = stmt.executeUpdate();
			
			stmt1 = conn.prepareStatement(sql1);
			stmt1.setInt(1, userId);
			stmt1.setInt(2, goodsDataId);
			stmt1.setInt(3, goodsCarId);
			res = stmt1.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt1 != null) {
				try {
					stmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	@Override
	public int updateGoosCar(int userId) {
		
		String sql = "update goods_car set state=9  where userid= ? and state=0 and id not in (select  goods_car_id from  tbl_preshoppingcar_info where userid= 1645 and flag=0) ";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			res = stmt.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return res;
	}
	
	 
	@Override
	public int saveEvaluate(Evaluate evaluate) {
		return dao.saveEvaluate(evaluate);
	}
	@Override
	public int upOrderState(String orderNo, int state) {
		return dao.upOrderState(orderNo, state);
	}
	@Override
	public int cancelOrder(String orderNo, int cancel_obj) {
		return dao.cancelOrder(orderNo, cancel_obj);
	}
	 
	@Override
	public Map<String, String> getOrderChangeState(String[] orderNo) {
		Map<String, String> map = dao.getOrderChangeState(orderNo);
		String[] orderNo1 = new String[orderNo.length-map.size()];
		int j = 0;
		for (int i = 0; i < orderNo.length; i++) {
			if(map.get(orderNo[i])==null){
				orderNo1[j] = orderNo[i];
				j++;
			}
		}
		/*Map<String, String> map1 = dao.getOrderChangeState1(orderNo1);
		for (String key : map1.keySet()) {
			
			map.put(key, map1.get(key));
		}*/
		return map;
	}

	@Override
	public float getTotalPrice(String goodsids) {
		return dao.getTotalPrice(goodsids);
	}

	@Override
	public void updateOrderState(int userid, String orderid) {
		// TODO Auto-generated method stub
		dao.updateOrderState(userid, orderid);
	}

	@Override
	public AdvanceOrderBean getOrders(String orderNo) {
		return dao.getOrders(orderNo);
	}

	@Override
	public List<SpiderBean> getSpiders(String orderNo) {
		return dao.getSpiders(orderNo);
	}

	public List<ProductChangeBean> getProductChangeInfo(String orderNo,String flag){
		return dao.getProductChangeInfo(orderNo,flag);
	}
	
	public List<ProductChangeBean> getPriceReductionOffer(String userId){
		return dao.getPriceReductionOffer(userId);
	}
	
	
	@Override
	public int upQuestions(String orderid, String questions) {
		String orderidStr = dao.getAdvance(orderid);
		if(orderidStr != null){
			return dao.upQuestions(orderid, questions);
		}else{
			return dao.addAdvance(orderid, questions);
		}
	}


	@Override
	public int getOrderState(String orderNo) {
		// TODO Auto-generated method stub
		return dao.getOrderState(orderNo);
	}
 
	
}
