package com.cbt.website.userAuth.impl;

import com.cbt.jdbc.DBHelper;
import com.cbt.util.Cache;
import com.cbt.website.userAuth.Dao.AuthInfoDao;
import com.cbt.website.userAuth.bean.AuthInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthInfoDaoImpl implements AuthInfoDao {

	@Override
	public AuthInfo getAuthInfo(String authName, String url) {
		StringBuffer sql=new StringBuffer("select * from tbl_auth_info where del=0 ");
		if (authName != null && !"".equals(authName)) {
			sql.append("and authName = '" + authName + "' ");
		}
		if (url != null && !"".equals(url)) {
			sql.append("and url = '" + url + "' ");
		}
		sql.append(" order by orderNo");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		AuthInfo authInfo = null;
		try {
			stmt = conn.prepareStatement(sql.toString());	
			rs = stmt.executeQuery();
			while(rs.next()){
				authInfo= new AuthInfo();
				authInfo.setAuthId(rs.getInt("authId"));
				authInfo.setAuthName(rs.getString("authName"));
				authInfo.setUrl(rs.getString("url"));
				authInfo.setReMark(rs.getString("reMark"));
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
		return authInfo;
	}

	@Override
	public void saveAllAuth() throws Exception {
		StringBuffer sql=new StringBuffer("select * from tbl_auth_info where del=0 order by orderNo ");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			stmt = conn.prepareStatement(sql.toString());	
			rs = stmt.executeQuery();
			while(rs.next()){
				AuthInfo auth = new AuthInfo();
				auth.setAuthId(rs.getInt("authId"));
				auth.setAuthName(rs.getString("authName"));
				auth.setUrl(rs.getString("url"));
				auth.setReMark(rs.getString("reMark"));
				Cache.saveCache(auth);
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
	}

}
