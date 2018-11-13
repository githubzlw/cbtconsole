package com.cbt.email.TimerListener;

import com.cbt.email.entity.EmailUser;
import com.cbt.email.util.InitEmailInfoTime;
import com.cbt.email.util.ReceiveTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


/**
 * 系统启动时的监听类 
 */
public class InitListener implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(InitListener.class);
	public static List<EmailUser> list = new ArrayList<EmailUser>();
	String path = "";
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		// context销毁时，销毁初始化数据
	}

	public void contextInitialized(ServletContextEvent event) {
		try {
			//初始化存放邮箱的信息
			initEmailInfo();
			//获取邮件信息
			getEmailInfo();
/*			System.out.println(list.size());
			if(list.size() > 0) {
				for (EmailUser e : list) {
					Timer timmerTask = new Timer();
					// 每个邮箱 每十分钟执行一次接收邮件的任务
					timmerTask.schedule(new ReceiveTime(e), 6000*10, 1000 * 60 * 10);
				}
			}*/
			//ServletContext context = event.getServletContext();
			//path = context.getRealPath("/")+"accessories";
			//path = PathUtil.SENDFile;
			//goTimer();
			//receive();
		} catch (Exception e) {
			LOG.warn("失败:" + e.getMessage());
		}
	}

	/*private void goTimer() {
		Timer timmerTask = new Timer();
		Calendar calEnviron = Calendar.getInstance();
		// 每天的02:00.am开始执行
		calEnviron.set(Calendar.HOUR_OF_DAY, 16);
		calEnviron.set(Calendar.MINUTE, 00);
		// date为制定时间
		Date dateSetter = new Date();
		dateSetter = calEnviron.getTime();
		// nowDate为当前时间
		Date nowDateSetter = new Date();
		// 所得时间差为，距现在待触发时间的间隔
		long intervalEnviron = dateSetter.getTime() - nowDateSetter.getTime();
		if (intervalEnviron < 0) {
			calEnviron.add(Calendar.DAY_OF_MONTH, 4);
			dateSetter = calEnviron.getTime();
			intervalEnviron = dateSetter.getTime() - nowDateSetter.getTime();
		}
		//获取服务器附件上传的文件目录
		File file = new File(path);
		// 每24小时执行一次
		//timmerTask.schedule(new delTimer(timmerTask,file), 0, 9000);
		timmerTask.schedule(new DelTimer(timmerTask,file), intervalEnviron, 4 * 1000
				* 60 * 60 * 24);
	}*/
	
	private void initEmailInfo() {
		Timer timmerTask = new Timer();
		/*Calendar calEnviron = Calendar.getInstance();
		// 每天的02:00.am开始执行
		calEnviron.set(Calendar.HOUR_OF_DAY, 16);
		//calEnviron.set(Calendar.MINUTE, 00);
		// date为制定时间
		Date dateSetter = new Date();
		dateSetter = calEnviron.getTime();
		// nowDate为当前时间
		Date nowDateSetter = new Date();
		// 所得时间差为，距现在待触发时间的间隔
		long intervalEnviron = dateSetter.getTime() - nowDateSetter.getTime();
		if (intervalEnviron < 0) {
			calEnviron.add(Calendar.DAY_OF_MONTH, 1);
			dateSetter = calEnviron.getTime();
			intervalEnviron = dateSetter.getTime() - nowDateSetter.getTime();
		}*/
		// 每十分钟执行一次
		timmerTask.schedule(new InitEmailInfoTime(), 0, 60 * 1000* 60);
	}
	
	private void getEmailInfo() {
		Timer timmerTask = new Timer();
		// 每二十分钟执行一次
		//if(this.list.size() > 0) {
			//for (EmailUser e : this.list) {
				timmerTask.schedule(new ReceiveTime(), 3000, 20 * 1000 * 60);
			//}
		//}
	}
}