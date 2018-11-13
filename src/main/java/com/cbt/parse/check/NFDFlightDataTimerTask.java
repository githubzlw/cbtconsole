package com.cbt.parse.check;

import java.util.TimerTask;

/**执行八大类检验程序
 * @author abc
 *
 */
public class NFDFlightDataTimerTask extends TimerTask {
	 @Override
	 public void run() {
	  try {
	   //在这里写你要执行的内容
	    CheckGoodsUrl.check();
	  } catch (Exception e) {
	  } catch (Throwable e) {
	}
	 }

}
