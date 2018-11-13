package com.cbt.processes.servlet;

import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 重新发送邮件
 */
public class SendEmailServlet  {
	private static final long serialVersionUID = 1L;
    
    public SendEmailServlet() {
        super();
    }
 

	protected void sendEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String email = request.getParameter("email");
		IUserServer us = new UserServer();
//		String res = us.sendEmail(email);
		PrintWriter out  = response.getWriter();
//		out.print(res);
		out.flush();
		out.close();
	}
	
}
