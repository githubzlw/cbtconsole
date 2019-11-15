package com.importExpress.mail;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author luohao
 * @date 2018/10/23
 * https://docs.aws.amazon.com/zh_cn/ses/latest/DeveloperGuide/send-using-smtp-java.html
 */
public class SendMailByAmazon implements SendMail {

    private final static Logger logger = LoggerFactory.getLogger("maillog");

    private static final String FROM = "service@importexpress.com";
    private static final String FROMNAME = "Import-Express.com";
    private static final String FROM2 = "service@chinawholesaleinc.com";
    private static final String FROMNAME2 = "ChinaWholesaleInc.com";
    private static final String HOST = "email-smtp.us-west-2.amazonaws.com";
    private static final String SMTP_USERNAME = "AKIAIO7TWKGGFXB5WY2A";
    private static final String SMTP_PASSWORD = "AuYzbo9jZAUkWX35u5mwPdFeUJVdKI6K2sqTHCXZyiK6";
    private static final int PORT = 587;

    private Integer siteType;
    public SendMailByAmazon(Integer siteType) {
        this.siteType = siteType;
    }

    public static void main(String[] args) {
        try {
            final String BODY = String.join(
                    System.getProperty("line.separator"),
                    "<h1>Amazon SES SMTP Email Test</h1>",
                    "<p>This email was sent with Amazon SES using the ",
                    "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
                    " for <a href='https://www.java.com'>Java</a>."
            );
            new SendMailByAmazon(2).sendMail("luohao518@163.com", "", "Amazon SES test (SMTP interface accessed using Java)", BODY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMail(String TO, String BCC, String SUBJECT, String BODY) throws Exception {

        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);

        MimeMessage msg = new MimeMessage(session);
        if (siteType > 1) {
            msg.setFrom(new InternetAddress(FROM2, FROMNAME2));
        } else {
            msg.setFrom(new InternetAddress(FROM, FROMNAME));
        }
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(TO));
        if (StringUtils.isNotBlank(BCC)) {
            msg.setRecipient(Message.RecipientType.BCC, new InternetAddress(BCC));
        }
        msg.setSubject(SUBJECT);
        msg.setContent(BODY, "text/html");
        Transport transport = null;
        try {
            transport = session.getTransport();
            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            logger.info("邮件发送成功..ToMail:["+TO+"],Subject:["+SUBJECT+"]");
        } finally {
            transport.close();
        }
    }
}
