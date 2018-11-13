package com.cbt.website.servlet;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.WebTool;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.Dao.UserAuthDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.bean.AuthInfo;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;
import com.cbt.website.userAuth.impl.UserAuthDaoImpl;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebsiteLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public WebsiteLoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		String username = request.getParameter("userName");
		String password = request.getParameter("passWord");
		String action=request.getParameter("action");
		//对用户的密码进行MD5加密
//		System.out.println(Md5Util.encoder(password));
//		password = Md5Util.encoder(password);
		
		if ("login".equals(action)) {
			AdmUserDao admuserDao = new AdmUserDaoImpl();
			UserAuthDao userauthDao = new UserAuthDaoImpl();
			Admuser admuser = null;
			try {
				admuser = admuserDao.getAdmUser(username, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String result = null;
			List<AuthInfo> authlist = new ArrayList<AuthInfo>();
			if (admuser != null) {
				result = "{\"status\":true,\"message\":\"登陆成功\"}";
				try {
					authlist = userauthDao.getUserAuth(username);
					
					
//					HttpSession session = request.getSession();
//					session.setAttribute("admuser", admuser);
//					session.setAttribute("authlist", authlist);
					
					
					String sessionId = request.getSession().getId();
//					//将数据保存进redis
//					System.out.println("登录时用户json:"+SerializeUtil.ObjToJson(admuser));
//					System.out.println("登录时权限json:"+SerializeUtil.ListToJson(authlist));
//					Redis.jedis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
//					Redis.jedis.hset(sessionId, "userauth", SerializeUtil.ListToJson(authlist));
					
					Redis.hset(sessionId, "admuser", SerializeUtil.ObjToJson(admuser));
					Redis.hset(sessionId, "userauth",JSONArray.fromObject(authlist).toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				result = "{\"status\":false,\"message\":\"登录信息错误!\"}";
			}
			
			WebTool.writeJson(result, response);
		}else if ("logout".equals(action)) {
//			request.getSession().invalidate();
			request.getSession().removeAttribute("admuser");
			request.getSession().removeAttribute("userauth");
			response.sendRedirect("/cbtconsole/website/main_login.jsp");
		}
			
		}

}
