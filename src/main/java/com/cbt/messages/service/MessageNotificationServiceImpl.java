package com.cbt.messages.service;

import com.cbt.messages.dao.MessageNotificationMapper;
import com.cbt.pojo.MessageNotification;
import com.cbt.pojo.MessageNotificationStatistical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MessageNotificationService")
public class MessageNotificationServiceImpl implements MessageNotificationService {

	@Autowired
	private MessageNotificationMapper messageNotificationMapper;

	@Override
	public List<MessageNotification> queryForListByAdminId(Integer adminId, Integer sendType) {
		return messageNotificationMapper.queryForListByAdminId(adminId,sendType);
	}

	@Override
	public List<MessageNotification> queryForListByType(Integer type) {
		return messageNotificationMapper.queryForListByType(type);
	}

	@Override
	public List<MessageNotification> queryForListByIsRead(String isRead) {
		return messageNotificationMapper.queryForListByIsRead(isRead);
	}

	@Override
	public List<MessageNotification> queryForListByOrderNo(String orderNo) {
		return messageNotificationMapper.queryForListByOrderNo(orderNo);
	}

	@Override
	public MessageNotification queryById(Integer id) {
		return messageNotificationMapper.queryById(id);
	}

	@Override
	public void insertMessageNotification(MessageNotification messageNotification) {
		messageNotificationMapper.insertMessageNotification(messageNotification);
	}

	@Override
	public void updateIsReadById(Integer id) {
		messageNotificationMapper.updateIsReadById(id);
	}

	@Override
	public int countUnReaded(Integer type) {

		return messageNotificationMapper.countUnReaded(type);
	}

	@Override
	public List<MessageNotification> getMsgList(Integer type, String isread, String orderno) {

		return messageNotificationMapper.getMsgList(type, isread, orderno);
	}

	@Override
	public void updateApprovedOrder() {

		messageNotificationMapper.updateApprovedOrder();
	}

	@Override
	public List<MessageNotification> queryByRoleType(Integer roleType) {
		return messageNotificationMapper.queryByRoleType(roleType);
	}

	@Override
	public void updateByOrderNoTypeSenderId(String orderNo, int type, int senderId,int message_id) {
		messageNotificationMapper.updateByOrderNoTypeSenderId(orderNo, type, senderId,message_id);
	}

	@Override
	public void updateIsReadByUserId(int userId, int type) {
		messageNotificationMapper.updateIsReadByUserId(userId, type);
	}

	@Override
	public List<MessageNotification> queryForListByLingId() {
		return messageNotificationMapper.queryForListByLingId();
	}

	@Override
	public MessageNotification queryExistsMsgByType(String orderNo, int senderId, int sendType, String remarkUserId,
                                                    String sendContent) {
		return messageNotificationMapper.queryExistsMsgByType(orderNo, senderId, sendType, remarkUserId, sendContent);
	}

	@Override
	public MessageNotification queryExistsSysMsg(int senderId, String remarkUserId, String sendContent) {
		return messageNotificationMapper.queryExistsSysMsg(senderId, remarkUserId, sendContent);
	}

	@Override
	public void deleteBySendIdAndType() {
		messageNotificationMapper.deleteBySendIdAndType();

	}

	@Override
	public int queryAdmuserId(String username) {
		return messageNotificationMapper.queryAdmuserId(username);
	}

	@Override
	public List<MessageNotificationStatistical> queryGroupNumber(int senderId) {
		return messageNotificationMapper.queryGroupNumber(senderId);
	}

}
