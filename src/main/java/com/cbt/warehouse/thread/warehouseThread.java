package com.cbt.warehouse.thread;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.util.SpringContextUtil;
import com.cbt.warehouse.service.IWarehouseService;
import com.importExpress.utli.NotifyToCustomerUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 异步线程
 * 
 * @author admin
 *
 */
public class warehouseThread extends Thread {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(warehouseThread.class);
	private static IWarehouseService iWarehouseService = (IWarehouseService) SpringContextUtil
			.getBean("warehouseServiceImpl");;

	private Map<String, String> m2;
	private List<Map<String, String>> list;

	public warehouseThread(Map<String, String> m, List<Map<String, String>> list) {
		// TODO Auto-generated constructor stub
		super();
		this.m2 = m;
		this.list = list;
	}

	@Override
	public synchronized void run() {
		try {

			// 出库日志记录
			iWarehouseService.batchInsertSPLog(list);
			//
			// 同步更新线上数据
			// 删除原来orderid对应的数据

			if (iWarehouseService.selectShippingPackage(m2) > 0) {
				// SendMQ.sendMsg(new RunSqlModel("delete from shipping_package where orderid='"+m2.get("orderid")+"'"));
			}
			//StringBuilder sqls=new StringBuilder("delete from shipping_package where orderid='"+m2.get("orderid")+"';");
			for(Map<String, String> map:list){
				StringBuilder sqls=new StringBuilder("INSERT INTO shipping_package (shipmentno,orderid,remarks,createtime) VALUES");
				sqls.append("(");
				sqls.append("'"+map.get("shipmentno")+"','"+map.get("orderid")+"','"+map.get("remarks")+"',now()");
				sqls.append(") on duplicate key update shipmentno ='" + map.get("shipmentno")
						+ "',createtime = now(),remarks='" + map.get("remarks") +"'");
				iWarehouseService.insertMqLog(sqls.toString(), map.get("shipmentno"), map.get("orderid"),map.toString());
				NotifyToCustomerUtil.sendSqlByMq(sqls.toString());
			}
		} catch (Exception e) {
			LOG.error("打印便签纸，更新线上数据异常 【订单号:" + m2.get("orderid") + "】", e);
		}

	}
}
