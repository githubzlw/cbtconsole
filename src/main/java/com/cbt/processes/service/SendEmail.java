package com.cbt.processes.service;

import com.cbt.util.AppConfig;
import com.cbt.util.UUIDUtil;
import com.cbt.util.Utility;
import com.cbt.website.dao.IOrderSplitDao;
import com.cbt.website.dao.OrderSplitDaoImpl;

import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;

public class SendEmail {
	private static int count=1;
	private static int index=0;
	private final static org.slf4j.Logger LOG = LoggerFactory.getLogger(SendEmail.class);
	public static final String HOST = "secure.emailsrvr.com"; 
    public static final String NOSSLSMTP_HOST = "smtp.emailsrvr.com";
    public static final String PROTOCOL = "smtp";   
    public static final int PORT = 465; 
    public static final int NOSSLSMTP_PORT = 25;
    public static final String FROM = "service@import-express.com";//发件人的email
    public static final String PWD = "CustomerFirst1248";//发件人密码
    public static final String TIMEOUT = "80000";
    public static final String CONNECTIONTIMEOUT = "80000";
    private final static String IS_ENABLED_SSL = "true";
    private final static String IS_ENABLED_DEBUG_MOD = "false";
    
    //当发送3次失败后换邮箱地址发送
    public static final String POP = "mail.made-china.org";
    public static final String SMTP = "mail.made-china.org";
    public static final String FROMTWO = "noreply@made-china.org";//发件人的email
    public static final String PWDTWO = "qazwEx902eQ";//发件人密码

//  public static final String FROM = "import1@import-express.com";//发件人的email
//  public static final String PWD = "importa1b2";
//	public static final String HOST = "smtp.126.com";
//    public static final String PROTOCOL = "smtp";
//    public static final int PORT = 25;
//    public static final String FROM = "elaine_ylm@126.com";//鍙戜欢浜虹殑email
//    public static final String PWD = "ylm123.";//鍙戜欢浜哄瘑鐮�
    
    // 初始化连接邮件服务器的会话信息 
    private static Properties props = null; 
    
	static {
		props = new Properties();

		// props.put("mail.transport.protocol", PROTOCOL);//设置协议
		props.put("mail.store.protocol", PROTOCOL);// 设置协议
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.timeout", TIMEOUT);
		props.put("mail.smtp.connectiontimeou", CONNECTIONTIMEOUT);
		//props.put("mail.smtp.ssl.enable", IS_ENABLED_SSL);//设置SSL
		//props.put("mail.smtp.port", PORT);// 设置端口
		//props.put("mail.smtp.host", HOST);// 设置服务器地址

		props.put("mail.smtp.ssl.enable", IS_ENABLED_DEBUG_MOD); //设置SSL
		props.put("mail.smtp.host", NOSSLSMTP_HOST);// 设置服务器地址
		props.put("mail.smtp.port", NOSSLSMTP_PORT);// 设置端口
		
		// 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
		//props.put("mail.debug", IS_ENABLED_DEBUG_MOD);
	}
	
