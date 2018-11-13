package com.cbt.util;


/**
 * @author Administrator
 *返回码
 */
public class ResCode {

	public static final int VALIDATECODE = 1001;//激活码不正确
	
	public static final int YZCODE = 1008;//验证码不正确
	
	public static final int VALIDATECODE_PVERDUE = 1002;//激活码已过期
	
	public static final int EMAIL_EXIST = 1003;//邮箱已激活，请登录！
	
	public static final int EMAIL_NOEXIST = 1004;//该邮箱未注册（邮箱地址不存在）！
	
	public static final int REG_FAIL = 1005;//用户注册失败
	
	public static final int EMAIL_SDENDFAIL = 1006;//邮件发送失败
	
	public static final int EMAIL_TIMEOUT = 1007;//激活码不正确
}
