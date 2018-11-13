package com.cbt.pay.service;

import com.cbt.bean.SpiderBean;
import com.cbt.bean.UserBean;
import com.cbt.pay.dao.ISpidersDao;
import com.cbt.pay.dao.SpidersDao;
import com.cbt.processes.dao.IUserDao;
import com.cbt.processes.dao.UserDao;
import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.util.UUIDUtil;
import com.cbt.util.Utility;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SpidersServer implements ISpidersServer {

	ISpidersDao dao = new SpidersDao();

	public List<SpiderBean> getSelectedItem(int userid, String itemid, int state) {
		return dao.getSelectedItem(userid, itemid, state);
	}

	@Override
	public List<Object[]> getSelectedItemPrice(int userid, String itemid,
			int state) {
		return dao.getSelectedItemPrice(userid, itemid, state);
	}

	public static void main(String[] args) {
		ISpidersServer spidersServer = new SpidersServer();
		spidersServer.sendEmail("NC229053356018991,NC229053356018992,NC229053356018993", 31);
	}
	
	@Override
	public int sendEmail(String orderNos,
			int userId) {
		String uuid = UUIDUtil.getEffectiveUUID(userId, "");
		List<String[]> orderLists = dao.getOrderInfo(orderNos);
		List<Object[]> orderdetails = dao.getOrderDetails(orderNos);
		StringBuffer od = new StringBuffer();
		IUserDao userDao = new UserDao();
		UserBean ub = userDao.getUserEmailId(userId);
		if (orderLists.size() > 0) {
			String[] orderAddress = orderLists.get(0);
			StringBuffer sb = new StringBuffer("<div style='font-size: 14px;'>");
			sb.append("<a href='" + AppConfig.ip_email
					+ "'><img style='cursor: pointer' src='" + AppConfig.ip_email
					+ "/img/logo.png' ></img></a>");
			
//			String path1="";
//			try {
//				path1 = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//			sb.append("<div style='border: 1px solid #BDBDBD;background-color: #EBEBEB;padding-left: 10px;width: 950px;'><div style='margin-bottom: 10px;font-weight: bolder;color: red;'>PLEASE NOTE:</div>");
//			sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
//			sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account "+
//					"<a style='color: #0070C0' href='"+AppConfig.ip_email+path1+"'>here</a>.");
//			sb.append("</div></div></div>");
			
			
			sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear "
					+ (Utility.getStringIsNull(ub.getName()) ? ub.getName()
							: "Valued Customer")
					+ ",</div><div style='font-size: 13px;'>");

			sb.append("Thanks for shopping with us at <a style='color: #0070C0' href='http://www.import-express.com'>www.import-express.com</a>, we have received your order. Please refer to below order detail: </div>");
			sb.append("<div style='font-size: 13px;'><font style='font-weight: bolder;'>Shipping To:</font>"
					+ orderAddress[7]
					+ "</div>");
			sb.append("<div style='margin-left: 95px;font-size: 14px;'>" + orderAddress[1] + "</div>");
			sb.append("<div style='margin-left: 95px;font-size: 14px;'>" + orderAddress[6] + "</div>");
			sb.append("<div style=' margin-left: 95px;font-size: 14px;'>");
			sb.append(orderAddress[2]);
			sb.append("&nbsp;&nbsp;");
			sb.append(orderAddress[12]);
			sb.append("&nbsp;&nbsp;");
			sb.append(orderAddress[4]);
			sb.append("&nbsp;&nbsp;");
			sb.append("</div>");
			sb.append("<div style='font-size: 13px;list-style:none;'> <font style='font-weight: bolder;'>Phone:</font>"
					+ orderAddress[3]
					+ "</div>");
			for (int k = 0; k < orderLists.size(); k++) {
				int delivery_time = 0;
				String[] orderList = orderLists.get(k);
				String cu = orderList[10];
				String mode_transport = orderList[11];
				String transport = "";
				String time1 = "0";
				String orderNo = orderList[13];
				if (Utility.getStringIsNull(mode_transport)) {
					if (mode_transport.indexOf("@") > -1) {
						String[] mode_transports = mode_transport.split("@");
						transport = mode_transports[0];
						time1 = mode_transports[1];
						if (time1.indexOf("-") > -1) {
							time1 = time1.split("-")[1];
						}
					}
				}
				for (int i = 0; i < orderdetails.size(); i++) {
					Object[] orderdetail = orderdetails.get(i);
					if (orderNo.equals(orderdetail[7])) {
						String goods_type = (String) orderdetail[5];
						String goods_typeimg = (String) orderdetail[6];
						od.append("<tr><td><img style='cursor: pointer' width='50px;' height='50px;'  src='"
								+ orderdetail[3]
								+ "' ></img></td><td><div  style='width: 435px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;'>"
								+ orderdetail[0] + "</div>");
						od.append("<div>");
						if (goods_type.indexOf(",") > -1) {
							String[] goods_types = goods_type.split(",");
							String typesHTML = "";
							for (int j = 0; j < goods_types.length; j++) {
								typesHTML += "<span style='border: 1px solid #E1E1E1;'>"
										+ goods_types[j].split("@")[0]
										+ "</span>";
							}
							od.append(typesHTML);
						}
						if (goods_typeimg.indexOf("@") > -1) {
							String[] goods_types = goods_typeimg.split("@");
							String typesHTML = "";
							for (int j = 0; j < goods_types.length; j++) {
								typesHTML += "&nbsp;<img  height='20' width='20' src='"
										+ goods_types[j] + "'>";
							}
							od.append(typesHTML);
						}
						od.append("</div>");
						od.append("</td><td>" + orderdetail[4]
								+ "pc&nbsp;&nbsp;</td><td align='left'>" + cu
								+ "&nbsp;" + orderdetail[1]
								+ "</td></tr>");
						String delivery = (String) orderdetail[2];
						if (Utility.getStringIsNull(delivery)) {
							int delivery1 = Integer.parseInt(delivery);
							if (delivery_time < delivery1) {
								delivery_time = delivery1;
							}
						}
					}
				}
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c = Calendar.getInstance();
				c.add(Calendar.DAY_OF_MONTH, delivery_time);

				//自动登录路径
				String path = "";
				try {
					path = UUIDUtil.getAutoLoginPath("/processesServlet?action=emailLink&className=OrderInfo&orderNo="+ orderNo, uuid);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				sb.append("<div style='margin-bottom: 10px;'><div style='list-style: none;width: 2560px;text-ali"
						+ "gn: left;font-size: 14px;'><br><div><font style='font-weight: bolder;'>Order#:</font><a style='color: #0070C0' href='"
						+ AppConfig.ip_email
						//+ "/processesServlet?action=emailLink&className=OrderInfo&orderNo="
						+ path+ "'>"+orderNo+"</a></div>");
				sb.append("<div><font style='font-weight: bolder;'>Estimated ship out date:</font>"
						+ sf.format(c.getTime()) + "</div>");
				if (Utility.getStringIsNull(mode_transport)
						&& mode_transport.indexOf("@") > -1) {
					sb.append("<div><font style='font-weight: bolder;'>Shipping Method you sellected:</font>"
							+ transport + "</div>");
					c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(time1));
					sb.append("<div><font style='font-weight: bolder;'>Estimated Time of Arrival:</font>"
							+ sf.format(c.getTime())
							+ "&nbsp;The date presented is an estimated due date and is subject to change.</div>");
				}

				sb.append("</div>");
				/*sb.append("<div style='color: #0070C0;font-size: 12px;'>Order Detail&nbsp;&nbsp;&nbsp;(<a style='color: #0070C0' href='"
						+ AppConfig.ips
						+ "/processesServlet?action=emailLink&className=OrderInfo&orderNo="
						+ orderNo + "'>My Order   ></a>)</div>");*/
				sb.append("<table style='width: 750px;font-size: 12px;'>");
				sb.append("<tr><td colspan='4' style='color: #0070C0;'>Order Detail&nbsp;&nbsp;&nbsp;</td></tr>");
				sb.append("<tr><td colspan='4' style='border-top: 1px solid;'>Order Created Date: "
						+ orderList[8] + "</td></tr>");
				sb.append(od);
				sb.append("<tr style='font-weight: bold;font-size: 12px;' ><td colspan='3' align='right'>Shipping Fee Paid:</td><td colspan='4' >"
						+ cu
						+ "&nbsp;"
						+ (Utility.getIsDouble(orderList[9]) ? orderList[9] : 0)
						+ "</tr>");
				sb.append("<tr style='font-weight: bold;font-size: 12px;'><td colspan='3' style='border-bottom: 1px solid;' align='right'>Order Amount:</td><td style='font-weight: bold;border-bottom: 1px solid;'>"
						+ cu + "&nbsp;" + orderList[0] + "</tr></table>");
				od.setLength(0);
			}
			sb.append("</div><div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>");
			sb.append("<div style='border: 1px solid #BDBDBD;background-color: #EBEBEB;padding-left: 10px;width: 950px;'><div style='margin-bottom: 10px;font-weight: bolder;color: red;'>PLEASE NOTE:</div>");
			sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
			String path="";
			try {
				path = UUIDUtil.getAutoLoginPath("/individual/getCenter", uuid);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account "+
					"<a style='color: #0070C0' href='"+AppConfig.ip_email+path+"'>here</a>.");
			sb.append("</div></div></div>");
			// 发送邮件
			String[] adminEmail = userDao.getAdminUser(0, null, userId);
			if (adminEmail != null && Utility.getStringIsNull(ub.getEmail())) {
				SendEmail.send(null, null, ub.getEmail(),
						sb.toString(),
						"Your Import-Express Order is received, Thanks!",
						orderNos, 1);
			}
		}
		return 1;
	}
 
}
