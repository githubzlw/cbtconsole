package com.cbt.change.util;

import com.cbt.bean.OrderDetailsBean;
import com.cbt.bean.Orderinfo;
import com.cbt.jdbc.DBHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ChangeRecordsDao {
	private static final Log LOG = LogFactory.getLog(ChangeRecordsDao.class);

	public void insertOrderChange(Orderinfo orderinfo, int adminId, int operationType) {

		doOrderChangeInsert(orderinfo, adminId, operationType);
		/*
		 * if (check <= 0) { // 插入失败，重试3次 for (int i = 0; i < 3; i++) { int rs =
		 * doOrderChangeInsert(orderinfo, adminId, operationType); if (rs > 0) {
		 * break; } } }
		 */

	}

	public void insertOrderDetailsChange(OrderDetailsBean orderDetails, int operationType, int adminId) {

		doOrderDetailsChangeInsert(orderDetails, operationType, adminId);
		/*
		 * if (check <= 0) { // 插入失败，重试3次 for (int i = 0; i < 3; i++) { int rs =
		 * doOrderDetailsChangeInsert(orderDetails, operationType, adminId); if
		 * (rs > 0) { break; } } }
		 */

	}

	private int doOrderChangeInsert(Orderinfo orderinfo, int adminId, int operationType) {

		Connection conn = DBHelper.getInstance().getConnection();
		String querySql = "insert into order_change_records(order_no,user_id,admin_id,operation_type,product_cost,"
				+ "pay_price,remaining_price,status) values(?,?,?,?,?,?,?,?)";
		PreparedStatement stm = null;
		int rs = 0;
		try {

			stm = conn.prepareStatement(querySql);
			stm.setString(1, orderinfo.getOrderNo());
			stm.setInt(2, orderinfo.getUserId());
			stm.setInt(3, adminId);
			stm.setInt(4, operationType);
			stm.setString(5, orderinfo.getProductCost());
			stm.setString(6, orderinfo.getPayPrice());
			stm.setDouble(7, orderinfo.getRemainingPrice());
			stm.setString(8, orderinfo.getState());
			rs = stm.executeUpdate();
			return rs;

		} catch (Exception e) {
			e.printStackTrace();
			if (orderinfo != null) {
				LOG.error("插入order_change_records失败，订单号：" + orderinfo.getOrderNo() + "原因： " + e.getMessage());
			} else {
				LOG.error("插入order_change_records失败，参数无数据,原因： " + e.getMessage());
			}
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
		return rs;

	}

	private int doOrderDetailsChangeInsert(OrderDetailsBean orderDetails, int operationType, int adminId) {

		Connection conn = DBHelper.getInstance().getConnection();
		String querySql = "insert into order_details_change_records(order_no,ods_id,goods_id,goods_price,goods_number,ods_status,"
				+ "purchase_status,user_id,admin_id,operation_type) values(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stm = null;
		int rs = 0;
		try {

			stm = conn.prepareStatement(querySql);
			stm.setString(1, orderDetails.getOrderid());
			stm.setInt(2, orderDetails.getId());
			stm.setInt(3, orderDetails.getGoodsid());
			stm.setString(4, orderDetails.getGoodsprice());
			stm.setInt(5, orderDetails.getYourorder());
			stm.setInt(6, orderDetails.getState());
			stm.setInt(7, orderDetails.getPurchase_state());
			stm.setInt(8, orderDetails.getUserid());
			stm.setInt(9, adminId);
			stm.setInt(10, operationType);
			rs = stm.executeUpdate();
			return rs;

		} catch (Exception e) {
			e.printStackTrace();
			if (orderDetails != null) {
				LOG.error("插入order_details_change_records失败，订单号：" + orderDetails.getOrderid() + ",商品id:" + orderDetails.getGoodsid()
						+ ",原因： " + e.getMessage());
			} else {
				LOG.error("插入order_details_change_records失败，参数无数据,原因： " + e.getMessage());
			}
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
		return rs;

	}

}
