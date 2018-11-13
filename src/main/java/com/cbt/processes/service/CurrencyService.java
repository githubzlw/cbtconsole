package com.cbt.processes.service;

import com.cbt.jdbc.DBHelper;
import com.cbt.processes.dao.CurrencyDao;
import com.cbt.processes.dao.ICurrencyDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyService implements ICurrencyService {

	@Override
	public int upUserCurrency(int userid, String sessionId, String currency, double exchange_rate) {
		ICurrencyDao currencyDao = new CurrencyDao();
		int res =  0;
		if(userid != 0){
			res = currencyDao.upUserCurrency(userid, currency, exchange_rate);
		}
		if(res > 0 || userid == 0){
			res = currencyDao.upGoodCurrency(userid, sessionId, currency, exchange_rate);
		}
		return res;
	}

	@Override
	public double currencyConverter(String cur) {
		String sql = "select exchange_rate from exchange_rate where country = ?";

		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		double currency = 0.00;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cur);
			rs = stmt.executeQuery();
			while (rs.next()) {
				currency = rs.getDouble("exchange_rate");
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
		return currency;
	}

}
