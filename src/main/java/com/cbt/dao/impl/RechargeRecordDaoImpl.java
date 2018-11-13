package com.cbt.dao.impl;

import com.cbt.bean.RechargeRecord;
import com.cbt.dao.IRechargeRecordSSMDao;
import com.cbt.jdbc.DBHelper;
import com.cbt.util.Util;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RechargeRecordDaoImpl implements IRechargeRecordSSMDao {

	@Override
	public RechargeRecord getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer update(RechargeRecord t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer add(RechargeRecord t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RechargeRecord> findRecordByUid(Integer uid) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RechargeRecord> list = new ArrayList<RechargeRecord>();
		String sql = "select r.id,r.datatime,r.remark_id,r.currency,r.balanceAfter,"
				+ "r.usesign,r.type,r.price,p.paytype,p.paymentid from recharge_record r"
				+ " LEFT JOIN payment p on p.orderid LIKE r.remark_id where r.userid=? "
				+ "and r.type in(1,7) order by  r.datatime desc";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, uid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				RechargeRecord record = new RechargeRecord();
				record.setId(rs.getInt(1));
				record.setDatatime(rs.getString(2));
				record.setRemark_id(rs.getString(3));
				record.setCurrency(rs.getString(4));
				record.setBalanceAfter(rs.getDouble(5));
				record.setUsesign(rs.getInt(6));
				record.setType(rs.getInt(7));
				record.setPrice(rs.getDouble(8));
				record.setPaytype(rs.getInt(9));
				record.setPaymentId(rs.getString(10));

				DateFormat df = new SimpleDateFormat("yyyy-M-d HH:mm:ss");

				long time_long1 = 0;
				long time_long2 = 0;
				time_long1 = df.parse("2016-03-10 00:00:00").getTime();
				time_long2 = df.parse(rs.getString(2)).getTime();
				if (time_long1 >= time_long2) {
					record.setAfterBalanceshow("N/A");
				} else {
					record.setAfterBalanceshow(
							Util.currencyChange(record.getCurrency()) + Double.toString(record.getBalanceAfter()));
				}
				record.setCurrencyshow(Util.currencyChange(record.getCurrency()));
				list.add(record);
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
	public List<RechargeRecord> findRefundRecordsByUid(Integer uid) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RechargeRecord> list = new ArrayList<RechargeRecord>();
		String sql = "select r.id,r.datatime,r.remark_id,r.currency,r.balanceAfter,r.usesign,"
				+ "r.type,r.price,p.paytype,p.paymentid from recharge_record r "
				+ "LEFT JOIN payment p on p.orderid LIKE r.remark_id where "
				+ "r.userid=? and r.type in(8,9) order by  r.datatime desc";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, uid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				RechargeRecord record = new RechargeRecord();
				record.setId(rs.getInt(1));
				record.setDatatime(rs.getString(2));
				record.setRemark_id(rs.getString(3));
				record.setCurrency(rs.getString(4));
				record.setBalanceAfter(rs.getDouble(5));
				record.setUsesign(rs.getInt(6));
				record.setType(rs.getInt(7));
				record.setPrice(rs.getDouble(8));
				record.setPaytype(rs.getInt(9));
				record.setPaymentId(rs.getString(10));
				list.add(record);
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
	public List<RechargeRecord> getRecordList(Integer uid, int page) {
		String sql = "select sql_calc_found_rows *from recharge_record where userid=? "
				+ "order by  datatime desc limit ?,20";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PreparedStatement stmt2 = null;
		ResultSet rs2 = null;
		int count = 0;
		List<RechargeRecord> list = new ArrayList<RechargeRecord>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, uid);
			stmt.setInt(2, (page - 1) * 20);
			rs = stmt.executeQuery();
			stmt2 = conn.prepareStatement("select found_rows();");
			rs2 = stmt2.executeQuery();
			if (rs2.next()) {
				count = rs2.getInt("found_rows()");
			}
			while (rs.next()) {
				RechargeRecord record = new RechargeRecord();
				record.setId(rs.getInt("id"));
				record.setDatatime(rs.getString("datatime"));
				record.setRemark_id(rs.getString("remark_id"));
				record.setCurrency(rs.getString("currency"));
				record.setBalanceAfter(rs.getDouble("balanceAfter"));
				record.setUsesign(rs.getInt("usesign"));
				record.setType(rs.getInt("type"));
				record.setPrice(rs.getDouble("price"));
				record.setUserid(rs.getInt("userid"));
				record.setRemark(rs.getString("remark"));
				record.setAdminUser(rs.getString("adminuser"));
				record.setCount(count);
				list.add(record);
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
			if (rs2 != null) {
				try {
					rs2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

	@Override
	public List<RechargeRecord> queryBalancePayRecords(int userId) {
		String sql = "select id,remark_id,currency,usesign,type,price,userid"
				+ " from recharge_record where userid=?"
				+ " and type = 7 and usesign = 1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RechargeRecord> list = new ArrayList<RechargeRecord>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				RechargeRecord record = new RechargeRecord();
				record.setId(rs.getInt("id"));
				record.setRemark_id(rs.getString("remark_id"));
				record.setCurrency(rs.getString("currency"));
				record.setUsesign(rs.getInt("usesign"));
				record.setType(rs.getInt("type"));
				record.setPrice(rs.getDouble("price"));
				record.setUserid(rs.getInt("userid"));
				list.add(record);
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
