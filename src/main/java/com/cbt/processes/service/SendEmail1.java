package com.cbt.processes.service;

import com.cbt.common.StringUtils;
import com.cbt.util.AppConfig;
import com.cbt.util.UUIDUtil;
import com.cbt.util.Utility;
import com.cbt.website.dao.IOrderSplitDao;
import com.cbt.website.dao.OrderSplitDaoImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;

public class SendEmail1 {
	

//  发送邮件的props文件  
  private final transient static Properties props = System.getProperties(); 
  private static int count=1;
	private static int index=0;
	private static final Log LOG = LogFactory.getLog(SendEmail1.class);   
	public static final String HOST = "secure.emailsrvr.com"; 
	public static final String imap_HOST = "pop.emailsrvr.com";
  public static final String PROTOCOL = "smtp";   
  public static final int PORT = 465; 
  public static final int imap_PORT = 110;
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

 
//    邮件服务器登录验证  
  private transient MailAuthenticator authenticator;  
 
//    邮箱session  
  private transient Session session;  
      
  /**  
    * 初始化邮件发送器  
    * @param smtpHostName SMTP邮件服务器地址  
    * @param username 发送邮件的用户名(地址)  
    * @param password 发送邮件的密码  
    */ 
  public SendEmail1(final String smtpHostName, final String username,  
      final String password) {  
    init(username, password, smtpHostName);  
  }  
 
  /**  
    * 初始化邮件发送器  
    * @param username 发送邮件的用户名(地址)，并以此解析SMTP服务器地址  
    * @param password 发送邮件的密码  
    */ 
  public SendEmail1(final String username, final String password) {  
    // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用  
	  final String smtpHostName = "smtp.emailsrvr.com" ;
    //final String smtpHostName = "smtp." + username.split("@")[1];
    //final String smtpHostName = "mail." + username.split("@")[1];  
    init(username, password, smtpHostName);  
  }  
 
  /**  
    * 初始化  
    * @param username 发送邮件的用户名(地址)  
    * @param password 密码  
    * @param smtpHostName SMTP主机地址  
    */ 
  private void init(String username, String password, String smtpHostName) {  
    // 初始化props  
    props.put("mail.smtp.auth", "true");  
    props.put("mail.smtp.host", smtpHostName);
    //props.put("mail.smtp.port", 2525);
    // 验证  
    authenticator = new MailAuthenticator(username, password);  
    // 创建session  
    session = Session.getInstance(props, authenticator);  
  }  
  
  /**  
   * 给接收邮件调用的获取session
   * @param username 发送邮件的用户名(地址)  
   * @param password 密码  
   * @param smtpHostName SMTP主机地址  
   */ 
 public static Session getSession(String username, String password,Properties props) {  
   // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用  
	 final String smtpHostName = "smtp.emailsrvr.com" ;
   // 初始化props  
   props.put("mail.smtp.auth", "true");  
   props.put("mail.smtp.host", smtpHostName);  
   // 验证  
   MailAuthenticator authenticator = new MailAuthenticator(username, password);  
   // 创建session  
   Session session = Session.getInstance(props, authenticator);  
   return session;
 }  
 /**  
  * 给接收邮件调用的获取session
  * @param username 发送邮件的用户名(地址)  
  * @param password 密码  
  * @param smtpHostName SMTP主机地址  
  */ 
 public static Session getSession1(String username, String password,Properties props) {  
	 // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用  
	 final String smtpHostName = "mail.made-china.org" ;
	 // 初始化props  
	 props.put("mail.smtp.auth", "true");  
	 
	 props.put("mail.smtp.host", smtpHostName);  
	 // 验证  
	 MailAuthenticator authenticator = new MailAuthenticator(username, password);  
	 // 创建session  
	 Session session = Session.getInstance(props, authenticator);  
	 return session;
 }  
	

  
	

    
    

