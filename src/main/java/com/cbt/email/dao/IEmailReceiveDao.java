package com.cbt.email.dao;

import com.cbt.email.entity.EmailReceive;
import com.cbt.email.entity.EmailReceive1;

import java.sql.Date;
import java.util.List;

public interface IEmailReceiveDao {
	/**
	 * 方法描述:将客户信息保存到数据
	 * author:
	 * date:2017年3月6日
	 * @param file
	 * @return
	 */



	public int add(EmailReceive receive);
	 /**
		 * 方法描述:根据问题id获取客户回复信息
		 * author:
		 * date:2017年3月7日
		 * @param file
		 * @return
		 */
		public List<EmailReceive> getall(EmailReceive er);
		/**
		 * 方法描述:根据问题id获取客户回复信息数
		 * author:
		 * date:2017年3月7日
		 * @param file
		 * @return
		 */
		public int getalltotal(EmailReceive er);
		/**
		 * 方法描述:根据销售id获取客户回复信息
		 * author:
		 * date:2017年3月7日
		 * @param file
		 * @return
		 */

	public List<EmailReceive> getall1(int id, EmailReceive er);
	/**
	 * 方法描述:根据销售id获取客户回复信息数
	 * author:
	 * date:2017年3月7日
	 * @param file
	 * @return
	 */
	public int getalltotal1(int id, EmailReceive er);
	/**
	 * 方法描述:获取客户回复信息
	 * author:
	 * date:2017年3月7日
	 * @param file
	 * @return
	 */
	public List<EmailReceive> getall2(EmailReceive er);
	/**
	 * 方法描述:获取客户回复信息数
	 * author:
	 * date:2017年3月7日
	 * @param file
	 * @return
	 */
	public int getalltotal2(EmailReceive er);
	/**
	 * 方法描述:根据邮件id,获取邮件内容
	 * author:
	 * date:2017年3月7日
	 * @param file
	 * @return
	 */
public EmailReceive getEmail(int id);
/**
 * 方法描述:回复留言问题
 * author:wy
 * date:2017年3月8日
 * @return
 */
	public int reply(int id, String content, String title, Date date);
	/**
	 * 方法描述:将客户信息保存到数据
	 * author:
	 * date:2017年3月8日
	 * @param file
	 * @return
	 */
public int add1(EmailReceive receive);
/**
 * 方法描述:查看项目对应邮件
 * author:
 * date:2017年5月24日
 * @param file
 * @return
 */
	public List<EmailReceive1> getall(String orderNo);
	

}
