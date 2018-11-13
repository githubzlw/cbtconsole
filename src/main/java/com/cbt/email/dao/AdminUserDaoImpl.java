package com.cbt.email.dao;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdminUserDaoImpl implements IAdminUserDao{

	@Override
	public String getemail(String email) {
		String sql = "select au.Email  from admin_r_user ad left join admuser au on au.id=ad.adminid where ad.useremail=? ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String address=null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if(rs.next()) {
				address=rs.getString(1);
			 	
				
				
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
		
		return address;
	}

	@Override
	public int getId(String email) {
		String sql = "select id  from user ad  where ad.email=? ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int id=0;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if(rs.next()) {
				id=rs.getInt(1);
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
		
		return id;
	}

	@Override
	public String getname(String email) {
		String sql = "select ad.admName  from admin_r_user ad  where ad.useremail=? ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String name=null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			rs = stmt.executeQuery();
			if(rs.next()) {
				name=rs.getString(1);
			 	
				
				
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
		
		return name;
	}

	@Override
	public int getid(int id) {
		String sql = "select ad.adminid  from admin_r_user ad  where ad.userid=? ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int name=0;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if(rs.next()) {
				name=rs.getInt(1);
			 	
				
				
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
		
		return name;
	}

}
