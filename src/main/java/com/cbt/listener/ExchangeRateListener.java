package com.cbt.listener;

import com.cbt.exchangeRate.service.ExchangeRateService;
import com.cbt.exchangeRate.service.impl.ExchangeRateServiceImpl;
import net.sf.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


/**
 * 监听器  
 * 定时更新汇率
 * @author wkk  
 *
 */
public class ExchangeRateListener implements ServletContextListener {

	public long PERIOD_DAY = 24 * 60 * 60 * 1000; 
	
	 @Override  
	    public void contextDestroyed(ServletContextEvent arg0) {
	        System.out.println("+++++++++++++++++++定时更新线上汇率   over");  
	  
	    }  
	  
	    @Override  
	    public void contextInitialized(ServletContextEvent org0) {
		       /* Runnable runnable = new Runnable() {  
		            public void run() { 
		            	 System.out.println("============定时更新:"+new Date());  
			                System.out.println("=====更新汇率====="); 
			                try {    
					            	WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
					            	if(springContext!=null){
							            	ExchangeRateService  exchaneRateService = (ExchangeRateService)springContext.getBean("exchangeRateServiceImpl");
							            	List<Map<String,String>>  list = new ArrayList<Map<String,String>>();
							            	String[]  countries = {"USD","AUD","GBP","CAD","EUR","CNY"}; 
							                for(String str:countries){
							            		//scur  源币种
							                	//tcur  目标币种
												
													URL u =new URL("http://api.k780.com:88/?app=finance.rate&scur=USD&tcur="+str+"&appkey=23129&sign=4a71855e2b3761be1dc63905a6d53c6a&format=json");
													InputStream in=u.openStream();
									        	     ByteArrayOutputStream out=new ByteArrayOutputStream();
									        	     try {
									     	            byte buf[]=new byte[1024];
									     	            int read = 0;
									     	            while ((read = in.read(buf)) > 0) {
									     	                out.write(buf, 0, read);
									     	            }
									     	        }  finally {
									     	            if (in != null) {
									     	                in.close();
									     	            }
									     	        }
								        	            byte b[]=out.toByteArray( );
									        	        JSONObject json = JSONObject.fromObject(new String(b,"utf-8")); 
									        	        Object obj = json.get("result");
									        	        JSONObject  subJson = JSONObject.fromObject(obj);
									        	        System.out.println(obj);
									        	        Map<String,String>  map =  new HashMap<String,String>();
									        	        if(str=="CNY"){
									        	        	map.put("country","RMB");
									        	        }else{
									        	        	map.put("country",str);
									        	        }
									        	        map.put("rate", subJson.get("rate").toString());
//									        	        if(str=="AUD"){
//									        	        	 map.put("rate", "1.44");
//									        	        }if(str=="GBP"){
//									        	        	 map.put("rate", "0.854");
//									        	        }if(str=="CAD"){
//									        	        	 map.put("rate","1.326" );
//									        	        }if(str=="CNY"){
//									        	        	 map.put("rate","6.88" );
//									        	        }if(str=="EUR"){
//									        	        	 map.put("rate","0.945" );
//									        	        }if(str=="USD"){
//									        	        	 map.put("rate","1" );
//									        	        }
									        	        list.add(map);
							                }
							        	        exchaneRateService.updateRate(list);
					            }
				            } catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		            }
		        };  */
//		        ScheduledExecutorService service = Executors  
//		                .newSingleThreadScheduledExecutor();  
//		        System.out.println(new Date());  
//		        //第一个参数是触发事件s，第二个参数是执行延迟时间day，第三个参数是时间间隔  
//		        service.scheduleAtFixedRate(runnable, 1, 1, TimeUnit.DAYS);
		        
		        //定时任务
		        Calendar calendar = Calendar.getInstance();  
		        calendar.set(Calendar.HOUR_OF_DAY, 13); //凌晨1点  
		        calendar.set(Calendar.MINUTE, 42);  
		        calendar.set(Calendar.SECOND, 0);  
		        Date date=calendar.getTime(); //第一次执行定时任务的时间  
		        System.out.println(date);
		        //如果第一次执行定时任务的时间 小于当前的时间  
		        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。  
		        if (date.before(new Date())) {  
		            date = this.addDay(date, 1);  
		        }  
		        Timer timer = new Timer();
		        Task task = new Task(org0);
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


class Task extends TimerTask {
	
	private ServletContextEvent sce ;
    public Task(ServletContextEvent sce) {
		// TODO Auto-generated constructor stub
    	this.sce = sce;
	}

	public void run() {  
    	System.out.println("============定时更新:"+new Date());  
        System.out.println("=====更新汇率====="); 
        try {    
		            	ExchangeRateService exchaneRateService = new ExchangeRateServiceImpl();
		            	List<Map<String,String>>  list = new ArrayList<Map<String,String>>();
		            	String[]  countries = {"USD","AUD","GBP","CAD","EUR","CNY"}; 
		                for(String str:countries){
		            		//scur  源币种
		                	//tcur  目标币种
								URL u =new URL("http://api.k780.com:88/?app=finance.rate&scur=USD&tcur="+str+"&appkey=23129&sign=4a71855e2b3761be1dc63905a6d53c6a&format=json");
								InputStream in=u.openStream();
				        	     ByteArrayOutputStream out=new ByteArrayOutputStream();
				        	     try {
				     	            byte buf[]=new byte[1024];
				     	            int read = 0;
				     	            while ((read = in.read(buf)) > 0) {
				     	                out.write(buf, 0, read);
				     	            }
				     	        }  finally {
				     	            if (in != null) {
				     	                in.close();
				     	            }
				     	        }
			        	            byte b[]=out.toByteArray( );
				        	        JSONObject json = JSONObject.fromObject(new String(b,"utf-8")); 
				        	        Object obj = json.get("result");
				        	        JSONObject  subJson = JSONObject.fromObject(obj);
				        	        System.out.println(obj);
				        	        Map<String,String>  map =  new HashMap<String,String>();
				        	        if(str=="CNY"){
				        	        	map.put("country","RMB");
				        	        }else{
				        	        	map.put("country",str);
				        	        }
				        	        map.put("rate", subJson.get("rate").toString()); 
				        	       /* if(str=="AUD"){
				        	        	 map.put("rate", "1.44");
				        	        }if(str=="GBP"){
				        	        	 map.put("rate", "0.854");
				        	        }if(str=="CAD"){
				        	        	 map.put("rate","1.326" );
				        	        }if(str=="CNY"){
				        	        	 map.put("rate","6.88" );
				        	        }if(str=="EUR"){
				        	        	 map.put("rate","0.945" );
				        	        }if(str=="USD"){
				        	        	 map.put("rate","11" );
				        	        } */
				        	        list.add(map);
		                }
		                //更新完汇率后，在上下文作用域内同步更新
		                ServletContext application = sce.getServletContext();
		                System.out.println(list);
		                Map<String,Double>  resultMap = exchaneRateService.updateRate(list);
		                if(resultMap!=null){
		                	application.setAttribute("exchangeRate", resultMap);
		                } 
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
} 
    }  
