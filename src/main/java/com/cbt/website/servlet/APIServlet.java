package com.cbt.website.servlet;

import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.util.UUIDUtil;
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

public class APIServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Log LOG = LogFactory.getLog(APIServlet.class);
	
	
	public APIServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String userid = req.getParameter("userid");
		String email = req.getParameter("email");
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
				
				//生成8位密钥
				String signkey = UUIDUtil.generateUUID(8);
				int res = server.updateUserCategory(user, email, null,signkey);
				if (res != 0) {
					resMap.put("statu", true);
					resMap.put("message", "申请API密钥成功!");
					
					// 拼接邮件发送内容
					StringBuffer sb = new StringBuffer();
					String EmailTitle = "Your API key has been successfully created!";
					sb.append("<div style='font-size: 14px;'>");
					sb.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='" + AppConfig.ip_email + "/img/logo.png' ></img></a>");
					sb.append(" <div style='font-weight: bolder; margin-bottom: 10px;'>Dear " + userinfo.getEmail() + ",</div><br>");
					sb.append(" <div style='margin-top: 10px;'>Thanks for visiting us, Your api key is <span style='color:#f60;font-size: 24px'><span style='border-bottom:1px dashed #ccc;z-index:1' t='7' onclick='return false;' data='"+signkey+"'>"+signkey+"</span></span>");
					//sb.append(" <a style='color: #0070C0' href='"+ AppConfig.ip_email + "/individual/getCenter'>Checkout</a>.</div>");
					sb.append(" <div style='margin-top: 5px;'>If you have any question before making payment, Please feel free to contact us at <b>Contact@import-express.com</b> or add our Whatsapp at <b>+86 13564025061</b>.</div><br/>");
					//sb.append(" <div style='margin-top: 10px;'>For Wholesale Import><a style='color: #0070C0' href='http://www.import-express.com/apa/guide.html'>Start Here</a></div><br/>");
					sb.append(" <div style='font-weight: bold;margin-top: 10px;'>Best regards, </div><div style='font-weight: bold'><a href='http://www.Import-Express.com'>www.Import-Express.com</a></div></div>");
					sb.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
					sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
					sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='"+AppConfig.ip+"/individual/getCenter'>here</a>.");
					sb.append("</div></div></div>");
					
					int sendres = SendEmail.sendSetDropship(null, null, userinfo.getEmail(), sb.toString(), EmailTitle, 1);
					if (sendres == 1) {
						LOG.info("用户:"+userinfo.getUserid()+"-"+userinfo.getEmail()+"申请API密钥邮件发送成功!");
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
							LOG.info("用户:" + userinfo.getUserid() + "添加申请API密钥邮件记录失败!");
						}
					}else {
						LOG.info("给用户:"+userinfo.getUserid()+"-"+userinfo.getEmail()+"发送申请API密钥邮件发送失败!");
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
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		PrintWriter out = resp.getWriter();
		out.print(JSONObject.fromObject(resMap));
		out.flush();
		out.close();
	}

	
	
}
