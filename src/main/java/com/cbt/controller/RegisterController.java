package com.cbt.controller;

import com.cbt.bean.LogBean;
import com.cbt.bean.UserBean;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.processes.service.UserServer;
import com.cbt.processes.servlet.Currency;
import com.cbt.processes.servlet.ProcessesServlet;
import com.cbt.service.impl.LogServiceImpl;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/cbt/register")
public class RegisterController {

	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> reg(HttpServletRequest request, HttpServletResponse response, String email,
                                   String name, String pass, String purl, String businessName) throws IOException {
		Map<String, Object> result=new HashMap<String, Object>();
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
			user.setBusinessName(businessName);
//				String activationCode = Md5Util.encoder(email+new Date().getTime());
//				user.setActivationCode(activationCode);
			user.setPass(pass);
			user.setToken(session.getId());
			//获取用户中的货币
			String currency1 = WebCookie.cookie(request, "currency");
			if(!Utility.getStringIsNull(currency1)){
				currency1 = "USD";
			}
			user.setCurrency(currency1);
			//获取汇率
			Map<String, Double> maphl = Currency.getMaphl(request);
			double exchange_rate = maphl.get(currency1)/maphl.get("USD");
			double applicable_credit = 50;
			user.setApplicable_credit(new BigDecimal(applicable_credit * exchange_rate).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			res = userS.regUser(user);
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
				cookie=new Cookie("pass_req", "@" + user.getId());
				cookie.setMaxAge(3600*24*90);
				cookie.setPath("/");
				response.addCookie(cookie);
				
				String sessionId = WebCookie.cookieValue(request, "sessionId");
				if(sessionId != null){
					//保存未登录之前加入购物车的商品
					is.LoginGoogs_car(sessionId, user.getId(),currency1,Currency.getMaphl(request));
				}
				String cartNumber = is.getGoogs_carNum(user.getId(), sessionId)+"";
				Cookie cookieCart=new Cookie("cartNumber", cartNumber);
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
				result.put("code", "0");
				result.put("currency", currency1);
				result.put("cartNumber", cartNumber);
			}
		else{
			result.put("code","1");
			result.put("msg", "Already exists");
		}
			return result;
	}

}