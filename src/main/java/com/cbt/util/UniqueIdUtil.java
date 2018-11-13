package com.cbt.util;

import com.cbt.jdbc.DBHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 从数据库中获取唯一id
 * 
 * @author JiangXianwei
 *
 */
public class UniqueIdUtil {
	private static final Log LOG = LogFactory.getLog(UniqueIdUtil.class);

	/**
	 * 调用存储过程获取唯一id
	 * 
	 * @return
	 */
	public static int queryByDb() {
		int uniqueId = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			cs = conn.prepareCall("{call queryUniqueId()}");
			rs = cs.executeQuery();
			if (rs.next()) {
				uniqueId = rs.getInt(1);
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("获取数据失败,原因:" + e.getMessage());
		} finally {
			DBHelper.getInstance().closeConnection(conn);
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return uniqueId;
	}
	
	/**
	 * 调用存储过程获取唯一id
	 * 
	 * @return
	 */
	public static int queryByDbForMessage() {
		int uniqueId = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			cs = conn.prepareCall("{call queryUniqueIdForMessage()}");
			rs = cs.executeQuery();
			if (rs.next()) {
				uniqueId = rs.getInt(1);
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("获取数据失败,原因:" + e.getMessage());
		} finally {
			DBHelper.getInstance().closeConnection(conn);
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return uniqueId;
	}

}
