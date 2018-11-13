package com.cbt.dc.servlet;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@SuppressWarnings("all")
/**
 * Servlet implementation class DcServlet
 */
public class DcServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static ApplicationContext context;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DcServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String tag = request.getParameter("action");
		String ClassName = "com.cbt.dc.servlet."+request.getParameter("className");
		String keywords = request.getParameter("keywords");
		String catList = request.getParameter("catList");
		String catid = request.getParameter("catid");
		try {
			Class clazz = Class.forName(ClassName);
			context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			Method method = clazz.getDeclaredMethod(tag, new Class[] {HttpServletRequest.class, HttpServletResponse.class });
			method.invoke(clazz.newInstance(), new Object[] { request, response });
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
