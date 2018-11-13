package com.cbt.website.userAuth.servlet;

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

public class AdmUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	
	
	public AdmUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		
		AdmUserDao admuserDao = new AdmUserDaoImpl();
		UserAuthDao userauthDao = new UserAuthDaoImpl();
		
		if (action != null && "findAllUser".equals(action)) {
			String sessionId = request.getSession().getId();
			String userJson = Redis.hget(sessionId, "admuser");
			Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
			
//			Admuser user = (Admuser) request.getSession().getAttribute("admuser");
			
			List<String> admuserList = new ArrayList<String>();
			try {
				admuserList = admuserDao.getAllAdmuser(user.getAdmName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String result = (JSONArray.fromObject(admuserList).toString());
			WebTool.writeJson(result, response);
		}else if (action != null && "findAdmuserAuth".equals(action)) {
			List<AuthInfo> authinfoList = new ArrayList<AuthInfo>();
			List<String> userauthList = new ArrayList<String>();
			String admName = request.getParameter("admName");
			try {
				authinfoList = userauthDao.getUserAuth(admName);
				for (AuthInfo authInfo : authinfoList) {
					userauthList.add(authInfo.getAuthId()+"");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String result = (JSONArray.fromObject(userauthList).toString());
			WebTool.writeJson(result, response);
		}else if (action != null && "update".equals(action)) {
			String admName = request.getParameter("admName");
			String obj = request.getParameter("authId");
			String[] auth = obj.split("'");
			try {
				//添加权限
				int[] res = userauthDao.insertUserAuth(admName, auth);
				String result = "{\"status\":false,\"message\":\"对不起,操作失败!\"}";
				if (res.length > 0) {
					result = "{\"status\":true,\"message\":\"操作成功!\"}";
				}
				WebTool.writeJson(result, response);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
}
