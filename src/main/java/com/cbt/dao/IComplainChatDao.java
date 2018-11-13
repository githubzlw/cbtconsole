package com.cbt.dao;

import com.cbt.bean.ComplainChat;
import com.cbt.bean.ComplainFile;

public interface IComplainChatDao{
	
	public Integer addChat(ComplainChat t);
	
	public void add(ComplainFile t);
}
