package com.cbt.customer.dao;

import com.cbt.bean.Reply;
import com.cbt.jdbc.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReplyDaoImpl implements IReplyDao {

	@Override
	public int addReply(Reply rep) {
		String sql;
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		sql = "insert into reply(user_id,user_name,guestbook_id,reply_content,reply_time) values(?,?,?,?,?)";
		conn = DBHelper.getInstance().getConnection();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
		Date now = new Date();
		dateFormat.setLenient(false);
		try {
			Date parse = dateFormat.parse(dateFormat.format(now));
			stmt = conn.prepareStatement(sql, 
					PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, rep.getUserId());
			stmt.setString(2, rep.getUserName());
			stmt.setInt(3, rep.getGuestbookId());
			stmt.setString(4, rep.getReplyContent());
			stmt.setDate(5, new java.sql.Date(parse.getTime()));
			result = stmt.executeUpdate();
			if (result == 1) {
				stmt.getGeneratedKeys();
			}
			stmt.close();
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
	public List<Reply> findByPid(int guestbookId) {
		List<Reply> repList = new ArrayList<Reply>();;
		String sql = "select * from reply where guestbook_id=?";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		conn = DBHelper.getInstance().getConnection();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1,guestbookId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Reply rep = new Reply();
				rep.setId(rs.getInt("id"));
				rep.setUserId(rs.getInt("user_id"));
				rep.setUserName(rs.getString("user_name"));
				rep.setGuestbookId(rs.getInt("guestbook_id"));
				rep.setReplyContent(rs.getString("reply_content"));
				rep.setReplyTime(rs.getDate("reply_time"));
				repList.add(rep);
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
		return repList;
	}


}
