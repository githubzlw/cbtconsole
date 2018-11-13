package com.cbt.pay.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Servlet implementation class PayServelt
 */
public class PaysServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	  
    public PaysServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String tag = request.getParameter("action");
		String ClassName = "com.cbt.pay.servlet."+request.getParameter("className");
		try
		{
		 Class clazz = Class.forName(ClassName); 
		
		Method method = clazz.getDeclaredMethod(tag, new Class[] {HttpServletRequest.class, HttpServletResponse.class });
		method.invoke(clazz.newInstance(), new Object[] { request, response });
		
		} catch (Exception e)
		{
		// TODO: handle exception
		e.printStackTrace();
		}
	}
	 
}