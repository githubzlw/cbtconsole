package com.cbt.change.util;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SyncDataInfoDao {

	public static void insertQueue(String sqlStr, int flag, String remark) {

		doInsert(sqlStr, flag, remark);
		/*if (check <= 0) {
			// 插入失败，重试3次
			for (int i = 0; i < 3; i++) {
				int rs = doInsert(sqlStr, flag, remark);
				if (rs > 0) {
					break;
				}
			}
		}*/

	}

	private static int doInsert(String sqlStr, int flag, String remark) {

		Connection conn = DBHelper.getInstance().getConnection();
		String querySql = "insert into syncdatainfo(sqlStr,creatTime,flag,remaek) " + "values(?,?,?,?)";
		PreparedStatement stm = null;
		int rs = 0;
		try {

			stm = conn.prepareStatement(querySql);
			stm.setString(1, sqlStr);
			stm.setDate(2, new Date(System.currentTimeMillis()));
			stm.setInt(3, flag);
			stm.setString(4, remark);
			rs = stm.executeUpdate();
			return rs;

		} catch (Exception e) {
			e.printStackTrace();
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
