package com.importExpress.utli;

import com.cbt.util.Utility;
import com.importExpress.pojo.EmailStatisticsLog;
import com.importExpress.pojo.IdGen;
import com.importExpress.service.EmailStatisticsLogService;
import com.importExpress.service.OrderSplitService;
import com.importExpress.service.SendMailService;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SendEmailNew {

    private final static org.slf4j.Logger MAILLOG = LoggerFactory.getLogger(SendEmailNew.class);


    /*private static OrderSplitService orderSplitService = SpringContextUtil.getBean("orderSplitServiceImpl", OrderSplitService.class);
    private static EmailStatisticsLogService emailStatisticsLogService = SpringContextUtil.getBean("emailStatisticsLogServiceImpl", EmailStatisticsLogService.class);
    private static SendMailService sendMailService = SpringContextUtil.getBean("sendMailServiceImpl", SendMailService.class);*/

    @Autowired
    private OrderSplitService orderSplitService;

    @Autowired
    private EmailStatisticsLogService emailStatisticsLogService;

    @Autowired
    private SendMailService sendMailService;

    //发件邮箱、抄送邮箱
    private static PropertiesUtils reader = new PropertiesUtils("mail.properties");
    private static String mailAddr = reader.getProperty("send.mail.address");
    private static String bccMailAddr = reader.getProperty("send.mail.bccAddress");

    private int index = 0;
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(SendEmailNew.class);
    public static final String HOST = "smtp.emailsrvr.com";
    public static final String imap_HOST = "pop.emailsrvr.com";
    public static final String PROTOCOL = "smtp";
    public static final int PORT = 25;
    //    public static final int PORT = 465;
    public static final int imap_PORT = 110;
    public static final String FROM = "service@import-express.com";//发件人的email
    public static final String PWD = "CustomerFirst1248";//发件人密码
    public static final String TIMEOUT = "80000";
    public static final String CONNECTIONTIMEOUT = "80000";
    private final static String IS_ENABLED_DEBUG_MOD = "false";
//    private final static String IS_ENABLED_SSL = "true";

    //当发送3次失败后换邮箱地址发送
    public static final String POP = "mail.made-china.org";
    public static final String SMTP = "mail.made-china.org";
    public static final String FROMTWO = "noreply@made-china.org";//发件人的email
    public static final String PWDTWO = "qazwEx902eQ";//发件人密码

    // 初始化连接邮件服务器的会话信息 
    private static Properties props = null;

    static {
        props = new Properties();
        props.put("mail.smtp.host", HOST);// 设置服务器地址
        // props.put("mail.transport.protocol", PROTOCOL);//设置协议
        props.put("mail.store.protocol", PROTOCOL);// 设置协议
        props.put("mail.smtp.port", PORT);// 设置端口
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.timeout", TIMEOUT);
        props.put("mail.smtp.connectiontimeou", CONNECTIONTIMEOUT);
        // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
        props.put("mail.debug", IS_ENABLED_DEBUG_MOD);
//        props.put("mail.smtp.ssl.enable", IS_ENABLED_SSL);

    }

    /**
     * 获取Session
     *
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

    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Async
    public void send(String email, String pwd, String toEmail, String content, String title, String orderNo, int number) {

        if (!checkEmail(toEmail)) {

            MAILLOG.info(String.format("邮箱地址非法： ToMail:%s", toEmail));

            //StringUtils.containsIgnoreCase(toEmail, "@qq")
        } else if (StringUtils.containsIgnoreCase(toEmail, "@163")
                || StringUtils.containsIgnoreCase(toEmail, "@sina")
                || StringUtils.containsIgnoreCase(toEmail, "@sohu")) {

            MAILLOG.info(String.format("QQ，163，sina,sohu邮箱忽略发送 ToMail:[%s],title:[%s]", toEmail, title));
        } else {

            //通过mailgun发送邮件
            // int result = MailUtil.sendMailByMailgun(title, content, toEmail, null, FROM, null, 3);
            /*if (result == 0) {
                //邮件发送成功
                MAILLOG.info(String.format("发送邮件成功 ToMail:[%s],title:[%s]", toEmail, title));
                insertEmailStatisticsLog(MailUtil.MAIL_GUN_ADDRESS, email, toEmail, content, title, orderNo, 1, null, "1");
            } else {

            }*/
            //邮件发送失败
            try {
                sendMailService.sendHtmlMail(title, content, toEmail);
            } catch (Exception e) {
                MAILLOG.error(String.format("发送邮件失败 ToMail:[%s],title:[%s]", toEmail, title), e);
                insertEmailStatisticsLog(mailAddr, email, toEmail, content, title, orderNo, 2, e.getMessage(), "1");
                orderSplitService.addMessage_error(title, "0", title + ",orderNo:" + orderNo + ",email:" + toEmail);
                //用第三种方法发送邮件
                sendByLast(email, pwd, toEmail, content, title, orderNo, 1);
            }
        }
    }

    private void insertEmailStatisticsLog(String strMailAddr, String email, String toEmail, String content, String title, String orderNo, int type, String errMsg, String strEmailType) {

        String emailStatisticsLogId = IdGen.uuid();

        EmailStatisticsLog emailStatisticsLog = new EmailStatisticsLog();
        emailStatisticsLog.setEmailStatisticsLogId(emailStatisticsLogId);
        emailStatisticsLog.setSendEmail(strMailAddr);
        emailStatisticsLog.setReceiveEmail(toEmail);
        emailStatisticsLog.setEmailType(strEmailType);
        emailStatisticsLog.setTitle(title);
        emailStatisticsLog.setOrderNo(orderNo);
//            emailStatisticsLog.setContent(content);
        emailStatisticsLog.setContent(content == null || content.length() <= 60000 ? content : content.substring(0, 60000) + "...");
        if (type == 1) {
            //邮件发送成功
            emailStatisticsLog.setSendStatus("发送成功");
        } else {
            //邮件发送失败
            emailStatisticsLog.setSendStatus("发送失败");
            if (StringUtils.length(errMsg) > 500) {
                emailStatisticsLog.setErrorLog(StringUtils.left(errMsg, 500) + "...");
            } else {
                emailStatisticsLog.setErrorLog(errMsg);
            }
        }
        emailStatisticsLog.setDenseEmail(bccMailAddr);
        emailStatisticsLog.setCopyEmail(email);
        emailStatisticsLog.setCreateDate(new Date());
        emailStatisticsLogService.addEmailStatisticsLog(emailStatisticsLog);
    }

    //邮件头尾信息
    public String SetContent(String name, StringBuffer content) {
        StringBuilder sb = new StringBuilder(2000);
        sb.append("<div style='font-size: 14px;'>");
        //sb.append("<a href='" + StringUtil.readProperties("appConfig.path") + "'><img style='cursor: pointer' src='" + StringUtil.readProperties("appConfig.path") + "img/logo.png' ></img></a>");
        sb.append(" <div style='font-weight: bolder;margin-bottom: 10px;'>Dear " + name + ",</div><br><div style='font-size: 13px;'>");
        sb.append(content);
        sb.append("<br><div style='font-weight: bolder;'>Best regards,</div><div style='font-weight: bolder;margin-bottom: 10px;'>Import-Express.com</div>");
//        sb.append("</div>");
        sb.append("<div style='border: 2px solid;background-color: #A5A5A5;padding-left: 10px;'><div style='margin-bottom: 10px;font-weight: bolder;'>PLEASE NOTE:</div>");
        sb.append("<div style='margin-bottom: 10px;font-size: 13px;'>");
        //sb.append("This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style='color: #0070C0' href='" + StringUtil.readProperties("appConfig.path") + "individual/getCenter'>here</a>.");
        sb.append("</div></div></div>");
        return sb.toString();
    }


    private void sendByLast(String email, String pwd, String toEmail, String content, String title, String orderNo, int number) {
        Session session = getSession("lingzheng@import-express.com", "LZ@impA7B8");
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(FROMTWO));
            InternetAddress[] address = {new InternetAddress(toEmail)};
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setContent(content, "text/html;charset=utf-8");
            if (Utility.getStringIsNull(email)) {
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(email)); // 抄送人
            }
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccMailAddr)); // 密送人
            Transport.send(msg);

            insertEmailStatisticsLog(FROMTWO, email, toEmail, content, title, orderNo, 1, null, "2");
        } catch (Exception e) {
            MAILLOG.error(String.format("发送邮件失败 ToMail:[%s],title:[%s]", toEmail, title), e);
            insertEmailStatisticsLog(FROMTWO, email, toEmail, content, title, orderNo, 2, e.getMessage(), "2");
            orderSplitService.addMessage_error(title, e.getMessage(), title + ",orderNo:" + orderNo + ",email:" + toEmail);
        }
    }
}
