package com.cbt.website.servlet;

import com.cbt.website.bean.ApplicationSummary;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;
import net.minidev.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class ApplicationSummaryServlet
 */
public class ApplicationSummaryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationSummaryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain");
		PrintWriter out=response.getWriter();
		String previousdate=request.getParameter("previousdate");//上一个日期
		String nextdate=request.getParameter("nextdate");//下一个日期
		int page=Integer.parseInt(request.getParameter("page"));
		int pagesize=20;//每页显示记录数
		int userid=0;
		if(!request.getParameter("userid").equals("")&&request.getParameter("userid")!=null){
			userid=Integer.parseInt(request.getParameter("userid"));
		}
		int confirmuserid=Integer.parseInt(request.getParameter("confirmuserid"));
		
		String username=request.getParameter("username");
		String useremail=request.getParameter("useremail");
		List<ApplicationSummary> list= new ArrayList<ApplicationSummary>();
		UserDao dao=new UserDaoImpl();
		list=dao.getApplication(userid, username, useremail, previousdate, nextdate,page,pagesize,confirmuserid);
		out.print(JSONArray.toJSONString(list)+"+"+list.size());
		out.flush();
		out.close();
	}

}
