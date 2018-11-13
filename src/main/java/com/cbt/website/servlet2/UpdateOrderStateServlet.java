package com.cbt.website.servlet2;

import com.cbt.change.util.CheckCanUpdateUtil;
import com.cbt.change.util.ErrorLogDao;
import com.cbt.messages.ctrl.InsertMessageNotification;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.website.dao2.IWebsiteOrderDetailDao;
import com.cbt.website.dao2.WebsiteOrderDetailDaoImpl;
import com.cbt.website.userAuth.bean.Admuser;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class UpdateOrderStateServlet
 */
public class UpdateOrderStateServlet extends HttpServlet {
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(UpdateOrderStateServlet.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateOrderStateServlet() {
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
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String sessionId = request.getSession().getId();
		String admuserJson = Redis.hget(sessionId, "admuser");
		if (admuserJson == null) {
			response.sendRedirect("website/main_login.jsp");
			return;
		}
		Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);

		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String orderid = request.getParameter("orderid");
		int state = Integer.parseInt(request.getParameter("updatestate"));
		int res = 0;
		
		try {
			// jxw 2017-4-24添加订单状态判断,修改订单状态为state，操作人admuser.getId()）
			boolean isCheck = CheckCanUpdateUtil.updateOnlineOrderInfoByLocal(orderid,state,admuser.getId());
			if(isCheck){ 
				IWebsiteOrderDetailDao dao = new WebsiteOrderDetailDaoImpl();
				res = dao.websiteUpdateOrderState(orderid, state);
				// 插入消息提醒表中
				InsertMessageNotification msgNtDao = new InsertMessageNotification();
				msgNtDao.insertOrderStatusChanges(orderid, admuser,state);
			}
		} catch (Exception e) {
			e.getStackTrace();
			LOG.error("更新线上orderinfo订单信息失败," + "订单号:" +orderid,e);
			// jxw 2017-4-24 插入失败，插入信息放入失败记录表中
			String sqlStr = "update orderinfo set state ='"+state+"' where order_no = '" + orderid + "';";
			ErrorLogDao.insertErrorInfo("orderinfo", sqlStr, admuser.getId(), state, "更新失败,原因:" + e.getMessage());
		}
		
		//原代码
		/*IWebsiteOrderDetailDao dao = new WebsiteOrderDetailDaoImpl();
		res = dao.websiteUpdateOrderState(orderid, state);
		// 插入消息提醒表中
		InsertMessageNotification msgNtDao = new InsertMessageNotification();
		msgNtDao.insertOrderStatusChanges(orderid, admuser,state);*/

		PrintWriter out = response.getWriter();
		out.print(res);
		out.close();
	}

}
