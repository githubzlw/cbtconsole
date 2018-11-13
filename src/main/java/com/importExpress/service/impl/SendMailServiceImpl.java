package com.importExpress.service.impl;//package com.cn.hnust.service.impl;

import com.cbt.util.Utility;
import com.importExpress.service.SendMailService;
import com.importExpress.utli.PropertiesUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;


@Service("sendMailServiceImpl")
public class SendMailServiceImpl implements SendMailService {


    private static PropertiesUtils reader = new PropertiesUtils("mail.properties");

    private static String mailAddr = reader.getProperty("send.mail.address");
    private static String bccMailAddr = reader.getProperty("send.mail.bccAddress");
    private static String nickName = reader.getProperty("nick.name");

    private static final Log MAILLOG = LogFactory.getLog("maillog");


    @Resource
    JavaMailSender mailSender;
    @Resource
    SimpleMailMessage simpleMailMessage;


    public void sendSimpleMail(String subject, String content, String toMail) {
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        simpleMailMessage.setTo(toMail);
        simpleMailMessage.setSentDate(new Date());
        mailSender.send(simpleMailMessage);
        MAILLOG.info("邮件发送成功..ToMail:[" + toMail + "],Subject:[" + subject + "]");

    }


    public void sendSimpleMail(String subject, String content, String toMail, String email) {
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        simpleMailMessage.setTo(toMail);
        if (Utility.getStringIsNull(email)) {
            simpleMailMessage.setCc(email);
        }
        simpleMailMessage.setBcc(bccMailAddr);
        simpleMailMessage.setSentDate(new Date());
        mailSender.send(simpleMailMessage);
        MAILLOG.info("邮件发送成功..ToMail:[" + toMail + "],Subject:[" + subject + "]");

    }


    public void sendHtmlMail(String subject, String content, String toMail, String email) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);

        messageHelper.setFrom(mailAddr, nickName);
        messageHelper.setTo(toMail);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        if (Utility.getStringIsNull(email)) {
            simpleMailMessage.setCc(email);
        }
        simpleMailMessage.setBcc(bccMailAddr);
        simpleMailMessage.setSentDate(new Date());
        mailSender.send(mailMessage);
        MAILLOG.info("邮件发送成功..ToMail:[" + toMail + "],Subject:[" + subject + "]");


    }

    public void sendHtmlMail(String subject, String content, String toMail) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
        messageHelper.setFrom(mailAddr, nickName);
        messageHelper.setTo(toMail);
        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);
        simpleMailMessage.setSentDate(new Date());
        mailSender.send(mailMessage);
        MAILLOG.info("邮件发送成功..ToMail:[" + toMail + "],Subject:[" + subject + "]");


    }

    public void sendMailTakeAccessory(String subject, String content,
                                      String toMail, String accessoryPath, String accessoryName) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
        messageHelper.setTo(toMail);
        messageHelper.setSubject(subject);
        messageHelper.setText("<html><head></head><body><h1>" + content + "</h1></body></html>", true);
        FileSystemResource file = new FileSystemResource(new File(accessoryPath));
        messageHelper.addAttachment(accessoryName, file);
        mailSender.send(mailMessage);
        MAILLOG.info("邮件发送成功..ToMail:[" + toMail + "],Subject:[" + subject + "]");


    }

}

