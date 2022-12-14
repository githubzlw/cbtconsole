package com.cbt.userinfo.service;

import com.cbt.pojo.UserEx;
import org.apache.ibatis.annotations.Param;
import com.cbt.website.bean.UserInfo;
import com.importExpress.pojo.UserRecommendEmail;

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

    Map<String,String> getUserInfoById(@Param("id") int id);

    /**
	 * 查询用户所有信息
	 * @param userId
	 * @return
	 */
	UserInfo queryAllInfoById(int userId);

	/**
	 * 根据客户ID查询所有的推荐邮箱
	 * @param userId
	 * @return
	 */
	List<UserRecommendEmail> queryRecommendEmailInfo(int userId);

    /**
     * 插入发送消息
     * @param userRecommendEmail
     * @return
     */
	int insertIntoUserRecommendEmail(UserRecommendEmail userRecommendEmail);

	/**
     * 分页查询商业会员授权
     * @param userInfo
     * @return
     */
	List<UserInfo> queryBusinessMembershipAuthorization(UserInfo userInfo);

	int queryBusinessMembershipAuthorizationCount(UserInfo userInfo);
}
