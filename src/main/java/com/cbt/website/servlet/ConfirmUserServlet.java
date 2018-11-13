package com.cbt.website.servlet;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.GradeDiscount;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;
import net.minidev.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet implementation class ConfirmUserServlet
 */
public class ConfirmUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConfirmUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter out = response.getWriter();
		UserDao dao = new UserDaoImpl();
		List<ConfirmUserInfo> list = null;
		List<GradeDiscount> list1 = null;
		String action = request.getParameter("action");

		if ("currentUser".equals(action)) {
			String sessionId = request.getSession().getId();
			String admuserJson = Redis.hget(sessionId, "admuser");
			Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			list = dao.getCurSub(admuser);
			out.print(JSONArray.toJSONString(list));
		} else if ("sell".equals(action)) {
			list = dao.getAll();
			out.print(JSONArray.toJSONString(list));
		} else if("getDiscount".equals(action)){
//			list1 = dao.getDiscount();
			out.print(JSONArray.toJSONString(list1));
		}else {
			list = dao.getAllByRoleType(0);
			out.print(JSONArray.toJSONString(list));
		}
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
