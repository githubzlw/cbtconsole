package com.cbt.website.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.bean.EmailSendInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailSendInfoDaoImpl implements EmailSendInfoDao {

	@Override
	public EmailSendInfo getEmailSendInfo(String orderNo, int userId) throws Exception{
		StringBuffer sql = new StringBuffer("select * from email_sendinfo where 1 = 1 ");
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("and orderid = '" + orderNo + "' ");
		}
		if (userId != 0) {
			sql.append("and userid = " + userId + " ");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EmailSendInfo emailsendinfo = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				emailsendinfo = new EmailSendInfo();
				emailsendinfo.setUserid(rs.getInt("userid"));
				emailsendinfo.setEmail(rs.getString("email"));
				emailsendinfo.setOrderid(rs.getString("orderid"));
				emailsendinfo.setOrderstate(rs.getInt("order_state"));
				emailsendinfo.setType(rs.getInt("type"));
				emailsendinfo.setResult(rs.getInt("result"));
				emailsendinfo.setInfo(rs.getString("info"));
				emailsendinfo.setCreatetime(rs.getString("createtime"));
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
		return emailsendinfo;
	}

	/**
	 * 根据订单号,用户id判断是否存在该邮件发送记录
	 */
	@Override
	public Boolean isExistEmailSendInfo(String orderNo, int userId,Integer type) throws Exception{
		StringBuffer sql = new StringBuffer("select * from email_sendinfo where 1 = 1 ");
		if (orderNo != null && !"".equals(orderNo)) {
			sql.append("and orderid = '" + orderNo + "' ");
		}
		if (userId != 0) {
			sql.append("and userid = " + userId + " ");
		}
		if (type != null) {
			sql.append("and type = " + type + " ");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Boolean result = false;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = true;
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
		return result;
	}

	@Override
	public int addEmailSendInfo(EmailSendInfo emailsendinfo) throws Exception{
		String sql = "insert into email_sendinfo(userid,email,orderid,order_state,type,result,createtime) values(?,?,?,?,?,?,now())";
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,emailsendinfo.getUserid());
			stmt.setString(2, emailsendinfo.getEmail());
			stmt.setString(3, emailsendinfo.getOrderid());
			stmt.setInt(4, emailsendinfo.getOrderstate());
			stmt.setInt(5, emailsendinfo.getType());
			stmt.setInt(6, emailsendinfo.getResult());
			result = stmt.executeUpdate();
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
		return result;
	}

}
