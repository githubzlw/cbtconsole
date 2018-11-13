package com.cbt.processes.service;

import com.cbt.bean.UserBean;

public interface IUserServer {

	/**
	 * 注册用户
	 * 
	 * @param user
	 * 	用户信息
	 */	
	public int regUser(UserBean user);
	
	/**
	 * 发送邮件记录日志
	 * 
	 * @param user
	 * 	用户信息
	 */	
	public void regSendEmail(String email, String name, String pass);

	/**
	 * 重新发送邮件
	 *
	 * @param user
	 * 	用户信息
	 *//*
	public String sendEmail(String email);*/
	/**
	 * 发送找回密码的邮件
	 *
	 * @param user
	 * 	用户信息
	 */
	public String sendEmailfind(String email);
	/**
	 * 查询用户名是否存在
	 *
	 * @param name
	 * 	用户名称
	 */
	public boolean getUserName(String name);

	public boolean getUserEmail(String name);
	/**
	 * 查询用户名或者邮箱是否存在
	 *
	 * @param name
	 * 	用户名称
	 */
	public int getNameEmail(String name, String email);
	/**
	 * 查询用户名和邮箱是否一致
	 *
	 * @param name
	 * 	用户名称
	 */
	public boolean getNameEmial(String name, String email);

	/**
	 * 获取用户
	 *
	 * @param name
	 * 	用户名称
	 */
	public String getUserEmail(String email, String validateCode);

	/**
	 * 修改用户激活状态
	 *
	 * @param name
	 * 	用户名称
	 */
	public String upUserState(String email, String validateCode);

	/**
	 * 用户登录
	 *
	 * @param name
	 * 	用户名称
	 */
	public UserBean login(String name, String pass);

	/**
	 * facebook登录
	 *
	 * @param name
	 * 	用户名称
	 */
	public UserBean loginFacebook(String facebookID, UserBean user);

	/**
	 * 找回密码校验连接中的验证码
	 *
	 * @param email，validateCode
	 * 	邮箱地址，找回密码的验证码
	 */
	public boolean passActivate(String email, String validateCode);

	/**
	 * 修改密码
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upPassword(String password, String emial);

	/**
	 * 修改密码
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upPasswordName(String password, String username);
	/**
	 * 获取用户余额
	 *
	 * @param name
	 * 	用户名称
	 */
	public double[] getBalance(int id);

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

	public int upUserPrice(int userId, double price);

	/**
	 * 修改用户剩余运费补贴和余额
	 * @param name
	 * 	用户名称
	 */
	public int upUserPrice(int userId, double price, double acprice);
	
	/**
	 * 取得客户经理信息
	 * 
	 * @param name，email
	 * 	
	 */
	public UserBean getUserInfo(int userId);
	
}
