package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.daoimp.IFilterDataDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**搜索过滤
 * @author abc
 *
 */
public class FilterDataDao implements IFilterDataDao{
	
	public int getStoreFilter(String url){
		String sql = "select count(*) from filter_data where valid!=0 and filter=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, url);
			set = stmt.executeQuery();
			while(set.next()){
				result = set.getInt("count(*)");
			}
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
			if (set != null) {
				try {
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}
	

}
