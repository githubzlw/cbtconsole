package com.cbt.bean;


public class BlackList {
	private Integer id;
	/*黑名单email	*/
	private String email;
	/*创建时间	*/
	private String createtime;
	/*操作人id */
	private String operatorid;
	/*黑名单ip 	*/
	private String userip;
	/*黑名单人用户名	*/
	private String username;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	public String getOperatorid() {
		return operatorid;
	}
	
	public void setOperatorid(String operatorid) {
		this.operatorid = operatorid;
	}
	
	public String getUserip() {
		return userip;
	}
	
	public void setUserip(String userip) {
		this.userip = userip;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
}
