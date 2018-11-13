package com.cbt.website.dao;

import com.cbt.website.bean.ApplicationSummary;
import com.cbt.website.bean.ConfirmUserInfo;
import com.cbt.website.bean.GradeDiscount;
import com.cbt.website.userAuth.bean.Admuser;

import java.util.List;
import java.util.Map;

public interface UserDao {
	
	public String getUserEmailByUserName(String username, int userId);
	
	public String getUserIdByEmail(String email);
	//统计用户留言数量：日期，userid，username，email，跟进人
	public List<Object[]> getMessage(String date, int userid, String username, String email, int page, int pagesize);
	//统计批量优惠申请
	public List<Object[]> getBatchApplication(String date, int userid, String username, String email, int page, int pagesize);
	//统计邮费折扣申请
	public List<Object[]> getPostageDiscount(String date, int userid, String username, String email, int page, int pagesize);
	//统计business询盘
	public List<Object[]> getBusinessInquiries(String date, int userid, String username, String email, int page, int pagesize);
	//跟进人员
	public List<ConfirmUserInfo> getAll();
	//获取VIP等级
	public List<GradeDiscount> getDiscount();
	//查询所有申请统计表
	public List<ApplicationSummary> getApplication(int userid, String username, String email, String previousDate, String nextDate, int page, int pagesize, int confirmuserid);
	//查询订单的paypal地址
	public Map<String, String> getIpnaddress(String orderid);
	//更新确认人
	public int updateConfirmuser(String confirminfo, int id);
	//更新跟进人
	public int updateAdminuser(int uid, int aid, String users, String email, String userName, String admName);
	/*//变更用户余额
	public int updateUserAvailable(int userid,float available,String remark,String modifyuser,int usersign);*/
	//变更用户余额和运费抵扣金额
	/**
	 * @date 2016年10月25日
	 * @author abc
	 * @param userid 用户id
	 * @param available 金额
	 * @param remark 备注
	 * @param modifyuser 操作人
	 * @param usersign  0-收入，1-支出
	 * @param order_ac  赠送运费
	 * @param state  1.取消订单，2多余金额，3重复支付，4手动，5其他
	 * @return
	 */
	public int updateUserAvailable(int userid, float available, String remark, String remarkId, String modifyuser, int usersign, float order_ac, int state);
	//验证后台管理用户的密码
	public boolean isPassword(int modifyuserid, String password);
	//显示修改余额的备注信息
	public String getAvailableMoneyRemark(int userid);
	/**
	 * 获取销售人员的邮箱地址和密码
	 * ylm
	 * @param userId
	 * 		用户ID,订单状态
	 */
	public String[] getAdminUser(int adminId, String email, int userId);
	
	//获取当前销售人员及其下属
	public List<ConfirmUserInfo> getCurSub(Admuser admuser);
	//根据用户id获取用户名
	public String getCustomerNameByUid(int uid);
	
	/**
	 * 获取roletype类型的用户,注意如果是销售的,要加上ling,所有的测试订单销售都是分配给Ling的
	 * @param roleType : 角色类型
	 * @return
	 */
	public List<ConfirmUserInfo> getAllByRoleType(int roleType);
	
	/**
	 * 查询所有电商运营的账号信息，管理员、销售、采购和仓库
	 * @return
	 */
	public List<ConfirmUserInfo> getAllByOperations();


	/**
	 * 查询所有的后台人员，包含离职的
	 * @return
	 */
	List<ConfirmUserInfo> getAllUserHasOffUser();
}
