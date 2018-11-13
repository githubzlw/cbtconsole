package com.cbt.customer.dao;

import com.cbt.bean.Reply;

import java.util.List;

public interface IReplyDao {
	/**
	 * 方法描述:添加回复内容
	 * author:lizhanjun
	 * date:2015年4月20日
	 * @param rep
	 * @return
	 */
	public int addReply(Reply rep); 
	
	/**
	 * 方法描述:根据留言问题id查询对应的所有回复内容
	 * author: lizhanjun
	 * date:2015年4月20日
	 * @param guestbookId
	 * @return
	 */
	public List<Reply>  findByPid(int guestbookId);
	
	
}
