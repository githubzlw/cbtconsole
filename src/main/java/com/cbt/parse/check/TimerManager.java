package com.cbt.parse.check;

import java.util.Timer;
import java.util.TimerTask;

/**定时管理器
 * @author abc
 *
 */
public class TimerManager {
	private TimerTask task;
	private long delay;
	private long period;
	  
	public TimerManager() {
		
		
	}
	public TimerManager(TimerTask task,int delay,long period) {
		this.delay = delay;
		this.task = task;
		this.period = period;

	}
	 /**
	 * @param task 安排指定的任务
	 * @param hour 定制每日xx:00执行方法
	 * @param period_day  //时间间隔  天数
	 */
	public void execute() {
	  //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
	  if(task!=null){
		  Timer timer = new Timer();
		  timer.schedule(task, delay, period);
	  }
	 }
	 
	  
	}
