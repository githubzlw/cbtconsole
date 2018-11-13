package com.cbt.service.impl;


import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;
import com.cbt.dao.IComplainChatDao;
import com.cbt.service.IComplainChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplainChatServiceImpl implements IComplainChatService{

	@Autowired
	private IComplainChatDao complainChatDao;

	@Override
	public Integer addChat(ComplainChat t) {
		return complainChatDao.addChat(t);
	}
	
	@Override
	public void add(ComplainFile t) {
		complainChatDao.add(t);
	}

}
