package com.cbt.bean;


public class BlackList {
	private Integer id;


	/*黑名单email	*/
	private String blackVlue;
	/*创建时间	*/
	private String createtime;
	/*操作人id */
	private String operatorid;
	/*黑名单ip 	*/
	private String type;
	/*黑名单人用户名	*/
	private String username;

	private String updateTime;
	private String flag;
	private String option;
	public String getBlackVlue() {
		return blackVlue;
	}

	public void setBlackVlue(String blackVlue) {
		this.blackVlue = blackVlue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
}
