package com.cbt.website.quartz;


import com.cbt.auto.ctrl.OrderAutoServlet;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


public class PreAutoSourceAddTime implements Job{
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		OrderAutoServlet o=new OrderAutoServlet();
		o.PreAutoSourceAddTime();
	}
}
