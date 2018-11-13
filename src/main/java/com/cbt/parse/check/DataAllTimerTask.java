package com.cbt.parse.check;

import com.cbt.parse.driver.DataUpdateDriver;

import java.util.TimerTask;

/**任务
 * @author abc
 *
 */
public class DataAllTimerTask extends TimerTask {
	 @Override
	 public void run() {
	  try {
	   //在这里写你要执行的内容
		  DataUpdateDriver driver = new DataUpdateDriver();
		  driver.updateRemark(0);
	  } catch (Exception e) {
		  
	  } catch (Throwable e) {
	}
	 }

}
