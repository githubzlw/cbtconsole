package com.cbt.website.servlet;

import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.Utility;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.userAuth.bean.Admuser;
import net.minidev.json.JSONArray;

import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

/**
 * Servlet implementation class DeliveryWarningServlet
 */
public class DeliveryWarningServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(DeliveryWarningServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeliveryWarningServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void DeliveryWarningList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String admid = request.getParameter("admid");
		String userid = request.getParameter("userid") == "" ? "0" : request.getParameter("userid");
		
//		int user = (Integer) ((userid == "") ? 0 : userid);
		String sessionId = request.getSession().getId();
		String admuserJson = Redis.hget(sessionId, "admuser");
		Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
		if (Integer.parseInt(admuser.getRoletype()) == 0 && admid=="" ) {
			admid = "0";
		}else if (Integer.parseInt(admuser.getRoletype()) != 0) {
			admid = admuser.getId().toString();			
		}
		IOrderwsDao dao = new OrderwsDao();
		List<Object[]> list= dao.getDeliveryWarningList(Integer.parseInt(admid),Integer.parseInt(userid));
		PrintWriter out = response.getWriter();
		out.print(JSONArray.toJSONString(list));
	}
	
	protected void updatePurchaseDays(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		int days=Integer.parseInt(request.getParameter("dayvalue"));
		String orderid=request.getParameter("orderid");
		IOrderwsDao dao = new OrderwsDao();
		int res=dao.updatePurchaseDays(orderid, days);
		PrintWriter out=response.getWriter();
		out.print(res);
	}
	
	
	//订单拆分发送给用户邮件
	protected void sendEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ParseException {
		String urlPath = request.getRequestURL().toString();
		StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
//        sb.append(" <div style='font-weight: bolder; margin-bottom: 10px;'>Dear "+ name +",</div><br>"); 
//        sb.append(" <div style='margin-bottom: 10px; font-size: 13px;'>Order Number#"+ orderno +"</div><br>"); 
        sb.append(" <div style='margin-bottom: 10px;'>Due to the supply reason, our purchase team is still working on the products stock checking with suppliers and it may take slightly longer to complete the process.</div><br>");
        sb.append(" <div style='margin-bottom: 10px;'>Please be patient and you will soon see your order status update which will be shown in <a href='"+urlPath+"individual/getCenter'>【Your account】</a>.</div><br>"); 
        sb.append(" <div style='style='font-weight: bold''>Best regards, </div><div style='font-weight: bold'><a href='http://www.import-express.com'>www.Import-Express.com</a></div></div>");
		
		
		try {
			String copyEmail = request.getParameter("copyEmail");
			String email = request.getParameter("email");
			String emailInfo = request.getParameter("emailInfo");
			String orderNo = request.getParameter("orderNo");
			String sendemail = null;
    	    String pwd = null;
			 if(Utility.getStringIsNull(copyEmail)){
    		    IUserDao userDao = new UserDao();
    		    String[] adminEmail =  userDao.getAdminUser(0, copyEmail, 0);
    		    if(adminEmail != null){
    			   sendemail = adminEmail[0];
    			   pwd = adminEmail[1];
    		    }
	    	  }
			int res = SendEmail.send(sendemail,pwd,email, emailInfo,"Your ImportExpress Split Order details, please Advise.",copyEmail,orderNo, 1);
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug("orderSplit:"+e.getMessage());
		}
		LOG.info("ordersplit end");
	}

}
