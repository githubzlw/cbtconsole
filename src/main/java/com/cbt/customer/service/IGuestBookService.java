package com.cbt.customer.service;

import com.cbt.bean.GuestBookBean;
import com.cbt.bean.OrderBean;

import java.util.List;

public interface IGuestBookService {
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
	 * 订单出运时给客户发送邮件
	 * @return
	 */
	public int SendEmailForBatck(OrderBean ob);

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
	public List<GuestBookBean> findAll(int userId, String date,
                                       int state, String userName, String pname, int start, int end,
                                       String useremail, String timeFrom, String timeTo, int adminid, int type);

	/**
	 * 方法描述:回复留言问题
	 * author:lizhanjun
	 * date:2015年4月24日
	 * @return
	 */
	public int reply(int id, String replyContent, String date, String name, String qustion, String pname, String email, int userId, String purl, String sale_email, String picPath);

	/**
	 *
	 * @Title 回复用户反馈问题
	 * @Description 2018-04-24
	 * @param replyContent  回复内容
	 * @param date  日期
	 * @param name  用户名称
	 * @param qustion  客户问题
	 * @param email  客户邮箱
	 * @param userId  用户ID
	 * @param sale_email  发送人邮箱（客户对应的销售人邮箱）
	 * @return
	 * @return int  是否发送成功  0 失败  1成功
	 * @author whj
	 */
	public int replyReport(int id, String replyContent, String date, String name, String qustion, String email, int userId, String sale_email);

	/**
	 *
	 * @Title 回复产品单页用户提问问题
	 * @Description 2018-04-24
	 * @param replyContent  回复内容
	 * @param date  日期
	 * @param name  用户名称
	 * @param qustion  客户问题
	 * @param email  客户邮箱
	 * @param userId  用户ID
	 * @param sale_email  发送人邮箱（客户对应的销售人邮箱）
	 * @return
	 * @return int  是否发送成功  0 失败  1成功
	 * @author whj
	 */
	public int replyReportQes(int id, String replyContent, String date, String name, String qustion, String email, int userId, String sale_email, String url);

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

	/**
	 * 方法描述:统计所有的留言数量
	 * author:lizhanjun
	 * date:2015年4月25日
	 * @return
	 */
	public int total(int userId, String date, int state, String userName, String pname, int start,
                     int end, String timeFrom, String timeTo, int adminid, int qtype);
}
