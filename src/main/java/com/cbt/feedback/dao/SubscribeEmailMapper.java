package com.cbt.feedback.dao;

import com.cbt.feedback.bean.SubscribeEmail;

import java.util.List;


public interface SubscribeEmailMapper {

	/**
	 * 查询所有的订阅邮件数据
	 * 
	 * @return
	 */
	public List<SubscribeEmail> queryForList();

	/**
	 * 插入订阅邮件数据
	 * 
	 * @param subscribeEmail
	 */
	public void insertSubscribeEmail(SubscribeEmail subscribeEmail);

	/**
	 * 修改订阅邮件数据
	 * 
	 * @param subscribeEmail
	 */
	public void updateSubscribeEmail(SubscribeEmail subscribeEmail);

	/**
	 * 根据id删除订阅邮件数据
	 * 
	 * @param id
	 */
	public void deleteSubscribeEmail(int id);

}