	/**
	 * 获取Session
	 * @param email
	 * @param pwd
	 * @return
	 */
	private static Session getSession(final String email, final String pwd) {
		/*Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM, PWD);
			}
		});*/
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				if (Utility.getStringIsNull(email) && Utility.getStringIsNull(pwd)) {
					return new PasswordAuthentication(email, pwd);
				} else {
					return new PasswordAuthentication(FROM, PWD);
				}
			}
		});
		return session;
	}
    

    public static int send(String email, String pwd,String toEmail , String content, String title, String orderNo, int number) {
    	//更新uuid
    	UUIDUtil.getEffectiveUUID(0, toEmail);
    	
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
    	Session session = getSession(email,pwd);
        try {
            Message msg = new MimeMessage(session);
//            msg.setHeader("Disposition-Notification-To","1");
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sales@sourcing-cn.com")); // 密送人 
            Transport.send(msg, msg.getAllRecipients());
            splitDao.addMessage_error(title, "0", title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
            return 1;
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
            LOG.error("send",mex);
            System.out.println("SendEmail:"+mex);
            splitDao.addMessage_error(title, mex.getMessage(), title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
            	if(number != 0){
                    send( email,  pwd, toEmail ,  content,  title, orderNo, number-1);
                }
            return 0;
        }
    }
    public static int send(String email, String pwd,String toEmail , String content, String title, String recEmail,String orderNo,int number) {
    	//更新uuid
    	UUIDUtil.getEffectiveUUID(0, toEmail);
    	Session session = getSession(email,pwd);
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROM));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
            if(Utility.getStringIsNull(email)){
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(email)); // 抄送人 
            }
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sales@sourcing-cn.com")); // 密送人 
            Transport.send(msg, msg.getAllRecipients());
            splitDao.addMessage_error(title, "0", title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
            return 1;
        }
        catch (Exception mex) {
        	LOG.info("发送邮件失败["+count+"]次失败");
            mex.printStackTrace();
            LOG.error("send",mex);
            System.out.println("SendEmail:"+mex);
            splitDao.addMessage_error(email, mex.getMessage(), title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
            if(count==3){
            	//当原有的发送邮件失败3次后，使用新账号发送邮件  whj 2016-09-27
            	index=sendToEmail(email,  pwd, toEmail ,  content,  title,  recEmail, orderNo);
            	//return index;
            }else if(count<3){
            	count++;
            	// if(number != 0){
                 send( email,  pwd, toEmail ,  content,  title,  recEmail, orderNo, number-1);
               //}
            }
            return index;
        }
    }
    
    /**
     * 当原有的发送邮件失败3次后，使用新账号发送邮件
     * @param email
     * @param pwd
     * @param toEmail
     * @param content
     * @param title
     * @param recEmail
     * @param orderNo
     * @return 
     * 2016-09-27
     * @author 王宏杰
     */
    public static int sendToEmail(String email, String pwd,String toEmail , String content, String title, String recEmail,String orderNo) {
        UUIDUtil.getEffectiveUUID(0, toEmail);
    	
    	Session session = getSession(email,pwd);
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            Message msg = new MimeMessage(session);
            try {
				msg.setFrom(new InternetAddress(FROMTWO));
				InternetAddress[] address = {new InternetAddress(toEmail)};
	            msg.setRecipients(Message.RecipientType.TO, address);
	            msg.setSubject(title);
	            msg.setSentDate(new Date());
	            //System.out.println("0="+content.split("<tr><td>Dear")[0].toString());
	            //System.out.println("1="+content.split("<tr><td>Dear")[1].toString());
	            //content=content.split("<tr><td>Dear")[0].toString()+"<tr><td>reply to contact@import-express.com</td></tr><tr><td>Dear"+content.split("<tr><td>Dear")[1].toString();
	            content="<tr><td>reply to contact@import-express.com</td></tr><tr><td>Dear "+toEmail;
	            msg.setContent(content , "text/html;charset=utf-8");
	            if(Utility.getStringIsNull(email)){
	                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(email)); // 抄送人 
	            }
	            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sales@sourcing-cn.com")); // 密送人 
	            Transport.send(msg, msg.getAllRecipients());
	            splitDao.addMessage_error(title, "0", title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
	            return 1;
			}catch (Exception e) {
				e.printStackTrace();
				 LOG.error("sendToEmail",e);
		         System.out.println("SendEmail:"+e);
		         splitDao.addMessage_error(title, e.getMessage(), title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
				return 0;
			}
    }
    
    //邮件头尾信息
    public static StringBuffer SetContent(String name, StringBuffer content) {
    	StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
        sb.append("<a href='"+AppConfig.ip_email+"'><img style='cursor: pointer' src='"+AppConfig.ip_email+"/img/logo.png' ></img></a>");
        sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear Valued " + name + ",</div><br><div style='font-size: 13px;'>"); 
		sb.append(content);
		sb.append("<br><div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>"); 
		sb.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
		sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
		sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='"+AppConfig.ip_email+"/individual/getCenter'>here</a>.");
		sb.append("</div></div></div>");
		return sb;
    }
    
    //邮件头尾信息(客诉投诉 )
  //邮件头尾信息
    public static StringBuffer SetComplainContent(String name, StringBuffer content) {
    	StringBuffer sb=new StringBuffer("<div style='font-size: 14px;'>");
        sb.append("<a href='"+AppConfig.ip_email+"'><img style='cursor: pointer' src='"+AppConfig.ip_email+"/img/logo.png' ></img></a>");
        sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'><span>Dear Sir/Madam,</span> " + name + ",</div><br><div style='font-size: 13px;'>");
		sb.append(content);
		sb.append("<br><div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>"); 
		sb.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
		sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
		sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message.");
		sb.append("</div></div></div>");
		return sb;
    }
    
    
    //邮件发送
    public static int sendCheckDeliveryWarning(String email, String pwd,String toEmail , String content, String title, String orderNo, int number) {
    	//更新uuid
    	UUIDUtil.getEffectiveUUID(0, toEmail);
    	
    	Session session = getSession(email,pwd);
        try {
            Message msg = new MimeMessage(session);
//            msg.setHeader("Disposition-Notification-To","1");
            
            if (Utility.getStringIsNull(email) && Utility.getStringIsNull(pwd)) {
            	msg.setFrom(new InternetAddress(email));
            	//InternetAddress[] iaToListms = new InternetAddress().parse("sales@sourcing-cn.com"); 
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sales@sourcing-cn.com")); // 密送人 
			}else {
				msg.setFrom(new InternetAddress(FROM));				
			}
            
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
//            if(Utility.getStringIsNull(email)){
//                InternetAddress[] iaToListcs = new InternetAddress().parse(email);
//                msg.setRecipients(Message.RecipientType.CC, iaToListcs); // 抄送人
//            }
     
            /*Transport transport = null;
            transport = session.getTransport("smtp");
            if(email != null){
                transport.connect(HOST, email, pwd);
            }else{
                transport.connect(HOST, FROM, PWD);
            }
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();*/
            Transport.send(msg, msg.getAllRecipients());
            return 1;
        }
        catch (Exception mex) {
            mex.printStackTrace();
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            splitDao.addMessage_error(title, mex.getMessage(), title+",orderNo：" + orderNo);
            if(number != 0){
                send( email,  pwd, toEmail ,  content,  title, orderNo, number-1);
            }
            return 0;
        }
    }

    //发送邮件并 带有附件 
	public static int sendMailAndAttachment(String sendemail, String pwd,String emailInfo, String emailaddress, String copyEmail, File attachment,String customId) {
		// TODO Auto-generated method stub
		Session session = getSession(copyEmail,pwd);
        try {
            Message msg = new MimeMessage(session);
//            msg.setHeader("Disposition-Notification-To","1");
            
            if (Utility.getStringIsNull(copyEmail) && Utility.getStringIsNull(pwd)) {
            	msg.setFrom(new InternetAddress(copyEmail));
            	//InternetAddress[] iaToListms = new InternetAddress().parse("sales@sourcing-cn.com"); 
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sales@sourcing-cn.com")); // 密送人 
			}else {
				msg.setFrom(new InternetAddress(FROM));				
			}
            
            // 收件人
            InternetAddress to = new InternetAddress(emailaddress);
            msg.setRecipient(Message.RecipientType.TO, to);
            
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            
            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(emailInfo, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                
                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
//                sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            
            // 将multipart对象放到message中
            msg.setContent(multipart);
            msg.setSentDate(new Date());
            // 保存邮件
            msg.saveChanges();
            //异步线程发送邮件
            TransportThread thread  = new TransportThread(emailaddress,msg,customId);
            thread.start(); 
            return 1;
        }
        catch (Exception mex) {
            mex.printStackTrace();
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            splitDao.addMessage_error("", mex.getMessage(), "");
            return 0; 
        }
	}
	
	
	//邮件发送
    public static int sendSetDropship(String email, String pwd,String toEmail , String content, String title, int number) {
    	Session session = getSession(email,pwd);
        try {
            Message msg = new MimeMessage(session);
//            msg.setHeader("Disposition-Notification-To","1");
            
            if (Utility.getStringIsNull(email) && Utility.getStringIsNull(pwd)) {
            	msg.setFrom(new InternetAddress(email));
            	//InternetAddress[] iaToListms = new InternetAddress().parse("sales@sourcing-cn.com"); 
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sales@sourcing-cn.com")); // 密送人 
			}else {
				msg.setFrom(new InternetAddress(FROM));				
			}
            
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
//            if(Utility.getStringIsNull(email)){
//                InternetAddress[] iaToListcs = new InternetAddress().parse(email);
//                msg.setRecipients(Message.RecipientType.CC, iaToListcs); // 抄送人
//            }
     
            /*Transport transport = null;
            transport = session.getTransport("smtp");
            if(email != null){
                transport.connect(HOST, email, pwd);
            }else{
                transport.connect(HOST, FROM, PWD);
            }
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();*/
            Transport.send(msg, msg.getAllRecipients());
            return 1;
        }
        catch (Exception mex) {
            mex.printStackTrace();
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            splitDao.addMessage_error(title, mex.getMessage(), title);
            if(number != 0){
                send( email,  pwd, toEmail ,  content,  title, null, number-1);
            }
            return 0;
        }
    }
	
    
}


