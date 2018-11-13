package com.cbt.processes.servlet;

import com.cbt.bean.Payment;
import com.cbt.processes.service.IOrderServer;
import com.cbt.processes.service.IPayServer;
import com.cbt.processes.service.OrderServer;
import com.cbt.processes.service.PayServer;
import com.cbt.util.WebCookie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;

/**
 * Servlet implementation class PayServlet
 */
public class PayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(PayServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PayServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String tag = request.getParameter("action");
		String ClassName = "com.cbt.processes.servlet.PayServlet";
		try
		{
		 Class clazz = Class.forName(ClassName); 
		
		Method method = clazz.getDeclaredMethod(tag, new Class[] {HttpServletRequest.class, HttpServletResponse.class });
		method.invoke(clazz.newInstance(), new Object[] { request, response });
		
		} catch (Exception e)
		{
		// TODO: handle exception
		e.printStackTrace();
		}
	}
 
		
		 /**
	     * 个人中心确认价格中的支付
	     */
		protected void paychack(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			IPayServer ps = new PayServer();
			String paymentid=request.getParameter("tx").toUpperCase();
			String str="cmd=_notify-synch&tx="+paymentid+"&at=Mi-Qu2mmOY841halXqpYeQABC7ZelRe7Ts5hL9a4__qabCxAE4bGsQ3IWqy";
			URL u = new URL("https://www.paypal.com/cgi-bin/webscr");  
		    //正式环境  
		    URLConnection uc = u.openConnection();  
		    uc.setDoOutput(true);  
		    uc.setRequestProperty("Content-Type",  
		            "application/x-www-form-urlencoded");  
		    PrintWriter pw = new PrintWriter(uc.getOutputStream());  
		    pw.println(str);  
			pw.flush();
			pw.close();
		    //接受 PayPal 对 IPN 回发的回复信息  
		    BufferedReader in = new BufferedReader(new InputStreamReader(
		            uc.getInputStream()));
		    String header=in.readLine();
		    LOG.warn(header);
		    String res="";
		    String orderid = "";
		    int paystatus = 0;
		    String payment_cc = "";
			String orderdesc = "订单描述";
			String payment_amounts = "";
		    while((res=in.readLine())!=null){	
		    	String[] temp=res.split("=");
		    	if(temp.length>1){
		    		LOG.warn(temp[0]+"--"+temp[1]);
		    		String paystatuString = temp[1];
		    		if(temp[0].equals("item_number")){
		    			orderid = paystatuString;
			    		LOG.warn("orderid:"+orderid);
		    		}
		    		else if(temp[0].equals("mc_currency")){
		    			payment_cc = temp[1];
		    		}else if(temp[0].equals("payment_gross")){
		    			payment_amounts = temp[1];
		    		}
		    		else if(header.equals("SUCCESS")&&temp[0].equals("payment_status")&&(paystatuString.equals("Completed")||paystatuString.equals("Pending"))){
		    			LOG.warn("付款成功");
		    			orderdesc = paystatuString;
		    			paystatus = paystatuString.equals("Pending") ? 2 : 1;
		    		}else if(temp[0].equals("payment_status")&&!temp[1].equals("Completed")){
		    			response.sendRedirect("cbt/individual-payfailed.jsp");
		    		}
		    	}else{
		    		LOG.warn(temp[0]);
		    	}
		    }
		    if(paystatus != 0){
		    	Payment pay= new Payment();
    			String[] userinfo = WebCookie.getUser(request);
    			int userid = userinfo != null ? Integer.parseInt(userinfo[0]) : 0;
    			pay.setUserid(userid);//添加用户id
    			pay.setOrderid(orderid);//添加订单id
    			pay.setOrderdesc(orderdesc);//添加订单描述
    			pay.setUsername(userinfo != null ? userinfo[1] : "");//添加用户名
    			pay.setPaystatus(paystatus);//添加付款状态
    			pay.setPaymentid(paymentid);//添加付款流水号（paypal返回的）
    			float payment_amount= Float.parseFloat( java.net.URLDecoder.decode(payment_amounts,"utf-8"));
    			pay.setPayment_amount(payment_amount);//添加付款金额（paypal返回的）
    			pay.setPayment_cc(payment_cc);//添加付款币种（paypal返回的）
    			if(ps.getPayment(paymentid) == 0){
	    			ps.addPayment(pay);
    			}
    			response.sendRedirect("cbt/individual-paysuccess.jsp");
		    }else{
    			response.sendRedirect("cbt/individual-payfailed.jsp");
		    }
		    
		    in.close();
		}
		
		 /**
	     * 个人中心等待付款的支付
	     */
		protected void paychacking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			IPayServer ps = new PayServer();
			String paymentid=request.getParameter("tx").toUpperCase();
			String str="cmd=_notify-synch&tx="+paymentid+"&at=Mi-Qu2mmOY841halXqpYeQABC7ZelRe7Ts5hL9a4__qabCxAE4bGsQ3IWqy";
			URL u = new URL("https://www.paypal.com/cgi-bin/webscr");  
		    //正式环境  
		    URLConnection uc = u.openConnection();  
		    uc.setDoOutput(true);  
		    uc.setRequestProperty("Content-Type",  
		            "application/x-www-form-urlencoded");  
		    PrintWriter pw = new PrintWriter(uc.getOutputStream());  
		    pw.println(str);
			pw.flush();
			pw.close();
		    //接受 PayPal 对 IPN 回发的回复信息  
		    BufferedReader in = new BufferedReader(new InputStreamReader(
		            uc.getInputStream()));  
		    String header=in.readLine();
		    LOG.warn(header);
		    String res="";
		    String orderid = "";
		    int paystatus = 0;
		    String payment_cc = "";
			String orderdesc = "订单描述";
			String payment_amounts = "";
		    while((res=in.readLine())!=null){	
		    	String[] temp=res.split("=");
		    	if(temp.length>1){
		    		LOG.warn(temp[0]+"--"+temp[1]);
		    		String paystatuString = temp[1];
		    		if(temp[0].equals("item_number")){
		    			orderid = paystatuString;
			    		LOG.warn("orderid:"+orderid);
		    		}
		    		else if(temp[0].equals("mc_currency")){
		    			payment_cc = temp[1];
		    		}else if(temp[0].equals("payment_gross")){
		    			payment_amounts = temp[1];
		    		}
		    		else if(header.equals("SUCCESS")&&temp[0].equals("payment_status")&&(paystatuString.equals("Completed")||paystatuString.equals("Pending"))){
		    			LOG.warn("付款成功");
		    			orderdesc = paystatuString;
		    			paystatus = paystatuString.equals("Pending") ? 2 : 1;
		    		}else if(temp[0].equals("payment_status")&&!temp[1].equals("Completed")){
		    			response.sendRedirect("cbt/individual-payfailed.jsp");
		    		}
		    	}else{
		    		LOG.warn(temp[0]);
		    	}
		    }
		    if(paystatus != 0){
		    	Payment pay= new Payment();
    			String[] userinfo = WebCookie.getUser(request);
    			int userid = userinfo != null ? Integer.parseInt(userinfo[0]) : 0;
    			pay.setUserid(userid);//添加用户id
    			pay.setOrderid(orderid);//添加订单id
    			pay.setOrderdesc(orderdesc);//添加订单描述
    			pay.setUsername(userinfo != null ? userinfo[1] : "");//添加用户名
    			pay.setPaystatus(paystatus);//添加付款状态
    			pay.setPaymentid(paymentid);//添加付款流水号（paypal返回的）
    			float payment_amount= Float.parseFloat( java.net.URLDecoder.decode(payment_amounts,"utf-8"));
    			pay.setPayment_amount(payment_amount);//添加付款金额（paypal返回的）
    			pay.setPayment_cc(payment_cc);//添加付款币种（paypal返回的）
    			if(ps.getPayment(paymentid) == 0){
	    			ps.addPayment(pay);
    			}
    			//将状态改为确认价格中
    			if(paystatus ==1){
	    			IOrderServer iOrderServer = new OrderServer();
	    			iOrderServer.updateOrderState(userid, orderid);
    			}
    			String[] strings = {orderid,paymentid,payment_amount+""};
    			request.setAttribute("payinfo", strings);
    			response.sendRedirect("cbt/individual-paysuccess.jsp");
		    }else{
    			LOG.warn("付款失败");
    			response.sendRedirect("cbt/individual-payfailed.jsp");
		    }
		    
		    in.close();  
		}
		

	
		
}
