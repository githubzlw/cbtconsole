package com.cbt.website.thread;

import com.cbt.jdbc.DBHelper;

import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 E-mail: saycjc@outlook.com
 * @version 创建时间：2016年11月18日 上午9:58:13 类说明 :异步操作
 */
public class PurchaseThred extends Thread {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(PurchaseThred.class);
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
		int re = 0;
		try {
			// 同步更新线上数据
			List<String> lstValues = new ArrayList<>();
			lstValues.add(String.valueOf(od_id));
			lstValues.add(orderNo);

			String runSql = DBHelper.covertToSQL(sqlc,lstValues);
			re=Integer.parseInt(SendMQ.sendMsgByRPC(new RunSqlModel(runSql)));
			if (re > 0) {
				LOG.info("异步线程更新线上订单详情的state,订单号：" + orderNo + ",商品od_id：" + od_id + ",purchase_state状态为0");
			} else {
				LOG.warn("异步线程更新线上订单详情的state没有成功,订单号：" + orderNo + ",商品od_id：" + od_id + ",purchase_state状态为0");
			}

		} catch (Exception e) {
			LOG.error("更新线上订单详情的state失败：" , e);
			e.printStackTrace();
		}
	}

}
