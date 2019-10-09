package com.cbt.dao.impl;

import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;
import com.cbt.dao.IComplainChatDao;
import com.cbt.jdbc.DBHelper;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import com.mysql.jdbc.Statement;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ComplianChatDaoImpl implements IComplainChatDao {

	@Override
	public Integer addChat(ComplainChat t) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int row=0;
		String sql = "insert into tb_complain_chat (complainid,chatText,chatTime,chatAdmin,chatAdminid,flag) values(?,?,now(),?,?,1)";
		String sql1= "select max(id) as maxId from tb_complain_chat ";
		conn = DBHelper.getInstance().getConnection2();
		try {
//			stmt=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
//			stmt.setInt(1, t.getComplainid());
//			stmt.setString(2, t.getChatText());
//			stmt.setString(3, t.getChatAdmin());
//			stmt.setInt(4, t.getChatAdminid());
//			row =stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(t.getComplainid()));
			lstValues.add(t.getChatText());
			lstValues.add(t.getChatAdmin());
			lstValues.add(String.valueOf(t.getChatAdminid()));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			row = Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));

//			rs = stmt.getGeneratedKeys();
//			if (rs.next()) {
//                int id = rs.getInt(1);
//                row =id ;
//            }

			stmt = conn.prepareStatement(sql1);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("maxId");
                row =id + 1;
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
		return row;
	}
	
	@Override
	public void add(ComplainFile t) {
		Connection conn = null;
		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		String sql="insert into tb_complain_file(complainid,imgUrl,delState,complainChatid,flag) values('"+t.getComplainid()+"','"+t.getImgUrl()+"',0,'"+t.getComplainChatid()+"',"+t.getFlag()+")";
		String sql="insert into tb_complain_file(complainid,imgUrl,delState,complainChatid,flag) values(?,?,0,?,?)";
//		conn = DBHelper.getInstance().getConnection2();
		try {
			stmt=conn.prepareStatement(sql);
			stmt.executeUpdate();

			List<String> lstValues = new ArrayList<String>();
			lstValues.add(String.valueOf(t.getComplainid()));
			lstValues.add(String.valueOf(t.getImgUrl()));
			lstValues.add(String.valueOf(t.getComplainChatid()));
			lstValues.add(String.valueOf(t.getFlag()));

			String runSql = DBHelper.covertToSQL(sql,lstValues);
			SendMQ.sendMsg(new RunSqlModel(runSql));



		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			if (stmt != null) {
//				try {
//					stmt.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//			DBHelper.getInstance().closeConnection(conn);
		}
	}

}
