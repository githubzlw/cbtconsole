package com.cbt.parse.check;

import java.util.TimerTask;

/**监测alibaba网页规则变化
 * @author abc
 *
 */
public class AliDataTimerTask extends TimerTask {
	 @Override
	 public void run() {
	  try {
	   //在这里写你要执行的内容
		  AliCheckPage.Check();
	  } catch (Exception e) {
		  System.out.println("NFDFlightDataTimerTask Exception");
	  } catch (Throwable e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 }

}
