package com.cbt.website.servlet;

import com.cbt.customer.service.IPictureComparisonService;
import com.cbt.customer.service.PictureComparisonServiceImpl;
import com.cbt.pay.service.ISpidersServer;
import com.cbt.pay.service.SpidersServer;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.util.MessageSender;
import com.cbt.util.UUIDUtil;
import com.cbt.util.Utility;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.service.IOrderSplitServer;
import com.cbt.website.service.OrderSplitServer;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发邮件
 */
public class SendEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SendEmailServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
	}
    
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter out=response.getWriter();
		String company=request.getParameter("company");
		String email=request.getParameter("email");
		String ordervalue=request.getParameter("ordervalue");
		String needs=request.getParameter("needs");
		Map<String,Object> resultMap=new HashMap<String,Object>();
		Map<String,List<Map<String, String>>> addressMap=new HashMap<String,List<Map<String, String>>>();
    	Map<String, String> paramsMap=new HashMap<String, String>(); 
    	Map<String, String> paramsMap2=new HashMap<String, String>(); 
    	paramsMap.put("address", "contact@sourcing-cn.com");
    	paramsMap.put("content", "company:"+company+"\r\n email:"+email+"\r\n ordervalue:"+ordervalue+"\r\n needs:"+needs);
    	List<Map<String, String>> list=new ArrayList<Map<String,String>>();
    	list.add(paramsMap);
    	paramsMap2.put("address", "lingzheng@sourcing-cn.com");
    	paramsMap2.put("content", "company:"+company+"\r\n email:"+email+"\r\n ordervalue:"+ordervalue+"\r\n needs:"+needs);
    	list.add(paramsMap2);
    	addressMap.put("send", list);
    	int result=MessageSender.startSendMail(addressMap);
    	if(result==1){
    		Map<String,List<Map<String, String>>> addressMap2=new HashMap<String,List<Map<String, String>>>();
	    	Map<String, String> paramsMap3=new HashMap<String, String>(); 
	    	paramsMap3.put("address", email);
	    	List<Map<String, String>> list2=new ArrayList<Map<String,String>>();
	    	list2.add(paramsMap3);
	    	addressMap2.put("hz", list2);
	    	int result2=MessageSender.startSendMail(addressMap2);
	    	if(result2!=1){
	    		resultMap.put("code", "0");
	    		resultMap.put("msg", "邮件回执失败");
	    	}else{
	    		resultMap.put("code", "1");
	    		resultMap.put("msg", "操作成功");
	    	}
    	}else{
    		resultMap.put("code", "0");
    		resultMap.put("msg", "邮件发送失败");
    	}
		String resultJSON=JSONArray.fromObject(resultMap).toString();
		out.print(resultJSON);
		out.flush();
		out.close();
	}
	
	//后台发送邮件按钮
	public void sendEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String emailInfo=request.getParameter("emailInfo");
		String email=request.getParameter("email");
		String copyEmail=request.getParameter("copyEmail");
		String orderNo=request.getParameter("orderNo");
		String userId=request.getParameter("userId");
		String title = request.getParameter("title");
		int res = 0;
		if(Utility.getStringIsNull(emailInfo)){
			String sendemail = null;
    	    String pwd = null;
			 if(Utility.getStringIsNull(copyEmail)){
    		    IUserDao userDao = new com.cbt.processes.dao.UserDao();
    		    String[] adminEmail =  userDao.getAdminUser(0, copyEmail, 0);
    		    if(adminEmail != null){
    			   sendemail = adminEmail[0];
    			   pwd = adminEmail[1];
    		    }
	    	  }
			res = SendEmail.send(sendemail,pwd,email, emailInfo,title,copyEmail,orderNo,1);
		}else{
			ISpidersServer spidersServer = new SpidersServer();
			res = spidersServer.sendEmail(orderNo, Integer.parseInt(userId));
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	//后台发送邮件按钮-提醒支付运费
	public void sendEmail_fright(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderNo=request.getParameter("orderNo");
		String remark=request.getParameter("remark");
		String price=request.getParameter("price");
		String copyEmail=request.getParameter("copyEmail").trim();
		String actual_ffreight=request.getParameter("actual_ffreight");//实际运费
		String ys_ffreight="";//原先运费
		String currency=request.getParameter("currency");
		String pay_ffreight=request.getParameter("pay_ffreight");//支付运费金额
		String weight=request.getParameter("weight");//原本重量
		String actual_weight=request.getParameter("actual_weight");//实际重量
		String arrive_time=request.getParameter("arrive_time");//到货日期
		String transport_time=request.getParameter("transport_time");//国际运输时间
		String userid = request.getParameter("userid");
		IOrderwsDao dao=new OrderwsDao();
		if(Utility.getStringIsNull(remark)){
			ys_ffreight = dao.getOrder_reductionfreight(orderNo)+"";
			dao.upOrder_reductionfreight(orderNo, Utility.getIsDouble(price) ? Double.parseDouble(price) : 0, remark);
		}
		int res = 0;
		//获取用户email
		IOrderSplitServer splitServer = new OrderSplitServer();
		String toEmail = splitServer.getUserEmailByUserName(Integer.parseInt(userid));
		String uuid1 = UUIDUtil.getEffectiveUUID(0, toEmail);
		String path1 = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid1);
		StringBuffer sbBuffer = new StringBuffer("<div style='font-size: 14px;'>");
		sbBuffer.append("<a href='" + AppConfig.ip_email
				+ "'><img style='cursor: pointer' src='" + AppConfig.ip_email
				+ "/img/logo.png' ></img></a>");
//		sbBuffer.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
//		sbBuffer.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
//		sbBuffer.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account "+
//				"<a style='color: #0070C0' href='"+AppConfig.ip_email+path1+"'>here</a>.");
//		sbBuffer.append("</div></div></div>");
		
		sbBuffer.append("<div style='font-size: 14px;'><div style='font-weight: bolder;'>Dear "+toEmail+"</div>");
		double actual_ffreight_ = Utility.getIsDouble(actual_ffreight)? Double.parseDouble(actual_ffreight):0;
		double actual_weight_ = Utility.getIsDouble(actual_weight)? Double.parseDouble(actual_weight):0;
		double pay_ffreight_ = Utility.getIsDouble(pay_ffreight)? Double.parseDouble(pay_ffreight):0;
		double weight_ = Utility.getIsDouble(weight)? Double.parseDouble(weight):0;
		if(pay_ffreight_ > 0){
			sbBuffer.append("<br>Hello, your products are ready to ship, but you need to pay some additional shipping fee.<br>");
		}else{
			sbBuffer.append("<br>Hello, your products are ready to ship, please pay the shipping fee now.<br>");
		}
		sbBuffer.append(remark);
		double price_ = Utility.getIsDouble(price)? Double.parseDouble(price):0;
		if(actual_weight_ > weight_){
			sbBuffer.append("The original estimated shipping weight is " + weight + ", but the actual weight is " + actual_weight + ".  So, the shipping cost has changed from " + currency + ys_ffreight + " to " + currency + actual_ffreight + ".");
		}
		
		actual_ffreight_ = new BigDecimal(actual_ffreight_-price_).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
		if(price_ > 0){
			sbBuffer.append("We have also granted you an additional discount of "+currency+price_+".  This reduces shipping cost from " + currency + ys_ffreight + " to " + currency + actual_ffreight_ + ".");
		}
		if(pay_ffreight_ > 0){
			sbBuffer.append("<div>Shipping Fee Due:" + actual_ffreight_ + "</div>");
		}else{
			sbBuffer.append("<div>You have paid " + currency + pay_ffreight_ + " of shipping cost previously</div>");
			double remainning = new BigDecimal(actual_ffreight_ - pay_ffreight_).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
			sbBuffer.append("<div>Remaining Shipping Fee Due:" + currency + remainning + "</div>");
		}
		sbBuffer.append("<div>The estimated transit time is " + transport_time + " days, and you can expect to receive the package on " + arrive_time + "</div>");
		//获取uuid拼接邮件
		String uuid = UUIDUtil.getEffectiveUUID(0, toEmail);
		String path = UUIDUtil.getAutoLoginPath("/processesServlet?action=emailLink&className=OrderInfo&orderNo="+orderNo, uuid);
		sbBuffer.append("<br>Click <a href='"+AppConfig.ip_email+path+"'>here</a> to go to import-express.com to pay.</div>");
//		sbBuffer.append("<br>Click <a href='"+AppConfig.ip_email+"/processesServlet?action=emailLink&className=OrderInfo&orderNo="+orderNo+"'>here</a> to go to import-express.com to pay.</div>");
		sbBuffer.append("<br><div style='font-weight: bolder;'>Best regards</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>"); 
		sbBuffer.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
		sbBuffer.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
		path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
		sbBuffer.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account "+
				"<a style='color: #0070C0' href='"+AppConfig.ip_email+path+"'>here</a>.");
		sbBuffer.append("</div></div></div>");
		
		String sendemail = null;
	    String pwd = null;
		 if(Utility.getStringIsNull(copyEmail)){
		    IUserDao userDao = new com.cbt.processes.dao.UserDao();
		    String[] adminEmail =  userDao.getAdminUser(0, copyEmail, 0);
		    if(adminEmail != null){
			   sendemail = adminEmail[0];
			   pwd = adminEmail[1];
		    }
    	  }
		res = SendEmail.send(sendemail,pwd,toEmail, sbBuffer.toString(),"Shipping fee needed for your Import-Express order!","",orderNo,1);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	public void chaPsendEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String emailInfo=request.getParameter("emailInfo");
		String email=request.getParameter("email");
		String copyEmail=request.getParameter("copyEmail");
		String orderNo=request.getParameter("orderNo");
		String userId=request.getParameter("userId");
		String title = request.getParameter("title");
		
		IPictureComparisonService ips = new PictureComparisonServiceImpl();
		ips.updateCgdEmailFlag(orderNo);
		//线上插入changegooddata数据
		ips.insertOnlineChange(orderNo);
		int res = 0;
		if(Utility.getStringIsNull(emailInfo)){
			String sendemail = null;
    	    String pwd = null;
			 if(Utility.getStringIsNull(copyEmail)){
    		    IUserDao userDao = new com.cbt.processes.dao.UserDao();
    		    String[] adminEmail =  userDao.getAdminUser(0, copyEmail, 0);
    		    if(adminEmail != null){
    			   sendemail = adminEmail[0];
    			   pwd = adminEmail[1];
    		    }
	    	  }
			res = SendEmail.send(sendemail,pwd,email, emailInfo,title,copyEmail,orderNo,1);
		}else{
			ISpidersServer spidersServer = new SpidersServer();
			res = spidersServer.sendEmail(orderNo, Integer.parseInt(userId));
		}
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
}
