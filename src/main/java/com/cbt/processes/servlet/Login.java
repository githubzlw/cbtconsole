package com.cbt.processes.servlet;

import com.cbt.bean.LogBean;
import com.cbt.bean.SpiderBean;
import com.cbt.bean.UserBean;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.processes.service.UserServer;
import com.cbt.service.LogService;
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
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
		String remember = request.getParameter("remember");
		Date date=new Date();
		String time=Utility.format(date,Utility.datePattern1);
		long type=1;
		String ip= Utility.getIpAddress(request);
		//ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());  
		LogService logService=(LogService) ProcessesServlet.context.getBean("logServiceImpl");
		//----lizhanjun--start-------------
		String purl = request.getParameter("purl");//用来判断如果不为空说明是商品详情页留言登陆 登陆完成后回到商品详情页
		String gbid = request.getParameter("gbid");//用来判断如果不为空说明是回复留言登陆 登陆完成后回到回复以及留言详情页面
		String redirect=request.getParameter("redirect");
		String info=request.getParameter("info");
		//----lizhanjun--end-------------
		String pre = request.getParameter("pre");//用来判断不为空说明是商品详情页申请批量优惠
		String uid = request.getParameter("uid");//用户点击批量优惠邮件中的链接
		 IUserServer us = new UserServer();
//		String loginCode = request.getParameter("loginCode");
//		 UserBean ub=(UserBean) request.getSession().getAttribute("userInfo");
		 String[] ub = WebCookie.getUser(request);
		UserBean user = us.login(name, pass_req);
//		if (loginCode != null) {
//			if (code.equals(loginCode.toUpperCase())) {
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
					logService.saveLog(LogBean);
					String[] userinfo = WebCookie.getUser(request);
					if(userinfo != null){
						if(!userinfo[1].equals(name)){
							Cookie cookie=new Cookie("pageState",null);
							cookie.setMaxAge(0);
							cookie.setPath("/");
							response.addCookie(cookie);
						}
					}
					ISpiderServer is = new SpiderServer();
					user.setPass(null);
					request.getSession().setAttribute("userInfo", user);
					request.getSession().setMaxInactiveInterval(3600*24);
					request.setAttribute("username", user.getName());
					//存放用户名和 密码
					Cookie cookie=new Cookie("userName", user.getName());
					cookie.setMaxAge(3600*24*90);
					cookie.setPath("/");
					response.addCookie(cookie);
					cookie=new Cookie("email", user.getEmail());
					cookie.setMaxAge(3600*24*90);
					cookie.setPath("/");
					response.addCookie(cookie);
					cookie=new Cookie("pass_req", "@" + user.getId());
					cookie.setMaxAge(3600*24*90);
					cookie.setPath("/");
					response.addCookie(cookie);
					cookie=new Cookie("currency", user.getCurrency());
					cookie.setMaxAge(3600*24*90);
					cookie.setPath("/");
					response.addCookie(cookie);
					if(remember != null){
						if(remember.equals("on")){
							cookie=new Cookie("saveuser", "true");
							cookie.setMaxAge(3600*24*90);
							cookie.setPath("/");
							response.addCookie(cookie);
						}
					}
					
					String sessionId = WebCookie.cookieValue(request, "sessionId");
					int res = 0;
					if(sessionId != null && ub == null){
						//保存未登录之前加入购物车的商品
						res = is.LoginGoogs_car(sessionId, user.getId(),user.getCurrency(),Currency.getMaphl(request));
					}
					//购物车数量
					Cookie cookieCart = WebCookie.getCookieByName(request, "cartNumber");
					HttpSession session = request.getSession();
					List<SpiderBean> list = is.getGoogs_cars("", user.getId(), 0);
					session.setAttribute("shopcar", list);
					if(cookieCart != null){
						cookieCart.setValue(list.size()+"");
					}else{
						cookieCart=new Cookie("cartNumber", list.size()+"");
					}
					cookieCart.setMaxAge(3600*24*2);
					cookieCart.setPath("/");
					response.addCookie(cookieCart);
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
					if(redirect != null && !"".equals(redirect)){
						response.sendRedirect(redirect);
						return;
					}
					//----lizhanjun--start-------------
					if(purl != null && !"".equals(purl) && !"null".equals(purl)) {//用户留言后跳转到当前浏览的商品详情页面
						request.setAttribute("lyts", request.getParameter("lyts"));
						purl = URLDecoder.decode(purl,"utf-8");
						String str= AppConfig.ip+"/processesServlet?action=getSpider&className=SpiderServlet&"+purl+"&pre="+pre;
						response.sendRedirect(str);
					}else if(gbid != null && !"".equals(gbid)) {//回复留言后跳转到显示回复以及留言信息页面
						String str= AppConfig.ip+"/processesServlet?action=findByGBid&className=GuestBookServlet&guestbookId="+gbid+"";
						response.sendRedirect(str);
					}
					//----lizhanjun--end-------------
					else if(Utility.getStringIsNull(info)){
						String str= AppConfig.ip+"/paysServlet?action=getSelectedItem&className=RequireInfoServlet&info="+info;
						response.sendRedirect(str);
					}
					else if(Utility.getStringIsNull(uid)){
						String str= AppConfig.ip+"/processesServlet?action=sendcart&className=Preferential&uid="+uid;
						response.sendRedirect(str);
					}
					else if(res == 0){//指定登录后跳转的地址
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
				/*return;
			}
			request.setAttribute("code", "验证码错误");
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		} else {
			if (user != null) {
				System.out.println("登录成功");
				user.setPass(null);
				request.getSession().setAttribute("userInfo", user);
				request.setAttribute("username", user.getName());
				javax.servlet.RequestDispatcher homeDispatcher = request
						.getRequestDispatcher("cbt/spider.jsp");
				homeDispatcher.forward(request, response);
				return;
			}
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/spider.jsp");
			homeDispatcher.forward(request, response);
		}*/

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
		session.setAttribute("shopcar",null);
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
    
}
