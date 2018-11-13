package com.cbt.processes.servlet;

import com.cbt.bean.*;
import com.cbt.pay.dao.IPaymentDao;
import com.cbt.pay.dao.PaymentDao;
import com.cbt.processes.dao.IOrderuserDao;
import com.cbt.processes.dao.ISpiderDao;
import com.cbt.processes.dao.OrderuserDao;
import com.cbt.processes.dao.SpiderDao;
import com.cbt.processes.service.*;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import com.cbt.website.service.IOrderwsServer;
import com.cbt.website.service.OrderwsServer;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ylm
 * 订单操作
 */
public class OrderInfo extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private static final Log LOG = LogFactory.getLog(OrderInfo.class);
	
	
	//根据状态获取订单
	public void getOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String state = request.getParameter("state");
		 String page = request.getParameter("page");
		 int state_i = state == null ? 0 : Integer.parseInt(state);
		 int page_i = page == null ? 1 : Integer.parseInt(page);
		 
		/* HttpSession session = request.getSession();
		 
		 UserBean user = (UserBean) session.getAttribute("userInfo");*/
		String[] user =  WebCookie.getUser(request);
		IOrderServer os = new OrderServer();
		if(user != null){
			/*user = new UserBean();
			user.setId(2);user.setName("wanyang");*/
			int userID = Integer.parseInt(user[0]);
			double count = os.getOrderdNumber(userID, state_i);
			List<OrderDetailsBean> orders = os.getOrders(userID, state_i, page_i);
			int counts = (int)Math.ceil(count/8);
			request.setAttribute("count", counts);
			request.setAttribute("orderDetail", orders);
			request.setAttribute("username", user[1]);
			/*request.setAttribute("picture", user.getPicture());*/
			request.setAttribute("state", state);
			request.setAttribute("page", page);
			if(state_i == 5 && orders.size()>0){
				//查询确认价格中的有变动的数量和状态
				//(select sum(payment_amount) from payment where orderid='1426672037944' and paystatus=1)
				//select  sum(yourorder*goodsprice+ifnull(goodsfreight,0) ) from order_details  where orderid='1426672037944'
				//select count(id) from order_change where (roptype<6 or roptype=7) and del_state=0 and orderno='1429688620802'
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < orders.size(); i++) {
					map.put(orders.get(i).getOrderid(), "");
				}
				String[] orderno = new String[map.size()];
				int i = 0;
				for (String key : map.keySet()) {
					orderno[i] = key;
					i++;
				}
				request.setAttribute("change",os.getOrderChangeState(orderno));
			}
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/individual-order.jsp");
			homeDispatcher.forward(request, response);
		 }else{
			 response.sendRedirect("cbt/geton.jsp");
		 }
	}
	
	//zlw add start
	//根据状态获取订单
	public void getProductDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String state = request.getParameter("state");
		 String page = request.getParameter("page");
		 String orderNo = request.getParameter("orderNo");
		 int state_i = state == null ? 0 : Integer.parseInt(state);
		 int page_i = page == null ? 1 : Integer.parseInt(page);
		 
		String[] user =  WebCookie.getUser(request);
		IOrderServer os = new OrderServer();
		if(user != null){
			/*user = new UserBean();
			user.setId(2);user.setName("wanyang");*/
			int userID = Integer.parseInt(user[0]);
			double count = os.getOrderdNumber(userID, state_i);
			List<OrderDetailsBean> orders = os.getProductDetail(userID, state_i, page_i, orderNo);
			int counts = (int)Math.ceil(count/8);
			request.setAttribute("count", counts);
			request.setAttribute("orderDetail", orders);
			request.setAttribute("username", user[1]);
			request.setAttribute("state", state);
			request.setAttribute("page", page);
			if(state_i == 5 && orders.size()>0){
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < orders.size(); i++) {
					map.put(orders.get(i).getOrderid(), "");
				}
				String[] orderno = new String[map.size()];
				int i = 0;
				for (String key : map.keySet()) {
					orderno[i] = key;
					i++;
				}
				request.setAttribute("change",os.getOrderChangeState(orderno));
			}
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/individual-order.jsp");
			homeDispatcher.forward(request, response);
		 }else{
			 response.sendRedirect("cbt/geton.jsp");
		 }
	}
	//zlw add end
	
	public static void main(String[] args) {
		 String a="143.16";
		  int c = 5;
		  //System.out.println("c===>"+(*(float)c);   //1.75
	}
	
	//根据订单号 获取订单
	public void getCtpoOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		LOG.info("cbt_orderinfo.jsp:"+orderNo);
		String[] user =  WebCookie.getUser(request);
		if(user != null){
	//		os.getOrders(userID, state, startpage);
	//		List<CtpoOrderBean> ctpoOrderInfo = os.getCtpoOrderInfo(orderNo);
			IOrderServer os = new OrderServer();
			int userID = Integer.parseInt(user[0]);
			Map<String, Object> ctpoOrderInfo = os.getCtpoOrderInfo(orderNo,userID);
			List<CtpoOrderBean> ctpoList=(List<CtpoOrderBean>)ctpoOrderInfo.get("ctpoList");
			String jiaoqiTime=(String)ctpoOrderInfo.get("jiaoqiTime");
			BigDecimal b1 = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
			if(ctpoList !=null && ctpoList.size()>0){
				for(CtpoOrderBean ctpoOrderBean : ctpoList){
					int yourOrder=ctpoOrderBean.getYourOrder();
					List<String> changetotalpricelist=ctpoOrderBean.getChange1();
					if(changetotalpricelist != null && changetotalpricelist.size()>0){
						 BigDecimal b2 = new BigDecimal(Double.parseDouble(changetotalpricelist.get(1))*yourOrder).setScale(2, BigDecimal.ROUND_HALF_UP);
						 //BigDecimal b3 = new BigDecimal(Double.parseDouble(changetotalpricelist.get(0))*yourOrder).setScale(2, BigDecimal.ROUND_HALF_UP);
						 b1=b1.add(b2);
					}					         
				}
				request.setAttribute("currency", ctpoList.get(0).getCurrency());
			}
			//BigDecimal bproductCost=new BigDecimal(ctpoOrderInfo.get("productCost").toString()).setScale(2,   BigDecimal.ROUND_HALF_UP);
			//BigDecimal updatedtotalprice=bproductCost.add(b1);
			request.setAttribute("updatedtotalprice", b1.toString());
			request.setAttribute("ctpoorders", ctpoList);
			request.setAttribute("order_ac", ctpoOrderInfo.get("order_ac"));
			request.setAttribute("discount_amount", ctpoOrderInfo.get("discount_amount"));
			String foreign_freight = ctpoOrderInfo.get("foreign_freight").toString();
			String service_fee = ctpoOrderInfo.get("service_fee").toString();
			double foreign_freight_ = 0;
			if(Utility.getIsDouble(foreign_freight)){
				foreign_freight_ = Double.parseDouble(foreign_freight);
				if(Utility.getIsDouble(service_fee)){
					foreign_freight_ = new BigDecimal(Double.parseDouble(foreign_freight) + Double.parseDouble(service_fee)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
			}
			request.setAttribute("foreign_freight", foreign_freight_);
			request.setAttribute("productCost", new BigDecimal(ctpoOrderInfo.get("productCost").toString()).setScale(2,   BigDecimal.ROUND_HALF_UP).toString());
			request.setAttribute("totalProductCost", new BigDecimal(ctpoOrderInfo.get("totalProductCost").toString()).setScale(2,   BigDecimal.ROUND_HALF_UP).toString());
			request.setAttribute("jiaoqiTime", jiaoqiTime);
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/ctpo_orderinfo.jsp");
			homeDispatcher.forward(request, response);
		}else{
			 response.sendRedirect("cbt/geton.jsp");
		 }
	}
	 
	public void payfruit(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
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
		IInquiryServer server = new InquiryServer();
	    while((res=in.readLine())!=null){	
	    	String[] temp=res.split("=");
	    	if(temp.length>1){
	    		LOG.warn(temp[0]+"--"+temp[1]);
    			String orderid = request.getParameter("item_number");
	    		if(header.equals("SUCCESS")&&temp[0].equals("payment_status")&&temp[1].equals("Completed")){
	    			 
	    			String orderdesc = "询价描述";
	    			int paystatus = 1;
	    			float payment_amount= Float.parseFloat( java.net.URLDecoder.decode(request.getParameter("amt"),"utf-8"));
	    			String payment_cc=request.getParameter("cc");
	    			Payment pay= new Payment();
	    			String[] userinfo = WebCookie.getUser(request);
	    			int userid = userinfo != null ? Integer.parseInt(userinfo[0]) : 0;
	    			pay.setUserid(userid);//添加用户id
	    			pay.setOrderid(orderid);//添加订单id
	    			pay.setOrderdesc(orderdesc);//添加订单描述
	    			pay.setUsername(userinfo != null ? userinfo[1] : "");//添加用户名
	    			pay.setPaystatus(paystatus);//添加付款状态
	    			pay.setPaymentid(paymentid);//添加付款流水号（paypal返回的）
	    			pay.setPayment_amount(payment_amount);//添加付款金额（paypal返回的）
	    			pay.setPayment_cc(payment_cc);//添加付款币种（paypal返回的）
	    			if(ps.getPayment(paymentid) == 0){
		    			ps.addPayment(pay);
	    			}
	    			server.upInquiryPay(orderid, 1);
	    			response.sendRedirect("cbt/inquirysuccess.jsp");
	    		}else if(temp[0].equals("payment_status")&&!temp[1].equals("Completed")){
	    			server.upInquiryPay(orderid, 0);
	    			response.sendRedirect("cbt/inquiryfailed.jsp");
	    		}
	    	}else{
    			response.sendRedirect("cbt/inquiryfailed.jsp");
	    	}
	    }
	}
	 
	
	
	public String getSessionId(HttpServletRequest request,
			HttpServletResponse response){
		Cookie[] c =  request.getCookies();
		for (Cookie cookie2 : c) {
			if(cookie2.getName().equals("sessionId")){
				return cookie2.getValue();
			}
		}
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		Cookie cookie=new Cookie("sessionId", sessionId);
		cookie.setMaxAge(3600*24*2);
		cookie.setPath("/");
		response.addCookie(cookie);
		return sessionId;
	}
	
	
	//取消订单ylm
	public void closeOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String orderNo= request.getParameter("orderNo");
		String cancel_obj= request.getParameter("cancel_obj");
		IOrderServer server = new OrderServer();
		int res = server.cancelOrder(orderNo, Integer.parseInt(cancel_obj));
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/Deliveryfee.jsp");
		homeDispatcher.forward(request, response);
	}
	
	//删除订单
	public void delOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String data= request.getParameter("totalprice");
		request.setAttribute("productcost", data);
		response.sendRedirect("cbt/Deliveryfee.jsp");
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/Deliveryfee.jsp");
		homeDispatcher.forward(request, response);
	}
	
	//支付订单
	public void payOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String data= request.getParameter("totalprice");
		request.setAttribute("productcost", data);
		response.sendRedirect("cbt/Deliveryfee.jsp");
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/Deliveryfee.jsp");
		homeDispatcher.forward(request, response);
	}
	
	/**
	 * ylm
	 * 显示个人中心的订单详情
	 */
	public void individualOrderdetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String orderNo = request.getParameter("orderNo");
		IOrderwsServer serverws = new OrderwsServer();
		OrderBean order = serverws.getOrders(orderNo);
		IOrderuserDao  dao= new OrderuserDao();
		order.setOrderNo(orderNo);
		Map<String, String> map=Utility.getCountry();
		String country=map.get(order.getAddress().getCountry());
		if(country !=null ){
			Address address=order.getAddress();
			address.setCountry(country);
			order.setAddress(address);
		}
		List<OrderDetailsBean> orderDetail = dao.getIndividualOrdersDetails(orderNo);
		Forwarder forwarder = null;
		//出运中
		if(order.getState() == 3){
			forwarder = serverws.getForwarder(orderNo);
		}
		double credit = 0;
		if(order.getState() == 0){
			//获取运费抵扣金额
			String[] user =  WebCookie.getUser(request);
			if(user != null){
				IUserServer userServer = new UserServer();
				double[] balance_credit = userServer.getBalance(Integer.parseInt(user[0]));
				if(balance_credit[1] != 0){
					 //获取用户中的货币
					String currency1 = WebCookie.cookie(request, "currency");
					if(!Utility.getStringIsNull(currency1)){
						currency1 = "USD";
					}
					//获取汇率
					Map<String, Double> maphl = Currency.getMaphl(request);
					double exchange_rate = maphl.get(order.getCurrency())/maphl.get(currency1);
					credit = new BigDecimal(balance_credit[1]*exchange_rate).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
					request.setAttribute("credit", credit);
				}
			}else{
				 response.sendRedirect("cbt/geton.jsp");
				 return;
			}
		}
		String mode_transport = order.getMode_transport();
		int devtime = 0;
		//-----------lizhanjun start----------------
		for (OrderDetailsBean odb : orderDetail) {
			if(odb.getFileupload() != null && !"".equals(odb.getFileupload())) {
				//订单详情中的上传图片为多张图片名称 这里只显示一张 需截取第一个
				odb.setFileupload(odb.getFileupload().split(",")[0]);
			}
			if(Utility.getStringIsNull(odb.getDelivery_time())){
				int devtime1 = Utility.getIsInt(odb.getDelivery_time()) ? Integer.parseInt(odb.getDelivery_time()) : 5;
					if(devtime < devtime1){
						devtime = devtime1;
					}
			}
		}
		
		//-----------lizhanjun end------------------------
		if(order.getState() == 5 || order.getState() == 1){
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			String details_pay = "";
			Calendar c = Calendar.getInstance();
			try {
				date = f.parse(order.getCreatetime());
				c.setTime(date);
				c.add(Calendar.DATE, devtime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			details_pay = f.format(c.getTime()); 
			request.setAttribute("details_pay", details_pay);
		}
		if(forwarder!=null&&"emsinten".equals(forwarder.getLogistics_name())){
			forwarder.setLogistics_name("EMS");
		}
		if(Utility.getIsDouble(order.getActual_lwh())){
			request.setAttribute("actual_lwh", order.getActual_lwh());
		}
		request.setAttribute("order", order);
		//2016-1-7  sj update,remove serive_fee
//		double service_fee = 0;
		double service_fee = Utility.getIsDouble(order.getService_fee()) ? Double.parseDouble(order.getService_fee()) : 0;
		double product_cost = Double.parseDouble(order.getProduct_cost());
		double feight = 0;
		double order_oc = 0;
		if(order.getState() == 0){
			feight = Utility.getIsDouble(order.getForeign_freight()) ? Double.parseDouble(order.getForeign_freight()) : 0;
			if(feight > credit){
				order_oc = credit; 
			}else{
				order_oc = feight;
			}
		}else{
			feight = Utility.getIsDouble(order.getActual_ffreight()) ? Double.parseDouble(order.getActual_ffreight()) : 0;
		}
		double total_price = product_cost + feight + service_fee - order.getDiscount_amount() - order_oc;
		request.setAttribute("orderDetail", orderDetail);
		request.setAttribute("forwarder", forwarder);
		request.setAttribute("service_fee", service_fee);
		if(mode_transport.indexOf("@") > -1){
			String[] method = mode_transport.split("@");
			request.setAttribute("shipMethod", method[0]);
			request.setAttribute("shipMethod_day", method[1]);
			if(method.length > 3){
				request.setAttribute("shipMethod_price", method[3]);
			}
		}else{
			request.setAttribute("shipMethod", mode_transport);
			request.setAttribute("shipMethod_day", "20-45");
			request.setAttribute("shipMethod_price", 0);
		}
		if(Utility.getIsDouble(order.getActual_weight())){
			request.setAttribute("actual_weight",Double.parseDouble(order.getActual_weight()));
		}else{
			order.getActual_weight();
			request.setAttribute("actual_weight",0);
		}
		request.setAttribute("feight",feight+order.getExtra_freight());
		request.setAttribute("product_cost", Utility.getIsDouble(order.getProduct_cost()) ? Double.parseDouble(order.getProduct_cost()) : 0);
		order.setActual_weight(Utility.getIsDouble(order.getActual_weight()) ? order.getActual_weight() : "0");
		/*if(order.getState() == 0){
			request.setAttribute("total_price", new BigDecimal(product_cost - order.getDiscount_amount()).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		}else{*/
			request.setAttribute("total_price", new BigDecimal(total_price).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
		/*}*/
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/individual-orderdetails.jsp");
		homeDispatcher.forward(request, response);
	}
	/**
	 * ylm
	 * 保存用户订单评价
	 */
	public void saveEvaluate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String orderNo = request.getParameter("orderNo");
		String products = request.getParameter("products");
		String service = request.getParameter("service");
		String evaluates = request.getParameter("evaluate");
		String[] user =  WebCookie.getUser(request);
		PrintWriter out = response.getWriter();
		if(user != null){

			IOrderServer server = new OrderServer();
			Evaluate evaluate = new Evaluate();
			evaluate.setUserid(Integer.parseInt(user[0]));
			evaluate.setOrderNo(orderNo);
			evaluate.setProducts(Integer.parseInt(products));
			evaluate.setService(Integer.parseInt(service));
			evaluate.setEvaluate(evaluates);
		 
			int res = server.saveEvaluate(evaluate);
			out.print(res);
		}else{
			out.print(2);
		 }
		out.flush();
		out.close();
	}
	
	/**
	 * ylm
	 * 保存用户到货信息
	 */
	public void arriveOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String orderNo = request.getParameter("orderNo");
		IOrderServer server = new OrderServer();
		int res = server.upOrderState(orderNo, 4);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	/**
	 * ylm
	 *取消订单
	 */
	public void cancelOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String orderNo = request.getParameter("orderNo");
		IOrderServer server = new OrderServer();
		int cancel_obj = Integer.parseInt(request.getParameter("cancel_obj"));
		int res = server.cancelOrder(orderNo, cancel_obj);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	//提交订单前判断用户是否登录
	public void loginCarttoAdvance(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userId");
		String parameter = request.getParameter("itemId");
		String flag_s = request.getParameter("flag");
		int flag = Integer.parseInt(flag_s);
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo == null){
			Cookie cookie=new Cookie("pageState", (flag+8)+":"+parameter);
			cookie.setMaxAge(3600);
			cookie.setPath("/");
			response.addCookie(cookie);
			String exist = WebCookie.cookie(request, "userName");
			if(exist == null){
				response.sendRedirect("cbt/register.jsp?advance=1");
				return ;
			}
			response.sendRedirect("cbt/geton.jsp");
		}else{
			
			 if(flag==1){
				response.sendRedirect("cbt/DeliveryTime.jsp?userId="+userid+"&itemId="+parameter+"&advance=1");
			  }else if(flag==0){
				  response.sendRedirect("cbt/address.jsp?userId="+userid+"&itemId="+parameter+"&advance=1");
			  }else if(flag==2){
				  response.sendRedirect("cbt/RequiredInfo.jsp?userId="+userid+"&itemId="+parameter+"&advance=1");
			  }
			
		}
	}
	
	//ylm 提交预订单的客户问题
	public void saveQuestions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String question = request.getParameter("questions");
		String orderno = request.getParameter("orderno");
		IOrderServer server = new OrderServer();
		int res = server.upQuestions(orderno, question);
		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
		out.close();
	}
	
	//ylm 预订单显示订单详情列表 cbto_advance.jsp
	public void getOrderDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderno = request.getParameter("orderno");
		IOrderServer server = new OrderServer();
		AdvanceOrderBean orderdetail = server.getOrders(orderno);
		orderdetail.setOrderNo(orderno);
		request.setAttribute("advances", orderdetail);
		request.setAttribute("spider", orderdetail.getSpiderBean());
		javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/cbto_advance.jsp");
		homeDispatcher.forward(request, response);
	}
	//ylm 预订单显示订单详情列表 advance-order.jsp
	public void getSpiders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderno = request.getParameter("orderno");
		IOrderServer server = new OrderServer();
		List<SpiderBean> spider = server.getSpiders(orderno);
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo == null){
			response.sendRedirect("cbt/geton.jsp");
		}
		PrintWriter out = response.getWriter();
		out.print(spider);
		out.flush();
		out.close();
	}
	//ylm 发货邮件中链接点击
	public void emailLink(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderno = request.getParameter("orderNo");
		IOrderServer server = new OrderServer();
		int state = server.getOrderState(orderno);
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo == null){
			response.sendRedirect("cbt/geton.jsp");
		}
		if(state == 5){
			response.sendRedirect("processesServlet?action=getCtpoOrders&className=OrderInfo&orderNo="+orderno);
		}else{
			response.sendRedirect("processesServlet?action=individualOrderdetail&className=OrderInfo&orderNo="+orderno);
		}
	}
	
	//ylm订单详情更改运输方式
	public void changeModeTransport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		IPaymentDao dao = new PaymentDao();
		String[] userinfo = WebCookie.getUser(request);
		String orderNo = request.getParameter("orderid");
		String sf = request.getParameter("sf");//选择的运费
		String et = request.getParameter("et");//选择的运输方式
		int userid = 0;
		if (userinfo != null) {
			userid = Integer.parseInt(userinfo[0]);
		} else {
			LOG.warn("没有userid");
		}
		com.cbt.pay.service.IOrderServer os = new com.cbt.pay.service.OrderServer();
		List<OrderBean> list = os.getOrderInfo(userid, orderNo);
		String amount = "99999999999.00";
		int res = 0;
		if (list != null && list.size() > 0) {
			int obstatus = list.get(0).getState();
			if (obstatus == 5 || obstatus == 3) {
				out.print("1");
				return;
			}
			double remaining_sf_p = 0;
			double remaining_sf_t = 0;
			amount = String.valueOf(list.get(0).getRemaining_price());
			String pay_price_tow = list.get(0).getPay_price_tow();
			double pay_price_tow_ = Utility.getIsDouble(pay_price_tow) ? Double.parseDouble(pay_price_tow) : 0;
			double service_fee = Utility.getIsDouble(list.get(0).getService_fee()) ? Double.parseDouble(list.get(0).getService_fee()) : 0;
			double product_cost = Double.parseDouble(list.get(0).getProduct_cost());
			if(service_fee == -1){
				service_fee = 0;
			}else if(service_fee == 0){
				service_fee = Utility.getService_fee(product_cost);
			}
			double pay_price = list.get(0).getPay_price();
			if(Utility.getStringIsNull(et)){
				//修改订单表的运费和运输方式
				//判断是否已支付金额
				double sf_ = Double.parseDouble(sf);
				if(Utility.getIsDouble(pay_price_tow) && !pay_price_tow.equals("0")){
						//选择的运费小于已支付运费金额，
						if(sf_ < pay_price_tow_){
							remaining_sf_t = pay_price_tow_ - sf_;
							remaining_sf_t = new BigDecimal(list.get(0).getRemaining_price() - remaining_sf_t).setScale(2).doubleValue();
							//判断是否是负，负-退款到用户余额并且需要支付的金额为零
							if(remaining_sf_t <= 0){
								//添加余额变更记录
				    			RechargeRecord rr = new RechargeRecord();
				    			rr.setUserid(userid);
				    			rr.setPrice(remaining_sf_t);
				    			rr.setRemark_id(orderNo);
				    			rr.setType(2);
				    			rr.setRemark("运输方式修改退款shippingmethod_change："+orderNo);
				    			rr.setUsesign(1);
				    			dao.addRechargeRecord(rr);
								IUserServer userServer = new UserServer();
								//汇率换算
								String currency = WebCookie.cookie(request, "currency");
								double exchange_rate = 1;
								String currency_u = list.get(0).getCurrency();
								if(!currency_u.equals(currency)){
									//获取汇率单位
									ISpiderDao spiderDao = new SpiderDao();
									Map<String, Double> exchangeRate = spiderDao.getExchangeRate();//汇率值获取
									exchange_rate = exchangeRate.get(currency)/exchangeRate.get(currency_u);//汇率
								}
								//修改订单表的运费和运输方式
								pay_price = new BigDecimal(pay_price + remaining_sf_t).setScale(2).doubleValue();
								int ress = os.upOrderExpress(orderNo, et, sf, "0", sf, service_fee+"", pay_price); 
				    			//修改用户余额表
								if(ress > 0){
									userServer.upUserPrice(userid, -remaining_sf_t*exchange_rate);
									out.print(1+"@"+remaining_sf_t);
								}
								out.print(0);
								return;
							}else{
								//修改订单表的运费和运输方式
								amount = new BigDecimal(list.get(0).getRemaining_price() + remaining_sf_t).setScale(2).toString();
								res = os.upOrderExpress(orderNo, et, sf, amount, pay_price_tow, list.get(0).getService_fee(),pay_price);
								out.print(2+"@"+amount);
								return;
							}
						}else{//否则进行支付
							remaining_sf_p = sf_ - pay_price_tow_;
							amount = new BigDecimal(list.get(0).getRemaining_price() + remaining_sf_p).setScale(2).toString();
							res = os.upOrderExpress(orderNo, et, sf, amount, pay_price_tow, service_fee+"",pay_price); 
							out.print(2+"@"+amount);
							return;
						}
				}else{
					amount = new BigDecimal(list.get(0).getRemaining_price()).subtract(new BigDecimal(list.get(0).getActual_ffreight())).add(new BigDecimal(sf_)).add(new BigDecimal(service_fee)).setScale(2).toString();
					res = os.upOrderExpress(orderNo, et, sf, amount, pay_price_tow, service_fee+"" , pay_price);
					out.print(2+"@"+amount);
					return;
				}
			}
			out.print(res);
			out.flush();
			out.close();
		}
	}
	
	//根据订单号 获取替换商品
	public void getChangeProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderNo = request.getParameter("orderNo");
		String flag = request.getParameter("flag");
		//String currency = request.getParameter("currency");
		String[] user =  WebCookie.getUser(request);
		if(user != null){
			IOrderServer os = new OrderServer();
			List<ProductChangeBean> pcbList = os.getProductChangeInfo(orderNo,flag);
			//submitButton显隐
			int showFlag = 0;
			double exchange_rate = 1;
			DecimalFormat df=new DecimalFormat("#0.##");
			for(int i=0;i<pcbList.size();i++){
				//获取汇率
				 Map<String, Double> maphl = Currency.getMaphl(request);
				 exchange_rate = maphl.get(pcbList.get(i).getCurrency());
				 exchange_rate = exchange_rate/maphl.get("RMB");
				 //原商品价格
				 pcbList.get(i).setAliPrice(df.format(Double.parseDouble(pcbList.get(i).getAliPrice())*exchange_rate));
				 if(pcbList.get(i).getChangeFlag()==0){
					 showFlag=1;
				 }
				 List<ProductChangeBean> changeList = pcbList.get(i).getChangeList();
				 for(int j=0;j<changeList.size();j++){
					 //替代商品价格
					 changeList.get(j).setChangePrice(df.format(Double.parseDouble(changeList.get(j).getChangePrice())*exchange_rate));
				 }
			}
			
			request.setAttribute("showFlag", showFlag);
			request.setAttribute("pcbList", pcbList);
			request.setAttribute("orderNo", orderNo);
			//request.setAttribute("currency", currency);
			
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/productChange.jsp");
			homeDispatcher.forward(request, response);
		}else{
			 response.sendRedirect("cbt/geton.jsp");
		 }
	}
	
	//根据用户ID获取降价优惠商品
	public void getPriceReductionOffer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userid = request.getParameter("userid");
		String[] user =  WebCookie.getUser(request);
		if(user != null){
			IOrderServer os = new OrderServer();
			List<ProductChangeBean> pcbList = os.getPriceReductionOffer(userid);
			
			request.setAttribute("userId", userid);
			request.setAttribute("pcbList", pcbList);
			
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/priceReductionOffer.jsp");
			homeDispatcher.forward(request, response);
		}else{
			 response.sendRedirect("cbt/geton.jsp");
		 }
	}
	
	//取消单个降价优惠商品
	public void deletePriceReductionOffer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userId = Integer.parseInt(request.getParameter("userId"));
		int goodsDataId=Integer.parseInt(request.getParameter("goodsDataId"));
		int goodsCarId=Integer.parseInt(request.getParameter("goodsCarId"));
		String[] user =  WebCookie.getUser(request);
		PrintWriter out = response.getWriter();
		IOrderServer os = new OrderServer();
		if(user == null){
			out.println(-1);
		}else{
			
			int resultCount = os.updatePriceReductionOffer(userId, goodsDataId,goodsCarId);
			Map<String, String> map=new HashMap<String, String>();
			map.put("flag", resultCount+"");
			out.println(JSONArray.fromObject(map).toString());
		}
		out.flush();
		out.close();
	}
	
	//删除购物车单个降价优惠商品
	public void deleteGoodsCarInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int userId = Integer.parseInt(request.getParameter("userId"));
		String checkFlag = request.getParameter("checkFlag");
		String[] user =  WebCookie.getUser(request);
		PrintWriter out = response.getWriter();
		IOrderServer os = new OrderServer();
		if(user == null){
			out.println(-1);
		}else{
			int resultCount = 0;
			Map<String, String> map=new HashMap<String, String>();
			if("true".equals(checkFlag)){
				resultCount = os.updateGoosCar(userId);
			}else{
				resultCount =1;
				map.put("showFlag", resultCount+"");
			}
			
			
			map.put("flag", resultCount+"");
			out.println(JSONArray.fromObject(map).toString());
		}
		out.flush();
		out.close();
	}
	 
}
