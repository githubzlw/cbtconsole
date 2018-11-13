package com.cbt.website.servlet;

import com.cbt.bean.LogBean;
import com.cbt.bean.UserBean;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.processes.servlet.Currency;
import com.cbt.processes.servlet.ProcessesServlet;
import com.cbt.service.impl.LogServiceImpl;
import com.cbt.util.AppConfig;
import com.cbt.util.SendRedirectPage;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import com.cbt.website.service.IMessageServer;
import com.cbt.website.service.MessageServer;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 用户登录
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public Login() {
        super();
    }
    
	protected void login(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("userName");
		String pass_req = request.getParameter("password");
		String currency = request.getParameter("currency");//2015.12.04 sj add
		//String remember = request.getParameter("remember");
		Date date=new Date();
		String time=Utility.format(date,Utility.datePattern1);
		long type=1;
		String ip= Utility.getIpAddress(request);
		//ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());  
		//LogService logService=(LogService) ProcessesServlet.context.getBean("logServiceImpl");
//		IUserServer us = new UserServer();
//		String loginCode = request.getParameter("loginCode");
//		UserBean user = us.login(name, pass_req);
		//获取用户中的货币 2015.12.04 sj add
		if(!Utility.getStringIsNull(currency)){
			currency = "USD";
		}
		UserBean user= new UserBean();
		user.setId(Integer.parseInt(name));
		user.setName(pass_req);
		if (user != null) {
			LogBean LogBean=new LogBean();
			int userId=user.getId();
			LogBean.setUserId((long)userId);
			LogBean.setIp(ip);
			LogBean.setTime(time);
			LogBean.setType(type);
			Calendar c=Calendar.getInstance();
			LogBean.setYear(c.get(Calendar.YEAR)+"");
			LogBean.setMonth(c.get(Calendar.MONTH)+1+"");
			LogBean.setDay(c.get(Calendar.DATE)+"");
			ISpiderServer is = new SpiderServer();
			user.setPass(null);
			request.getSession().setAttribute("userInfo", user);
			request.setAttribute("username", user.getName());
			//存放用户名和 密码
			Cookie cookie=new Cookie("userName", user.getName());
			cookie.setMaxAge(3600*24*90);
			cookie.setPath("/");
			response.addCookie(cookie);
			
//			cookie=new Cookie("pass_req", pass_req + "@" + user.getId());
//			cookie.setMaxAge(3600*24*90);
//			cookie.setPath("/");
//			response.addCookie(cookie);
//			if(remember != null){
//				if(remember.equals("on")){
//					cookie=new Cookie("saveuser", "true");
//					cookie.setMaxAge(3600*24*90);
//					cookie.setPath("/");
//					response.addCookie(cookie);
//				}
//			}

			String sessionId = WebCookie.cookieValue(request, "sessionId");
			int res = 0;
			if(sessionId != null){
				//保存未登录之前加入购物车的商品
				res = is.LoginGoogs_car(sessionId, user.getId(),user.getCurrency(),Currency.getMaphl(request));
			}
			//购物车数量
			Cookie cookieCart = WebCookie.getCookieByName(request, "cartNumber") ;
			if(cookieCart != null){
				int cartNumbers = is.getGoogs_carNum(user.getId(), sessionId);
				cookieCart.setValue(cartNumbers+"");
				cookieCart.setMaxAge(3600*24*2);
				cookieCart.setPath("/");
				response.addCookie(cookieCart);
			}else{
				cookieCart=new Cookie("cartNumber", is.getGoogs_carNum(user.getId(), sessionId)+"");
				cookieCart.setMaxAge(3600*24*2);
				cookieCart.setPath("/");
				response.addCookie(cookieCart);
			}
			/*cookie=new Cookie("cartNumber", is.getGoogs_carNum(user.getId(), sessionId)+"");
			cookie.setMaxAge(3600*24*2);
			cookie.setPath("/");
			response.addCookie(cookie);*/
			// 获取用户未读消息数量
			IMessageServer messageServer = new MessageServer();
			int mes = messageServer.getMessageSize(user.getId());
			cookie=new Cookie("message", mes+"");
			cookie.setMaxAge(3600*24*2);
			cookie.setPath("/");
			response.addCookie(cookie);
			cookie=new Cookie("expressType",null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
//			cookie=new Cookie("currency", user.getCurrency());2015.12.04 sj  cancel
			cookie=new Cookie("currency",currency);
			cookie.setMaxAge(3600*24*90);
			cookie.setPath("/");
			response.addCookie(cookie);
			cookie=new Cookie("expressType",null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			if(res == 0){//指定登录后跳转的地址
				String pageString = AppConfig.ip+SendRedirectPage.sendPage(request,user.getId());
				response.sendRedirect(pageString);
			}else{
				response.sendRedirect("processesServlet?action=getShopCar&className=Goods");
			}
			 
		} else {
			request.setAttribute("code", "Incorrect user name or password");
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		}

	}
 /*
	 // 用户是否存在
	public UserBean islogin(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		HttpSession session = request.getSession(); 
		UserBean user = (UserBean) session.getAttribute("userInfo");
		return user;
	}*/

    // 注销登录
    public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String ip= Utility.getIpAddress(request);
		//ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());  
		LogServiceImpl logServiceImpl=(LogServiceImpl) ProcessesServlet.context.getBean("logServiceImpl");
		HttpSession session = request.getSession();
		LogBean LogBean=new LogBean();
		UserBean ub=(UserBean) session.getAttribute("userInfo");
		if(ub != null){
			LogBean.setUserId((long)ub.getId());
		}
		Date date=new Date();
		String time=Utility.format(date,Utility.datePattern1);
		long type=2;		
		session.removeAttribute("userInfo");
		session.invalidate();
		//清空Cookie操作 
		/*Cookie cookie = new Cookie("cartNumber",null); 
		cookie.setMaxAge(0); 
		cookie.setPath("/");//根据你创建cookie的路径进行填写 
		response.addCookie(cookie); */
		Cookie cookie = WebCookie.getCookieByName(request, "cartNumber") ;
		if(cookie != null){
			cookie.setValue(0+"");
			cookie.setMaxAge(3600*24*2);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
		String[] userinfo = WebCookie.getUser(request);
		if(userinfo!=null){
			String userId=userinfo[0];
			LogBean.setUserId(Long.parseLong(userId));
			cookie = new Cookie("sessionId",null);
			cookie.setMaxAge(0); 
			cookie.setPath("/");//根据你创建cookie的路径进行填写 
			response.addCookie(cookie); 
		}
		LogBean.setIp(ip);
		LogBean.setTime(time);
		LogBean.setType(type);
		Calendar c=Calendar.getInstance();
		LogBean.setYear(c.get(Calendar.YEAR)+"");
		LogBean.setMonth(c.get(Calendar.MONTH)+1+"");
		LogBean.setDay(c.get(Calendar.DATE)+"");
		logServiceImpl.saveLog(LogBean);

	    cookie=new Cookie("expressType",null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	    cookie=new Cookie("pass_req",null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		cookie=new Cookie("pageState",null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
    	response.sendRedirect("cbt/geton.jsp");
		/*javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/geton.jsp");
		homeDispatcher.forward(request, response);*/
    }
    public static void main(String[] args) {
    	Date date = new Date();
    	 DateFormat df1 = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL); //显示日期，周，上下午，时间（精确到秒）
    	 df1.setTimeZone(TimeZone.getTimeZone("Etc/GMT+7"));
         System.out.println(df1.format(date)); 
         DateFormat df2 = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL); //显示日期，周，上下午，时间（精确到秒）
    	 df2.setTimeZone(TimeZone.getTimeZone("Etc/GMT+5"));
         System.out.println(df2.format(date));
	}
}
