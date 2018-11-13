package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.daoimp.IGoodsHtmlDao;
import com.cbt.parse.service.TypeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class GoodsHtmlDao implements IGoodsHtmlDao {

	@Override
	public int add(String key, String file, String url) {
		String sql = "insert goodshtml (keyname,file,time,url) values(?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, key);
			stmt.setString(2, file);
			stmt.setString(3, TypeUtils.getTime());
			stmt.setString(4, url);
			result = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public int update(String key, String file, String url) {
		String sql = "update goodshtml set file=?,time=?,url=? where keyname=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, file);
			stmt.setString(2, TypeUtils.getTime());
			stmt.setString(3, url);
			stmt.setString(4, key);
			result = stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}

	@Override
	public ArrayList<HashMap<String, String>> query(String key) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		String sql = "select  distinct file,time,url from goodshtml where keyname=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		HashMap<String, String> map = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, key);
			rs = stmt.executeQuery();
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("file", rs.getString("file"));
				map.put("time", rs.getString("time"));
				map.put("url", rs.getString("url"));
				list.add(map);
				map = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt!=null){
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}

}
