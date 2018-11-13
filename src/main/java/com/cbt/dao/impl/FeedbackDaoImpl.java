package com.cbt.dao.impl;

import com.cbt.bean.FeedbackBean;
import com.cbt.dao.FeedbackDao;
import com.cbt.jdbc.DBHelper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FeedbackDaoImpl implements FeedbackDao {
	private static final Logger logger = Logger.getLogger(FeedbackDaoImpl.class);

	@Override
	public FeedbackBean queryById(Integer id) {
		String sql = "select id,type,content,otherComment,searchUrl,searchKeywords from feedback where id=" + id;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FeedbackBean feedback = new FeedbackBean();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				feedback.setId(rs.getInt("id"));
				feedback.setType(rs.getInt("type"));
				feedback.setContent(rs.getString("content"));
				feedback.setOtherComment(rs.getString("otherComment"));
				feedback.setSearchUrl(rs.getString("searchUrl"));
				feedback.setSearchKeywords(rs.getString("searchKeywords"));
			}
		} catch (Exception e) {
			logger.error("queryById error,reason:" + e.getMessage());
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
		return feedback;
	}

	@Override
	public List<FeedbackBean> queryByType(Integer type) {
		String sql = "select id,type,content,otherComment,searchUrl,searchKeywords from feedback where type=" + type;
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FeedbackBean> feedbacks = new ArrayList<FeedbackBean>();
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				FeedbackBean feedback = new FeedbackBean();
				feedback.setId(rs.getInt("id"));
				feedback.setType(rs.getInt("type"));
				feedback.setContent(rs.getString("content"));
				feedback.setOtherComment(rs.getString("otherComment"));
				feedback.setSearchUrl(rs.getString("searchUrl"));
				feedback.setSearchKeywords(rs.getString("searchKeywords"));
				feedbacks.add(feedback);
			}
		} catch (Exception e) {
			logger.error("queryByType error,reason:" + e.getMessage());
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
		return feedbacks;
	}

}
