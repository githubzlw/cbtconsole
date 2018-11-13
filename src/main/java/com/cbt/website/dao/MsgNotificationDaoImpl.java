package com.cbt.website.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.pojo.MessageNotification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;

public class MsgNotificationDaoImpl implements MsgNotificationDao {
	private static final Log LOG = LogFactory.getLog(MsgNotificationDaoImpl.class);

	@Override
	public void insertMessageNotification(MessageNotification messageNotification) throws Exception {

		String sql = "insert into message_notification(order_no, sender_id, send_type,send_content,"
				+ "link_url,create_time, is_read,refund_id,reservation1,reservation2,reservation3,message_id)"
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String orderNo = messageNotification.getOrderNo() == null ? "" : messageNotification.getOrderNo();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderNo);
			stmt.setInt(2, messageNotification.getSenderId());
			stmt.setInt(3, messageNotification.getSendType());
			stmt.setString(4, messageNotification.getSendContent());
			stmt.setString(5, messageNotification.getLinkUrl() == null ? "" : messageNotification.getLinkUrl());
			stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			stmt.setString(7, "N");
			stmt.setInt(8, messageNotification.getRefundId());
			stmt.setString(9, messageNotification.getReservation1());
			stmt.setString(10, messageNotification.getReservation2());
			stmt.setString(11, messageNotification.getReservation3());
			stmt.setInt(12, messageNotification.getMessage_id());
			stmt.execute();
		} catch (Exception e) {
			LOG.error("插入消息提醒失败,订单号:" + orderNo + ",原因:" + e.getMessage());
			throw new Exception(e);
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
	}

	public int queryAdmuserId(String username) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int admuserid = 0;
		String sql = "select adminid from tb_1688_accounts where account='" + username + "'";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				admuserid = rs.getInt("adminid");
			}
		} catch (SQLException e) {
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
		return admuserid;
	}
	
	@Override
	public void updateTbStatus(String shipno) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		String sql="update taobao_1688_order_history set is_processing=1 where shipno='"+shipno+"'";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.executeUpdate();
		} catch (SQLException e) {
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
	}

	public int getInfoSendId(String id) {
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int sender_id = 0;
		String sql = "select reservation1 from message_notification where id='" + id + "'";
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				sender_id = rs.getInt("reservation1");
			}
		} catch (SQLException e) {
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
		return sender_id;
	}

	@Override
	public void deleteBySendIdAndType() throws Exception {

		String sql = "delete from message_notification where sender_id = 15 and send_type = 6";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.execute();
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
	}

	@Override
	public void insertBySendIdAndType(int count) throws Exception {

		String sql = "insert into message_notification(sender_id, send_type,send_content,create_time, is_read)"
				+ "values(?,?,?,?,?)";
		String secondSql = "insert into message_notification(sender_id, send_type,send_content,create_time, is_read)"
				+ "values(?,?,?,?,?)";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		PreparedStatement secondStmt = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, 15);// Eric的id 15
			stmt.setInt(2, 6);
			stmt.setString(3, "有" + count + " 个订单已审核，待出库");
			stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			stmt.setString(5, "N");
			stmt.execute();

			// 单独添加给ling提醒
			secondStmt = conn.prepareStatement(secondSql);
			secondStmt.setInt(1, 1);// Ling的id 1
			secondStmt.setInt(2, 6);
			secondStmt.setString(3, "有" + count + " 个订单已审核，待出库");
			secondStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			secondStmt.setString(5, "N");
			secondStmt.execute();
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
			if (secondStmt != null) {
				try {
					secondStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}

	}

	@Override
	public MessageNotification queryExistsMsgByType(String orderid, String sendContent, int senderId, int type,
                                                    int remarkUserId) throws Exception {

		String sql = "select id, order_no, sender_id, send_type,send_content, link_url,create_time,"
				+ "is_read,refund_id,reservation1,reservation2,reservation3 "
				+ "from message_notification where date(create_time) =date(sysdate()) and order_no =? and sender_id =? "
				+ "and send_type =? and ifnull(reservation1,'') =? and send_content =?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		MessageNotification msgNtf = new MessageNotification();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, orderid);
			stmt.setInt(2, senderId);
			stmt.setInt(3, type);
			stmt.setString(4, String.valueOf(remarkUserId));
			stmt.setString(5, sendContent);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				msgNtf.setId(rs.getInt("id"));
				msgNtf.setOrderNo(rs.getString("order_no"));
				msgNtf.setSenderId(rs.getInt("sender_id"));
				msgNtf.setSendType(rs.getInt("send_type"));
				msgNtf.setSendContent(rs.getString("send_content"));
				msgNtf.setLinkUrl(rs.getString("link_url"));
				msgNtf.setCreateTime(rs.getTimestamp("create_time"));
				msgNtf.setIsRead(rs.getString("is_read"));
				msgNtf.setRefundId(rs.getInt("refund_id"));
				msgNtf.setReservation1(rs.getString("reservation1"));
				msgNtf.setReservation2(rs.getString("reservation2"));
				msgNtf.setReservation3(rs.getString("reservation3"));
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
		return msgNtf;
	}

	@Override
	public MessageNotification queryExistsSysMsg(int senderId, String sendContent, int remarkUserId) throws Exception {

		String sql = "select id, order_no, sender_id, send_type,send_content, link_url,create_time,"
				+ "is_read,refund_id,reservation1,reservation2,reservation3 "
				+ "from message_notification where sender_id =? "
				+ "and send_type =0 and ifnull(reservation1,'') =? and send_content=?";
		Connection conn = DBHelper.getInstance().getConnection();
		PreparedStatement stmt = null;
		MessageNotification msgNtf = new MessageNotification();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, senderId);
			stmt.setString(2, String.valueOf(remarkUserId));
			stmt.setString(3, sendContent);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				msgNtf.setId(rs.getInt("id"));
				msgNtf.setOrderNo(rs.getString("order_no"));
				msgNtf.setSenderId(rs.getInt("sender_id"));
				msgNtf.setSendType(rs.getInt("send_type"));
				msgNtf.setSendContent(rs.getString("send_content"));
				msgNtf.setLinkUrl(rs.getString("link_url"));
				msgNtf.setCreateTime(rs.getTimestamp("create_time"));
				msgNtf.setIsRead(rs.getString("is_read"));
				msgNtf.setRefundId(rs.getInt("refund_id"));
				msgNtf.setReservation1(rs.getString("reservation1"));
				msgNtf.setReservation2(rs.getString("reservation2"));
				msgNtf.setReservation3(rs.getString("reservation3"));
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
		return msgNtf;
	}

}
