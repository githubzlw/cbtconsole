package com.cbt.processes.servlet;

import com.cbt.bean.UserBean;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;
import com.cbt.util.WebCookie;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class User extends HttpServlet {

	/**
	 * 注册：查询用户名称是否存在
	 */
	protected void getUserName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String name = request.getParameter("name");
		boolean getName = false;
		if(name != null){
			IUserServer uServer = new UserServer();
			getName = uServer.getUserName(name);
		}
		PrintWriter out = response.getWriter();
		out.print(getName);
		out.flush();
		out.close();
	}
	
	/**
	 * 注册：查询邮箱是否存在
	 */
	protected void getUserEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String name = request.getParameter("email");                                                                                                                                                               
		boolean getName = false;
		if(name != null){
			IUserServer uServer = new UserServer();
			getName = uServer.getUserEmail(name);
		}
		PrintWriter out = response.getWriter();
		out.print(getName);
		out.flush();
		out.close();
	}
	
	/**
	 * 查询用户名和邮箱是否一致
	 */
	protected void getNameEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String name = request.getParameter("name");
		boolean getName = false;
		if(name != null){
			IUserServer uServer = new UserServer();
			getName = uServer.getUserName(name);
		}
		PrintWriter out = response.getWriter();
		out.print(getName);
		out.flush();
		out.close();
	}
	
	/**
	 * 修改密码
	 */
	public void chengePass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String oldpass = request.getParameter("oldpass");
		String pass = request.getParameter("pass");
		String[] user =  WebCookie.getUser(request);
		String res = "";
		if(user == null){
			javax.servlet.RequestDispatcher homeDispatcher = request
					.getRequestDispatcher("cbt/geton.jsp");
			homeDispatcher.forward(request, response);
			return ;
		}else{
			String username = user[1];
			IUserServer uServer = new UserServer();
			UserBean userBean = uServer.login(username, oldpass);
			if(userBean != null){
				int ress = uServer.upPasswordName(pass, username);
				res = ress>0?"Successful modification":"Fail modification";
			}else{
				res = "Wrong password";
			}
		}
		request.setAttribute("email", user[2]);
		request.setAttribute("code", res);
		javax.servlet.RequestDispatcher homeDispatcher = request
				.getRequestDispatcher("cbt/uppass.jsp");
		homeDispatcher.forward(request, response);
	}
}
