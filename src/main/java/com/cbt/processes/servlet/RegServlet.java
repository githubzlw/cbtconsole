package com.cbt.processes.servlet;

import com.cbt.bean.LogBean;
import com.cbt.bean.UserBean;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.processes.service.UserServer;
import com.cbt.service.impl.LogServiceImpl;
import com.cbt.util.AppConfig;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * 用户注册
 */
public class RegServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(RegServlet.class);
    public RegServlet() {
        super();
    }

	protected void reg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String businessName = request.getParameter("businessName");
		//-------------lzj---start获取商品的链接--------
		String purl = request.getParameter("purl");//用来判断如果不为空说明是商品详情页留言注册  注册完成后回到商品详情页
		//-------------lzj--end----------
//		String yz = request.getParameter("yz");
		IUserServer userS = new UserServer();
//		System.out.println("保存失败"+yz);
		int res = 0;
//		if(yz != null){
			HttpSession session = request.getSession();
		/*	String code = (String) session.getAttribute(RandomValidate.RANDOMCODEKEY);*/
//			if(code.equals(yz.toUpperCase())){
			UserBean user = new UserBean();
			user.setEmail(email);
			user.setName(name);
//				String activationCode = Md5Util.encoder(email+new Date().getTime());
//				user.setActivationCode(activationCode);
			user.setPass(pass);
			user.setBusinessName(businessName);
			user.setToken(session.getId());
			 String[] ub = WebCookie.getUser(request);
			//获取用户中的货币
			String currency1 = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency1)){
				currency1 = "USD";
				Cookie cookie=new Cookie("currency", currency1);
				cookie.setMaxAge(3600*24*90);
				cookie.setPath("/");
			}
			double applicable_credit = 50;
			//获取汇率
			Map<String, Double> maphl = Currency.getMaphl(request);
			double exchange_rate = maphl.get(currency1)/maphl.get("USD");
			user.setCurrency(currency1);
			user.setApplicable_credit(new BigDecimal(applicable_credit * exchange_rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			res = userS.regUser(user);
			PrintWriter out = response.getWriter();
			if(res > 0){
				//保存注册信息
				ISpiderServer is = new SpiderServer();
				user.setPass(null);
				user.setId(res);
				request.getSession().setAttribute("userInfo", user);
				request.setAttribute("username", user.getName());
				//存放用户名和 密码
				Cookie cookie=new Cookie("userName", name);
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
	
				String sessionId = WebCookie.cookieValue(request, "sessionId");
				
				if(sessionId != null && ub == null){
					//保存未登录之前加入购物车的商品
					is.LoginGoogs_car(sessionId, user.getId(),currency1,Currency.getMaphl(request));
				}
				Cookie cookieCart=new Cookie("cartNumber", is.getGoogs_carNum(user.getId(), sessionId)+"");
				cookieCart.setMaxAge(3600*24*2);
				cookieCart.setPath("/");
				response.addCookie(cookieCart);
				LogServiceImpl logServiceImpl=(LogServiceImpl) ProcessesServlet.context.getBean("logServiceImpl");
				Date date=new Date();
				String time=Utility.format(date,Utility.datePattern1);
				String ip= Utility.getIpAddress(request);
				long type=3;
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
				logServiceImpl.saveLog(LogBean);
				//------------lzj-------start
				if(purl != null && !"".equals(purl) && !"null".equals(purl)) {//用户留言后跳转到当前浏览的商品详情页面
					//request.setAttribute("lyts", request.getParameter("lyts"));
					String str= AppConfig.ip+"/processesServlet?action=getSpider&className=SpiderServlet&url="+purl;
					//request.setAttribute("purl", str);
					//request.getRequestDispatcher("cbt/reg_success.jsp").forward(request, response);
//					response.sendRedirect("cbt/reg_success.jsp?purl="+URLEncoder.encode(str,"UTF-8")+"");
					out.print(URLEncoder.encode(str,"UTF-8"));
					out.flush();
					out.close();
				}else {
//					response.sendRedirect("cbt/reg_success.jsp");
					out.print(0);
					out.flush();
					out.close();
				}
				//------------lzj-------end
				LOG.warn("code="+res);
				return;
				//购物车数量
/*				int cartNumber = is.getGoogs_carNum(user.getId(), sessionId);
				cookie=new Cookie("cartNumber", cartNumber+"");
				cookie.setMaxAge(3600*24*2);
				cookie.setPath("/");
				response.addCookie(cookie);
				//指定登录后跳转的地址
				if(cartNumber == 0){
					response.sendRedirect("cbt/reg_success.jsp");
					return;
				}
				String pageString = SendRedirectPage.sendPage(request,user.getId());
				response.sendRedirect(pageString);
				return;*/
			}
//			}else{
//				res = "code="+ResCode.YZCODE+"&info=验证码输入错误";
//			}
//		}
		else{
			out.print(1);
			out.flush();
			out.close();
		}
	}

	/**
	 * 激活帐号，改变用户激活状态
	 */
	protected void regActivate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String email = request.getParameter("email");
		String validateCode = request.getParameter("validateCode");
		IUserServer us = new UserServer();
		String res = us.upUserState(email, validateCode);
		if(res.indexOf("code=1")>-1){
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/reg_success.jsp");
			homeDispatcher.forward(request, response);
		}else{
			request.setAttribute("code", res);
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/reg_activate.jsp");
			homeDispatcher.forward(request, response);
			
		}
	}
	
	/**
	 * 注册发送邮件记录日志
	 */
	protected void regSendEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String email = request.getParameter("email");
		IUserServer us = new UserServer();
		us.regSendEmail(email, name, pass);
	}
	
	/**
	 * 查询 用户名称和邮箱是否存在
	 */
	protected void getNameEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		IUserServer us = new UserServer();
		int id = us.getNameEmail(name, email);
		PrintWriter out = response.getWriter();
		out.print(id);
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) {
		//System.out.println("jksjd.com".substring("jksjd.com".length()-4));
	}
}
