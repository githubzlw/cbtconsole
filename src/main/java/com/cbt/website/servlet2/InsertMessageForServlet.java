package com.cbt.website.servlet2;

import com.cbt.messages.ctrl.InsertMessageNotification;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.warehouse.util.StringUtil;
import com.cbt.website.userAuth.bean.Admuser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet implementation class UpdateOrderStateServlet
 */
public class InsertMessageForServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InsertMessageForServlet() {
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
		InsertMessageNotification msgNtDao = new InsertMessageNotification();
		String sessionId = request.getSession().getId();
		String admuserJson = Redis.hget(sessionId, "admuser");
		if (admuserJson == null) {
			response.sendRedirect("website/main_login.jsp");
			return;
		}
		Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String tbOrderid = request.getParameter("tbOrderId");
		String remarkUserId =request.getParameter("remarkUserId");
		String buyUrl =request.getParameter("buyUrl");
		String id =request.getParameter("id");
		msgNtDao.queryAdmuserId(remarkUserId);
		remarkUserId=String.valueOf(msgNtDao.queryAdmuserId(remarkUserId));
		String text="采购快递单号【"+tbOrderid+"】没有关联到销售订单，请核查";
		if(id!=null && !id.equals("")){
			remarkUserId=String.valueOf(msgNtDao.getInfoSendId(id));
			text="【"+admuser.getAdmName()+"】已重新录入货源，请重新入库";
		}
//		if(remarkUserId==null || remarkUserId.equals("") || remarkUserId.equals("0")){
//			remarkUserId="9";
//		}
		// 插入消息提醒表中
		int res=msgNtDao.insertTreasuryNoteFor(tbOrderid, String.valueOf(admuser.getId()),remarkUserId,buyUrl,text);
		//查询淘宝订单包裹的入库信息在哪个库位对应哪个销售的订单
		String info=msgNtDao.getIdrealtionInfo(tbOrderid);
       if(res==1){
    	   System.out.println("发送成功");
    	   //更新采购处理标识
    	   msgNtDao.updateTbStatus(tbOrderid);
       }else{
    	   System.out.println("发送失败");
       }
		PrintWriter out = response.getWriter();
		out.print(res+","+ (StringUtil.isNotBlank(info)?info:""));
		out.close();
	}

}
