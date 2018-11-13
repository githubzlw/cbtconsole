package com.cbt.website.dao;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateCustomFlagDaoImpl implements UpdateCustomFlagDao {

	@Override
	public int updateFlag(String emailaddress,String customId) {
		// TODO Auto-generated method stub
		String sql = "update custom_search set flag = 1 where email = ? and id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int  rs = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, emailaddress);
			stmt.setInt(2, Integer.parseInt(customId));
			rs = stmt.executeUpdate();
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
		return rs;
	}

}
