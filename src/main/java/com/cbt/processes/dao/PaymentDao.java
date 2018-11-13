package com.cbt.processes.dao;

import com.cbt.bean.Payment;
import com.cbt.bean.RechargeRecord;
import com.cbt.jdbc.DBHelper;

import java.sql.*;

public class PaymentDao implements ProcessPaymentDao {
	
	@Override
	public void addPayment(Payment pay) throws Exception{
		// TODO Auto-generated method stub
		String sql = "insert payment(userid,orderid,paymentid,orderdesc,username,paystatus,payment_amount,payment_cc,payflag,paytype,createtime) values(?,?,?,?,?,?,?,?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, pay.getUserid());
			stmt.setString(2, pay.getOrderid());
			stmt.setString(3, pay.getPaymentid());
			stmt.setString(4, pay.getOrderdesc());
			stmt.setString(5, pay.getUsername());
			stmt.setInt(6, pay.getPaystatus());
			stmt.setFloat(7, pay.getPayment_amount());
			stmt.setString(8, pay.getPayment_cc());
			stmt.setString(9, pay.getPayflag());
			stmt.setString(10, pay.getPaytype());
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
			}
		} catch (Exception e) {
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
	}

	@Override
	public Payment getPayment(int userid, String orderid,String paymentid) {
		// TODO Auto-generated method stub
		String sql = "select * from payment where userid=? and orderid=? and paymentid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Payment pay = new Payment();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			stmt.setString(2, orderid);
			stmt.setString(3, paymentid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				pay.setOrderid(rs.getString("orderid"));
				pay.setOrderdesc(rs.getString("orderdesc"));
				pay.setPaystatus(rs.getInt("paystatus"));
				pay.setUsername(rs.getString("username"));
				pay.setPayment_amount(rs.getFloat("payment_amount"));
				pay.setPayment_cc(rs.getString("payment_cc"));
				pay.setPaymentid(rs.getString("paymentid"));
				pay.setCreatetime(rs.getString("createtime"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		
		return pay;
	}
	
	@Override
	public int getPayment(String paymentid) {
		// TODO Auto-generated method stub
		String sql = "select id from payment where paymentid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int id = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, paymentid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				id = rs.getInt("id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		
		return id;
	}

	@Override
	public void addRechargeRecord(RechargeRecord rr) {
		String sql = "insert recharge_record(userid,price,type,remark,remark_id,datatime,usesign) values(?,?,?,?,?,now(),?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;

		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, rr.getUserid());
			stmt.setDouble(2, rr.getPrice());
			stmt.setInt(3, rr.getType());
			stmt.setString(4, rr.getRemark());
			stmt.setString(5, rr.getRemark_id());
			stmt.setInt(6, rr.getUsesign());
			int result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
			}
		} catch (Exception e) {
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
	}
}
