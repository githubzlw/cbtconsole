package com.cbt.dao.impl;

import com.cbt.bean.Payment;
import com.cbt.dao.IPaymentSSMDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.refund.bean.RefundBeanExtend;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentDaoImpl implements IPaymentSSMDao {

	@Override
	public Payment getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer update(Payment t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer add(Payment t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Payment> getRefundAblePaymentByUid(Integer uid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Payment> list = new ArrayList<Payment>();
		//查询3个月以内的支付记录，以90天为3月
		String sql = "select id,createtime,paytype,paySID,payment_amount,payment_cc,orderdesc,orderid,username from payment "
				+ "WHERE userid=? AND paystatus=1 AND datediff(NOW(),createtime) <90 and paytype in(0,1,2)  ORDER BY createtime DESC";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, uid);
			rs= stmt.executeQuery();
			while (rs.next()) {
				Payment payment =new Payment();
				payment.setId(rs.getInt(1));
				payment.setCreatetime(rs.getString(2));
				payment.setPaytype(rs.getString(3));
				payment.setPaySID(rs.getString(4));
				payment.setPayment_amount(rs.getFloat(5));
				payment.setPayment_cc(rs.getString(6));
				payment.setOrderdesc(rs.getString(7));
				payment.setOrderid(rs.getString(8));
				payment.setUsername(rs.getString(9));
				list.add(payment);
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
		return list;
	}

	@Override
	public List<RefundBeanExtend> getUserCoustomRecordsByUid(Integer uid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RefundBeanExtend> list = new ArrayList<RefundBeanExtend>();
		//查询3个月以内的支付记录，以90天为3月
		String sql = "select id,createtime,paytype,paySID,payment_amount,payment_cc,orderdesc,orderid,username from payment WHERE userid=? AND paystatus=1   ORDER BY createtime DESC";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, uid);
			rs= stmt.executeQuery();
			while (rs.next()) {
				RefundBeanExtend payment =new RefundBeanExtend();
				payment.setPid(rs.getInt(1));
				payment.setPcreatetime(rs.getString(2));
				payment.setPpaytype(rs.getString(3));
				payment.setPpaySID(rs.getString(4));
				payment.setPpayment_amount(rs.getFloat(5));
				payment.setPpayment_cc(rs.getString(6));
				payment.setPorderdesc(rs.getString(7));
				payment.setPorderid(rs.getString(8));
				payment.setPusername(rs.getString(9));
				list.add(payment);
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
		return list;
	}

	@Override
	public List<Object[]> getUserRealChargeMoneyByUid(Integer uid) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		//查询3个月以内的支付记录，以90天为3月
		List<Object[]> list = new ArrayList<Object[]>();
		String sql = "select SUM(payment_amount) mount,payment_cc FROM payment WHERE userid = ? and paytype in(0,1) and paytype<>''  and paystatus=1  and username!='' AND paySID!='' group by payment_cc";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, uid);
			rs= stmt.executeQuery();
			while (rs.next()) {
				list.add(new Object[]{rs.getDouble(1),rs.getString(2)});
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
		return list;
	}

}
