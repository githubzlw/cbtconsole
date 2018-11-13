package com.cbt.parse.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.parse.bean.CatPvdBean;
import com.cbt.parse.daoimp.ICatPvdDao;
import com.cbt.parse.service.TypeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class CatPvdDao implements ICatPvdDao {

	@Override
	public int add(CatPvdBean bean) {
		String sql = "insert ali_catpvd (keyword,catid,catidlist,pvidlist,time) values(?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getKeyword());
			stmt.setString(2, bean.getCatid());
			stmt.setString(3, bean.getCatidlist());
			stmt.setString(4, bean.getPvidlist());
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
	public int update(CatPvdBean bean) {
		String sql = "update ali_catpvd set catidlist=?,pvidlist=?,time=? where keyword=? and catid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getCatidlist());
			stmt.setString(2, bean.getPvidlist());
			stmt.setString(3, TypeUtils.getTime());
			stmt.setString(4, bean.getKeyword());
			stmt.setString(5, bean.getCatid());
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
	public int updateCList(CatPvdBean bean) {
		String sql = "update ali_catpvd set catidlist=?,time=now() where keyword=? and catid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getCatidlist());
			stmt.setString(2, bean.getKeyword());
			stmt.setString(3, bean.getCatid());
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
	public int updatePList(CatPvdBean bean) {
		String sql = "update ali_catpvd set pvidlist=?,time=now() where keyword=? and catid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		int result = 0;
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, bean.getPvidlist());
			stmt.setString(2, bean.getKeyword());
			stmt.setString(3, bean.getCatid());
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
	public ArrayList<CatPvdBean> query(String keyword,String catid) {
		String sql = "select catidlist,pvidlist from ali_catpvd where keyword=? and catid=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ArrayList<CatPvdBean> list = new ArrayList<CatPvdBean>();
		CatPvdBean bean = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, keyword);
			stmt.setString(2, catid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				bean = new CatPvdBean();
				bean.setCatidlist(rs.getString("catidlist"));
				bean.setPvidlist(rs.getString("pvidlist"));
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
