package com.cbt.processes.dao;

import com.cbt.bean.UserBean;

public interface IUserDao {
	
	/**
	 * 注册用户
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int regUser(UserBean user);
	
	/**
	 * 修改用户
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int upUser(UserBean user);
	
	/**
	 * 修改密码
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int upPassword(String password, String email);
	
	/**
	 * 修改密码
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int upPasswordName(String password, String name);
	/**
	 * 查询用户名是否存在
	 * 
	 * @param name
	 * 	用户名称
	 */
	public int getUserName(String name);
	
	/**
	 * 查询用户名和邮箱是否一致
	 * 
	 * @param name
	 * 	用户名称
	 */
	public boolean getNameEmial(String name, String email);
	
	/**
	 * 查询用户名或者邮箱是否存在
	 * 
	 * @param name
	 * 	用户名称
	 */
	public int getNameEmail(String name, String email);
	
	/**
	 * 根据emial查询用户
	 * 
	 * @param name
	 * 	用户名称
	 */
	public UserBean getUserEmail(String email);
	
	/**
	 * 根据emial查询用户名称
	 * 
	 * @param name
	 * 	用户名称
	 */
	public String getUserEmailName(String email);
	/**
	 * 根据emial查询用户名称
	 * 
	 * @param name
	 * 	用户名称
	 */
	public UserBean getUserEmailId(int userid);
	
	/**
	 * 查询用户
	 * 
	 * @param name，pass
	 * 	用户名称,用户密码
	 */
	public UserBean getUser(String name, String pass);
	/**
	 * 修改用户激活状态
	 *
	 * @param name
	 * 	用户名称
	 */
	public boolean upUserState(String email);

	/**
	 * 修改用户激活码
	 *
	 * @param email,activationCode,state
	 * 	邮箱地址，激活码，1=注册激活码，2=找回密码激活码
	 */
	public boolean upUserActivationCode(String email, String activationCode, int state);

	/**
	 * 绑定facebook用户
	 *
	 * @param name
	 * 	用户名称
	 */
	public boolean facebookbound(int userid, String facebookid);

	/**
	 * facebook登录获取用户信息
	 *
	 * @param name
	 * 	用户名称
	 */
	public UserBean getFacebookUser(String facebookid);

	/**
	 * 修改用户可用余额
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upUserPrice(int userId, double price);

	/**
	 * 修改用户可用余额,剩余运费补贴
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upUserPrice(int userId, double price, double acprice);

	/**
	 * 获取用户可用余额
	 *
	 * @param name
	 * 	用户名称
	 */
	public double[] getUserPrice(int userId);

	/**
	 * 获取用户余额和货币单位
	 *
	 * @param name
	 * 	用户名称
	 */
	public String[] getBalance_currency(int id);

	/**
	 * 获取用户剩余运费补贴
	 *
	 * @param name
	 * 	用户名称
	 */
	public double getUserApplicableCredit(int userId);

	/**
	 * 修改用户剩余运费补贴
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upUserApplicableCredit(int userId, double acprice);
	/**
	 * 根据用户id获得用户名
	 *
	 * @param name
	 * 	用户名称
	 */
	public String getUsernameByid(int userId);

	/**
	 * 根据用户id获得货币单位
	 *
	 * @param name
	 * 	用户名称
	 *//*
	public String getUserCurrency(int userId);*/

	/**
	 * 取得客户经理信息
	 *
	 * @param name，email
	 * 	用户名称,邮箱
	 */
	public UserBean getUserInfo(int userId);

	/**
	 * 获取销售人员的邮箱地址和密码
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public String[] getAdminUser(int adminId, String email, int userId);
	
	
	/**
	 * 根据用户id获取user对象
	 * @param userId
	 * @return
	 */
	public UserBean getUserFromId(int userId);
	/**
	 * 根据用户id获取user对象
	 * @param userId
	 * @return
	 */
	public UserBean getUserFromIdForCheck(int userId);
}