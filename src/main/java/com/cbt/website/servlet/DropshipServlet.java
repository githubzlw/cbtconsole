package com.cbt.website.servlet;

import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.website.bean.EmailSendInfo;
import com.cbt.website.bean.UserInfo;
import com.cbt.website.dao.EmailSendInfoDao;
import com.cbt.website.dao.EmailSendInfoDaoImpl;
import com.cbt.website.server.GoodsServer;
import com.cbt.website.server.IGoodsServer;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class DropshipServlet
 */
public class DropshipServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Log LOG = LogFactory.getLog(DropshipServlet.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DropshipServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String userid = request.getParameter("userid");
		String email = request.getParameter("email");
		Integer user = null;
		if ("".equals(userid.trim()) && "".equals(email.trim())) {
			resMap.put("statu", false);
			resMap.put("message", "请填写用户信息!");
		}else {
			if (!"".equals(userid.trim())) {
				user = Integer.parseInt(userid);
			}
			IGoodsServer server = new GoodsServer();
			List<UserInfo> userInfos = server.getUserInfo(user, email);
			
			if (userInfos!=null && userInfos.size() >0) {
				UserInfo userinfo = userInfos.get(0);
				
				int res = server.updateUserCategory(user, email, "2",null);
				if (res != 0) {
					resMap.put("statu", true);
					resMap.put("message", "设置成Drop ship客户成功!");
					
					// 拼接邮件发送内容
					StringBuffer sb = new StringBuffer();
					String EmailTitle = "Your drop shipping application has been approved!";
					sb.append("<div style='font-size: 14px;'>");
					sb.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='" + AppConfig.ip_email + "/img/logo.png' ></img></a>");
					sb.append(" <div style='font-weight: bolder; margin-bottom: 10px;'>Dear " + userinfo.getEmail() + ",</div><br>");
					sb.append(" <div style='margin-top: 10px;'>You can now start your business with our drop shipping service.</div>");
					
					sb.append(" <div style='margin-top: 10px;'><span style='color:#F00;font-size: 16px;font-weight:bold'>IMPORTANT:</span><span style='color:#f60'>Please make sure you will refresh your web browser and login our website at <a href='http://www.Import-Express.com'>www.import-express.com</a>. If your web browser already logged you in by remember your username and password, please make sure you will <span style='color:#f00;font-weight:bold'>sign out and re-login</span>.</span></div>");
					sb.append(" <div style='margin-top: 10px;'>Below is where you can find the drop shipping button:<br/>"
							+"<img style='cursor: pointer' src='" + AppConfig.ip_email + "/img/dropshipemail1.png' ></img><br/>"
							+"<img style='cursor: pointer' src='" + AppConfig.ip_email + "/img/dropshipemail2.png' ></img><br/></div>");
					
					sb.append(" <div style='margin-top: 5px;'>If you have any question during the process, please feel free to contact your account manager or if you don’t know who is your account manager is, please send email to <b>Contact@import-express.com</b> or send massage to our 24/7 WhatsApp service at <b>+86 13564025061</b>.</div><br/>");
					sb.append(" <div style='font-weight: bold;margin-top: 10px;'>Best regards, </div><div style='font-weight: bold'><a href='http://www.Import-Express.com'>www.Import-Express.com</a></div></div>");
					sb.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
					sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
					sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='"+AppConfig.ip+"/individual/getCenter'>here</a>.");
					sb.append("</div></div></div>");
					
					int sendres = SendEmail.sendSetDropship(null, null, userinfo.getEmail(), sb.toString(), EmailTitle, 1);
					if (sendres == 1) {
						LOG.info("给用户:"+userinfo.getUserid()+"-"+userinfo.getEmail()+"发送设置drop shipping用户邮件发送成功!");
						EmailSendInfo emailSendInfo = new EmailSendInfo();
						emailSendInfo.setUserid(userinfo.getUserid());
						emailSendInfo.setEmail(userinfo.getEmail());
						emailSendInfo.setOrderid(null);
						emailSendInfo.setOrderstate(0);
						emailSendInfo.setType(0);// 0:未支付邮件
						emailSendInfo.setResult(0);// 成功
						// emailSendInfo.setInfo();
						try {
							EmailSendInfoDao emailSendDao = new EmailSendInfoDaoImpl();
							emailSendDao.addEmailSendInfo(emailSendInfo);
						} catch (Exception e) {
							e.printStackTrace();
							LOG.info("用户:" + userinfo.getUserid() + "添加设置drop shipping用户邮件记录失败!");
						}
					}else {
						LOG.info("给用户:"+userinfo.getUserid()+"-"+userinfo.getEmail()+"发送设置drop shipping用户邮件发送失败!");
					}
				}else {
					resMap.put("statu", true);
					resMap.put("message", "没有操作!!");
				}
			}else {
				resMap.put("statu", false);
				resMap.put("message", "当前用户信息不存在!");
			}
		}
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.print(JSONObject.fromObject(resMap));
		out.flush();
		out.close();
	}

}
