package com.cbt.website.servlet;

import com.cbt.messages.ctrl.InsertMessageNotification;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
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
 * Servlet implementation class OrderRemarkServlet
 */
public class OrderRemarkServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OrderRemarkServlet() {
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
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		IOrderwsDao dao = new OrderwsDao();
		if (action.equals("get")) {
			String orderid = request.getParameter("orderid");
			List<Object[]> list = dao.getOrderRemark(orderid);
			out.print(JSONArray.toJSONString(list));
		} else if (action.equals("add")) {
			String orderid = request.getParameter("orderid");
			String orderremark = request.getParameter("orderremark");
			// orderremark = new String(orderremark.getBytes("ISO-8859-1"),
			// "UTF-8");
			String sessionId = request.getSession().getId();
			String admuserJson = Redis.hget(sessionId, "admuser");
			if (admuserJson == null) {
				response.sendRedirect("website/main_login.jsp");
				return;
			}
			Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			int res = dao.addOrderRemark(orderid, orderremark, admuser.getId(), 0);

			// 插入消息提醒表中
			String sendContent = "订单详情页," + admuser.getAdmName() + "对订单商品进行备注";
			InsertMessageNotification msgNtDao = new InsertMessageNotification();
			msgNtDao.insertByOrderComment(sendContent, orderremark, orderid, admuser,"订单详情页,");

			out.print(res);
			//针对问题订单
		}  else  if (action.equals("delProblemOrder")){
			String orderid = request.getParameter("orderid");
			String orderremark = request.getParameter("orderremark");
			// orderremark = new String(orderremark.getBytes("ISO-8859-1"),
			// "UTF-8");
			String sessionId = request.getSession().getId();
			String admuserJson = Redis.hget(sessionId, "admuser");
			if (admuserJson == null) {
				response.sendRedirect("website/main_login.jsp");
				return;
			}
			Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			int res = dao.addOrderRemark(orderid, orderremark, admuser.getId(), 0);
			
			// 插入消息提醒表中
			InsertMessageNotification msgNtDao = new InsertMessageNotification();
			try {
//				msgNtDao.insertProblemOrder2(orderid, orderremark, admuser.getId(), 9);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
