package com.cbt.app.servlet;

import com.cbt.bean.UserBean;
import com.cbt.processes.service.IUserServer;
import com.cbt.processes.service.UserServer;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户端登录接口
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public LoginServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("userName");
		String pass_req = request.getParameter("password");
		IUserServer us = new UserServer();
		UserBean user = us.login(name, pass_req);
		Map<String, String> map = new HashMap<String, String>();
		if(user != null){
			map.put("result", "1");
			map.put("userId", user.getId()+"");
			map.put("userName", user.getName());
		}else{
			map.put("result", "0");
			map.put("userId", "0");
			map.put("userName", "");
		}
		map.put("met", "regServlet");
		PrintWriter out = response.getWriter();
		out.print(JSONArray.fromObject(map));
		out.flush();
		out.close();
	}

}
