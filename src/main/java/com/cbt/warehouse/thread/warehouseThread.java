package com.cbt.warehouse.thread;

import com.cbt.common.dynamics.DataSourceSelector;
import com.cbt.util.SpringContextUtil;
import com.cbt.warehouse.service.IWarehouseService;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;

/**
 * 异步线程
 * 
 * @author admin
 *
 */
public class warehouseThread extends Thread {
	private static final Log LOG = LogFactory.getLog(warehouseThread.class);
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
			SendMQ sendMQ = new SendMQ();
			// 同步更新线上数据
			DataSourceSelector.set("dataSource127hop");
			// 删除原来orderid对应的数据
			if (iWarehouseService.selectShippingPackage(m2) > 0) {
				sendMQ.sendMsg(new RunSqlModel("delete from shipping_package where orderid='"+m2.get("orderid")+"'"));
			}
			StringBuilder sqls=new StringBuilder();
			sqls.append("INSERT INTO shipping_package (shipmentno,orderid,remarks,createtime) VALUES");
			for(Map<String, String> map:list){
				sqls.append("(");
				sqls.append("'"+map.get("shipmentno")+"','"+map.get("orderid")+"','"+map.get("remarks")+"',now()");
				sqls.append("),");
			}
			if(list.size()>0){
				sendMQ.sendMsg(new RunSqlModel(sqls.toString().substring(0,sqls.toString().length()-1)));
			}
			sendMQ.closeConn();
		} catch (Exception e) {
			LOG.error("打印便签纸，更新线上数据异常 【订单号:" + m2.get("orderid") + "】", e);
		}finally {
			DataSourceSelector.restore();
		}

	}
}
