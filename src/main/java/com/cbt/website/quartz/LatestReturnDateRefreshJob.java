package com.cbt.website.quartz;


import com.cbt.auto.ctrl.OrderAutoServlet;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class LatestReturnDateRefreshJob implements Job{
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		//刷新采购订单的最晚退货时间
		OrderAutoServlet o=new OrderAutoServlet();
		o.reFreshData();
	}
}
