package com.cbt.website.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * ylm
 * servlet反射调用
 */
public class WebsiteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static ApplicationContext context=null;
  
    public WebsiteServlet() {
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
		String ClassName = "com.cbt.website.servlet."+request.getParameter("className");
		try
		{
		 context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		 Class clazz = Class.forName(ClassName); 
		
		Method method = clazz.getDeclaredMethod(tag, new Class[] {HttpServletRequest.class, HttpServletResponse.class });
		method.invoke(clazz.newInstance(), new Object[] { request, response });
		
		} catch (Exception e)
		{
		e.printStackTrace();
		}
	}
	 
}
