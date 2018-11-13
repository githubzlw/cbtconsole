package com.cbt.dao.impl;

import com.cbt.bean.LogBean;
import com.cbt.dao.LogDao;
import com.cbt.jdbc.DBHelper;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LogDaoImpl implements LogDao{

	@Override
	public int saveLog(LogBean log) {
		// TODO Auto-generated method stub
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		String sql = "insert into cbtlog(user_id,ip,time,type,year,month,day) values(?,?,?,?,?,?,?)";
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql, 
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, log.getUserId());
			stmt.setString(2, log.getIp());
			stmt.setString(3, log.getTime());
			stmt.setLong(4, log.getType());
			stmt.setString(5, log.getYear());
			stmt.setString(6, log.getMonth());
			stmt.setString(7, log.getDay());
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
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
	
}
