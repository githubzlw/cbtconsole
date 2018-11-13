package com.cbt.website.servlet;

import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.website.bean.EmailSendInfo;
import com.cbt.website.dao.EmailSendInfoDao;
import com.cbt.website.dao.EmailSendInfoDaoImpl;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;
import com.cbt.website.userAuth.Dao.AdmUserDao;
import com.cbt.website.userAuth.bean.Admuser;
import com.cbt.website.userAuth.impl.AdmUserDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class CheckDeliveryWarning implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(CheckDeliveryWarning.class);   
	
	public void run() {
		LOG.info("自动扫描交期预警......");
		//查询出
		IOrderwsDao dao = new OrderwsDao();
		//从存储过程中取出数据,遍历发送邮件并记录到数据库
		List<Object[]> list= dao.getDeliveryWarningList(0,0);
//		String listJson = SerializeUtil.ListToJson(list);
		
		EmailSendInfoDao emailSendDao = new EmailSendInfoDaoImpl();
		AdmUserDao admuserDao = new AdmUserDaoImpl();
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.setLength(0);
			String orderNo = (String) list.get(i)[0];
			int userId = (Integer) list.get(i)[13];
			String email = (String) list.get(i)[2];
			String name = (String) list.get(i)[3];
			int state = (Integer) list.get(i)[8];
			
			//拼接邮件发送内容
			String EmailTitle = "Your Import-Express Order is still under stock checking! ";
			sb.append("<div style='font-size: 14px;'>");
			sb.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='" + AppConfig.ip_email + "/img/logo.png' ></img></a>");
			sb.append(" <div style='font-weight: bolder; margin-bottom: 10px;'>Dear "+ name +",</div><br>"); 
			sb.append(" <div style='margin-bottom: 10px; font-size: 13px;'>Order #<a style='color: #0070C0' href='"+ AppConfig.ip_email +"/individual/getCenter'>"+ orderNo +"</a></div><br>"); 
			sb.append(" <div style='margin-bottom: 10px;'>Due to the supply reason, our purchase team is still working on the products stock checking with suppliers.</div>");
			sb.append(" <div style='margin-bottom: 3px;'>Please be patient and you will be soon see your order status update to [Under Purchasing] which will be shown in <a style='color: #0070C0' href='"+ AppConfig.ip_email +"/individual/getCenter'>Your account</a>.</div><br>"); 
			sb.append(" <div style='font-weight: bold'>Best regards, </div><div style='font-weight: bold'><a href='http://www.Import-Express.com'>www.Import-Express.com</a></div></div>");
			
			//判断是否存在已发送记录,已发送则跳出当前
			
			
			Boolean isexist = null;;
			try {
				isexist = emailSendDao.isExistEmailSendInfo(orderNo, userId, 1);
			} catch (Exception e1) {
				LOG.info("查询已发送邮件记录错误!");
			}
			if (isexist) {
				continue;//已存在记录则跳出本次循环
			}else{
				//根据userid查询与该客户对应的负责销售
				Admuser admuser = null;
				try {
					admuser = admuserDao.getAdmUserFromUser(userId);
				} catch (Exception e1) {	
					e1.printStackTrace();
				}
				if (admuser == null || admuser.getEmialpass().isEmpty() || admuser.getEmialpass()==null || admuser.getEmail() == null || admuser.getEmialpass().isEmpty()) {
					continue;
				}
				int result = SendEmail.sendCheckDeliveryWarning(admuser.getEmail(), admuser.getEmialpass(), email, sb.toString(), EmailTitle, null, 1);
			
				if (result == 1) {
					
					LOG.info("管理人员:"+ admuser.getAdmName() +"给用户:"+userId + "发送交期预警邮件发送成功!");
					EmailSendInfo emailSendInfo = new EmailSendInfo();
					emailSendInfo.setUserid(userId);
					emailSendInfo.setEmail(email);
					emailSendInfo.setOrderid(orderNo);
					emailSendInfo.setOrderstate(state);
					emailSendInfo.setType(1);//交期预警邮件
					emailSendInfo.setResult(0);//成功
		//				emailSendInfo.setInfo();
		//				emailSendInfo.setCreatetime();
					try {
						emailSendDao.addEmailSendInfo(emailSendInfo);
					} catch (Exception e) {
						e.printStackTrace();
						LOG.info("用户:"+userId + "交期预警邮件发送失败!下次自动扫描重发...");
					}
					
				}
				
				try {
					Thread.sleep(10000);//阻塞线程,避免被当成垃圾邮件
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}