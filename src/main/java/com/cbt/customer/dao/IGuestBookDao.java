package com.cbt.customer.dao;

import com.cbt.bean.GuestBookBean;

import java.util.List;

public interface IGuestBookDao {
	/**
	 * 方法描述:添加留言内容
	 * author:lizhanjun
	 * date:2015年4月20日
	 * @param gbb
	 * @return
	 */
	public int addComment(GuestBookBean gbb);
	
	/**
	 * 方法描述:根据商品id和用户id查询所有留言内容
	 * author: lizhanjun
	 * date:2015年4月20日
	 * @param pid
	 * @return
	 */
	public List<GuestBookBean>  findByPid(String pid, int userId, int start, int number);

	/**
	 * 方法描述:根据主键id查询留言信息
	 * author:lizhanjun
	 * date:2015年4月21日
	 * @param id
	 * @return
	 */
	public GuestBookBean findById(int id);

	/**
	 * 方法描述:查看所有用户留言
	 * author:lizhanjun
	 * date:2015年4月24日
	 * @return
	 */
	public List<GuestBookBean> findAll(int userId, String date, int state, String userName, String pname, int start, int end, String useremail,
                                       String TimeFrom, String TimeTo, int adminid, int type);

	/**
	 * 方法描述:统计所有的留言数量
	 * author:lizhanjun
	 * date:2015年4月25日
	 * @return
	 */
	public int total(int userId, String date, int state, String userName, String pname, int start, int end,
                     String TimeFrom, String TimeTo, int adminid, int type);

	/**
	 * 方法描述:回复留言问题
	 * author:lizhanjun
	 * date:2015年4月24日
	 * @return
	 */
	public int reply(int id, String replyContent, String date);
	/**
	 * 回复付款页面反馈问题
	 * @Title replyReport
	 * @Description TODO
	 * @param id
	 * @param replyContent
	 * @param date
	 * @author:whj
	 * date:2018年4月25日
	 * @return int
	 */
	public int replyReport(int id, String replyContent, String date);
	
	/**
	 * 方法描述:查看ID所有留言
	 * author:lizhanjun
	 * date:2015年4月24日
	 * @return
	 */
	public GuestBookBean getGuestBookBean(String gid);
	
	/**
	 * 方法描述:删除留言问题
	 * author:lizhanjun
	 * date:2015年4月24日
	 * @return
	 */
	public int delreply(int id);
}
