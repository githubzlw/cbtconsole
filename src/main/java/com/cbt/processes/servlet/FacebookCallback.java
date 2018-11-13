package com.cbt.processes.servlet;

import com.cbt.bean.UserBean;
import com.cbt.processes.service.ISpiderServer;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.SpiderServer;
import com.cbt.processes.service.UserServer;
import com.cbt.util.AppConfig;
import com.cbt.util.SendRedirectPage;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 
 */
public class FacebookCallback extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private static final Log LOG = LogFactory.getLog(FacebookCallback.class);  
   
    public FacebookCallback() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code= request.getParameter("code");
		//-------------lzj---start获取商品的链接--------
		String purl = request.getParameter("purl");//用来判断如果不为空说明是商品详情页留言注册  注册完成后回到商品详情页

		String purl1 = "";
		if(purl!=null){
			purl1 = "%3Fpurl%3D"+URLEncoder.encode(purl,"utf-8").replaceAll("%", "%25");
		}
		//-------------lzj--end----------
		if(StringUtils.isBlank(code)){
            response.sendRedirect("https://www.facebook.com/dialog/oauth/?client_id="+AppConfig.APP_ID+"&redirect_uri="+AppConfig.redirect_uri+"&scope=email&display=page&expires=0");
            return;
		}
		//超时时间
		String url = "https://graph.facebook.com/oauth/access_token?client_id="+AppConfig.APP_ID+"&redirect_uri="+AppConfig.redirect_uri+purl1+"&client_secret="+AppConfig.APP_SECRET+"&code="+code;
		String  access_token1 = accessTokenFacebook(url);
		String access_token = null;//facebook的token
		Integer expires = null;
		String[] pairs = access_token1.split("&"); 
		for (int i = 0; i < pairs.length; i++) {
			if(pairs[i].indexOf("access_token") > -1){
				access_token = pairs[i];
			}
			if (pairs[i].indexOf("expires") > -1){
                expires = Integer.valueOf(pairs[i].split("=")[1]);
            }
		}
		
		  if (access_token != null && expires != null) {

				url = "https://graph.facebook.com/me?"+access_token+"&fields=id,name,picture,email";
				//{"id":"1379683048998970","name":"Limei Yin","picture":{"data":{"is_silhouette":false,"url":"https:\/\/fbcdn-profile-a.akamaihd.net\/hprofile-ak-xfa1\/v\/t1.0-1\/p50x50\/10868051_1380988952201713_8779052534558717183_n.jpg?oh=fc1d8a3c00ec30b343b31b05d108a596&oe=5534BFBE&__gda__=1425688632_9c7f6ca0a6592d2a5250356a5ac68c7d"}},"email":"1351535753\u0040qq.com"}
				//&fields=id,name,email,first_name,last_name,link,birthday,picture&access_token=AAABB
				
				String fbUser = accessTokenFacebook(url); 
				net.sf.json.JSONObject json1 =net.sf.json.JSONObject.fromObject(fbUser);
				net.sf.json.JSONObject json2 =net.sf.json.JSONObject.fromObject(json1.get("picture"));
				net.sf.json.JSONObject json3 =net.sf.json.JSONObject.fromObject(json2.get("data"));
				LOG.warn("facebook:"+fbUser);
				UserBean user = new UserBean();
				user.setActivationState(1);
				user.setEmail((String) json1.get("email"));
				user.setPicture((String) json3.get("picture"));
				String username = (String) json1.get("name");
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
				
				user.setName(username);
				
				IUserServer ius = new UserServer();
				user = ius.loginFacebook((String)json1.get("id"), user);
				username = user.getName();
				if(user != null){
						String[] userinfo = WebCookie.getUser(request);
						if(userinfo != null){
							if(!userinfo[1].equals(username)){
								Cookie cookie=new Cookie("pageState",null);
								cookie.setMaxAge(0);
								cookie.setPath("/");
								response.addCookie(cookie);
							}
						}
					ISpiderServer is = new SpiderServer();
					String sessionId = WebCookie.cookieValue(request, "sessionId");
					
					if(user.getLogReg() == 0){
						currency1 = user.getCurrency();
					}
					if(user.getLogReg() == 1){
						//保存未登录之前加入购物车的商品
						is.LoginGoogs_car(sessionId, user.getId(),currency1,Currency.getMaphl(request));
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
					Cookie cookie=new Cookie("userName", username);
					cookie.setMaxAge(3600*24*90);
					cookie.setPath("/");
					response.addCookie(cookie);
					request.getSession().setAttribute("userInfo", user);
					request.setAttribute("username", username);
					//------------lzj-------start
					if(purl != null && !"".equals(purl) && !"null".equals(purl)) {//用户留言后跳转到当前浏览的商品详情页面
						//request.setAttribute("lyts", request.getParameter("lyts"));
						String str= AppConfig.ip+"/processesServlet?action=getSpider&className=SpiderServlet&"+URLDecoder.decode(purl,"utf-8");
						response.sendRedirect(str);
					}else {//指定登录后跳转的地址
							String pageString = AppConfig.ip+SendRedirectPage.sendPage(request,user.getId());
							response.sendRedirect(pageString);
					}
					//------------lzj-------end
					return;
				}
		  }
			request.setAttribute("code", "The Facebook logon failure");
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
		 
	}
	 public static void main(String[] args) throws IOException {
		FacebookCallback f = new FacebookCallback();
		f.accessTokenFacebook("https://maps.googleapis.com/maps/api/distancematrix/json?origins=90210&destinations=94596&mode=driving&language=fr-FR&sensor=true");
	}

	private String accessTokenFacebook(String url) throws IOException {
		URI ui = null;
		String temp = null;
		String data = null;
		try {
		ui = new URI(url);
		LOG.warn(ui.getHost());
		LOG.warn(ui.isAbsolute());
		LOG.warn(url);

		HttpClient hc = null;
		hc = new DefaultHttpClient();
		//如果是美服则删除
		/*org.apache.http.HttpHost proxy = new org.apache.http.HttpHost("127.0.0.1",3213 );
		hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);*/

		HttpGet hg = new HttpGet(ui);

		HttpResponse hr = null;
		hr = hc.execute(hg);
		HttpEntity en = hr.getEntity();
		InputStream in = en.getContent();
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		while( (temp = br.readLine())!=null )
		{
			if(temp.indexOf("access_token")>-1){
				data = temp;
			}else if(temp.indexOf("picture")>-1){
				data = temp;
			}
		}
		br.close();

		} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return data; 
	  }
	 
	  
	/*	System.getProperties().setProperty("http.proxySet", "true");
		System.getProperties().setProperty("http.proxyHost", "174.139.78.250");
        System.getProperties().setProperty("http.proxyPort", "80");*/
     
}
