package com.cbt.app.servlet;

import com.cbt.bean.UserBean;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端注册接口
 */
public class RegServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(RegServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String email = request.getParameter("email");
		String name = request.getParameter("name");
		String pass = request.getParameter("pass");
		String businessName = request.getParameter("businessName");
		IUserServer userS = new UserServer();
		int res = 0;
		UserBean user = new UserBean();
		user.setEmail(email);
		user.setName(name);
		user.setPass(pass);
		user.setBusinessName(businessName);
		res = userS.regUser(user);
		/*	String sessionId = WebCookie.cookieValue(request, "sessionId");
			if(sessionId != null){
				//保存未登录之前加入购物车的商品
				is.LoginGoogs_car(sessionId, user.getId());
			}*/
			
		//------------lzj-------start
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", res > 0 ? "1" : res + "");
		map.put("userId", res > 0 ? res + "" : "0");
		map.put("met", "regServlet");		
		PrintWriter out = response.getWriter();
		out.print(JSONArray.fromObject(map));
		out.flush();
		out.close();
	}
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("result", "1");
		map.put("userId", "122");
		map.put("userName", "ww");
		map.put("met", "regServlet");
		System.out.println(JSONArray.fromObject(map));
	}
}
