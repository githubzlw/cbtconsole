package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.daoimp.ISaveKeyDao;
import com.cbt.parse.service.TypeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**保存搜索页面的静态网页数据
 * @author abc
 *
 */
public class SaveKeyDao implements ISaveKeyDao {
	
	/**添加一条数据信息
	 * @param 
	 * @return
	 */
	public  int addData(String keyword,String website,String file,String url,String isexists) {
		int result = 0;
			String sql = "insert savekey(keyword,website,time,file,url,isexists)values(?,?,?,?,?,?)";
			Connection conn = DBHelper.getInstance().getConnection();
			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, keyword);
				stmt.setString(2, website);
				stmt.setString(3, TypeUtils.getTime());
				stmt.setString(4, file);
				stmt.setString(5, url);
				stmt.setString(6, isexists);
				result = stmt.executeUpdate();
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
				DBHelper.getInstance().closeConnection(conn);
			}
		return result;
	}
	
	/**
	 * @param 
	 * @return
	 */
	public  int updateData(String keyword,String website,String file,String url,String isexists) {
		int result = 0;
		String sql = "update savekey set time =?,file=?,url=?,isexists=? where keyword=? and website=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, TypeUtils.getTime());
			stmt.setString(2, file);
			stmt.setString(3, url);
			stmt.setString(4, isexists);
			stmt.setString(5, keyword);
			stmt.setString(6, website);
			result = stmt.executeUpdate();
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
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}
	
	
	/**查询数据
	 * @return
	 */
	public ArrayList<HashMap<String, String>> queryData(String keyword,String website) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "select *from savekey where keyword=? and website=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String> rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, website);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new HashMap<String, String>();
				rsTree.put("id", String.valueOf(rs.getInt("id")));
				rsTree.put("keyword", rs.getString("keyword"));
				rsTree.put("website", rs.getString("website"));
				rsTree.put("time", rs.getString("time"));
				rsTree.put("file", rs.getString("file"));
				rsTree.put("url", rs.getString("url"));
				rsTree.put("isexists", rs.getString("isexists"));
				list.add(rsTree);
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
		return list;
	}

}
