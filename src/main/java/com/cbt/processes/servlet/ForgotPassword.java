package com.cbt.processes.servlet;

import com.cbt.bean.UserBean;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author ylm
 * 找回密码
 *
 */
public class ForgotPassword  {
	private static final long serialVersionUID = 1L;

    public ForgotPassword() {
        super();
    }

	//找回密码邮件
	protected void sendEmailFind(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String email = request.getParameter("email");
		IUserDao userDao = new UserDao();
		UserBean user = userDao.getUserEmail(email);
		if(user == null){
			request.setAttribute("code", "error");
			request.setAttribute("msg", "E-mail does not exist");
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/forgot.jsp");
			homeDispatcher.forward(request, response);
			return;
		}
		IUserServer us = new UserServer();
		String res = us.sendEmailfind(email);
		if(res.indexOf("code=1")>-1){
			request.setAttribute("email", email);
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/forgot-email.jsp");
			homeDispatcher.forward(request, response);
		}else{
			request.setAttribute("code", res);
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/forgot.jsp");
			homeDispatcher.forward(request, response);
		} 
	}
	
	//确认邮箱的验证码
	protected void passActivate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String email = request.getParameter("email");
		String validateCode = request.getParameter("validateCode");
		IUserServer us = new UserServer();
		boolean res = us.passActivate(email, validateCode);
		if(res){
			request.setAttribute("email", email);
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/forgot-pass.jsp");
			homeDispatcher.forward(request, response);
		}else{
			request.setAttribute("code", "The link has expired");
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/forgot.jsp");
			homeDispatcher.forward(request, response);
			
		}
	}
	
	//修改密码
	protected void upPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		IUserServer us = new UserServer();
		int res = us.upPassword(email, password);
		if(res>0){
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/forgot-end.jsp");
			homeDispatcher.forward(request, response);
		}else{
			request.setAttribute("code", "Modify the failure");
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("cbt/forgot.jsp");
			homeDispatcher.forward(request, response);
			
		}
	}
}
