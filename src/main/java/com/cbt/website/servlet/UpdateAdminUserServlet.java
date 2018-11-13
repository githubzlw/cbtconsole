package com.cbt.website.servlet;

import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class UpdateAdminUserServlet
 */
public class UpdateAdminUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateAdminUserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8"); 
		response.setContentType("text/plain"); 
		int adminid = Integer.parseInt(request.getParameter("adminid"));
		int userid=Integer.parseInt(request.getParameter("userid"));
		String  email = request.getParameter("email");
		
		//Admuser user = (Admuser) request.getSession().getAttribute("admuser");
		String admJson = Redis.hget(request.getSession().getId(), "admuser");
		Admuser user = (Admuser)SerializeUtil.JsonToObj(admJson, Admuser.class);
		String users = user.getAdmName();
		String username=request.getParameter("userName");
		String admName=request.getParameter("admName");
		PrintWriter out=response.getWriter();
       UserDao dao = new UserDaoImpl();
//		IOrderwsDao orderwsDao=new OrderwsDao();
		int res = 0;
//		if(orderwsDao.queryUserInCharge(userid)>0){
//			System.out.println("updateUserInCharge");
//			res = orderwsDao.updateUserInCharge(userid,adminid,admName);
//		}else{
//			System.out.println("updateAdminuser");
//			res=dao.addUserInCharge(userid, username, email, adminid, admName);
//		}
		res=dao.updateAdminuser(userid, adminid, users, email, username, admName);
		out.print(res);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
