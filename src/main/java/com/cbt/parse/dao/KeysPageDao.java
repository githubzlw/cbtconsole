package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.KeysPageBean;
import com.cbt.parse.daoimp.IKeysPageDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**keywords_r_page.sql
 * @author abc
 *
 */
public class KeysPageDao implements IKeysPageDao {

	@Override
	public int add(KeysPageBean bean) {
		String sql = "insert keywords_r_page (RectName,PageName,PageUrl,KeyWords,time) values(?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getRectName());
			stmt.setString(2, bean.getPageName());
			stmt.setString(3, bean.getPageUrl());
			stmt.setString(4, bean.getKeyWords());
			stmt.setDate(5, new java.sql.Date(new Date().getTime()));
			result = stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
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
	public int updateValid(String rectName,int valid) {
		String sql = "update keywords_r_page set valid=? where RectName=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, valid);
			stmt.setString(2,rectName);
			result = stmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public String querry(String keyword) {
		String result = null;
		String sql = "select PageUrl from keywords_r_page where match(KeyWords) against(? in boolean mode)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, "+"+keyword);
			rs = stmt.executeQuery();
			while(rs.next()){
				result = rs.getString("PageUrl");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

}
