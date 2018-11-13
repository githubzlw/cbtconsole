package com.cbt.website.userAuth.impl;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.bean.Admuser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdmUserDaoImpl implements AdmUserDao {

	@Override
	public Admuser getAdmUser(String name, String pass) throws Exception {
		StringBuffer sql = new StringBuffer("select * from admuser where 1=1 ");
		if (name != null && !"".equals(name)) {
			sql.append("and admName = '" + name + "' ");
		}
		if (pass != null && !"".equals(pass)) {
			sql.append("and password = md5('" + pass + "')");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Admuser admuser = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
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
		return admuser;
	}
	@Override
	public Admuser getAdmUserMd5(String name, String pass) throws Exception {
		StringBuffer sql = new StringBuffer("select * from admuser where 1=1 ");
		if (name != null && !"".equals(name)) {
			sql.append("and admName = '" + name + "' ");
		}
		if (pass != null && !"".equals(pass)) {
			sql.append("and password = '" + pass + "'");
		}
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Admuser admuser = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
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
		return admuser;
	}
	@Override
	public Admuser getAdmUserById(int id) throws Exception {
		String sql = "select * from admuser where id= " + id;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Admuser admuser = null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
			}
		} catch (Exception e) {
			throw new Exception(e);
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
		return admuser;
	}

	@Override
	public List<String> getAllAdmuser(String admName) throws Exception {
		StringBuffer sql = new StringBuffer("select distinct admName from admuser where 1=1 and status =1");
		if (admName != null && !"".equals(admName)) {
			sql.append(" and admName != '" + admName + "'");
		}
		
		sql.append(" order by status desc,admName");
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> userList = new ArrayList<String>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				userList.add(rs.getString("admName"));
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
		return userList;
	}

	@Override
	public Admuser getAdmUserFromUser(int userid) throws Exception {
		String sql = "select * from admuser where id in (select adminid from admin_r_user where userid = ?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Admuser admuser = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
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
		return admuser;
	}

	@Override
	public List<Admuser> queryForList() throws Exception {

		String sql = "select * from admuser where 1=1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<Admuser> admusers = new ArrayList<Admuser>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				Admuser admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
				admusers.add(admuser);
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
		return admusers;
	}

	@Override
	public List<Admuser> queryByRoleType(int roleType) throws Exception {

		String sql = "select * from admuser where 1=1 and roleType = " + roleType;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<Admuser> admusers = new ArrayList<Admuser>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				Admuser admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
				admusers.add(admuser);
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
		return admusers;
	}
	/**
	 * 根据orderid,goodsid获取采购人员
	 * @param orderid
	 * @param goodsid
	 * @return 采购人员ID
	 * @author 王宏杰 2017-07-27
	 */
	@Override
	public int queryByBuyerOrderNo(String orderid, String goodsid) {
		int admuserid=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql="SELECT admuserid FROM goods_distribution WHERE orderid=? AND goodsid=?";
		try{
			stmt=conn.prepareStatement(sql);
		    stmt.setString(1, orderid);
		    stmt.setString(2, goodsid);
		    rs=stmt.executeQuery();
		    if(rs.next()){
		    	admuserid=rs.getInt("admuserid");
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			DBHelper.getInstance().closeResultSet(rs);
			DBHelper.getInstance().closePreparedStatement(stmt);
			DBHelper.getInstance().closeConnection(conn);
		}
		return admuserid>0?admuserid:1;
	}

	@Override
	public List<Admuser> queryByOrderNo(String orderNo) throws Exception {

		List<Admuser> admusers = new ArrayList<Admuser>();
		String sql = "select admuser.* from admuser,admin_r_user, orderinfo "
				+ "where admuser.id = admin_r_user.adminid and admin_r_user.userid = orderinfo.user_id "
				+ "and orderinfo.order_no = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Admuser admuser = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
				admusers.add(admuser);
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
		return admusers;

	}
	
	@Override
	public Admuser querySalesByOrderNoAndGoodid(String orderNo, int goodid)
			throws Exception {
		Admuser admuser = new Admuser();
		String sql="SELECT a.* FROM admuser a INNER JOIN admin_r_user ar ON a.id=ar.adminid INNER JOIN orderinfo o ON ar.userid=o.user_id WHERE o.order_no=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			rs = stmt.executeQuery();
			while (rs.next()) {
				admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
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
		return admuser;
	}

	@Override
	public Admuser queryByOrderNoAndGoodid(String orderNo, int goodid) throws Exception {

		Admuser admuser = new Admuser();
		String sql = "select admuser.* from admuser,goods_distribution "
				+ "where admuser.id = goods_distribution.admuserid and goods_distribution.orderid = ? "
				+ "and goods_distribution.goodsid = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, goodid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				admuser = new Admuser();
				admuser.setId(rs.getInt("id"));
				admuser.setAdmName(rs.getString("admName"));
				admuser.setEmail(rs.getString("email"));
				admuser.setPassword(rs.getString("password"));
				admuser.setTitle(rs.getString("title"));
				admuser.setRoletype(rs.getString("roleType"));
				admuser.setStatus(rs.getString("status"));
				admuser.setEmialpass(rs.getString("emailpass"));
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
		return admuser;
	}

}
