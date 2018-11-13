package com.cbt.email.util;

import com.cbt.email.TimerListener.InitListener;
import com.cbt.email.entity.EmailUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 被调用执行类
 */
public class InitEmailInfoTime extends TimerTask {
	private static final Logger LOG = LoggerFactory.getLogger(InitEmailInfoTime.class);
	 Timer timmerTask = new Timer();
	 
	public InitEmailInfoTime() {
		
	}
	
	/*
	 * 被调用具体的方法
	 */
	public void run() {
		try {
			InitListener.list = init();
			/*if(list.size() > 0) {
				for (EmailUser e : list) {
					// 每个邮箱 每十分钟执行一次接收邮件的任务
					timmerTask.schedule(new ReceiveTime(e), 0, 1 * 1000 * 60);
				}
			}
*/		}catch(Exception e) {//有任何的异常的时候，停止计时器的执行
			LOG.warn("初始化邮箱信息了出现异常，定时器停止");
			cancel();
		}
	}
	
	
	/**
	 * 方法描述:初始化邮箱信息 用来定时接收邮件
	 * author:lzj
	 * date:2015年6月16日
	 */
	private List<EmailUser> init() {  
		List<EmailUser> list = new ArrayList<EmailUser>();
		EmailUser info  = new EmailUser();
		info.setId(1);
		info.setUserName("Ed");
		info.setPwd("123456");
		info.setEmailAddress("service@import-express.com");
		info.setEmailPWD("CustomerFirst1248");
		info.setFlag(1);
		info.setRoleNo(3);
		info.setTrueName("Ed");
		info.setJob("下载");
		list.add(info);
        LOG.warn("本次初始化了"+list.size()+"个邮箱信息");
        return list;
    } 
	
	public static void main(String[] args) {
		Timer timer = new Timer();
		//File file = new File("F:\\test");
		//deleteAllFiles(file);
		//timer.schedule(new delTimer(timer,null), 0, 6000);
	}
}