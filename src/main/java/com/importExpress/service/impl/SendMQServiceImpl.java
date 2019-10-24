package com.importExpress.service.impl;

import com.cbt.jdbc.DBHelper;
import com.importExpress.service.SendMQService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import net.sf.json.JSONObject;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 用于通过mq方式更新线上数据并保存更新记录
 * @author 
 *
 */
@Service
public class SendMQServiceImpl implements SendMQService {
	
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SendMQServiceImpl.class);

	//通过mq更新线上数据并保存更新记录到本地 成功返回1 否则返回0
	@Override
	public int runSqlOnline(String kp, String sql) {
		Connection conn = DBHelper.getInstance().getConnection();
		// 在27上面保存通过mq更新线上数据的记录
        String insertSqlLog = "INSERT INTO `alidata`.`mq_update_log` (`pid`, `message`, `createtime`) VALUES (?, ?, NOW())";
		PreparedStatement stmt = null;

		try {
			//创建mq工具 用于同步线上数据

			RunSqlModel model= new RunSqlModel(sql);
			//保存记录
			stmt = conn.prepareStatement(insertSqlLog);
			stmt.setString(1, kp);
            stmt.setString(2, JSONObject.fromObject(model).toString());
            stmt.executeUpdate();
            //发送mq消息
            SendMQ.sendMsg(model);
            return 1;
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("SendMQServiceImpl异常，原因： ", e);
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
		return 0;
	}
	
}
