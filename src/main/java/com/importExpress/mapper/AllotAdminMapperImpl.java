package com.importExpress.mapper;

import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AllotAdminMapperImpl implements AllotAdminMapper{

	@Override
	public int exAdmin(int userid, String useremail) {
		int id=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		String sql="select id  from admin_r_user  where userid=? limit 1";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while(rs.next()){
				id=rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return id;
	}

	@Override
	public String seAdmin(int id) {
		String admName="";
		String sql="select admName  from admuser  where id=? limit 1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			while(rs.next()){
				admName=rs.getString("admName");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return admName;
	}

	@Override
	public int addAdminSaler(int adminid) {
		int res=0;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql="insert tab_admin_onedayallot(adminid) values(?)";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,adminid);
			res = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		DBHelper.getInstance().closeConnection(conn);
		return res;
	}

//	@Override
//	public int allotAdminSaler(int userid, String useremail, int adminid) {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public Integer getAllotAdmin() {
		Integer admuserid=0;
		String sql="select  adm.id as admuserid from (select a.id,count(a.id)cou  from admuser  a left join  tab_admin_onedayallot o on o.adminid=a.id where  automatic=1 group by  o.adminid order by cou limit 1) adm";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				admuserid=rs.getInt("admuserid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return admuserid;
	}

	@Override
	public Integer getAllotAdminPage(int userid, String sessionid, int type) {
		Integer admuserid=0;
		String sql="SELECT DISTINCT at.id,at.creatTime,oi.user_id,CASE WHEN utm_campaign='adult' THEN '21' WHEN utm_campaign='motherboard' THEN '22' WHEN utm_campaign='pet' THEN '21' "
					+ "WHEN utm_campaign='outer' THEN '22' WHEN utm_campaign='wig' THEN '22' WHEN utm_campaign='motherboard' THEN '22' WHEN "
					+ "utm_campaign='phone' THEN '16' END AS admuserid FROM advertising_tracking AT INNER JOIN orderinfo oi ON at.access_ip=oi.ip "
					+ "WHERE TO_DAYS( NOW( ) ) - TO_DAYS(creatTime)=178 AND oi.user_id=? ORDER BY id DESC LIMIT 1";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			while(rs.next()){
				admuserid=rs.getInt("admuserid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return admuserid;
	}
    
	@Override
	public Integer getAdmuserid(String admName) {
		Integer admuserid=0;
		String sql="select id from admuser where admName='"+admName+"'";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs=null;
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()){
				admuserid=rs.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
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
		}
		DBHelper.getInstance().closeConnection(conn);
		return admuserid;
	}
	
}
