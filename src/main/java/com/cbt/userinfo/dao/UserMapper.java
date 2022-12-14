package com.cbt.userinfo.dao;

import com.cbt.pojo.UserEx;
import com.cbt.website.bean.UserInfo;
import com.importExpress.pojo.UserRecommendEmail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
	
	public Map<String, Object> getUserCount(@Param("userID") int userID);

	public List<String> getPaypal(@Param("userID") int userID);

	public UserEx getUserEx(@Param("userID") int userID);
	int checkUserName(@Param("username") String username);
	public List<String> getAllAdmuser(@Param("admName") String admName);

	/**
	 * 查询用户email是否已经存在
	 */
	public String exitEmail(@Param("email") String email);

	/**
	 * 查询用户电话是否已经存在
	 * @param phone
	 * @return
	 */
	public String exitPhone(@Param("phone") String phone, @Param("userid") int userid);

	/**
	 * 修改用户email地址
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upEmail(@Param("password") String password, @Param("email") String email, @Param("uid") int uid);

	/**
	 * 修改电话号码
	 * @param newPhone
	 * @param userid
	 * @param oldPhone
	 * @return
	 */
	public int upPhone(@Param("newPhone") String newPhone, @Param("userid") int userid, @Param("oldPhone") String oldPhone);


	/**
	 * 新增user_ex
	 * @param newPhone
	 * @param userid
	 * @return
	 */
	public int InPhone(@Param("newPhone") String newPhone, @Param("userid") int userid);

	public void saveupemail_log(@Param("oldemail") String oldemail, @Param("newemail") String newemail, @Param("adminid") int adminid);

    List<String> queryUserRemark(@Param("userid") int userid);

    void insertUserRemark(@Param("userid") int userid, @Param("remark") String remark);

    void updateUserRemark(@Param("id") String id);

    String queryFollowMeCodeByUserId(@Param("userId") int userId);

    String queryForUUID();

    int checkFollowMeCode(@Param("followCode") String followCode);

    int updateUserFollowCode(@Param("followCode") String followCode,@Param("userId") int userId);

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
	Map<String,String> queryAdmByUser(@Param("userId")String userId);
}