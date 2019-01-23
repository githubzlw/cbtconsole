package com.cbt.service;

import com.cbt.bean.RechargeRecord;
import com.cbt.refund.bean.AdminUserBean;
import com.cbt.refund.bean.RefundBean;
import com.cbt.refund.bean.RefundBeanExtend;
import com.cbt.website.bean.PaymentBean;

import java.util.List;
import java.util.Map;

public interface RefundSSService {
	/**同意或拒绝退款
	 * @date 2016年10月11日
	 * @author abc
	 * @param uid   用户id
	 * @param appcount 退款金额
	 * @param rid  退款表id
	 * @param admuser  处理人员
	 * @param agreeOrNot  同意还是拒绝  1-同意  2-拒绝
	 * @param appcurrency  货币
	 * @param refuse  拒绝理由
	 * @param type 类型  0-提现  1-paypal  2-投诉  3-邮件投诉
	 * @return  
	 */
	public int agreeRefund(int uid, double appcount, int rid, String admuser,
                           int agreeOrNot, String appcurrency, String refuse, int type);

	/**根据条件查询退款记录
	 * @date 2016年10月11日
	 * @author abc
	 * @param userid  用户id
	 * @param username  用户名称
	 * @param appdate  申请日期
	 * @param agreeTime  处理日期
	 * @param statue  状态(-3-所有退款   0-新申请退款 -2-用户取消  1-银行处理中 2-完结 -1-拒绝)
	 * @param startpage  页码
	 * @param type  来源  0-余额提现  1-paypal申诉  2 -用户投诉  3-邮件
	 * @return
	 */
	public List<RefundBean> searchRefund(int userid, String username, String appdate, String agreeTime,
                                         int statue, int startpage, int type, int rid, String admin);

	/**更新退款信息
	 * @date 2017年3月25日
	 * @author abc
	 * @param id 退款id
	 * @param userid 用户id
	 * @param orerid 订单号
	 * @param paypalName 申诉账号
	 * @return
	 */
	int updateRefund(int id, int userid, String orerid, String paypalName);


	/**查询支付信息
	 * @date 2017年3月25日
	 * @author abc
	 * @param appCount 申诉金额
	 * @return
	 */
	List<PaymentBean> getPayment(double appCount, String curreny, int page);



	/**无匹配用户申诉退款
	 * @date 2017年3月25日
	 * @author abc
	 * @param page 页码
	 * @return
	 */
	List<RefundBean> getRefundList(int page);


	/**反馈
	 * @date 2016年8月26日
	 * @author abc
	 * @param rid
	 * @param feedback
	 * @return
	 */
	public int addFeedback(int rid, String feedback);
	/**备注
	 * @date 2016年10月8日
	 * @author abc
	 * @param account  实际退款金额
	 * @param rid    退款表id
	 * @param remark   备注
	 * @param agreepeople  操作人
	 * @param additionId   余额补偿表id
	 * @return
	 */
	public int addRemark(double account, int rid, String remark, String agreepeople,
                         int additionId, int status, int resontype);
	/**获取申请退款金额
	 * 更新:2016-10-12  申请金额(已完成的、未完成的)按照实际退款金额来计算;
	 * @param userid 用户ID
	 * @return
	 */
	public double getApplyRefund(int userid);
	/**获取申请退款金额paypal
	 * @param userid 用户ID
	 * @return
	 */
	public double getApplyPaypal(int userid);

	/**获取申请退款金额
	 * 更新:2016-10-12  申请金额(已完成的、未完成的)按照实际退款金额来计算;
	 * @param list  用户id列表
	 * @return
	 */
	public Map<String, String> getApplyRefundByUserids(List<Integer> list);

	/**获取申请退款金额
	 * 更新:2016-10-12  申请金额(已完成的、未完成的)按照实际退款金额来计算;
	 * @param list  用户id列表
	 * @return
	 */
	public Map<String, String> getApplyPaypalByUserids(List<Integer> list);


