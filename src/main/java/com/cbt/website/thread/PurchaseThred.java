package com.cbt.website.thread;

import com.cbt.jdbc.DBHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author 作者 E-mail: saycjc@outlook.com
 * @version 创建时间：2016年11月18日 上午9:58:13 类说明 :异步操作
 */
public class PurchaseThred extends Thread {
	private static final Log LOG = LogFactory.getLog(PurchaseThred.class);
	private String orderNo;
	private Integer od_id;
	private String sqlc;
	PreparedStatement stmc2 = null;

	public PurchaseThred(String sqlc, int od_id, String orderNo) {
		super();
		this.orderNo = orderNo;
		this.od_id = od_id;
		this.sqlc = sqlc;
	}

	@Override
	public synchronized void run() {
		Connection conn2 = null;
		int re = 0;
		try {
			// 同步更新线上数据
			conn2 = DBHelper.getInstance().getConnection2();
			stmc2 = conn2.prepareStatement(sqlc);
			stmc2.setInt(1, od_id);
			stmc2.setString(2, orderNo);
			if (stmc2 != null) {
				re = stmc2.executeUpdate();
			}
			if (re > 0) {
				LOG.info("异步线程更新线上订单详情的state,订单号：" + orderNo + ",商品od_id：" + od_id + ",purchase_state状态为0");
			} else {
				LOG.debug("异步线程更新线上订单详情的state没有成功,订单号：" + orderNo + ",商品od_id：" + od_id + ",purchase_state状态为0");
			}

		} catch (Exception e) {
			LOG.error("更新线上订单详情的state失败：" + e);
			e.printStackTrace();
		} finally {
			DBHelper.getInstance().closePreparedStatement(stmc2);
			DBHelper.getInstance().closeConnection(conn2);
		}
	}

}
