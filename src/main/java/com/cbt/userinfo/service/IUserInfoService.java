package com.cbt.userinfo.service;

import com.cbt.pojo.UserEx;

import java.util.List;
import java.util.Map;

public interface IUserInfoService {

	
	Map<String, Object> getUserCount(int userid);
	
	List<String> getPaypal(int userID);

	int checkUserName(String username);
	
	UserEx getUserEx(int userID);

	public List<String> getAllAdmuser(String admName);
 	/**
	 * 查询用户email是否已经存在
	 */
	String exitEmail(String email);
	
	/**
	 * 查询用户电话是否已经存在
	 * @param phone
	 * @return
	 */
	String exitPhone(String phone, int userid);

	/**
	 * 修改email地址
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upEmail(String email, int uid, String oldemail, int adminid);

	/**
	 * 修改电话号码
	 * @param newPhone
	 * @param userid
	 * @param oldPhone
	 * @param admuserid
	 * @return
	 */
	public int upPhone(String newPhone, int userid, String oldPhone, int admuserid);

	/**
	 * 新增user_ex
	 * @param newPhone
	 * @param userid
	 * @return
	 */
	public int InPhone(String newPhone, int userid);

    List<String> queryUserRemark(int userid);

    void insertUserRemark(int userid, String remark);

    void updateUserRemark(String id);


    String queryFollowMeCodeByUserId(int userId);

    String queryForUUID();

    int checkFollowMeCode(String followCode);

    int updateUserFollowCode(String followCode,int userId);
}
