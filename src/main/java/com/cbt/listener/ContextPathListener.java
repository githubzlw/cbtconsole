package com.cbt.listener;

import com.cbt.parse.service.SearchUtils;
import com.cbt.service.InitCacheService;
import com.cbt.service.impl.InitCacheServiceImpl;
import com.cbt.util.AppConfig;
import com.cbt.website.quartz.CheckDeliveryWarningExample;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextPathListener implements ServletContextListener {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(ContextPathListener.class);

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		sc.setAttribute("path", getContextPath(sc));
		sc.setAttribute("words", SearchUtils.getIntensiveWords());
		
		InitCacheService initCacheService = new InitCacheServiceImpl();
		try {
			initCacheService.init();

			// 创建线程扫描交期预警客户,自动发送邮件
			// ScheduledExecutorService schedule =
			// Executors.newScheduledThreadPool(3);
			// final SimpleDateFormat sdf = new
			// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			// LOG.info((" begin to CheckDeliveryWarning at:" + sdf.format(new
			// Date())));
			// 参数:操作对象,初始化延迟多久执行第一次,延迟时间段,时间单位
			// schedule.scheduleAtFixedRate(new CheckDeliveryWarning(), 1, 10,
			// TimeUnit.MINUTES);
			// schedule.scheduleAtFixedRate(runnable, 1, 10, TimeUnit.MINUTES);
			
			CheckDeliveryWarningExample example = new CheckDeliveryWarningExample();
			example.run();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		sc.removeAttribute("path");
		sc.removeAttribute("words");
//		DBHelper.destory();
	}

	private String getContextPath(ServletContext sc) {
		// return sc.getContextPath();
		return AppConfig.ip;
	}

}
