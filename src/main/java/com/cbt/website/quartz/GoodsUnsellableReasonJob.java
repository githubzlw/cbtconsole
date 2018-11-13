package com.cbt.website.quartz;

import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;

import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @ClassName GoodsUnsellableReasonJob
 * @Description 商品下架原因定时更新
 * @author Jxw
 * @date 2018年4月21日
 */
public class GoodsUnsellableReasonJob implements Job {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsUnsellableReasonJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// 取出当前时间的前一天时间验证的unsellableReason = 3
		System.out.println("GoodsUnsellableReasonJob begin...");
		LOG.info("GoodsUnsellableReasonJob begin...");
		CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();

		try {
			Map<String, Integer> mapPids = customGoodsDao.queryUnsellableReason();
			if (mapPids.isEmpty()) {
				System.err.println("GoodsUnsellableReasonJob 本次获取数据:0");
			} else {
				System.err.println("GoodsUnsellableReasonJob 本次获取数据:" + mapPids.size());
				for (Entry<String, Integer> pidMap : mapPids.entrySet()) {
					int count = 0;
					boolean isSuccess = false;
					// 重试3次
					while (!(count >= 3 || isSuccess)) {
						isSuccess = customGoodsDao.updateOnlineUnsellableReason(pidMap.getKey(), 3);
						if (count > 1) {
							// 休眠10秒
							Thread.sleep(10000);
						}
					}
					System.err.println("pid:" + pidMap.getKey() + ",update result:" + isSuccess);
				}
				mapPids.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("GoodsUnsellableReasonJob error:" + e.getMessage());
			LOG.error("GoodsUnsellableReasonJob error:" + e.getMessage());
		}

		System.out.println("GoodsUnsellableReasonJob end!!");
		LOG.info("GoodsUnsellableReasonJob end!!");

	}

}
