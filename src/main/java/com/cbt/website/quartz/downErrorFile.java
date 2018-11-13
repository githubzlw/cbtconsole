package com.cbt.website.quartz;

import com.cbt.method.servlet.DownloadErrorLog;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class downErrorFile implements Job{
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		new DownloadErrorLog().listFileNames("216.244.83.218", 22, "downloaderrlog", "root@123", "./cbtprogram2016");
		new DownloadErrorLog().listFileNames("216.244.83.218", 22, "downloaderrlog", "root@123", "./cbtprogram2016/pay");
	}

}
