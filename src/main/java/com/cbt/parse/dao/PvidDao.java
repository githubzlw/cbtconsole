package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.PvidBean;
import com.cbt.parse.daoimp.IPvidDao;
import com.cbt.parse.service.TypeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class PvidDao implements IPvidDao {

	@Override
	public int add(PvidBean bean) {
		String sql = "insert ali_pvidlist (name,value,pvid,img,time) values(?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getName());
			stmt.setString(2, bean.getValue());
			stmt.setString(3, bean.getPvid());
			stmt.setString(4, bean.getImg());
			stmt.setString(5, TypeUtils.getTime());
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
	@Override
	public int update(PvidBean bean) {
		String sql = "update ali_catpvd set name=?,value=?,img=?,time=? where pvid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getName());
			stmt.setString(2, bean.getValue());
			stmt.setString(3, bean.getImg());
			stmt.setString(4, TypeUtils.getTime());
			stmt.setString(5, bean.getPvid());
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
	@Override
	public ArrayList<PvidBean> query(String pvid) {
		String sql = "select distinct name,value,img from ali_pvidlist where pvid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ArrayList<PvidBean> list = new ArrayList<PvidBean>();
		PvidBean bean = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pvid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = new PvidBean();
				bean.setName(rs.getString("name"));
				bean.setValue(rs.getString("value"));
				bean.setImg(rs.getString("img"));
				list.add(bean);
				bean = null;
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}
	@Override
	public int queryExsis(String pvid) {
		String sql = "select count(*) from ali_pvidlist where pvid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, pvid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("count(*)");
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return result;
	}
	
	public ArrayList<PvidBean> queryTest(String name,String value) {
		String sql = "select name,value,img,pvid from ali_pvidlist where name=? and value=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ArrayList<PvidBean> list = new ArrayList<PvidBean>();
		PvidBean bean = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, value);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = new PvidBean();
				bean.setName(rs.getString("name"));
				bean.setValue(rs.getString("value"));
				bean.setImg(rs.getString("img"));
				bean.setPvid(rs.getString("pvid"));
				list.add(bean);
				bean = null;
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		return list;
	}


}
