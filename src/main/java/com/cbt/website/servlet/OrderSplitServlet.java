package com.cbt.website.servlet;

import com.cbt.bean.OrderBean;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.Redis;
import com.cbt.util.SerializeUtil;
import com.cbt.util.UUIDUtil;
import com.cbt.util.Utility;
import com.cbt.website.dao.OrderInfoDao;
import com.cbt.website.dao.OrderInfoImpl;
import com.cbt.website.service.IOrderSplitServer;
import com.cbt.website.service.OrderSplitServer;
import com.cbt.website.userAuth.bean.Admuser;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderSplitServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Log LOG = LogFactory.getLog(OrderwsServlet.class);
	String urlll = null;
    
    /**
     * 订单拆分(正常订单和Drop Ship订单)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @throws ParseException
     */
	protected void orderSplit(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {
		LOG.info("ordersplit start");
		try {
			String orderno = request.getParameter("orderno");
			String odids = request.getParameter("odids");
			int state = Integer.parseInt(request.getParameter("state"));
			String jsonArr = request.getParameter("jsonArr");
			//前台商品和ordertailsId
//			HashMap <String,String> oddSMap = JSON.parseObject(jsonArr, HashMap.class);
			IOrderSplitServer splitServer = new OrderSplitServer();
			String sessionId = request.getSession().getId();
			LOG.info("获取sessionId:"+sessionId.toString());
			String admuserJson = Redis.hget(sessionId, "admuser");
			PrintWriter out = response.getWriter();
			if(admuserJson == null){
				Map<String, String> map = new HashMap<String, String>();
				map.put("res", "0");
				map.put("msg", "用户未登陆");
				out.print(JSONArray.fromObject(map));
				out.flush();
				out.close();
				return ;
			}
			Admuser admuser = (Admuser) SerializeUtil.JsonToObj(admuserJson, Admuser.class);
			//判断是否是Drop Ship订单，根据订单号获取订单信息
			OrderInfoDao orderInfoDao = new OrderInfoImpl();
			OrderBean orderBean=orderInfoDao.getOrderInfo(orderno,null);
			String res=null;
			if(orderBean.getIsDropshipOrder()==1){//Drop Ship 拆单流程
				Map<String, String> map=splitServer.splitOrderShip(orderno, odids, orderBean.getUserid(),String.valueOf(state));
				res = map.toString();
			}else{//正常拆单流程
//				res = splitServer.splitOrder(orderno,odids,state,admuser.getAdmName(), oddSMap);
				res = splitServer.splitOrder(orderno,odids,state,admuser.getAdmName());
			}
			out.print(res);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug("orderSplit:"+e.getMessage());
		}
		LOG.info("ordersplit end");
	}
	
	  //获取订单拆分
	protected void getOrderSplit(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {
		LOG.info("getOrderSplit start sendEmailInfo");
		try {
			String orderNo = request.getParameter("orderno");
			String ordernoNew = request.getParameter("ordernoNew");
			String time = request.getParameter("time");
			String time_ = request.getParameter("time_");
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 int state = Integer.parseInt(request.getParameter("state"));
			
			IOrderSplitServer splitServer = new OrderSplitServer();
			String[]  orderNos;
			if(state == 1){
				orderNos = new String[]{orderNo, ordernoNew};
			}else{
				orderNos = new String[]{ordernoNew};
			}
			List<OrderBean> orderBeans = splitServer.getSplitOrder(orderNos);
			 Calendar calendar=Calendar.getInstance();
			 String expect_arrive_time_ = "";
			 String expect_arrive_time = "";
			 OrderBean obBean = new OrderBean();
			 OrderBean obBean_ = new OrderBean();
			 if(state == 1){
					for (int i = 0; i < orderBeans.size(); i++) {
						//获取运输方式和对应的运输时间
						String mode_transport = orderBeans.get(i).getMode_transport();
						int mode_transport_day = 0;
						if(mode_transport != null && mode_transport.indexOf("@") > -1){
							String[] mode_transports = mode_transport.split("@");
							if(mode_transports.length > 3){
								mode_transport = mode_transport.split("@")[1];
								if(Utility.getStringIsNull(mode_transport)){
									if(mode_transport.indexOf("-") > -1){
										mode_transport = mode_transport.split("-")[1];
									}
									mode_transport_day = Integer.parseInt(mode_transport.replace("Days", "").trim());
								}
							}
						}
						if(orderBeans.get(i).getOrderNo().indexOf("_") > -1){
							if(mode_transport_day != 0 && Utility.getStringIsNull(time_)){
								calendar.setTime(sdf.parse(time_)); 
								calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+mode_transport_day);
								expect_arrive_time_ = sdf.format(calendar.getTime());
							}
							obBean_ = orderBeans.get(i);
						}else{
							if(mode_transport_day != 0 && Utility.getStringIsNull(time)){
								calendar.setTime(sdf.parse(time));
								calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+mode_transport_day);
								expect_arrive_time = sdf.format(calendar.getTime());
							}
							obBean = orderBeans.get(i);
						}
					}
			 }else{
				 obBean_ = orderBeans.get(0);
			 }
			
			 //获取用户uuid和个人中心自动登录路径
			 String uuid = UUIDUtil.getEffectiveUUID(orderBeans.get(0).getUserid(),"");
			 String url = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
			 request.setAttribute("autoUrl", url);
			//获取用户email
			request.setAttribute("email", splitServer.getUserEmailByUserName(orderBeans.get(0).getUserid()));
			List<Object[]> orderDetails = splitServer.getSplitOrderDetails(orderNos);
			List<Object[]> details_ = new ArrayList<Object[]>();//拆分的订单
			List<Object[]> details = new ArrayList<Object[]>();//剩余的订单
			//发送邮件开始
			for (int i = 0; i < orderDetails.size(); i++) {
				if((orderDetails.get(i)[0].toString().indexOf("_") > -1)){
					if (orderNo.length() > 17) {
						if(!orderDetails.get(i)[0].equals(ordernoNew)){
							details.add(orderDetails.get(i));
						}else {
							details_.add(orderDetails.get(i));
						}
					}else {
						details_.add(orderDetails.get(i));
					}
				}else{
					details.add(orderDetails.get(i));
				}
			}
			
			request.setAttribute("details", details);
			request.setAttribute("details_", details_);
			if(state == 1){
				request.setAttribute("expect_arrive_time_", expect_arrive_time_);
				request.setAttribute("expect_arrive_time", expect_arrive_time);
				request.setAttribute("orderbean", obBean);
				request.setAttribute("orderbean_", obBean_);
			}else{
				request.setAttribute("orderbean_", obBean_);
			}
			request.setAttribute("currency", orderBeans.get(0).getCurrency());
			
			javax.servlet.RequestDispatcher homeDispatcher = request.getRequestDispatcher("website/sendemail_split.jsp");
			homeDispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug("getOrderSplit:"+e.getMessage());
		}
		LOG.info("getOrderSplit sendEmailInfo end");
	}

	//订单拆分发送给用户邮件
	protected void sendEmail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {
		LOG.info("sendEmail start");
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
			int res = SendEmail.send(sendemail,pwd,email, emailInfo,"Due to supply reasons, we can only send your order partially at first.",copyEmail,orderNo, 1);
			PrintWriter out = response.getWriter();
			out.print(res);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug("sendEmail:"+e.getMessage());
		}
		LOG.info("sendEmail end");
	}
	
	//获取邮件发送失败的日志信息
	protected void getMessage_error(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, ParseException {
		LOG.info("ordersplit start");
		try {
			String time = request.getParameter("time");
			String endtime = request.getParameter("endtime");
			String page = request.getParameter("page");
			IOrderSplitServer splitServer = new OrderSplitServer();
			List<Object[]> messages = splitServer.getMessage_error(time, endtime, Integer.parseInt(page));
 			PrintWriter out = response.getWriter();
 			Map<String, Object> map = new HashMap<String, Object>();
 			map.put("count", splitServer.getMessage_error(time, endtime));
 			map.put("message", messages);
 			map.put("page", page);
			out.print(net.sf.json.JSONArray.fromObject(map));
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug("orderSplit:"+e.getMessage());
		}
		LOG.info("ordersplit end");
	}
		
}
