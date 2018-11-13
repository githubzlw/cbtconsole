package com.cbt.customer.service;

import com.cbt.bean.Reply;
import com.cbt.customer.dao.IReplyDao;
import com.cbt.customer.dao.ReplyDaoImpl;

import java.util.List;

public class ReplyService implements IReplyService {

	IReplyDao dao = new ReplyDaoImpl();
	@Override
	public int addReply(Reply rep) {
		return dao.addReply(rep);
	}

	@Override
	public List<Reply> findByGBid(int guestbookId) {
		return dao.findByPid(guestbookId);
	}

}
