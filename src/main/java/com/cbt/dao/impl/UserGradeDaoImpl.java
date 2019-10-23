package com.cbt.dao.impl;

import com.cbt.bean.UserGradeBean;
import com.cbt.dao.UserGradeDao;
import com.cbt.jdbc.DBHelper;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserGradeDaoImpl implements UserGradeDao {

	@Override
	public List<UserGradeBean> getGrades() {
		String sql = "select gid,gname from grade_discount";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<UserGradeBean> list = new ArrayList<UserGradeBean>();
		try{
			stmt = conn.prepareStatement(sql);
			rs =stmt.executeQuery(); 
			while(rs.next()){
				list.add(new UserGradeBean(rs.getInt("gid"), rs.getString("gname")));
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
	public int updateGrade(int gid, int uid) {
		String sql = "update  user set grade=? where id=?";
//		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		int rs = 0;
		try{
//			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, gid);
//			stmt.setInt(2, uid);
//			rs =stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(gid));
			lstValues.add(String.valueOf(uid));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
		return rs;
	}

	@Override
	public List<UserGradeBean> getGradeDiscount() {
		String sql = "select u.grade as grade,g.gid as gid,g.valid as valid,createtime,g.discount as discount"
				+ " from user_grade u,grade_discount g where u.id=g.gid and g.valid=1";
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<UserGradeBean> list = new ArrayList<UserGradeBean>();
		try{
			stmt = conn.prepareStatement(sql);
			rs =stmt.executeQuery(); 
			while(rs.next()){
				UserGradeBean userGradeBean = new UserGradeBean();
				userGradeBean.setCreatetime(rs.getString("createtime"));
				userGradeBean.setDiscount(rs.getDouble("discount"));
				userGradeBean.setGid(rs.getInt("gid"));
				userGradeBean.setGrade(rs.getString("grade"));
				userGradeBean.setValid(rs.getInt("valid"));
				
				list.add(userGradeBean);
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
	public int updateGradeDiscount(int gid, double discount, int valid) {
		
		String sql = "update grade_discount set createtime=now()";
		if(valid!=0){
			sql +=",discount=?";
		}
		
		sql += ",valid=? where gid=?";
//		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		int rs = 0;
		try{
//			stmt = conn.prepareStatement(sql);
//			int index = 1;
//			if(valid!=0){
//				stmt.setDouble(index, discount);
//				index ++;
//			}
//			stmt.setInt(index, valid);
//			stmt.setInt(index+1, gid);
//			rs =stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			if(valid!=0){
				lstValues.add(String.valueOf(discount));
			}
			lstValues.add(String.valueOf(valid));
			lstValues.add(String.valueOf(gid));
			String runSql = DBHelper.covertToSQL(sql,lstValues);
			rs = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}

		return rs;
	}

	@Override
	public int isExsis(int gid) {
		String sql = "select  count(*) as c from grade_discount where gid=?";
		
		Connection conn = DBHelper.getInstance().getConnection2();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int result = 0;
		try{
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, gid);
			rs =stmt.executeQuery(); 
			while(rs.next()){
				result = rs.getInt("c");
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

	@Override
	public int addDicount(int gid, double discount) {
		String sql = "insert into  grade_discount (gid,discount,valid,createtime) values(?,?,?,now())";
		
//		Connection conn = DBHelper.getInstance().getConnection2();
//		PreparedStatement stmt = null;
		int result = 0;
		try{
//			stmt = conn.prepareStatement(sql);
//			stmt.setInt(1, gid);
//			stmt.setDouble(2, discount);
//			stmt.setInt(3, 1);
//			result =stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(gid));
			lstValues.add(String.valueOf(discount));
			lstValues.add(String.valueOf(1));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			result = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}

		return result;
	}

}
