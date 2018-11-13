package com.cbt.website.dao;

import com.cbt.bean.Message;

import java.util.List;

/**
 * @author ylm
 *	消息管理
 */
public interface IMessageDao {
	
	/**
	 * 添加用户消息
	 * 
	 * @param userid,content
	 * 		用户ID,消息内容
	 */
	public int addMessage(int userid, String content, String codid);

	/**
	 * 删除用户消息
	 *
	 * @param messgeId
	 * 		消息ID
	 */
	public int delMessage(int messgeId);


	/**
	 * 删除该订单消息栏中含有确认价格信息的数据
	 *
	 * @param messgeId
	 * 		消息ID
	 */
	public int delMessage(String orderid, String count);

	/**
	 * 修改成已读消息
	 *
	 * @param messgeId
	 * 		消息ID
	 */
	public int upMessage(int messgeId);

	/**
	 * 获取用户消息
	 *
	 * @param userid,state
	 * 		用户ID,读取状态
	 */
	public List<Message> getMessage(int userid, int state);
	

	/**
	 * 获取用户未读消息数量
	 * 
	 * @param userid,state
	 * 		用户ID,读取状态
	 */
	public int getMessageSize(int userid);
}
