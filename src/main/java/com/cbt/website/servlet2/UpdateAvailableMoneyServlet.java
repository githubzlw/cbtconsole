package com.cbt.website.servlet2;

import com.cbt.bean.BalanceBean;
import com.cbt.service.AdditionalBalanceService;
import com.cbt.service.impl.AdditionalBalanceServiceImpl;
import com.cbt.website.dao.UserDao;
import com.cbt.website.dao.UserDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class UpdateAvailableMoneyServlet
 */
public class UpdateAvailableMoneyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateAvailableMoneyServlet() {
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
		PrintWriter out = response.getWriter();
		float available=Float.parseFloat(request.getParameter("available"));
		String remark=request.getParameter("remark");
		String modifyuser=request.getParameter("modifyuser");
		String password=request.getParameter("password");
		String orderid=request.getParameter("orderid");
		String complainid=request.getParameter("complainid");//用户网上投诉表的id
		int userid=Integer.parseInt(request.getParameter("userid"));
		int modifyuserid=Integer.parseInt(request.getParameter("modifyuserid"));
		int usersign=Integer.parseInt(request.getParameter("flag"));//0代表收入，1代表支出  2-余额奖励或者售后补偿
		Double money=Double.valueOf(request.getParameter("available"));//
		UserDao dao= new UserDaoImpl();
		boolean flag= dao.isPassword(modifyuserid, password);
		if(flag){
			int res=dao.updateUserAvailable(userid, available, remark,orderid, modifyuser,usersign,0,5);
			if(res!=0){
				//增加余额的时候  对于余额补偿或奖励  数据记录到additional_balance表
				if(usersign == 2 && money >0){
					AdditionalBalanceService  server = new AdditionalBalanceServiceImpl();
					server.insert(new BalanceBean(userid, money, remark, modifyuser, complainid, orderid));
				}
				out.print(1);
			}else{
				out.print(-1);
			}
			
		}else{
			out.print(0);
		}
		out.flush();
		out.close();
	}

}
