package com.cbt.onlinesql.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.onlinesql.bean.OnlineDataInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OnlineDataInfoDaoImpl implements OnlineDataInfoDao {

	@Override
	public int insertOnlineDataInfo(OnlineDataInfo info) throws Exception {

		int success = 0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stm = null;
		PreparedStatement scStm = null;
		ResultSet rs = null;
		String insertSql = "insert into online_datainfo(unique_id,userid,orderid,business_type,table_name,sql_str) "
				+ "values(?,?,?,?,?,?)";
		try {
			int userid = info.getUserId();
			// 如果获取的数据用户id(userid)是0的,并且订单号(orderid)不为空的,自动补全userid
			if (userid == 0 && info.getOrderId() != null && !"".equals(info.getOrderId().trim())) {
				String querySql = "select user_id from orderinfo where order_no = ?";
				stm = conn.prepareStatement(querySql);
				stm.setString(1, info.getOrderId());
				rs = stm.executeQuery();
				if (rs.next()) {
					userid = rs.getInt("user_id");
				}
			}
			scStm = conn.prepareStatement(insertSql);
			scStm.setInt(1, info.getUniqueId());
			scStm.setInt(2, userid);
			scStm.setString(3, info.getOrderId());
			scStm.setString(4, info.getBusinessType() == null ? "" : info.getBusinessType());
			scStm.setString(5, info.getTableName());
			scStm.setString(6, info.getSqlStr());
			success = scStm.executeUpdate();
		} finally {
			DBHelper.getInstance().closeConnection(conn);
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (scStm != null) {
				try {
					scStm.close();
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
		return success;
	}

}
