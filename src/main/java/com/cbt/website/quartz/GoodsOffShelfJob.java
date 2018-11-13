package com.cbt.website.quartz;

import com.cbt.dao.CustomGoodsDao;
import com.cbt.dao.impl.CustomGoodsDaoImpl;
import com.cbt.service.OffShelfService;
import com.cbt.service.impl.OffShelfServiceImpl;

import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GoodsOffShelfJob implements Job {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(GoodsOffShelfJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("GoodsOffShelfJob begin...");
		LOG.info("GoodsOffShelfJob begin...");
		CustomGoodsDao customGoodsDao = new CustomGoodsDaoImpl();
		OffShelfService offShelfService = new OffShelfServiceImpl();
		
		try {
			//下架商品
			List<String> pids = customGoodsDao.queryOffShelfPids();
			if (null == pids || pids.size() < 1) {
				return;
			}
			//热卖区商品 不下架列表 永不下架商品
			Set<String> hotList = customGoodsDao.queryHotSellingGoods();
			//可上架商品pid 服装 首饰 鞋子 精品店铺 精品商品
			Set<String> shelvesSet = customGoodsDao.queryShelvesGoods();
			Integer count;
			Integer i = 0;
			for(String pidCb : pids){
				//每200条延时3秒
				i++;
				if (i%200 == 0) {
					try {
						Thread.sleep(3000);
					} catch (Exception e) {
						// 
					}
				}
				String[] pidAr = pidCb.split("@");
				if(pidAr == null){
					continue;
				}else{
					if(pidAr == null || pidAr[0] == null || "".equals(pidAr[0]) || pidAr.length != 2){
						continue; //数据问题
					}else{
						String pid = pidAr[0];
						int reason = Integer.valueOf(pidAr[1]);
						if(reason != 5 && hotList.contains(pid)){//下架的 热卖区和永不下架商品 不下架
							customGoodsDao.updateOffShelfByPid(pid, 4);
							continue; 
						}
						if (reason == 5 && !shelvesSet.contains(pid)) { //上架的 不属于 精品店铺 精品商品 服装首饰鞋子分类 不上架
							customGoodsDao.updateOffShelfByPid(pid, 5);
							continue;
						}
						count = offShelfService.updateByPidJDBC(pid, reason);
						customGoodsDao.updateOffShelfByPid(pid,(count != 0 && count != 10 ? 2 : 1));
//						if(reason < 5){
//							int count = customGoodsDao.setGoodsValid(pid, "system", 0, -1,reason);
//							customGoodsDao.updateOffShelfByPid(pid,(count > 0 ? 2 : 1));
//						}else{
//							int count = customGoodsDao.setGoodsValid(pid, "system", 0, 1,reason);
//							customGoodsDao.updateOffShelfByPid(pid,(count > 0 ? 2 : 1));
//						}						
					}
				}
				
			}		
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("GoodsOffShelfJob error:" + e.getMessage());
			LOG.error("GoodsOffShelfJob error:" + e.getMessage());
		}
		
		System.out.println("GoodsOffShelfJob end!!");
		LOG.info("GoodsOffShelfJob end!!");
		
	}

}
