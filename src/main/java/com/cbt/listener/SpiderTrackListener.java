package com.cbt.listener;

import com.cbt.warehouse.service.TrackService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定时抓取物流信息
 * 间隔
 * @author admin
 *
 */
public class SpiderTrackListener implements ServletContextListener {

	public long  PERIOD_DAY = 24 * 60 * 60 * 1000; 
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		 System.out.println("+++++++++++++++++++定时抓球物流延迟信息   over");  
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		 //定时任务
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.HOUR_OF_DAY, 1); //凌晨1点  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        Date date=calendar.getTime(); //第一次执行定时任务的时间  
        //如果第一次执行定时任务的时间 小于当前的时间  
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。  
        if (date.before(new Date())) {  
            date = this.addDay(date, 1);  
        }  
        Timer timer = new Timer();
        Task1 task = new Task1(arg0);
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。  
        timer.schedule(task,date,PERIOD_DAY);   
	}
	
	 // 增加或减少天数  
    public   Date addDay(Date date, int num) {  
        Calendar startDT = Calendar.getInstance();  
        startDT.setTime(date);  
        startDT.add(Calendar.DAY_OF_MONTH, num);  
        return startDT.getTime();  
    } 
}

class Task1 extends TimerTask {

	private ServletContextEvent sce ;
	
    public Task1(ServletContextEvent arg0) {
		// TODO Auto-generated constructor stub
    	this.sce = arg0;
	}

	public void run() {
		WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		if(springContext!=null){
			 System.out.println("============定时抓取物流延迟信息:"+new Date());  
		       System.out.println("=====定时抓取物流延迟信息====="); 
		       try { 
		    	   TrackService trackservice = (TrackService)springContext.getBean("trackServiceImpl");
		    	   trackservice.getAllTracks();
		       } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			   }
		}
		   	  
} 
   }
