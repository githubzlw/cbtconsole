package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author abc
 *
 */
public class WordsDao{
	
	
	public int addData(String word) {
		int result = 0;
		String sql = new StringBuffer().append("insert words(word) ")
				   .append("values(?)").toString();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, word);
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

	public ArrayList<HashMap<String, String>> querryData(String word) {
		// TODO Auto-generated method stub
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String sql = "select *from words where word=?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		HashMap<String, String> rsTree = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, word);
			rs = stmt.executeQuery();
			while(rs.next()){
				rsTree = new HashMap<String, String>();
				rsTree.put("id", String.valueOf(rs.getInt("id")));
				rsTree.put("word", rs.getString("word"));
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

	public int updateValid(String word) {
		int result = 0;
		String sql = new StringBuffer().append("update words set word=?")
					.append(" where word=?").toString();
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, word);
			stmt.setString(2, word);
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

}
