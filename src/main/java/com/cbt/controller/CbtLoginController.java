package com.cbt.controller;

import com.cbt.bean.LogBean;
import com.cbt.bean.UserBean;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.processes.service.UserServer;
import com.cbt.processes.servlet.Currency;
import com.cbt.service.LogService;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import com.cbt.website.service.IMessageServer;
import com.cbt.website.service.MessageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/cbt/cbtlogin")
public class CbtLoginController {

	@Autowired
	private LogService logService;
	
	@RequestMapping(value = "/verify", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> verify(HttpServletRequest request, HttpServletResponse response, String userName,
                                      String password, String remember) throws IOException {
		Map<String, Object> map=new HashMap<String, Object>();
		String ip= Utility.getIpAddress(request);
		Date date=new Date();
		String time=Utility.format(date,Utility.datePattern1);
		long type=1;
		IUserServer us = new UserServer();
		UserBean user = us.login(userName, password);
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
						if(!userinfo[1].equals(userName)){
							Cookie cookie=new Cookie("pageState",null);
							cookie.setMaxAge(0);
							cookie.setPath("/");
							response.addCookie(cookie);
						}
					}
					ISpiderServer is = new SpiderServer();
					user.setPass(null);
					request.getSession().setAttribute("userInfo", user);
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
					if(sessionId != null){
						//保存未登录之前加入购物车的商品
						is.LoginGoogs_car(sessionId, user.getId(),user.getCurrency(),Currency.getMaphl(request));
					}
					//购物车数量
					int cartNumbers = 0;
					Cookie cookieCart = WebCookie.getCookieByName(request, "cartNumber") ;
					if(cookieCart != null){
						cartNumbers = is.getGoogs_carNum(user.getId(), sessionId);
						cookieCart.setValue(cartNumbers+"");
						cookieCart.setMaxAge(3600*24*2);
						cookieCart.setPath("/");
						response.addCookie(cookieCart);
					}else{
						cartNumbers = is.getGoogs_carNum(user.getId(), sessionId);
						cookieCart=new Cookie("cartNumber", cartNumbers+"");
						cookieCart.setMaxAge(3600*24*2);
						cookieCart.setPath("/");
						response.addCookie(cookieCart);
					}
					HttpSession session = request.getSession();
					session.setAttribute("shopcar",null);
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
					request.getSession().setAttribute("expressType",null);
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
					request.getSession().setAttribute("expressType",null);
					map.put("code", "0");
					map.put("msg", "success");
					map.put("currency", user.getCurrency());
					map.put("cartNumber", cartNumbers);
				} else {
					map.put("code", "1");
					map.put("msg", "Incorrect user name or password");					
				}
		return map;
	}

}