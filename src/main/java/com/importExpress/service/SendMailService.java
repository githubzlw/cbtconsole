package com.importExpress.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface SendMailService {

	/**
	 *
	 * @Description:普通文本发邮件形式(不含抄送)
	 * @param subject 主题
	 * @param content 正文
	 * @param toMail 收件人邮箱
	 * void
	 * @exception:
	 * @author: polo
	 * @time:2018年8月7日
	 */
	public void sendSimpleMail(String subject, String content, String toMail);

	/**
	 * 
	 * @Description:普通文本发邮件形式(含抄送)
	 * @param subject 主题
	 * @param content 正文
	 * @param toMail 收件人邮箱
	 * void
	 * @exception:
	 * @author: polo
	 * @time:2018年8月6日 
     */
	public void sendSimpleMail(String subject, String content, String toMail, String email);

	/**
	 *
	 * @Description:html发邮件形式(含抄送)
	 * @param subject 主题
	 * @param content 正文
	 * @param toMail 收件人邮箱
	 * void
	 * @exception:
	 * @author: polo
	 * @time:2018年8月6日
	 */
	public void sendHtmlMail(String subject, String content, String toMail, String email) throws MessagingException, UnsupportedEncodingException;

	/**
	 * 
	 * @Description:html发邮件形式(不含抄送)
	 * @param subject 主题
	 * @param content 正文
	 * @param toMail 收件人邮箱
	 * void
	 * @exception:
	 * @author: polo
	 * @time:2018年8月6日 
     */
	public void sendHtmlMail(String subject, String content, String toMail) throws MessagingException, UnsupportedEncodingException;
	
	
	
	/**
	 * 
	 * @Description:带附件发邮件形式
	 * @param subject 标题
	 * @param content 正文
	 * @param toMail 收件人邮箱
	 * @param accessoryPath 附件路径
	 * @param accessoryName 附件名
	 * void
	 * @exception:
	 * @author: polo
	 * @time:2018年8月6日 
     */
	public void sendMailTakeAccessory(String subject, String content, String toMail, String accessoryPath, String accessoryName) throws MessagingException;
	
}
