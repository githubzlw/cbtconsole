package com.cbt.website.servlet2;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Servlet implementation class PurchaseServlet
 */
public class PurchaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static ApplicationContext context;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PurchaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
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
		String ClassName = "com.cbt.website.servlet."+request.getParameter("className");
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
