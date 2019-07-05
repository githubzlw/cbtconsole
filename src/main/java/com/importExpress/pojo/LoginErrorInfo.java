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
    private String errorType;
    
    /**
     * guid
     */
    private String guid;
    
    /**
     *sessionId 
     */
    private String sessionId;
    
    /**
     *userId 
     */
    private String userId;
    
    /**
             *登陆时间 
     */
    private String loginTime;
    
    private long time;
    
    /**
     * 登录类型
     */
    private String loginType;
}