	/**获取已退款金额
	 * 更新:2016-10-12  申请金额(已完成的、未完成的)按照实际退款金额来计算;
	 * @param userid  用户id
	 * @return
	 */
	public double getRefund(int userid);
	/**获取已退款金额
	 * 更新:2016-10-12  申请金额(已完成的、未完成的)按照实际退款金额来计算;
	 * @param list  用户id列表
	 * @return
	 */
	public Map<String, String> getRefundByUserids(List<Integer> list);


	/**修改退款申请表，提出申请时添加数据
	 * (只有两种情况下使用该方法：①用户在paypal上申诉，②用户收到货后投诉)
	 * @date 2016年9月30日
	 * @author abc
	 * @param uid
	 * @param appcount  申请金额
	 * @param currency  货币单位
	 * @param paypalname paypal账号
	 * @param payid   交易号
	 * @param ordeid  订单号
	 * @param type  类型 0-用户提现  1- 用户paypal申诉  2-用户售后投诉  3-用户邮件投诉，后台手动添加
	 * @return
	 */
	public int addRefundFromAppeal(int uid, Double appcount, String paypalname,
                                   String payid, String ordeid, int type);



	/**检查用户是否有投诉退款
	 * @date 2016年10月11日
	 * @author abc
	 * @param useridList  用户id列表
	 * @return
	 */
	public Map<String,String> getComplainRefundByUserids(String useridList);







	/**
	 * 根据退款状态查询退款记录
	 *
	 * @param state
	 * @return
	 */
	public List<RefundBean> searchRefundByState(int state);

	/**
	 * 获取所有后台工作人员
	 * @return
	 */
	public List<AdminUserBean> getAllAdmUser();



	/**完结退款
	 * @date 2016年8月26日
	 * @author abc
	 * @param refundId
	 * @return
	 */
	public int finishRefund(int refundId);

	/**报表获取
	 * @param userid
	 * @return
	 */
	public List<RefundBean> reportGet(String sdate, String edate, String reason, int page);

	/**用户所有退款
	 * @date 2016年8月26日
	 * @author abc
	 * @param userid
	 * @return
	 */
	public List<RefundBeanExtend> searchAllRefundByUid(int userid);



	/**paypal申诉操作余额变更
	 * @date 2016年11月24日
	 * @author abc
	 * @param userid  用户id
	 * @param available 余额（变更值）
	 * @param remark 备注
	 * @param modifyuser
	 * @param usersign 0-余额增加  1-余额减少
	 * @param type 充值类型:1.取消订单，2多余金额，3重复支付，4手动，
	 * 5其他,6消费订单7.余额抵扣订单，8用户申请退款，9取消申请退款
	 * 10用户申请退款，操作员拒绝，回款导致金额变更
	 * @param orderno 订单号
	 * @return
	 */
	public int closeOrderForPaypal(int userid, double available, String orderno);



	/**余额变更记录列表
	 * @date 2017年3月29日
	 * @author abc
	 * @param userid 用户id
	 * @param page  分页
	 * @return
	 */
	List<RechargeRecord> getRecordList(int userid, int page);

	/**
	 * 退款的确认和备注
	 *
	 * @param id
	 *            : id
	 * @param userid
	 *            : 用户id
	 * @param remark
	 *            : 备注
	 * @param account
	 *            : 退款金额
	 * @param status
	 *            : 状态 0-申请退款 1-销售同意退款 2-退款完结 -1-销售驳回退款 -2 -客户取消退款 -3-管理员拒绝退款
	 * @param admName
	 *            : 用户名称
	 * @return
	 */
	public int confirmAndRemark(int id, int userid, String refundOrderNo, String remark, double account, int status, String admName);

	public List<com.cbt.bean.Payment> getRefundOrderNo(String paypalEmail);

	/**
	 * 查询所有支付订单信息
	 * @param userId
	 * @param type 0全部 1最近一个月 2最近两个月 3最近三个月
	 * @return
	 */
	List<PaymentBean> queryPayMentInfoByUserId(int userId, int type);

	/**
	 * 更新退款状态
	 * @param userId
	 * @param refundId
	 * @param state
	 * @return
	 */
	boolean updateRefundState(int userId, int refundId, int state);

	/**
	 * 查询申诉退款的总额
	 * @param userId
	 * @return
	 */
	double queryComplaintTotalAmount(int userId);

}