    public static int send(String email, String pwd,String toEmail , String content, String title, String orderNo, int number,String sale_email) {
    	//更新uuid
    	UUIDUtil.getEffectiveUUID(0, toEmail);
    	
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
    	Session session = getSession(FROM,PWD,props);
        try {
            Message msg = new MimeMessage(session);
//            msg.setHeader("Disposition-Notification-To","1");
            msg.setFrom(new InternetAddress(StringUtils.isStrNull(sale_email)?FROM:sale_email));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
           
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("service@import-express.com")); // 密送人 
            Transport.send(msg);
            splitDao.addMessage_error(title, "0", title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
            return 1;
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
            LOG.error(mex);
            System.out.println("SendEmail:"+mex);
            splitDao.addMessage_error(title, mex.getMessage(), title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
            	if(number != 0){
                    send( email,  pwd, toEmail ,  content,  title, orderNo, number-1,sale_email);
                }
            return 0;
        }
    }
    public static int send(String email, String pwd,String toEmail , String content, String title, String recEmail,String orderNo,int number) {
    	//更新uuid
    	UUIDUtil.getEffectiveUUID(0, toEmail);
    	Session session = getSession(FROM,PWD,props);
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
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sale1@import-express.com")); // 密送人 
            Transport.send(msg, msg.getAllRecipients());
            splitDao.addMessage_error(title, "0", title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
            return 1;
        }
        catch (Exception mex) {
        	LOG.info("发送邮件失败["+count+"]次失败");
            mex.printStackTrace();
            LOG.error(mex);
            System.out.println("SendEmail:"+mex);
            splitDao.addMessage_error(title, mex.getMessage(), title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
            /*if(count==3){
            	//当原有的发送邮件失败3次后，使用新账号发送邮件  whj 2016-09-27
            	index=sendToEmail(email,  pwd, toEmail ,  content,  title,  recEmail, orderNo);
            	//return index;
            }else if(count<3){
            	count++;
            	// if(number != 0){
                 send( email,  pwd, toEmail ,  content,  title,  recEmail, orderNo, number-1);
               //}
            }*/
            return 0;
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
    	
    	Session session = getSession1(FROMTWO,PWDTWO,props);
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            Message msg = new MimeMessage(session);
            try {
				msg.setFrom(new InternetAddress(FROMTWO));
				InternetAddress[] address = {new InternetAddress(toEmail)};
	            msg.setRecipients(Message.RecipientType.TO, address);
	            msg.setSubject(title);
	            msg.setSentDate(new Date());
	           // System.out.println("0="+content.split("<tr><td>Dear")[0].toString());
	            //System.out.println("1="+content.split("<tr><td>Dear")[1].toString());
	            content=content.split("<tr><td>Dear")[0].toString()+"<tr><td>reply to contact@import-express.com</td></tr><tr><td>Dear"+content.split("<tr><td>Dear")[1].toString();
	            msg.setContent(content , "text/html;charset=utf-8");
	            if(Utility.getStringIsNull(email)){
	                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(email)); // 抄送人 
	            }
	            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sale1@import-express.com")); // 密送人 
	            Transport.send(msg);
	            splitDao.addMessage_error(title, "0", title+",split_orderNo:" + orderNo+",toEmail:" + toEmail);
	            return 1;
			}catch (Exception e) {
				e.printStackTrace();
				 LOG.error(e);
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
    
    
    
    //邮件发送
    public static int sendCheckDeliveryWarning(String email, String pwd,String toEmail , String content, String title, String orderNo, int number) {
    	if(email != null){
    		//更新uuid
        	UUIDUtil.getEffectiveUUID(0, toEmail);
        	
        	Session session = getSession(email,pwd,props);
            try {
                Message msg = new MimeMessage(session);
//                msg.setHeader("Disposition-Notification-To","1");
                
                if (Utility.getStringIsNull(email) && Utility.getStringIsNull(pwd)) {
                	msg.setFrom(new InternetAddress(email));
                	//InternetAddress[] iaToListms = new InternetAddress().parse("sale1@import-express.com"); 
                    msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sale1@import-express.com")); // 密送人 
    			}else {
    				msg.setFrom(new InternetAddress(FROM));				
    			}
                
                InternetAddress[] address = {new InternetAddress(toEmail)};
                msg.setRecipients(Message.RecipientType.TO, address);
                msg.setSubject(title);
                msg.setSentDate(new Date());
                msg.setContent(content , "text/html;charset=utf-8");
               
                
                Transport.send(msg);
                return 1;
            }catch (Exception mex) {
                mex.printStackTrace();
                IOrderSplitDao splitDao = new OrderSplitDaoImpl();
                splitDao.addMessage_error(title, mex.getMessage(), title+",orderNo：" + orderNo);
                if(number != 0){
                    send( email,  pwd, toEmail ,  content,  title, orderNo, number-1,"");
                }
                return 0;
            }
    	}else{
    	
    	//更新uuid
    	UUIDUtil.getEffectiveUUID(0, toEmail);
    	
    	Session session = getSession(FROM,PWD,props);
        try {
            Message msg = new MimeMessage(session);
//            msg.setHeader("Disposition-Notification-To","1");
            
            if (Utility.getStringIsNull(email) && Utility.getStringIsNull(pwd)) {
            	msg.setFrom(new InternetAddress(email));
            	//InternetAddress[] iaToListms = new InternetAddress().parse("sale1@import-express.com"); 
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sale1@import-express.com")); // 密送人 
			}else {
				msg.setFrom(new InternetAddress(FROM));				
			}
            
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
           Transport.send(msg);
            return 1;
        }
        catch (Exception mex) {
            mex.printStackTrace();
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            splitDao.addMessage_error(title, mex.getMessage(), title+",orderNo：" + orderNo);
            if(number != 0){
                send( email,  pwd, toEmail ,  content,  title, orderNo, number-1,"");
            }
            return 0;
        }
    }
    }

    //发送邮件并 带有附件 
	public static int sendMailAndAttachment(String sendemail, String pwd,String emailInfo, String emailaddress, String copyEmail, File attachment,String customId) {
		
		Session session = getSession(copyEmail,pwd,props);
        try {
            Message msg = new MimeMessage(session);
          
            
            if (Utility.getStringIsNull(copyEmail) && Utility.getStringIsNull(pwd)) {
            	msg.setFrom(new InternetAddress(copyEmail));
            	//InternetAddress[] iaToListms = new InternetAddress().parse("sale1@import-express.com"); 
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sale1@import-express.com")); // 密送人 
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
              
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            
            // 将multipart对象放到message中
            msg.setContent(multipart);
            msg.setSentDate(new Date());
            // 保存邮件
            msg.saveChanges();
            
            Transport.send(msg);
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
    	if(email != null){
    		//更新uuid
    	Session session = getSession(email,pwd,props);
        try {
            Message msg = new MimeMessage(session);
      if (Utility.getStringIsNull(email) && Utility.getStringIsNull(pwd)) {
            	msg.setFrom(new InternetAddress(email));
            	//InternetAddress[] iaToListms = new InternetAddress().parse("sale1@import-express.com"); 
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sale1@import-express.com")); // 密送人 
			}else {
				msg.setFrom(new InternetAddress(FROM));				
			}
            
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content , "text/html;charset=utf-8");
            Transport.send(msg);
            return 1;
        }
        catch (Exception mex) {
            mex.printStackTrace();
            IOrderSplitDao splitDao = new OrderSplitDaoImpl();
            splitDao.addMessage_error(title, mex.getMessage(), title);
            if(number != 0){
                send( email,  pwd, toEmail ,  content,  title, null, number-1,"");
            }
            return 0;
        }
        
        	
        }else{
        	Session session = getSession(FROM,PWD,props);
            try {
                Message msg = new MimeMessage(session);
          if (Utility.getStringIsNull(email) && Utility.getStringIsNull(pwd)) {
                	msg.setFrom(new InternetAddress(email));
                	//InternetAddress[] iaToListms = new InternetAddress().parse("sale1@import-express.com"); 
                    msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("sale1@import-express.com")); // 密送人 
    			}else {
    				msg.setFrom(new InternetAddress(FROM));				
    			}
                
                InternetAddress[] address = {new InternetAddress(toEmail)};
                msg.setRecipients(Message.RecipientType.TO, address);
                msg.setSubject(title);
                msg.setSentDate(new Date());
                msg.setContent(content , "text/html;charset=utf-8");

                
                Transport.send(msg);
                return 1;
            }
            catch (Exception mex) {
                mex.printStackTrace();
                IOrderSplitDao splitDao = new OrderSplitDaoImpl();
                splitDao.addMessage_error(title, mex.getMessage(), title);
                if(number != 0){
                    send( email,  pwd, toEmail ,  content,  title, null, number-1,"");
                }
                return 0;
            }
    	
    }
	
    
}



	
	
}
