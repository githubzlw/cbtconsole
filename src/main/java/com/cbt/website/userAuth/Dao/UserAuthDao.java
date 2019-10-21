package com.cbt.website.userAuth.Dao;

import com.cbt.website.userAuth.bean.AuthInfo;

import java.util.List;

public interface UserAuthDao {

	/**
	 * 根据管理员名称查询对应的权限
	 * @param admName
	 * @return
	 */
	public List<AuthInfo> getUserAuth(String admName) throws Exception;
	/**
	 * 根据管理员名称与url查找是否有对应权限
	 * @param admName
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public int getUserAuthCount(String admName, String url) throws Exception;
	
	
	/**
	 * 根据用户名删除用户权限
	 * @param admName
	 * @return
	 * @throws Exception
	 */
	public int deleteUserAuth(String admName) throws Exception;
	
	/**
	 * 添加用户权限
	 * @param auth
	 * @return
	 * @throws Exception
	 */
	public int[] insertUserAuth(String admName, String[] auth) throws Exception;
	List<AuthInfo> getUserAuthPremession(String admName) throws Exception;
}
