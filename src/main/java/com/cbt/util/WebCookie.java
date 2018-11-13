package com.cbt.util;

import com.cbt.bean.UserBean;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class WebCookie {
	public static String cookie(HttpServletRequest request, String cookiev){
		Cookie[] cookie= request.getCookies();
		if(cookie!=null){
			for (Cookie cookie2 : cookie) {
				if(cookie2.getName().equals(cookiev)){
					return cookie2.getValue();
				}
			}
		}
		return null;
	}
	
	public static String cookieValue(HttpServletRequest request, String cookiev){
		Cookie[] cookie= request.getCookies();
		if(cookie!=null){
			for (Cookie cookie2 : cookie) {
				//LOG.warn("cookie:"+cookie2.getName()+","+cookie2.getValue());
				if(cookie2.getName().equals(cookiev)){
					return cookie2.getValue();
				}
			}
		}
		return null;
	}
 
	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String name){
		Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie;
	    }else{
	        return null;
	    }   
	}
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){
		Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
		Cookie[] cookies = request.getCookies();
		if(null!=cookies){
			for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
		}
		return cookieMap;
	}
	
	 
	
	/**
	 * 判断用户是否登录
	 */
	public static  String[] getUser(HttpServletRequest request){
		HttpSession session = request.getSession();
		UserBean user = (UserBean) session.getAttribute("userInfo");
		if(user == null){
			String exist = WebCookie.cookie(request, "pass_req");
			if(exist != null){
				String username = WebCookie.cookie(request, "userName");
				String email = WebCookie.cookie(request, "email");
				String currency = WebCookie.cookie(request, "currency");
				String userid = exist.split("@")[1];
				String[] userinfo = {userid,username,email,currency};
				return userinfo;
			}
		}else{
			String[] userinfo = {user.getId()+"",user.getName(),user.getEmail(),user.getCurrency()};
			return userinfo;
		}
		return null;
	}
}
