package com.cbt.website.userAuth.servlet;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.WebTool;
import com.cbt.website.userAuth.bean.AuthInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class UserAuthServlet extends HttpServlet {


	private static final long serialVersionUID = 1L;

	
	
	public UserAuthServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
	}

	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = request.getParameter("url");
		HttpSession session = request.getSession();
		String result = null;
		result = "{\"status\":false,\"code\":1,\"message\":\"对不起,您没有权限操作\"}";
//		String userauthJson = Redis.jedis.hget(session.getId(), "userauth");
		
		String userauthJson = Redis.hget(session.getId(), "userauth");
		List<Object> authlist = SerializeUtil.JsonToList(userauthJson, AuthInfo.class);
		
//		List<Object> authlist = (List<Object>) session.getAttribute("authlist");
		
		if (authlist!= null && authlist.size() > 0) {			
			for (int i = 0; i < authlist.size(); i++) {
				AuthInfo authinfo = (AuthInfo) authlist.get(i);
				if(url.equals(authinfo.getUrl())){
					result = "{\"status\":true,\"message\":\"\"}";
					continue;
				}
			}
		}else {
			result = "{\"status\":false,\"code\":2,\"message\":\"对不起,登录超时,请重新登录\"}";
		}
		WebTool.writeJson(result, response);
	}
	
	
	
}
