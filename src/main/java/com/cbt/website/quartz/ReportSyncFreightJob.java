package com.cbt.website.quartz;

import com.cbt.website.dao.IReportSyncFreightDao;
import com.cbt.website.dao.ReportSyncFreightDaoImpl;

import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class ReportSyncFreightJob implements Job {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ReportSyncFreightJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		synchronized (this) {
			LOG.info("--==开始同步==--");
			IReportSyncFreightDao dao = new ReportSyncFreightDaoImpl();
			int res = 0; int old=0;
			try{
				//获取无 重量和运费的运单号（新）
				List<String> expressno = dao.getExpressno("new");
				//根据运单号获取重量和运费（新）
				List<Object[]> wf = dao.getFreightByExpressno(expressno, "new");
				//填充重量和运费
				res = dao.updateWeightFreight(wf);
			} catch (Exception e){
				LOG.info("同步新记录出错");
			}
			try{
			//获取无 重量和运费的运单号（旧）
			List<String> expressno2 = dao.getExpressno("old");
			//根据运单号获取重量和运费（旧）
			List<Object[]> wf2 = dao.getFreightByExpressno(expressno2, "old");
			//填充重量和运费
			old = dao.updateWeightFreight(wf2);
			}catch(Exception e){
				LOG.info("同步旧记录出错");
			}
			LOG.info("同步结束。执行了"+res+"条新记录，"+old+"条旧记录。");
			System.out.println("执行了"+res+"条新记录，"+old+"条旧记录。");
		}
	}

}
