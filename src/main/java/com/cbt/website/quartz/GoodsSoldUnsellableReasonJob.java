package com.cbt.website.quartz;

import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.importExpress.utli.SendMQ;
import com.importExpress.utli.UpdateTblModel;

import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @ClassName GoodsSoldUnsellableReasonJob
 * @Description 销量无变化标志更新UnsellableReason
 * @author ly
 * @date 2018-6-15
 */
public class GoodsSoldUnsellableReasonJob implements Job {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsSoldUnsellableReasonJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// 取出 unsellableReason 有变化的数据
		System.out.println("GoodsSoldUnsellableReasonJob begin...");
		LOG.info("GoodsSoldUnsellableReasonJob begin...");
		CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();
		SendMQ sendMQ = null;
		try {
			Map<String, Integer> mapPids = customGoodsDao.querySoldUnsellableReason();
			if (mapPids.isEmpty()) {
				System.out.println("GoodsSoldUnsellableReasonJob 本次获取数据:0");
			} else {
				sendMQ = new SendMQ();
				UpdateTblModel model= new UpdateTblModel();
				System.out.println("GoodsSoldUnsellableReasonJob 本次获取数据:" + mapPids.size());
				for (Entry<String, Integer> pidMap : mapPids.entrySet()) {
					int count = 0;
					//更新线上数据(发送mq)
					model.setPk(pidMap.getKey());
			        model.setUnsellableReason(pidMap.getValue() + "");
			        sendMQ.sendMsg(model);			        
					//更新本地数据
					boolean isSuccess = customGoodsDao.updateOnlineSoldUnsellableReason(pidMap.getKey(), pidMap.getValue(), model);;
					System.out.println("pid:" + pidMap.getKey() + ",update sold unsellableReason result:" + isSuccess);
				}
				mapPids.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("GoodsSoldUnsellableReasonJob error:" + e.getMessage());
			LOG.error("GoodsSoldUnsellableReasonJob error:" + e.getMessage());
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

		System.out.println("GoodsSoldUnsellableReasonJob end!!");
		LOG.info("GoodsSoldUnsellableReasonJob end!!");

	}
	
}
