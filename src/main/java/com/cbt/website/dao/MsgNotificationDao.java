package com.cbt.website.dao;

import com.cbt.pojo.MessageNotification;

public interface MsgNotificationDao {

	/**
	 * 查询消息提醒的数据
	 * 
	 * @param messageNotification
	 */
	void insertMessageNotification(MessageNotification messageNotification) throws Exception;
	
	int queryAdmuserId(String username);
	
	int getInfoSendId(String id);
	
	/**
	 * 更新采购订单是否处理标识
	 * @param shipno
	 */
	void updateTbStatus(String shipno);

	/**
	 * 对消息类型是6，id是Eric的数据进行新增
	 */
	void insertBySendIdAndType(int count) throws Exception;

	/**
	 * 对消息类型是6，id是Eric的数据进行删除
	 */
	void deleteBySendIdAndType() throws Exception;

	/**
	 * 根据数据查询非系统消息的数据
	 * 
	 * @param orderid
	 *            ：订单id
	 * @param sendContent
	 *            ：推送内容
	 * @param senderId
	 *            ：被推送人id
	 * @param type
	 *            ：推送类型
	 * @param remarkUserId
	 *            ： 备注人id
	 * @return
	 * @throws Exception
	 */
	MessageNotification queryExistsMsgByType(String orderid, String sendContent, int senderId, int type,
                                             int remarkUserId) throws Exception;

	/**
	 * 根据数据查询系统消息的数据
	 * 
	 * @param orderid
	 *            ：订单id
	 * @param sendContent
	 *            ：推送内容
	 * @param senderId
	 *            ：被推送人id
	 * @param type
	 *            ：推送类型
	 * @param remarkUserId
	 *            ： 备注人id
	 * @return
	 * @throws Exception
	 */
	MessageNotification queryExistsSysMsg(int senderId, String sendContent, int remarkUserId) throws Exception;

}
