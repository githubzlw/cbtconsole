package com.cbt.change.util;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.Orderinfo;
import com.cbt.jdbc.DBHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OnlineOrderInfoDao {
	private static final Log LOG = LogFactory.getLog(OnlineOrderInfoDao.class);

	/**
	 * 根据订单号查询订单信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public Orderinfo queryOrderInfoByOrderNo(String orderNo) throws Exception {

		Orderinfo orderinfo = new Orderinfo();

		Connection conn = DBHelper.getInstance().getConnection2();
		String querySql = "select  orderid,order_no,user_id,product_cost,pay_price,remaining_price,state "
				+ "from orderinfo where order_no = ?";
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			stm = conn.prepareStatement(querySql);
			stm.setString(1, orderNo);
			rs = stm.executeQuery();
			if (rs.next()) {
				orderinfo.setOrderid(rs.getInt("orderid"));
				orderinfo.setOrderNo(rs.getString("order_no"));
				orderinfo.setUserId(rs.getInt("user_id"));
				orderinfo.setProductCost(rs.getString("product_cost"));
				orderinfo.setPayPrice(rs.getString("pay_price"));
				orderinfo.setRemainingPrice(rs.getDouble("remaining_price"));
				orderinfo.setState(rs.getString("state"));
			}
			// 如果获取的订单状态为空，默认为0
			if (orderinfo.getState() == null || "".equals(orderinfo.getState())) {
				orderinfo.setState("0");
			}

		} catch (Exception e) {
			LOG.error("查询线上orderinfo失败，订单号：" + orderNo + ",原因： " + e.getMessage());
			throw new Exception(e);
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}

		return orderinfo;
	}

	

	/**
	 * 根据订单号和商品id查询订单详情信息
	 * 
	 * @param orderNo
	 * @param goodsid
	 * @return
	 */
	public OrderDetailsBean queryOrderDetailsByOrderNoAndGoodsid(String orderNo, int goodsid) throws Exception {

		OrderDetailsBean orderDetails = new OrderDetailsBean();

		Connection conn = DBHelper.getInstance().getConnection2();
		String querySql = "select  id,goodsid,orderid,yourorder,goodsprice,state,purchase_state,userid "
				+ "from order_details where orderid = ? and goodsid = ?";
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			stm = conn.prepareStatement(querySql);
			stm.setString(1, orderNo);
			stm.setInt(2, goodsid);
			rs = stm.executeQuery();
			if (rs.next()) {
				orderDetails.setId(rs.getInt("id"));
				orderDetails.setGoodsid(rs.getInt("goodsid"));
				orderDetails.setOrderid(rs.getString("orderid"));
				orderDetails.setYourorder(rs.getInt("yourorder"));
				orderDetails.setGoodsprice(rs.getString("goodsprice"));
				orderDetails.setState(rs.getInt("state"));
				orderDetails.setPurchase_state(rs.getInt("purchase_state"));
				orderDetails.setUserid(rs.getInt("userid"));
			}

		} catch (Exception e) {
			LOG.error("查询线上order_details失败，订单号：" + orderNo + ",商品id：" + goodsid + ",原因： " + e.getMessage());
			throw new Exception(e);
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}

		return orderDetails;
	}
	
	
	/**
	 * 根据订单号查询全部订单详情信息
	 * 
	 * @param orderNo
	 * @return
	 */
	public List<OrderDetailsBean> queryOrderDetailsByOrderNo(String orderNo)  throws Exception {

		List<OrderDetailsBean> detailsLst = new ArrayList<OrderDetailsBean>();

		Connection conn = DBHelper.getInstance().getConnection2();
		String querySql = "select  id,goodsid,orderid,yourorder,goodsprice,state,purchase_state,userid "
				+ "from order_details where order_no = ?";
		PreparedStatement stm = null;
		ResultSet rs = null;
		try {

			stm = conn.prepareStatement(querySql);
			stm.setString(1, orderNo);
			rs = stm.executeQuery();
			if (rs.next()) {
				OrderDetailsBean orderDetails = new OrderDetailsBean();
				orderDetails.setId(rs.getInt("id"));
				orderDetails.setGoodsid(rs.getInt("goodsid"));
				orderDetails.setOrderid(rs.getString("orderid"));
				orderDetails.setYourorder(rs.getInt("yourorder"));
				orderDetails.setGoodsprice(rs.getString("goodsprice"));
				orderDetails.setState(rs.getInt("state"));
				orderDetails.setPurchase_state(rs.getInt("purchase_state"));
				orderDetails.setUserid(rs.getInt("userid"));
				detailsLst.add(orderDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("查询线上order_details失败，订单号：" + orderNo + ",原因： " + e.getMessage());
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}

		return detailsLst;
	}

}
