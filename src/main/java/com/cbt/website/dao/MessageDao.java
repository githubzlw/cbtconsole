package com.cbt.website.dao;

import com.cbt.bean.Message;
import com.cbt.jdbc.DBHelper;
import com.mysql.jdbc.Statement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDao implements IMessageDao {

	@Override
	public int addMessage(int userid, String content, String codid) {
		String sql = "insert message(userid,content,createtime,codid) values(?,?,now(),?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int googds_id = 0;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, userid);
			stmt.setString(2, content);	
			stmt.setString(3, codid);
			int result = stmt.executeUpdate();
			 if (result == 1) {
					rs = stmt.getGeneratedKeys();
					if (rs.next()) {
						googds_id = rs.getInt(1);
					}
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
			DBHelper.getInstance().closeConnection(conn);
	}
	 
		return googds_id;
	}

	@Override
	public int delMessage(int messgeId) {
		String sql = "update  message set delstate=1 where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, messgeId);
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
	public List<Message> getMessage(int userid, int state) {
		String sql = "select id,userid,codid,content,state,createtime from message where userid=? and delstate=0 ";
		if(state != -1){
			sql += " and state=?";
		}
		sql += " group by state,createtime desc";
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Message> list = new ArrayList<Message>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			if(state != -1){
				stmt.setInt(2, state);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setUserid(rs.getInt("userid"));
				message.setCodid(rs.getString("codid"));
				message.setContent(rs.getString("content"));
				message.setState(rs.getInt("state"));
				Timestamp ts = rs.getTimestamp("createtime");
				message.setCreatetime(ts);
				list.add(message);
			}
		}catch (Exception e) {
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
	public int getMessageSize(int userid) {
		String sql = "select count(id) from message where userid=? and delstate=0 and state=0";
	 
		Connection conn = DBHelper.getInstance().getConnection();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		List<Message> list = new ArrayList<Message>();
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, userid);
			rs = stmt.executeQuery();
			if(rs.next()){
				res = rs.getInt(1);
			}
		}catch (Exception e) {
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
		return res;
	}

	@Override
	public int upMessage(int messgeId) {
		String sql = "update message set state=1 where id = ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, messgeId);
			res = stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return res;
	}

	@Override
	public int delMessage(String orderid, String content) {
		String sql = "update  message set delstate=1 where codid = ? and content like ?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		int res = 0;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setString(2, "%"+content+"%");
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

}
