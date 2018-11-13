package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.daoimp.IAliCategoryDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class EllyCategoryDao implements IAliCategoryDao {

	@Override
	public String getAliCategory(int aid, String type) {
		String sql = "select category from elly_category  where cid =(select case when  LOCATE(',', path)-1=-1 then cid else  left(path, LOCATE(',', path)-1) end  from ali_category where cid=?)";
		String sql1 = "select category from elly_category  where cid =(select case when  LOCATE(',', path)-1=-1 then cid else  left(path, LOCATE(',', path)-1) end  from ali_category where category = ?)";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String category = null;
		try {
			if(aid != 0){
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, aid);
			}else{
				stmt = conn.prepareStatement(sql1);
				stmt.setString(1, type);
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				category = rs.getString("category");
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
		return category;
	}
	@Override
	public String getCid(String cat_name,String cid_parent) {
		String sql = "select cid,path from elly_category  where category =? ";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String cid = null;
		if(cid_parent!=null&&!cid_parent.isEmpty()&&!"0".equals(cid_parent)){
			sql = sql+" and match (path) against (? in boolean mode)";
		}
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cat_name);
			if(cid_parent!=null&&!cid_parent.isEmpty()&&!"0".equals(cid_parent)){
				stmt.setString(2, " +"+cid_parent+"");
			}
			rs = stmt.executeQuery();
			if(rs.next()){
				cid = rs.getString("cid");
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
		return cid;
	}
	@Override
	public String getCname(String cid) {
		String sql = "select category from elly_category  where cid =?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		String category = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cid);
			rs = stmt.executeQuery();
			if(rs.next()){
				category = rs.getString("category");
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
		return category;
	}
	@Override
	public ArrayList<HashMap<String, String>> getAllCategory(String cid) {
		String sql = "select *from elly_category  where path like ?";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map  =null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, "%"+cid+",%");
			rs = stmt.executeQuery();
			while(rs.next()){
				map = new HashMap<String, String>();
				map.put("category", rs.getString("category"));
				map.put("cid", rs.getString("cid"));
				map.put("path", rs.getString("path"));
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

}
