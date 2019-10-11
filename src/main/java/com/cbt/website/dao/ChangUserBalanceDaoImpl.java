package com.cbt.website.dao;

import com.cbt.jdbc.DBHelper;
import com.cbt.website.util.JsonResult;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChangUserBalanceDaoImpl implements ChangUserBalanceDao {

	@SuppressWarnings("resource")
	@Override
	public JsonResult changeBalance(int userId, float amount, int operationType, int operationSource, String orderNo,
			String remark, int adminId) {
		JsonResult json = new JsonResult();
		if (userId == 0) {
			json.setOk(false);
			json.setMessage("客户id为0，程序终止执行");
			return json;
		} else if (amount <= 0) {
			json.setOk(false);
			json.setMessage("操作金额必须大于0");
			return json;
		} else if (!(operationType == -1 || operationType == 1)) {
			json.setOk(false);
			json.setMessage("操作类型为空，程序终止执行");
			return json;
		} else if (operationSource == 0) {
			json.setOk(false);
			json.setMessage("操作来源为空，程序终止执行");
			return json;
		} else if (adminId == 0) {
			json.setOk(false);
			json.setMessage("操作人id为空，程序终止执行");
			return json;
		}

		Connection localConn = DBHelper.getInstance().getConnection();
		Connection remoteConn = DBHelper.getInstance().getConnection2();
		PreparedStatement rmStamt = null;
		ResultSet rmRes = null;
		PreparedStatement lcStamt = null;
		String querySql = "select id,available_m from user where id = ?";
		String updateSql = "update user set available_m = ? where id = ?";
		String insertSql = "insert into customer_balance_change_log("
				+ "user_id,order_no,amount,operation_type,operation_source,befor_balance,"
				+ "after_balance,remark,admin_id,create_time) values(?,?,?,?,?,?,?,?,?,now())";

		float balance = 0;
		float afterBalance = 0;
		try {
			// 执行逻辑
			// 1.查询线上客户信息
			rmStamt = remoteConn.prepareStatement(querySql);
			rmStamt.setInt(1, userId);
			rmRes = rmStamt.executeQuery();
			if (rmRes.next()) {
				// 2.获取客户余额
				balance = rmRes.getFloat("available_m");
				// 3.判断操作类型是否是减去余额操作，如果是判断减去金额是否大于原有余额
				if (operationType == -1) {
					if (balance < amount) {
						json.setOk(false);
						json.setMessage("客户：" + userId + " 减去金额：[" + amount + "]，超过原有余额：" + balance);
						return json;
					}
					afterBalance = balance - amount;
				} else if (operationType == 1) {
					afterBalance = balance + amount;
				}

				// 4.进行余额表更操作

				/*rmStamt = remoteConn.prepareStatement(updateSql);
				rmStamt.setFloat(1, afterBalance);
				rmStamt.setInt(2, userId);
				rmStamt.executeUpdate() > 0;*/

				List<String> listValues = new ArrayList<>();
				listValues.add(String.valueOf(afterBalance));
				listValues.add(String.valueOf(userId));
				listValues.add(String.valueOf(userId));
				String runSql = DBHelper.covertToSQL(updateSql, listValues);
				String rsStr = SendMQ.sendMsgByRPC(new RunSqlModel(runSql));
				int countRs = 0;
				if(StringUtils.isBlank(rsStr)){
					countRs = Integer.valueOf(rsStr);
				}

				// 判断远程更新是否成功
				if (countRs > 0) {
					localConn.setAutoCommit(false);
					lcStamt = localConn.prepareStatement(insertSql);
					lcStamt.setInt(1, userId);
					lcStamt.setString(2, orderNo == null ? "" : orderNo);
					lcStamt.setFloat(3, amount);
					lcStamt.setString(4, String.valueOf(operationType));
					lcStamt.setString(5, String.valueOf(operationSource));
					lcStamt.setFloat(6, balance);
					lcStamt.setFloat(7, afterBalance);
					lcStamt.setString(8, remark == null ? "" : remark);
					lcStamt.setInt(9, adminId);

					int count = 0;
					int rs = 0;
					// 重试5次更新本地日志
					while (count < 5 && rs == 0) {
						rs = lcStamt.executeUpdate();
						count++;
					}
					if (rs == 0) {
						localConn.rollback();
						json.setOk(true);
						json.setMessage("更新线上客户余额成功，更新本地余额变更日志失败");
					} else {
						localConn.commit();
						json.setOk(true);
						json.setMessage("更新线上客户余额成功，更新本地余额变更日志成功");
					}

				} else {
					json.setOk(false);
					json.setMessage("更新线上客户余额失败，程序终止执行");
					return json;
				}

			} else {
				json.setOk(false);
				json.setMessage("获取客户信息失败，程序终止执行");
				return json;
			}

		} catch (Exception e) {
			e.printStackTrace();
			json.setOk(false);
			json.setMessage("更新失败，原因：" + e.getMessage());
		} finally {

			if (rmStamt != null) {
				try {
					rmStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (lcStamt != null) {
				try {
					lcStamt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (rmRes != null) {
				try {
					rmRes.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(localConn);
			DBHelper.getInstance().closeConnection(remoteConn);
		}
		return json;
	}

}
