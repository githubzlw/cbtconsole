package com.cbt.processes.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.util.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CurrencyDao implements ICurrencyDao {

	@Override
	public int upUserCurrency(int userid, String currency,double exchange_rate) {
		String sql = "update user set available_m=convert(available_m*?,decimal(10,2)),applicable_credit=convert(applicable_credit*?,decimal(10,2)),currency=? where id=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, exchange_rate);
			stmt.setDouble(2, exchange_rate);
			stmt.setString(3, currency);
			stmt.setInt(4, userid);
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
	public int upGoodCurrency(int userid, String sessionId, String currency,double exchange_rate) {
		String sql = "update goods_car set googs_price=convert(googs_price*?,decimal(10,2)),currency=? where state=0 ";
		if(Utility.getStringIsNull(sessionId)){
			sql += " and sessionid=? ";
		}else{
			sql += "and userid = ?";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, exchange_rate);
			stmt.setString(2, currency);
			if(Utility.getStringIsNull(sessionId)){
				stmt.setString(3, sessionId);
			}else{
				stmt.setInt(3, userid);
			}
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

}
