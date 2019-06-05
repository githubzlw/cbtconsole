package com.importExpress.pojo;

import lombok.Data;

@Data
public class LoginErrorInfo {
	public final static int ZERO=0;
    public final static int ONE=1;
    /**
     * 登陆失败邮箱
     */
    private String email;
    /**
     * 登陆失败密码
     */
    private String pass;
    /**
     * 登陆失败类型
     */
    private int errorType;
    
    /**
             *登陆时间 
     */
    private String loginTime;
}
