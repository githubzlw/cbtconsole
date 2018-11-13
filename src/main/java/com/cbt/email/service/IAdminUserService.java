package com.cbt.email.service;

public interface IAdminUserService {

	/**
	 * 方法描述:根据客户邮箱查看销售邮箱
	 * author:
	 * date:2016年7月5日
	 * @param file
	 * @return
	 */



	public String getemail(String email);
	/**
	 * 方法描述:根据客户邮箱获取客户id
	 * author:
	 * date:2016年8月16日
	 * @param file
	 * @return
	 */

	public int getId(String email);
	/**
	 * 方法描述:根据客户邮箱获取销售名字
	 * author:
	 * date:2016年8月16日
	 * @param file
	 * @return
	 */
	public String getname(String email);
	/**
	 * 方法描述:根据客户id获取销售id
	 * author:
	 * date:2017年3月6日
	 * @param file
	 * @return
	 */
	public int getid(int id);

}
