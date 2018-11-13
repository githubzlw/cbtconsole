package com.cbt.processes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


//import com.sun.mail.smtp.SMTPSendFailedException;


/**  
* 邮件发送器，可单发，群发。  
*/ 
public class MailSender {  
	private static final Logger LOG = LoggerFactory.getLogger(MailSender.class);
//  发送邮件的props文件  
  private final transient Properties props = System.getProperties();  
 
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
  public MailSender(final String smtpHostName, final String username,  
      final String password) {  
    init(username, password, smtpHostName);  
  }  
 
  /**  
    * 初始化邮件发送器  
    * @param username 发送邮件的用户名(地址)，并以此解析SMTP服务器地址  
    * @param password 发送邮件的密码  
    */ 
  public MailSender(final String username, final String password) {  
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
/* public static Session getSisson(String username, String password,Properties props) {  
   // 通过邮箱地址解析出smtp服务器，对大多数邮箱都管用  
   String smtpHostName = "smtp." + username.split("@")[1]; 
   // 初始化props  
   props.put("mail.smtp.auth", "true");  
   props.put("mail.smtp.host", smtpHostName);  
   // 验证  
   MailAuthenticator authenticator = new MailAuthenticator(username, password);  
   // 创建session  
   Session session = Session.getInstance(props, authenticator);  
   return session;
 } */ 
  /**  
   * 发送邮件  
   * @param recipient收件人邮箱地址  
   * @param subject邮件主题  
   * @param content邮件内容  
   * @throws AddressException  
   * @throws MessagingException  
   */ 
 /*public void send(String recipient, String subject, String content)  
     throws AddressException, MessagingException {
	  Date x1 = new Date();
     long y1 = x1.getTime();
   // 创建mime类型邮件  
   final MimeMessage message = new MimeMessage(session);  
   // 设置发信人  
   message.setFrom(new InternetAddress(authenticator.getUsername()));  
   // 设置收件人  
   message.setRecipient(RecipientType.TO, new InternetAddress(recipient));  
   // 设置主题  
   message.setSubject(subject);  
   // 设置邮件内容  
   MimeBodyPart text = new MimeBodyPart();
   // setContent(“邮件的正文内容”,”设置邮件内容的编码方式”)
   content = content.replaceAll("(.*?)src=\"(.*?)","$1src=\"cid:$2");
   String content1=content;
   text.setContent(content1.toString(),"text/html;charset=utf-8");
   //创建图片
   MimeBodyPart img = new MimeBodyPart();
   JavaMail API不限制信息只为文本,任何形式的信息都可能作茧自缚MimeMessage的一部分.
    * 除了文本信息,作为文件附件包含在电子邮件信息的一部分是很普遍的.
    * JavaMail API通过使用DataHandler对象,提供一个允许我们包含非文本BodyPart对象的简便方法.
   
   DataHandler dh = new DataHandler(new FileDataSource("f_th.jpg"));
   img.setDataHandler(dh);
   //创建图片的一个表示用于显示在邮件中显示
   img.setContentID("/NBEmail/images/f_th.jpg");
   MimeMultipart mm = new MimeMultipart();
   mm.addBodyPart(text);
   mm.addBodyPart(img);
   mm.setSubType("related");//设置正文与图片之间的关系
   
   message.setContent(mm);
   Multipart mp = new MimeMultipart("related");  
   MimeBodyPart mbp = new MimeBodyPart();
   // content = content.replaceAll("(.*?)src=\"(.*?)","$1src=\"http://112.64.174.34:43887$2");
   
   content=content.replace("/NBEmail/images/","");
   
   String content1=content.replaceAll("(.*?)src=\"(.*?)","$1src=\"cid:$2");
   MimeBodyPart img = new MimeBodyPart();
   JavaMail API不限制信息只为文本,任何形式的信息都可能作茧自缚MimeMessage的一部分.
    * 除了文本信息,作为文件附件包含在电子邮件信息的一部分是很普遍的.
    * JavaMail API通过使用DataHandler对象,提供一个允许我们包含非文本BodyPart对象的简便方法.
   DataHandler dh = new DataHandler(new FileDataSource("/NBEmail/image/7.jpg"));
   img.setDataHandler(dh);
   //创建图片的一个表示用于显示在邮件中显示
   img.setContentID("7.jpg");
   mbp.setContent(content1.toString(),"text/html;charset=utf-8");
   MimeMultipart mm = new MimeMultipart();
   mm.addBodyPart(mbp);
   mm.addBodyPart(img);
   mm.setSubType("related");//设置正文与图片之间的关系
   //图班与正文的 body
   MimeBodyPart all = new MimeBodyPart();
   all.setContent(mm);
   MimeMultipart mm2 = new MimeMultipart();
   mm2.addBodyPart(all);
   message.setContent(mm2);
   
  // mbp.setContent(content1.toString(),"text/html;charset=utf-8");  
   //mp.addBodyPart(mbp);  
   //message.setContent(mp);  
   // 设置邮件内容  
   
   message.setSentDate(new Date());
   Date x2 = new Date();
   long y2= x2.getTime();
   LOG.warn("对邮件内容设置时间:"+(y2-y1));
 //  message.setContent(content.toString(), "text/html;charset=utf-8");  
   // 发送  
   Transport.send(message);
   Date x3 = new Date();
   long y3= x3.getTime();
   LOG.warn("邮件链接SMTP时间:"+(y3-y2));
   
 } */
  
  
 
/*  *//**  
    * 发送邮件  
    * @param recipient收件人邮箱地址  
    * @param subject邮件主题  
    * @param content邮件内容  
    * @throws AddressException  
    * @throws MessagingException  
    *//* 
  public void send(String recipient, String subject, String content)  
      throws AddressException, MessagingException {
	  Date x1 = new Date();
      long y1 = x1.getTime();
    // 创建mime类型邮件  
    final MimeMessage message = new MimeMessage(session);  
    // 设置发信人  
    message.setFrom(new InternetAddress(authenticator.getUsername()));  
    // 设置收件人  
    message.setRecipient(RecipientType.TO, new InternetAddress(recipient));  
    // 设置主题  
    message.setSubject(subject);  
    // 设置邮件内容  
    Multipart mp = new MimeMultipart("related");  
    MimeBodyPart mbp = new MimeBodyPart();
     content = content.replaceAll("(.*?)src=\"(.*?)","$1src=\"http://112.64.174.34:43887$2");
    String content1=content;
    mbp.setContent(content1.toString(),"text/html;charset=utf-8");  
    mp.addBodyPart(mbp);  
    message.setContent(mp);  
    // 设置邮件内容  
    
    message.setSentDate(new Date());
    Date x2 = new Date();
    long y2= x2.getTime();
    LOG.warn("对邮件内容设置时间:"+(y2-y1));
  //  message.setContent(content.toString(), "text/html;charset=utf-8");  
    // 发送  
    Transport.send(message);
    Date x3 = new Date();
    long y3= x3.getTime();
    LOG.warn("邮件链接SMTP时间:"+(y3-y2));
    
  } */
  /**  
   * 发送邮件  
   * @param recipient收件人邮箱地址  
   * @param subject邮件主题  
   * @param content邮件内容  
   * @throws AddressException  
   * @throws MessagingException  
   */ 
  public void send(String recipient, String subject, String content)  
		  throws AddressException, MessagingException {
	  Date x1 = new Date();
	  long y1= x1.getTime();
	  // 创建mime类型邮件  
	    final MimeMessage message = new MimeMessage(session);  
	    // 设置发信人  
	    message.setFrom(new InternetAddress(authenticator.getUsername()));  
	    // 设置收件人  
	    message.setRecipient(RecipientType.TO, new InternetAddress(recipient));  
	    // 设置主题  
	    message.setSubject(subject);
	    //设置时间
	    message.setSentDate(new Date()); 
	    // 设置邮件内容  
	    Multipart mp = new MimeMultipart("related");  
	    MimeBodyPart mbp = new MimeBodyPart();
	   
	    mbp.setContent(content.toString(),"text/html;charset=utf-8");  
	    mp.addBodyPart(mbp);  
	    message.setContent(mp);  
	 
	
	  Date x2 = new Date();
	  long y2= x2.getTime();
	  LOG.warn("对邮件内容设置时间:"+(y2-y1));
	  // 发送  
	  Transport.send(message); 
	  Date x3 = new Date();
	  long y3= x3.getTime();
	  LOG.warn("邮件链接SMTP时间:"+(y3-y2));
	  
  } 
  
  /**  
   * 发送附带有附件的邮件  
   * @param recipient收件人邮箱地址  
   * @param subject邮件主题  
   * @param content邮件内容  
   * @param emailfile邮件附件
   * @throws AddressException  
   * @throws MessagingException  
   */ 
 public void send(String recipient, String subject, String content,Map<String, String> map,String path)  
     throws AddressException, MessagingException {
	 Matcher   matcher   =   PATTERN.matcher(content);   
     List <String>  list   =   new   ArrayList();   
     while   (matcher.find())   {   
         String   group   =   matcher.group(1);   
         if   (group   ==   null)   {   
             continue;   
         }   
         //   这里可能还需要更复杂的判断,用以处理src="...."内的一些转义符   
         if   (group.startsWith("'"))   {   
             list.add(group.substring(1,   group.indexOf("'",   1)));   
         }   else   if   (group.startsWith("\""))   {   
             list.add(group.substring(1,   group.indexOf("\"",   1)));   
         }   else   {   
             list.add(group.split("\\s")[0]);  
         }   
     }   
     int size = list.size();
     String[] strs = (String[])list.toArray(new String[size]);
	 Date x1 = new Date();
     long y1 = x1.getTime();
   // 创建mime类型邮件  
   final MimeMessage message = new MimeMessage(session);  
   // 设置发信人  
   message.setFrom(new InternetAddress(authenticator.getUsername()));  
   // 设置收件人  
   message.setRecipient(RecipientType.TO, new InternetAddress(recipient));  
   // 设置主题  
   message.setSubject(subject);  
 

   // first part  (the html)
   BodyPart messageBodyPart = new MimeBodyPart();
  
   content = content.replaceAll("(.*?)src=\"(.*?)","$1src=\"cid:$2");
   /*content = content.replaceAll("/NBEmail/images/signature.jpg","image");*/
   for(int i=0;i<strs.length;i++){
		  int total=10000+i;
		  content = content.replaceAll(strs[i],total+"");
		
	  }
	  messageBodyPart.setContent(content, "text/html;charset=utf-8");
	  MimeMultipart multipart = new MimeMultipart("related");
	  // add it
	  multipart.addBodyPart(messageBodyPart);
	 
	  for(int i=0;i<size;i++){
		  int total=10000+i;
		  MimeBodyPart mbp=new MimeBodyPart();
		  String img=strs[i].replaceAll("/NBEmail/images/","" );
			 //DataSource fds = new FileDataSource("E:\\DevSoft\\tomcat7\\webapps\\NBEmail\\images\\"+img);
			 DataSource fds = new FileDataSource("E:\\webDir\\NBEmail\\images\\"+img);
			 mbp.setDataHandler(new DataHandler(fds));
		     mbp.setHeader("Content-ID",total+"");
		     multipart.addBodyPart(mbp);
		  
		}
  
   
   /*Multipart mp = new MimeMultipart("related");
   
   message.setSentDate(new Date()); 
   MimeBodyPart mbp = new MimeBodyPart();
   content = content.replaceAll("(.*?)src=\"(.*?)","$1src=\"http://112.64.174.34:43887$2");
   String content1=content;
   mbp.setContent(content1.toString(),"text/html;charset=utf-8");  
    
   mp.addBodyPart(mbp); */
   LOG.warn("附件个数:"+map.size()+"");
   if(map.size() > 0) {//说明有附件上传
	   Set<String> keySet = map.keySet();
	   for (String key : keySet) {
			String emailfile = map.get(key);
			// FileDataSource fds = new FileDataSource(path+"\\"+emailfile);
			   FileDataSource fds = new FileDataSource(path+File.separator+emailfile);
			   DataHandler dh = new DataHandler(fds);
			   //boolean flag = emailfile.contains("\\");
			   boolean flag = emailfile.contains(File.separator);
			   String fname = emailfile;
			   LOG.warn("文件路径+名称"+fname);
			   if(flag) {
				   //int index = fname.lastIndexOf("\\");
				   int index = fname.lastIndexOf(File.separator);
				   fname = emailfile.substring(index+1);// 提取文件名
				  LOG.warn("文件名称"+fname);
			   }
				try {// 处理文件名是中文的情况
					fname = new String(fname.getBytes("gb2312"), "ISO8859-1");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setFileName(fname);// 可以和原文件名不一致,但最好一样
				messageBodyPart.setDataHandler(dh);
				multipart.addBodyPart(messageBodyPart);
		}
   }
   
   
   message.setContent(multipart);
   Date x2 = new Date();
   long y2= x2.getTime();
   LOG.warn("对邮件内容设置时间:"+(y2-y1));
   // 设置邮件内容  
 //  message.setContent(content.toString(), "text/html;charset=utf-8");  
   // 发送  
    	Transport.send(message);
    	   Date x3 = new Date();
    	    long y3= x3.getTime();
    	    LOG.warn("邮件链接SMTP时间:"+(y3-y2));
 } 
 
 /**  
  * 群发邮件  
  * @param recipients收件人们  
  * @param subject 主题  
  * @param content 内容  
  * @throws AddressException  
  * @throws MessagingException  
  *//* 
public void send(List<String> recipients, String subject, String content,String to)  
    throws AddressException, MessagingException { 
	Date x1 = new Date();
    long y1 = x1.getTime();
  // 创建mime类型邮件  
  final MimeMessage message = new MimeMessage(session);  
  // 设置发信人  
  message.setFrom(new InternetAddress(authenticator.getUsername()));  
  // 设置收件人们  
  final int num = recipients.size();  
  InternetAddress[] addresses = new InternetAddress[num];  
  for (int i = 0; i < num; i++) {  
    addresses[i] = new InternetAddress(recipients.get(i));  
  }  
  message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
  message.setRecipients(Message.RecipientType.CC, addresses);  
  // 设置主题  
  message.setSubject(subject); 
  MimeMultipart multipart = new MimeMultipart("related");

  // first part  (the html)
  BodyPart messageBodyPart = new MimeBodyPart();
  content = content.replaceAll("(.*?)src=\"(.*?)","$1src=\"cid:$2");
  content = content.replaceAll("/NBEmail/images/signature.jpg","image");
  messageBodyPart.setContent(content, "text/html");

  // add it
  multipart.addBodyPart(messageBodyPart);

  // second part (the image)
  messageBodyPart = new MimeBodyPart();
  DataSource fds1 = new FileDataSource
    ("E:\\DevSoft\\tomcat7\\webapps\\NBEmail\\images\\signature.jpg");
  messageBodyPart.setDataHandler(new DataHandler(fds1));
  messageBodyPart.setHeader("Content-ID","<image>");
  multipart.addBodyPart(messageBodyPart);
  // 设置邮件内容  
  content = content.replaceAll("(.*?)src=\"(.*?)","$1src=\"http://112.64.174.34:43887$2");
  String content1=content;
  message.setContent(content1.toString(),"text/html;charset=utf-8");  
  message.setContent(multipart);
  
  message.setSentDate(new Date()); 
  Date x2 = new Date();
  long y2= x2.getTime();
  LOG.warn("对邮件内容设置时间:"+(y2-y1));
  // 发送  
  Transport.send(message); 
  Date x3 = new Date();
  long y3= x3.getTime();
  LOG.warn("邮件链接SMTP时间:"+(y3-y2));
}  */
 /**  
  * 群发邮件  
  * @param recipients收件人们  
  * @param subject 主题  
  * @param content 内容  
  * @throws AddressException  
  * @throws MessagingException  
  */ 
 public   static   final   Pattern   PATTERN   =   Pattern.compile("<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)",   Pattern.CASE_INSENSITIVE   |   Pattern.MULTILINE);  
public void send(List<String> recipients, String subject, String content,String to)  
    throws AddressException, MessagingException {
	 Matcher   matcher   =   PATTERN.matcher(content);   
     List <String>  list   =   new   ArrayList();   
     while   (matcher.find())   {   
         String   group   =   matcher.group(1); 
         String s = "/NBEmail/images/";
		 Boolean index1 = group.toLowerCase().contains(s.toLowerCase());
		 if(index1 !=false){
			 String s1 = "112.64.174.34:43887/NBEmail/images/";
			 Boolean index2 = group.toLowerCase().contains(s1.toLowerCase());
			 if(index2 !=false){}else{
				 if   (group.startsWith("'"))   {
		        	 boolean b =false;
		        	 String myNeed=group.substring(1,   group.indexOf("'",   1)); 
		        	 for(String str:list){
		        	      if(str.contains(myNeed)){
		        	      b=true;     
		        	      }
		        	 }
		        	 if(b==false){
		             list.add(group.substring(1,   group.indexOf("'",   1)));
		        	 }
		         }   else   if   (group.startsWith("\""))   {
		        	 boolean b =false;
		        	 String myNeed=group.substring(1,   group.indexOf("\"",   1)); 
		        	 for(String str:list){
		        	      if(str.contains(myNeed)){
		        	      b=true;     
		        	      }
		        	 }
		        	 if(b==false){
		             list.add(group.substring(1,   group.indexOf("\"",   1)));
		        	 }
		         }   else   { 
		        	 boolean b =false;
		        	 String myNeed=group.split("\\s")[0]; 
		        	 for(String str:list){
		        	      if(str.contains(myNeed)){
		        	      b=true;     
		        	      }
		        	 }
		        	 if(b==false){
		             list.add(group.split("\\s")[0]);
		        	 }
		         }
				 }
				 }
		     }   
     int size = list.size();
     String[] strs = (String[])list.toArray(new String[size]); 
	Date x1 = new Date();
    long y1 = x1.getTime();
    MailDateFormat formatter = new MailDateFormat();
    formatter.setTimeZone(TimeZone.getTimeZone("GMT"));   // always use UTC for outgoing mail
   
  // 创建mime类型邮件  
  final MimeMessage message = new MimeMessage(session);  
  // 设置发信人  
  message.setFrom(new InternetAddress(authenticator.getUsername()));  
  // 设置收件人们  
  final int num = recipients.size();  
  InternetAddress[] addresses = new InternetAddress[num];  
  for (int i = 0; i < num; i++) {  
    addresses[i] = new InternetAddress(recipients.get(i));  
  }  
  message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
  message.setRecipients(Message.RecipientType.CC, addresses);  
  // 设置主题  
  message.setSubject(subject);
  //message.setSentDate(new Date());
  message.setHeader("Date", formatter.format(new Date()));

  // first part  (the html)
  BodyPart messageBodyPart = new MimeBodyPart();
  content = content.replaceAll("(.*?)src=\"(.*?)","$1src=\"cid:$2");
  /*content = content.replaceAll("/NBEmail/images/signature.jpg","image");*/
  for(int i=0;i<strs.length;i++){
	  int total=10000+i;
	  content = content.replaceAll(strs[i],total+"");
	
  }
  messageBodyPart.setContent(content, "text/html;charset=utf-8");
  MimeMultipart multipart = new MimeMultipart("related");
  // add it
  multipart.addBodyPart(messageBodyPart);
 
  for(int i=0;i<size;i++){
	  int total=10000+i;
	  MimeBodyPart mbp=new MimeBodyPart();
	  String img=strs[i].replaceAll("/NBEmail/images/","" );
	//DataSource fds = new FileDataSource("E:\\DevSoft\\tomcat7\\webapps\\NBEmail\\images\\"+img);
		 DataSource fds = new FileDataSource("E:\\webDir\\NBEmail\\images\\"+img);		
		 mbp.setDataHandler(new DataHandler(fds));
	     mbp.setHeader("Content-ID",total+"");
	     multipart.addBodyPart(mbp);
	  
	}
  // 设置邮件内容  
  
  String content1=content;
  message.setContent(content1.toString(),"text/html;charset=utf-8");  
  message.setContent(multipart);
  
   
  Date x2 = new Date();
  long y2= x2.getTime();
  LOG.warn("对邮件内容设置时间:"+(y2-y1));
  // 发送  
  Transport.send(message); 
  Date x3 = new Date();
  long y3= x3.getTime();
  LOG.warn("邮件链接SMTP时间:"+(y3-y2));
}  
 
  

}