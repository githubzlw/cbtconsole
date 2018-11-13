package com.cbt.website.YFHtask;

import java.util.Timer;

public class YFHmain {
	public static void main(String[] args) {
		Timer timer = new Timer();
		YFHtask task = new YFHtask();
		timer.schedule(task,2000,3*1000);//3秒钟执行一次
	}
}