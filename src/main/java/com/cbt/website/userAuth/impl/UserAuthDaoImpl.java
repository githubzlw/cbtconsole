package com.cbt.website.userAuth.impl;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.userAuth.Dao.UserAuthDao;
import com.cbt.website.userAuth.bean.AuthInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserAuthDaoImpl implements UserAuthDao {

	@Override
	public List<AuthInfo> getUserAuth(String admName) throws Exception {
		StringBuffer sql=new StringBuffer("SELECT DISTINCT a.* from tbl_auth_info a,tbl_userauth_info u where del=0 ");
		sql.append("AND a.authID in (select authId from tbl_userauth_info where admName = '");
		if (admName != null && !"".equals(admName)) {
			sql.append(admName + "')");
		}
		sql.append(" order by module_type,orderNo");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		List<AuthInfo> list = new ArrayList<AuthInfo>();
		try {
			stmt = conn.prepareStatement(sql.toString());	
			rs = stmt.executeQuery();
			while(rs.next()){
				AuthInfo authinfo = new AuthInfo();
				authinfo.setAuthId(rs.getInt("authId"));
				authinfo.setAuthName(rs.getString("authName"));
				authinfo.setUrl(rs.getString("url"));
				authinfo.setReMark(rs.getString("reMark"));
				authinfo.setModuleType(rs.getInt("module_type"));
				authinfo.setOrderNo(rs.getInt("orderNo"));
				list.add(authinfo);
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

	@Override
	public int getUserAuthCount(String admName, String url) throws Exception {
		StringBuffer sql=new StringBuffer("select count(distinct admName) from tbl_userauth_info where 1=1 "
				+ " and admName = 'yin' and authId = (select authId from tbl_auth_info where url = '/website/shopping_cart.jsp')");
		if (admName != null && !"".equals(admName)) {
			sql.append("and admName = '" + admName +"' ");
		}
		if (url != null && !"".equals(url)) {
			sql.append("and authId = (select authId from tbl_auth_info where url = '" + url +"')");
		}
		
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		int count = 0;
		try {
			stmt = conn.prepareStatement(sql.toString());	
			rs = stmt.executeQuery();
			if(rs.next()){
				count=rs.getInt("count");
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
		return count;
	}

	@Override
	public int deleteUserAuth(String admName) throws Exception {
		String sql = null;
		if (admName != null || "".equals(admName)) {
			sql = "delete from tbl_userauth_info where admName = '"+admName+"'";
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			res = stmt.executeUpdate();
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public int[] insertUserAuth(String admName, String[] auth) throws Exception {
		//先删除用户权限
		deleteUserAuth(admName);
		String sql = "insert into tbl_userauth_info(admName,authId) values (?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res[] = {};
		try {
			stmt = conn.prepareStatement(sql);
			if(admName != null && auth.length>0){
				for(String str: auth){
					stmt.setString(1, admName);
					stmt.setString(2, str);
					stmt.addBatch();
				}
				res = stmt.executeBatch();
			}						
		}catch (Exception e) {
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
		return res;
	}
}
