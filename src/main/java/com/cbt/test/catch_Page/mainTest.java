package com.cbt.test.catch_Page;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class mainTest implements Runnable{
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
	  
    public void run() {  
//        try {  
//            Thread.sleep(2);  
//        } catch (InterruptedException ex) {  
//            ex.printStackTrace();  
//        }  
        System.out.println(Thread.currentThread().getName() + "do something at:" + sdf.format(new Date()));  
    }  

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception, MessagingException {

		
		
		//System.out.println(Utility.ImgMatch("http://g03.a.alicdn.com/kf/HTB1AutGIVXXXXbnXpXXq6xXFXXXJ/Hot-sale-Winner-skeleton-watch-women-blue-Pointer-automatic-mechanical-watch-women-skeleton-watch-brands-relojes.jpg_50x50.jpg"));
		
		
		Double a = 3.14501617;
		DecimalFormat df = new DecimalFormat("0.00");
		System.out.println(df.format(a));
		
		
//		StringBuffer sb = new StringBuffer("<div style='font-size: 14px;'>");
//		sb.append("<a href='" + AppConfig.ip
//				+ "'><img style='cursor: pointer' src='" + AppConfig.ip
//				+ "/img/logo.png' ></img></a>");
//		sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear "
//				+ ("123 : Valued Customer")
//				+ ",</div><div style='font-size: 13px;'>");
//
//		sb.append("Thanks for shopping with us at <a style='color: #0070C0' href='http://www.import-express.com'>www.import-express.com</a>, we have received your order. Please refer to below order detail: </div>");
//		sb.append("<div style='font-size: 13px;'><font style='font-weight: bolder;'>Shipping To:</font>"
//				+ 123
//				+ "</div>");
//		sb.append("<div style='margin-left: 95px;font-size: 14px;'>" + 123 + "</div>");
//		sb.append("<div style='margin-left: 95px;font-size: 14px;'>" + 123 + "</div>");
//		sb.append("<div style=' margin-left: 95px;font-size: 14px;'>");
//		sb.append(123);
//		sb.append("&nbsp;&nbsp;");
//		sb.append(123);
//		sb.append("&nbsp;&nbsp;");
//		sb.append(123);
//		sb.append("&nbsp;&nbsp;");
//		sb.append("</div>");
//		sb.append("<div style='font-size: 13px;list-style:none;'> <font style='font-weight: bolder;'>Phone:</font>"
//				+ 123
//				+ "</div>");
//		
//		
//		sb.append("<div style='margin-bottom: 10px;'><div style='list-style: none;width: 2560px;text-ali"
//				+ "gn: left;font-size: 14px;'><br><div><font style='font-weight: bolder;'>Order#:</font><a style='color: #0070C0' href='"
//				+ AppConfig.ips
//				+ "/processesServlet?action=emailLink&className=OrderInfo&orderNo=" + "1231233213" + "'>"+"312312312"+"</a></div>");
//		sb.append("<div><font style='font-weight: bolder;'>Estimated ship out date:</font>"
//				+ "20160202" + "</div>");
//
//			sb.append("<div><font style='font-weight: bolder;'>Shipping Method you sellected:</font>"
//					+ "321312312" + "</div>");
//
//			sb.append("<div><font style='font-weight: bolder;'>Estimated Time of Arrival:</font>"
//					+ "20160202 &nbsp;The date presented is an estimated due date and is subject to change.</div>");
//
//		sb.append("</div>");
//		/*sb.append("<div style='color: #0070C0;font-size: 12px;'>Order Detail&nbsp;&nbsp;&nbsp;(<a style='color: #0070C0' href='"
//				+ AppConfig.ips
//				+ "/processesServlet?action=emailLink&className=OrderInfo&orderNo="
//				+ orderNo + "'>My Order   ></a>)</div>");*/
//		sb.append("<table style='width: 750px;font-size: 12px;'>");
//		sb.append("<tr><td colspan='4' style='border-top: 1px solid;'>Order Created Date: "
//				+ "12312312321321321" + "</td></tr>");
//		sb.append("12312321321");
//		sb.append("<tr style='font-weight: bold;font-size: 12px;' ><td colspan='3' align='right'>Shipping Fee Paid:</td><td colspan='4' >"
//				+ "123123123123"
//				+ "&nbsp;123</tr>");
//		sb.append("<tr style='font-weight: bold;font-size: 12px;'><td colspan='3' style='border-bottom: 1px solid;' align='right'>Order Amount:</td><td style='font-weight: bold;border-bottom: 1px solid;'>"
//				+ "123" + "&nbsp;" + "123" + "</tr></table>");
//		
//	
//	sb.append("</div><div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>");
//		StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
//		sb.append("<a href='" + AppConfig.ip + "'><img style='cursor: pointer' src='" + AppConfig.ip + "/img/logo.png' ></img></a>");
//        sb.append(" <div style='font-weight: bolder; margin-bottom: 10px;'>Dear YU,</div><br>"); 
//        sb.append(" <div style='margin-bottom: 10px; font-size: 13px;'>Order Number#<a style='color: #0070C0' href='http://116.228.150.218:8086/cbtconsole/processesServlet?action=getCenter&className=IndividualServlet'>O1216268334029003</a></div><br>"); 
//        sb.append(" <div style='margin-bottom: 10px;'>Due to the supply reason, our purchase team is still working on the products stock checking with suppliers and it may take slightly longer to complete the process.</div>");
//        sb.append(" <div style='margin-bottom: 3px;'>Please be patient and you will soon see your order status update which will be shown in <a style='color: #0070C0' href='http://116.228.150.218:8086/cbtconsole/processesServlet?action=getCenter&className=IndividualServlet'>Your account</a>.</div><br>"); 
//        sb.append(" <div style='font-weight: bold'>Best regards, </div><div style='font-weight: bold'><a href='http://www.Import-Express.com'>www.Import-Express.com</a></div></div>");
		//SendEmail.sendCheckDeliveryWarning("roxie@import-express.com", "XieAaB3Qef", "2700590@qq.com", sb.toString(), "测试标题", "O1084735526303971", 1);
		
//		Redis.hset("testKey", "textfield1", "textvalue1");
//		System.out.println(Redis.exists("testKey"));
//		
//		ScheduledExecutorService schedule = Executors.newScheduledThreadPool(5);  
//        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
//        System.out.println(" begin to do something at:" + sdf.format(new Date()));  
//        schedule.scheduleAtFixedRate(new mainTest(), 3, 2, TimeUnit.MINUTES); 
		
	}

	
	 private static int index = 1;
	    public static String generateKey(){
	        return String.valueOf(Thread.currentThread().getId())+"_"+(index++);
	    }
	
}
