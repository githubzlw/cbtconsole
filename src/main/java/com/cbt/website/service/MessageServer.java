package com.cbt.website.service;

import com.cbt.bean.Message;
import com.cbt.website.dao.IMessageDao;
import com.cbt.website.dao.MessageDao;

import java.util.List;

public class MessageServer implements IMessageServer {

	IMessageDao dao = new MessageDao();
	
	@Override
	public int addMessage(int userid, String content,String codid) {
		return dao.addMessage(userid, content, codid);
	}

	@Override
	public int delMessage(int messgeId) {
		return dao.delMessage(messgeId);
	}

	@Override
	public List<Message> getMessage(int userid, int state) {
		return dao.getMessage(userid, state);
	}

	@Override
	public int getMessageSize(int userid) { 
		return dao.getMessageSize(userid);
	}

	@Override
	public int upMessage(int messgeId) {
		return dao.upMessage(messgeId);
	}

	@Override
	public int delMessage(String orderid, String count) {
		return dao.delMessage(orderid, count);
	}

}
