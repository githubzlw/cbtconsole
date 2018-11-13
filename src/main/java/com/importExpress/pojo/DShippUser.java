package com.importExpress.pojo;

import com.cbt.bean.UserBean;

import java.util.List;

public class DShippUser {
    private Integer id;
    
    private UserBean userBean;
    
    private List<DropShippApply> dropShippApplys;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public List<DropShippApply> getDropShippApplys() {
		return dropShippApplys;
	}

	public void setDropShippApplys(List<DropShippApply> dropShippApplys) {
		this.dropShippApplys = dropShippApplys;
	}

   
}