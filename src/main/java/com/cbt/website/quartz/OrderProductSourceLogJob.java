package com.cbt.website.quartz;

import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.cbt.website.bean.OrderProductSourceLogBean;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;

import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

/**
 * 
 * @ClassName OrderProductSourceLogJob
 * @Description 同步order_product_source_log表中数据到线上
 * @author ly
 * @date 2018/06/27 16:06
 */
public class OrderProductSourceLogJob implements Job {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(OrderProductSourceLogJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// 取出order_product_source_log表中新增的数据
		System.out.println("OrderProductSourceLogJob begin...");
		LOG.info("OrderProductSourceLogJob begin...");
		CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();
		SendMQ sendMQ = null;
		try { 
			List<OrderProductSourceLogBean> list = customGoodsDao.queryOrderProductSourceLog();
			if (list == null || list.isEmpty()) {
				System.out.println("OrderProductSourceLogJob 本次获取数据:0");
			} else {
				System.out.println("OrderProductSourceLogJob 本次获取数据:" + list.size());
				int count = 0;
				//创建mq工具 用于同步线上数据
				sendMQ = new SendMQ();
				RunSqlModel model= new RunSqlModel();
				for (OrderProductSourceLogBean orderProductSourceLogBean : list) {
					//同步本地标记和保存记录
					count = customGoodsDao.updateOrderProductSourceLog(orderProductSourceLogBean, model);
					//发送mq消息
					sendMQ.sendMsg(model);	
					System.out.println("OrderProductSourceLogJob id:" + orderProductSourceLogBean.getId() 
							+ ",update result:" + (count > 0?"success":"fail"));
				}
			}
			System.out.println("OrderProductSourceLogJob end!!");
			LOG.info("OrderProductSourceLogJob end!!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("OrderProductSourceLogJob error:" + e.getMessage());
			LOG.error("OrderProductSourceLogJob error:" + e.getMessage());
		} finally {
			if (sendMQ != null) {
				try {
					sendMQ.closeConn();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("GoodsSoldUnsellableReasonJob error:" + e.getMessage());
					LOG.error("GoodsSoldUnsellableReasonJob error:" + e.getMessage());
				}
			}
		}
		
	}

}
