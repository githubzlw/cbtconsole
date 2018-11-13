package com.importExpress.mapper;

import com.cbt.pojo.UserEx;
import com.cbt.warehouse.pojo.UserCouponBean;
import com.cbt.warehouse.pojo.ZoneBean;
import com.importExpress.pojo.QueAns;
import com.importExpress.pojo.UserBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserNewMapper {
	
	/**
	 * 注册用户
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int regUser(UserBean user);
	
	public int updateReg(@Param("email") String email, @Param("businessName") String businessName, @Param("pass1") String pass1, @Param("id") int id);
	public int updateUserInfo(
            @Param("businessIntroduction") String businessIntroduction,
            @Param("businessName") String businessName,
            @Param("password") String password,
            @Param("userId") int userId);

	/**
	 * 判断是否已绑定过importexpress账号
	 * @param email
	 * @return 数据行数
	 */
	public UserBean isBinding(@Param("email") String email, @Param("types") String types, @Param("name") String name);
	/**
	 * 第三方登录日志记录
	 * @param types
	 * @param email
	 * @return
	 */
	public int addLoginLog(@Param("types") String types, @Param("email") String email, @Param("ip") String ip, @Param("l_id") String l_id, @Param("name") String name);
	/**
	 * 根据国家ID获取国家信息
	 * @Title getZoneInfo
	 * @Description TODO
	 * @param id
	 * @return
	 * @return ZoneBean
	 */
	public ZoneBean getZoneInfo(@Param("id") int id);
	/**
	 * 修改用户
	 *
	 * @param user
	 * 	用户信息
	 */
	public int updateName(@Param("id") String id, @Param("name") String name);

	/**
	 * 修改用户
	 *
	 * @param user
	 * 	用户信息
	 */
	public int upUserCountry(@Param("countryId") int countryId, @Param("id") int id);

	/**
	 * 修改密码
	 *
	 * @param user
	 * 	用户信息
	 */
	public int updatePasswordByEmail(@Param("password") String password, @Param("email") String email);

	public String getPassByEmail(@Param("email") String email);

	/**
	 * 修改密码
	 *
	 * @param user
	 * 	用户信息
	 */
	public int updatePasswordByName(@Param("password") String password, @Param("name") String name);
	/**
	 * 查询用户名是否存在
	 *
	 * @param name
	 * 	用户名称
	 */
	public Integer getIdByName(@Param("name") String name);

	/**
	 * 查询用户名或者邮箱是否存在
	 *
	 * @param name
	 * 	用户名称
	 */
	public int getIdByNameAndEmail(@Param("name") String name, @Param("email") String email);

	/**
	 * 查询用户名或者邮箱是否存在
	 *
	 * @param name
	 * 	用户名称
	 */
	public Integer getIdByNameOrEmail(@Param("name") String name, @Param("email") String email);

	/**
	 * 根据emial查询用户
	 *
	 * @param name
	 * 	用户名称
	 */
	public UserBean getUserByEmail(String email);
	/**
	 * 密码找回验证
	 *
	 * @param name
	 * 	用户名称
	 */
	public UserBean getUserForActivation(String email);

	/**
	 * 根据emial查询用户名称
	 *
	 * @param name
	 * 	用户名称
	 */
	public String getNameByEmail(String email);
	/**
	 * 根据id查询用户信息
	 */
	public UserBean getUserById(int id);

	public UserCouponBean getUserCoupon(int id);

	/**
	 * 查询用户
	 *
	 * @param name，pass
	 * 	用户名称,用户密码
	 */
	public UserBean getUserByNameAndPass(@Param("name") String name, @Param("pass") String pass);

	/**
	 * 通过uuid获取用户信息
	 *
	 * @param name，pass
	 * 	用户名称,用户密码
	 */
	public UserBean getUserByUUID(@Param("uuid") String uuid);
	/**
	 * 修改用户激活状态
	 *
	 * @param name
	 * 	用户名称
	 */
	public boolean upUserState(String email);

	/**
	 * 修改用户激活码
	 *
	 * @param email,activationCode,state
	 * 	邮箱地址，激活码，1=注册激活码，2=找回密码激活码
	 */
	public boolean upUserActivationCode(@Param("email") String email, @Param("activationCode") String activationCode, @Param("state") int state);

	/**
	 * 绑定facebook用户
	 *
	 * @param name
	 * 	用户名称
	 */
	public boolean facebookbound(@Param("userid") int userid, @Param("facebookid") String facebookid);

	/**
	 * facebook登录获取用户信息
	 *
	 * @param name
	 * 	用户名称
	 */
	public UserBean getFacebookUser(String facebookid);

	/**
	 * 修改用户可用余额(加)
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upPrice_re(@Param("userId") int userId, @Param("price") double price, @Param("hasRecharge") int hasRecharge);
	public int upPrice(@Param("userId") int userId, @Param("price") double price);
	/**
	 * 修改用户cashback
	 *
	 */
	public int upCashBack(@Param("userId") int userId);

	/**
	 * 修改用户可用余额(减)
	 *
	 * @param name
	 * 	用户名称
	 */
	public int dePrice(@Param("userId") int userId, @Param("price") double price);

	/**
	 * 修改用户可用余额,剩余运费补贴
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upUserPrice(@Param("userId") int userId, @Param("price") double price, @Param("acprice") double acprice);

	public int upUserCouponStatus(@Param("userId") int userId, @Param("orderNo") String orderNo);

	/**
	 * 获取用户可用余额
	 *
	 * @param name
	 * 	用户名称
	 */
	public Map<String, Double> getUserPrice(int userId);

	/**
	 * 获取用户余额和货币单位
	 *
	 * @param name
	 * 	用户名称
	 */
	public Map<String, Object> getBalance_currency(int id);

	/**
	 * 获取产品单页提问数据
	 * @Title getQuestionData
	 * @Description TODO
	 * @param map
	 * @return
	 * @return List<QueAns>
	 */
	public List<QueAns> getQuestionData(Map<String, String> map);

	/**
	 * 获取用户剩余运费补贴
	 *
	 * @param name
	 * 	用户名称
	 */
	public Double getUserApplicableCredit(int userId);

	/**
	 * 修改用户剩余运费补贴
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upUserApplicableCredit(@Param("userId") int userId, @Param("acprice") double acprice);
	/**
	 * 根据用户id获得用户名
	 *
	 * @param name
	 * 	用户名称
	 */
	public String getNameById(int userId);

	public String getEmailById(int userId);

	public Integer getUserIdByEmail(String email);
	/**
	 * 根据用户邮箱删除用户
	 * @Title deleteUserByEmail
	 * @Description TODO
	 * @param email
	 * @return
	 * @return int
	 */
	public int deleteUserByEmail(@Param("email") String email);
	/**
	 * 修改用户email地址
	 *
	 * @param name
	 * 	用户名称
	 */
	public int upEmail(@Param("password") String password, @Param("email") String email, @Param("uid") int uid);
	/**
	 * @Title: upPass
	 * @Author: cjc
	 * @Despricetion:TODO
	 * @Date: 2018/6/21 10:28
	 * @Param: [password, email, uid, bName]
	 * @Return: int
	 */
	public int upPass(@Param("password") String password, @Param("email") String email, @Param("uid") int uid, @Param("bName") String bName);
	/**
	 * 根据用户id获得货币单位
	 *
	 * @param name
	 * 	用户名称
	 */
	public String getCurrencyById(int userId);

	/**
	 * 取得客户经理信息
	 *
	 * @param name，email
	 * 	用户名称,邮箱
	 */
	public UserBean getUserInfo(int userId);

	/**
	 * 获取销售人员的邮箱地址和密码
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public Map<String, String> getAdminUser(@Param("adminId") int adminId, @Param("email") String email, @Param("userId") int userId);


	/**
	 * 根据用户id获取user对象
	 * @param userId
	 * @return
	 */
	public UserBean getUserFromId(int userId);

	/**
	 *更新扩展用户联系方式
	 */
	public Integer updateUserEx(UserEx record);
	/**
	 * 获取扩展用户联系方式
	 */
	public UserEx getUserEx(int userid);
	/**
	 * 根据userid删除UserEx
	 */
	public int deleteUserEx(int userid);
	/**
	 * 插入UserEx表数据
	 */
	public int insertUserEx(UserEx userEx);


	public String getUserByUserId(int userid);

	//变更用户余额和运费抵扣金额
	public int updateUserAvailable(@Param("userid") int userid, @Param("available") float available, @Param("remark") String remark, @Param("modifyuser") String modifyuser,
                                   @Param("usersign") int usersign, @Param("order_ac") float order_ac, @Param("state") int state);
	//根据用户id修改available和order_ac
	public int UpUserById(@Param("available") float available, @Param("order_ac") float order_ac, @Param("userid") int userid);

	public Map<String, Object> getCountryId(int userId);

	public int upCountryId(@Param("userId") int userId, @Param("countryId") int countryId, @Param("changeexpress") int changeexpress);

	public int upDropShipType(@Param("userId") int userId);

	public String getUserByCategory(@Param("userId") int userId);

	public void saveupemail_log(@Param("oldemail") String oldemail, @Param("newemail") String newemail, @Param("userid") int userid);


	/**获取用户等级
	 * @date 2016年11月9日
	 * @author abc
	 * @param userid 用户id
	 * @return
	 */
	public Map<String,Integer> getGradeUserId(Integer userid);

	public UserBean getPassWordForEmail(@Param("email") String email, @Param("type") String type);

	public int regGoogleUser(UserBean user);

	public int bindGoogle(@Param("name") String name, @Param("google_email") String google_email, @Param("type") String type);

	public UserBean getUserInfos(@Param("email") String email, @Param("name") String name);

	public int addDropShippApplay(@Param("userId") int userId, @Param("webSite") String webSite, @Param("email") String email, @Param("phoneNumber") String phoneNumber, @Param("introduceYs") String introduceYs);

	public String getEmailByIdOrUserCookieId(@Param("userId") int userId, @Param("userCookieId") String userCookieId);
	public String getCountryName(@Param("countryId") String countryId);
	int upUserFirstDiscount(@Param("userId") int userid);
	int upOrderInfoCashback(@Param("userId") int userId, @Param("orderNo") String orderNo);
	int upOrderInfoFirstDiscount(@Param("userId") int userId, @Param("orderNo") String orderNo);
	int saveUserMark(@Param("userid") int userid, @Param("usercookieid") String usercookieid, @Param("sessionId") String sessionId, @Param("usermark") int usermark, @Param("ip") String ip);
	int updateUserMark(@Param("userId") int userId, @Param("sessionId") String sessionId);

	int updateUserMarkCookieid(@Param("usercookieid") String usercookieid);

	int bindUseridSessionid(@Param("usercookieid") String usercookieid, @Param("sessionId") String sessionId, @Param("userId") int userId);
	
	List<Map<String,Object>> getEmailByIdList(@Param("list") List<String> list);
}
