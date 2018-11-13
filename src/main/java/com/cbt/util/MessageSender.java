package com.cbt.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

/**
 * 邮件发送程序
 * @author haolloyin
 */
public class MessageSender {

    /**
     * 创建Session对象，此时需要配置传输的协议，是否身份认证
     */
    public Session createSession(String protocol) {
        Properties property = new Properties();
        property.setProperty("mail.transport.protocol", protocol);
        property.setProperty("mail.smtp.auth", "true");

        Session session = Session.getInstance(property);

        // 启动JavaMail调试功能，可以返回与SMTP服务器交互的命令信息
        // 可以从控制台中看一下服务器的响应信息
        session.setDebug(true);
        return session;
    }

    /**
     * 传入Session、MimeMessage对象，创建 Transport 对象发送邮件
     */
    public void sendMail(Session session, MimeMessage msg) throws Exception {

        // 设置发件人使用的SMTP服务器、用户名、密码
        String smtpServer = "smtp.sina.com";
        String user = "test_hao@sina.cn";
        String pwd = "123456";

        // 由 Session 对象获得 Transport 对象
        Transport transport = session.getTransport();
        // 发送用户名、密码连接到指定的 smtp 服务器
        transport.connect(smtpServer, user, pwd);

        transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
        transport.close();
    }

    // 测试：发送邮件
    public static void main1(String[] args) throws Exception {
        MessageSender sender = new MessageSender();

        // 指定使用SMTP协议来创建Session对象
        Session session = sender.createSession("smtp");
        // 使用前面文章所完成的邮件创建类获得 Message 对象
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("1946557099@qq.com"));
InternetAddress[] address = {new InternetAddress("1351535753@qq.com")};
msg.setRecipients(Message.RecipientType.TO, address);
msg.setSubject("账号激活邮件");
msg.setSentDate(new Date());
StringBuffer sb=new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
       /* sb.append("<a href=\""++"cbt/RegActivateServlet?email=");
        sb.append("1351535753@qq.com");
        sb.append("&validateCode=");
        sb.append("");
        sb.append("\">http://192.168.0.66/cbtconsole/cbt/RegActivateServlet?email=");
        sb.append("1351535753@qq.com");
        sb.append("&validateCode=");
        sb.append("");
        sb.append("</a>");
msg.setContent(sb.toString() , "text/html;charset=utf-8");
       // MimeMessage mail = new WithAttachmentMessage().createMessage(session);
        Transport.send(msg);*/
    }

    public static int startSendMail(Map<String,List<Map<String, String>>> addressMap){
        int result=1;
        try {
            String userName = "15216850352@163.com";
            String password = "dzucptlcgtfkhlnm";
            String smtp_server = "smtp.163.com";
            String from_mail_address = userName;
            Authenticator auth = new PopupAuthenticator(userName, password);
            Properties mailProps = new Properties();
            mailProps.put("mail.smtp.host", smtp_server);
            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("username", userName);
            mailProps.put("password", password);

            Session mailSession = Session.getDefaultInstance(mailProps, auth);
            mailSession.setDebug(true);
            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from_mail_address));
            for(String key : addressMap.keySet()){
                List<Map<String, String>> addressList=addressMap.get(key);
                Address[] address=new Address[addressList.size()];
                MimeMultipart multi = new MimeMultipart();
                if("hz".equals(key)){
                    for(int i=0; i<addressList.size(); i++){
                        Map<String, String> map=addressList.get(i);
                        String email=map.get("address");
                        address[i]=new InternetAddress(email);
                    }
                    msg.setSubject("www.import-express.com");
                    BodyPart textBodyPart = new MimeBodyPart();
                    textBodyPart.setText("We have received your inquiry and will process it ASAP. \r\n Customer Service, www.Import-Express.com");
                    // textBodyPart.setFileName("37af4739a11fc9d6b311c712.jpg");
                    multi.addBodyPart(textBodyPart);
                }else{
                    BodyPart textBodyPart = new MimeBodyPart();
                    for(int i=0; i<addressList.size(); i++){
                        Map<String, String> map=addressList.get(i);
                        String email=map.get("address");
                        address[i]=new InternetAddress(email);
                        msg.setSubject("www.import-express.com");
                        textBodyPart.setText(map.get("content"));
                    }
                    // textBodyPart.setFileName("37af4739a11fc9d6b311c712.jpg");
                    multi.addBodyPart(textBodyPart);
                }
                msg.setRecipients(Message.RecipientType.TO, address);
                msg.setContent(multi);
                msg.saveChanges();
                Transport.send(msg);
            }
            msg.setSentDate(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            result=0;
            // TODO: handle exception
        }
return result;
    }
    public static void main(String[] args) {
        Map<String,List<Map<String, String>>> addressMap=new HashMap<String,List<Map<String, String>>>();
        Map<String, String> paramsMap=new HashMap<String, String>();
        paramsMap.put("address", "564138495@qq.com");
        List<Map<String, String>> list=new ArrayList<Map<String,String>>();
        list.add(paramsMap);
        addressMap.put("hz", list);
        int result=startSendMail(addressMap);
        System.out.println("-------------result:"+result);
    }
}

class PopupAuthenticator extends Authenticator {
	private String username;
	private String password;

	public PopupAuthenticator(String username, String pwd) {
		this.username = username;
		this.password = pwd;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password);
	}
}
