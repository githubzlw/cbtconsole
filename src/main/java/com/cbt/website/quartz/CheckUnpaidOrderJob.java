package com.cbt.website.quartz;

import com.cbt.processes.service.SendEmail;
import com.cbt.util.AppConfig;
import com.cbt.website.bean.EmailSendInfo;
import com.cbt.website.dao.EmailSendInfoDao;
import com.cbt.website.dao.EmailSendInfoDaoImpl;
import com.cbt.website.dao.IOrderwsDao;
import com.cbt.website.dao.OrderwsDao;

import org.slf4j.LoggerFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;


public class CheckUnpaidOrderJob implements Job {

	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(CheckUnpaidOrderJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		synchronized (this) {
			LOG.info("开始扫描超期未支付订单......");
			// 查询出
			IOrderwsDao dao = new OrderwsDao();
			// 从存储过程中取出数据,遍历发送邮件并记录到数据库
			List<Object[]> list = dao.CheckUnpaidOrder();


			EmailSendInfoDao emailSendDao = new EmailSendInfoDaoImpl();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				sb.setLength(0);
				String orderNo = (String) list.get(i)[1];
				int userId = (Integer) list.get(i)[3];
				String name = (String) list.get(i)[4];
				String email = (String) list.get(i)[5];
				Integer state = Integer.parseInt((String) list.get(i)[2]);

				// 拼接邮件发送内容
				String EmailTitle = "Check out those items in your Import-Express shopping cart!";
				sb.append("<div style='font-size: 14px;'>");
				sb.append("<a href='" + AppConfig.ip_email + "'><img style='cursor: pointer' src='" + AppConfig.ip_email + "/img/logo.png' ></img></a>");
				sb.append(" <div style='font-weight: bolder; margin-bottom: 10px;'>Dear " + name + ",</div><br>");
				sb.append(" <div style='margin-top: 10px;'>Thanks for visiting us, the items in your shopping cart are waiting for you to <a style='color: #0070C0' href='"+ AppConfig.ip_email + "/individual/getCenter'>Checkout</a> Before they getting taken off from the shelf.</div>");
				sb.append(" <div style='margin-top: 5px;'>If you have any question before making payment, Please feel free to contact us at <b>Contact@import-express.com</b> or add our Whatsapp at <b>+86 13564025061</b>.</div><br/>");
				sb.append(" <div style='margin-top: 10px;'>For Wholesale Import><a style='color: #0070C0' href='http://www.import-express.com/cbtconsole/apa/guide.html'>Start Here</a></div><br/>");
				sb.append(" <div style='font-weight: bold;margin-top: 10px;'>Best regards, </div><div style='font-weight: bold'><a href='http://www.Import-Express.com'>www.Import-Express.com</a></div></div>");
				sb.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
				sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
				sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='"+AppConfig.ip+"/individual/getCenter'>here</a>.");
				sb.append("</div></div></div>");

				// 判断是否存在已发送记录,已发送则跳出当前
				Boolean isexist = null;
				
				try {
					isexist = emailSendDao.isExistEmailSendInfo(orderNo, userId, 0);
				} catch (Exception e1) {
					LOG.info("查询已发送邮件记录错误!");
				}
				if (isexist) {
					LOG.info("已存在记录:"+orderNo+"---"+userId+"---条次:"+i);
					continue;// 已存在记录则跳出本次循环
				} else {
					int result = 0;
					try {
						result = SendEmail.sendCheckDeliveryWarning(null, null,email, sb.toString(), EmailTitle, null,1);
					} catch (Exception e1) {
						LOG.info("当前计数:"+ i +"给用户:"+userId+"-"+email+"发送超期未支付邮件发送失败!");
					}

					if (result == 1) {
						LOG.info("当前计数:"+ i +"给用户:"+userId+"-"+email+"发送超期未支付邮件发送成功!");
						EmailSendInfo emailSendInfo = new EmailSendInfo();
						emailSendInfo.setUserid(userId);
						emailSendInfo.setEmail(email);
						emailSendInfo.setOrderid(orderNo);
						emailSendInfo.setOrderstate(state);
						emailSendInfo.setType(0);// 0:未支付邮件
						emailSendInfo.setResult(0);// 成功
						// emailSendInfo.setInfo();
						try {
							emailSendDao.addEmailSendInfo(emailSendInfo);
						} catch (Exception e) {
							e.printStackTrace();
							LOG.info("用户:" + userId + "添加超期未支付邮件记录失败!");
						}
					}else {
						LOG.info("当前计数:"+ i +"给用户:"+userId+"-"+email+"发送超期未支付邮件发送失败!!!!!");
					}
					try {
						Thread.sleep(5000);// 阻塞线程,避免被当成垃圾邮件
					} catch (InterruptedException e) {
						LOG.info("超期未支付邮件发送失败!线程休眠错误...");
						e.printStackTrace();
					}
				}
			}
			LOG.info("结束扫描超期未支付订单......");
		}

	

	}

}
