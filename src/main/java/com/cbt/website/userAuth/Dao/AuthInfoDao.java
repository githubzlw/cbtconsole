package com.cbt.website.userAuth.Dao;

import com.cbt.website.userAuth.bean.AuthInfo;

public interface AuthInfoDao {

	
	/**
	 * 根据菜单名获取权限对象
	 * @param authName
	 * @return
	 */
	public AuthInfo getAuthInfo(String authName, String url) throws Exception;
	
	
	public void saveAllAuth() throws Exception;
	
}
