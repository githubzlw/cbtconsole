package com.cbt.change.util;

import com.cbt.bean.Orderinfo;
import com.cbt.jdbc.DBHelper;
import com.cbt.messages.ctrl.InsertMessageNotification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OnlineOrderUpdateThread implements Runnable {
	private static final Log LOG = LogFactory.getLog(OnlineOrderUpdateThread.class);

	private String orderNo;// 订单号
	private int adminId;// 操作人id
	private int operationType;// 操作类型，-1、6:订单取消,4:完结,3:出运,2:入库,1:采购,5 :审核中

	public OnlineOrderUpdateThread(String orderNo, int adminId, int operationType) {
		super();
		this.orderNo = orderNo;
		this.adminId = adminId;
		this.operationType = operationType;
	}

	@Override
	public void run() {

		try {

			// 判断执行的数据是否健全，不健全的添加日志记录，健全的执行更新操作
			if (this.orderNo == null || "".equals(this.orderNo) || this.adminId == 0 || this.operationType == 0) {
				// 插入到更新错误日志中
				String sqlStr = "";
				String remark = "传递参数失败，orderNo:" + this.orderNo + ",adminId:" + this.adminId + ",operationType:"
						+ this.operationType;
				ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, this.adminId, 2, remark);
				InsertMessageNotification messageDao = new InsertMessageNotification();
				messageDao.orderChangeError(orderNo, adminId, remark);
				LOG.error("OnlineOrderUpdate error:" + remark);
			} else {
				// 判断状态是否通过，通过则执行更新操作
				if (CheckCanUpdateUtil.updateOnlineOrderInfoByLocal(this.orderNo, this.operationType, this.adminId)) {
					updateOrderState(this.orderNo, this.adminId, this.operationType);
				}
			}

		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("OnlineOrderUpdateThread error: " + e.getMessage());
		}

	}

	private void updateOrderState(String orderNo, int adminId, int operationType) {

		int check = doUpdate(orderNo, adminId, operationType);
		if (check <= 0) {
			/*
			 * // 插入失败，重试3次 int count = 0; for (int i = 0; i < 3; i++) { int rs
			 * = doUpdate(orderNo, adminId, operationType); count++; if (rs > 0)
			 * { break; } } // 如果3次更新失败，则进入到更新失败队列中 if (count == 3) { String
			 * sqlStr = "update orderinfo set state = '" + operationType +
			 * "' where order_no = '" + orderNo + "'";
			 * SyncDataInfoDao.insertQueue(sqlStr, 2, "3次尝试更新失败"); }
			 */
		} else {
			try {
				// 插入成功，插入信息放入更改记录表中
				OnlineOrderInfoDao infoDao = new OnlineOrderInfoDao();
				Orderinfo orderinfo = infoDao.queryOrderInfoByOrderNo(orderNo);
				ChangeRecordsDao cRecordsDao = new ChangeRecordsDao();
				cRecordsDao.insertOrderChange(orderinfo, adminId, operationType);
			} catch (Exception e) {
				e.getStackTrace();
				LOG.error("updateOrderState error: " + e.getMessage());
			}

		}

	}

	private int doUpdate(String orderNo, int adminId, int operationType) {
		Connection conn = DBHelper.getInstance().getConnection();
		String updateSql = "update orderinfo set state = ? where order_no = ?";
		PreparedStatement stm = null;
		int rs = 0;
		try {
			stm = conn.prepareStatement(updateSql);
			stm.setInt(1, operationType);
			stm.setString(2, orderNo);
			rs = stm.executeUpdate();
			return rs;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			DBHelper.getInstance().closeConnection(conn);
		}
		if (rs <= 0) {
			LOG.error(updateSql + ", the operation failure");
		}
		return rs;
	}

}
