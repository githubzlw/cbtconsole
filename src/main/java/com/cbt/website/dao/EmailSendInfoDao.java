package com.cbt.website.dao;

import com.cbt.website.bean.EmailSendInfo;

public interface EmailSendInfoDao {

	/**
	 * 根据订单号,用户id查找邮件发送记录
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	public EmailSendInfo getEmailSendInfo(String orderNo, int userId) throws Exception;

	/**
	 * 根据订单号,用户id判断是否存在该邮件发送记录
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	public Boolean isExistEmailSendInfo(String orderNo, int userId, Integer type) throws Exception;
	
	/**
	 * 添加邮件发送记录
	 * @param emailsendinfo
	 * @return
	 */
	public int addEmailSendInfo(EmailSendInfo emailsendinfo) throws Exception;
	
}
