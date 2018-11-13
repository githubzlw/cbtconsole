package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.daoimp.IImgFileDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ImgFileDao implements IImgFileDao {
	public int add(String imgurl,String file,String url,int flag){
		int result = 0;
		String sql = "insert imgfile (imgurl,file,url,flag,createtime) value (?,?,?,?,now())";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, imgurl);
			stmt.setString(2, file);
			stmt.setString(3, url);
			stmt.setInt(4, flag);
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
	public int queryExsis(String imgurl,String file,String url){
		int result = 0;
		String sql = "select count(*) from imgfile where imgurl=? and file=? and url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, imgurl);
			stmt.setString(2, file);
			stmt.setString(3, url);
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
	
	public ArrayList<String> query(String imgurl,String url){
		String sql = "select distinct file from imgfile where imgurl=? and url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, imgurl);
			stmt.setString(2, url);
			set = stmt.executeQuery();
			while(set.next()){
				list.add(set.getString("file"));
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
		return list;
	}
	
	public ArrayList<HashMap<String, String>> queryTest(int id1,int id2){
		String sql = "select distinct flag,file,url,imgurl from imgfile where id > ? and id < ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set =null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String>  map = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id1);
			stmt.setInt(2, id2);
			set = stmt.executeQuery();
			while(set.next()){
				map = new HashMap<String, String>();
				map.put("file", set.getString("file"));
				map.put("url",  set.getString("url"));
				map.put("imgurl",  set.getString("imgurl"));
				map.put("flag",  set.getString("flag"));
				list.add(map);
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
		return list;
	}
	
	public int addFlag(String imgurl,String file,String url){
		int result = 0;
		String sql = "update imgfile set upload=? where imgurl=? and file=? and url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 1);
			stmt.setString(2, imgurl);
			stmt.setString(3, file);
			stmt.setString(4, url);
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
	
	public String getImgFile(String imgurl,String url){
		String result = "";
		String sql = "select *from imgfile where imgurl=? and url=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet set = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, imgurl);
			stmt.setString(2, url);
			set = stmt.executeQuery();
			if(set.next()){
				result = set.getString("file");
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
