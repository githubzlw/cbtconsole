package com.cbt.processes.servlet;
/**
 * 主页
 */

import com.cbt.bean.UserBean;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;
import com.cbt.util.AppConfig;
import com.cbt.util.Utility;
import com.cbt.util.WebCookie;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public IndexServlet() {
        super();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		//cbtconsole
		String cbtCks="";
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for(Cookie c:cookies){
				if(c.getName().equals("cbtconsole")){
					cbtCks=c.getValue();
				}
			}
		}
		if(Utility.getStringIsNull(cbtCks)){
			//已存cookie
			String[] split = cbtCks.split("[$]");
			String cbtName=split[0];
			String cbtPass=split[1];
			IUserServer us = new UserServer();
			UserBean login = us.login(cbtName, cbtPass);
			login.setPass(null);
			request.getSession().setAttribute("userInfo", login);
			request.setAttribute("username", login.getName());
			us=null;
		}
		RequestDispatcher homeDispatcher = request.getRequestDispatcher("apa/index.html");
		homeDispatcher.forward(request, response);
	}
	

    /**
     * main_top数据获取
     */
	public void getUserInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] userinfo = WebCookie.getUser(request);
		Cookie cartNumber = WebCookie.getCookieByName(request, "cartNumber");
		String message = WebCookie.cookie(request, "message");
		String email = WebCookie.cookie(request, "email");
		if(message == null){
			message = "0";
		}
		PrintWriter out = response.getWriter();
		/*String s = String
				.format("{\"cartNumber\":\"%s\", \"message\":\"%s\", \"ip\":\"%s\", \"userid\":\"%s\", \"username\":\"%s\"}",
						(cartNumber==null?0:cartNumber.getValue()), message, AppConfig.ip, (userinfo==null?0:userinfo[0]), (userinfo==null?"":userinfo[1]));
		*/
		String ordering = (String)request.getSession().getAttribute("ordering");
		out.print((cartNumber==null?0:cartNumber.getValue())+","+ message+","+ AppConfig.ip+","+(userinfo==null?0:userinfo[0])+","+ (userinfo==null?"":userinfo[1])+","+ordering+","+email);
		out.flush();
		out.close();
	}
}
