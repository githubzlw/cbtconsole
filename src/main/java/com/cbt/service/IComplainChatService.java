package com.cbt.service;


import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;

public interface IComplainChatService{

	public Integer addChat(ComplainChat t);
	
	public void add(ComplainFile t);
}
