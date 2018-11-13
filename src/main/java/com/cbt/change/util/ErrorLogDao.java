package com.cbt.change.util;

import com.cbt.jdbc.DBHelper;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ErrorLogDao {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ErrorLogDao.class);

	public static void insertErrorInfo(String updateTable, String sqlStr, int adminId, int flag, String remark) {

		Connection conn = DBHelper.getInstance().getConnection();
		String querySql = "insert into update_table_error_log(update_table,sqlStr,admin_id,flag,remark) "
				+ "values(?,?,?,?,?)";
		PreparedStatement stm = null;
		try {

			stm = conn.prepareStatement(querySql);
			stm.setString(1, updateTable);
			stm.setString(2, sqlStr);
			stm.setInt(3, adminId);
			stm.setInt(4, flag);
			stm.setString(5, remark);
			stm.executeUpdate();

		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("插入update_table_error_log失败，原因： " + e.getMessage());
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

	}

}
