package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**敏感词
 * @author abc
 *
 */
public class IntensveDao{
	public int add(String rex,String cid,String catid) {
		String sql = "insert intensve (word,cid,catid) values(?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, rex);
			stmt.setString(2, cid);
			stmt.setString(3, catid);
			
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
	public ArrayList<HashMap<String, String>> querryIntensive() {
		String sql = "select  *from intensve where valid=1";
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String> map = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("word", rs.getString("word"));
				map.put("cid", rs.getString("cid"));
				map.put("catid", rs.getString("catid"));
				list.add(map);
				map = null;
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
	
	public ArrayList<HashMap<String, String>> getCatidFilters() {
		String sql = "select  *from intensve where valid=1 and (word is null or word='')";
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String> map = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("word", rs.getString("word"));
				map.put("cid", rs.getString("cid"));
				map.put("catid", rs.getString("catid"));
				list.add(map);
				map = null;
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
	
	public int getCatidFilter(String catid) {
		String sql = "select  count(*) from intensve where valid=1 and (word is null or word='') and catid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, catid);
			rs = stmt.executeQuery();
			while(rs.next()){
				result = rs.getInt("count(*)");
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
	
	public ArrayList<String> querryExsis(String word,String cid,String catid) {
		String sql = "select cid from intensve where valid=1 and word=? and cid=? and catid=?";
		ArrayList<String> list = new ArrayList<String>();
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, word);
			stmt.setString(2,cid);
			stmt.setString(3, catid);
			rs = stmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("cid"));
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
