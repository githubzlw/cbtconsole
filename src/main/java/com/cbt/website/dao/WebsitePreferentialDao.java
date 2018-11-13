package com.cbt.website.dao;

import com.cbt.bean.PostageDiscounts;
import com.cbt.bean.Preferential;
import com.cbt.bean.PreferentialWeb;

import java.util.List;

/**
 * @author ylm
 * 优惠申请接口（批量折扣）
 */
public interface WebsitePreferentialDao {

	/**
	 * 获取申请
	 * 
	 * @param user
	 * 	用户信息
	 */
	public Preferential getPreferential(Preferential preferential);
	
	/**
	 * 获取申请数量
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int getPreferentialsNumber(int type, int userid);

	public double getPreferentialsPayPrice(int id);

	/**
	 * 获取申请号
	 *
	 * @param user
	 * 	用户信息
	 */
	public List<PreferentialWeb> getPreferentials(int type, int userid, int page, int endpage, String hdate, String cdate, String email, int adminid);


	/**
	 * 修改申请
	 *
	 * @param user
	 * 	用户信息
	 */
	public int upPreferential(Preferential preferential);

	/**
	 * 保存批量优惠的交互信息并发送邮件
	 *
	 * @param user
	 * 	优惠申请id，商品ID，用户申请价格，商品名称，商品原价格
	 */
	public int savePai(List<Object[]> list, String uid, int userid, String sessionid);

	/**
	 * 保存paprestrain表信息
	 *
	 * @param user
	 * 	用户信息
	 */
	public int savePaprestrain(String uid, int userid);

	/**
	 * 取消批量优惠申请
	 *
	 * @param
	 *
	 */
	public int delPaprestrain(int pid, String cancel_reason);

	/**
	 * 修改优惠申请的同意标志
	 *
	 * @param user
	 * 	优惠申请id，商品ID，用户申请价格，商品名称，商品原价格
	 */
	public int upPaconfirm(String[] strings);

	/**
	 * 修改批量优惠申请的失效时间
	 *
	 * @param
	 *
	 */
	public int uptimePainteracted(int pid, String cancel_reason);

	/**
	 * ylm
	 * 查询邮费折扣申请
	 * 用户名，是否处理
	 */
	public List<PostageDiscounts> getPostageD(int userid, int type, int page, int endpage, String email);

	/**
	 * ylm
	 * 获取邮费折扣申请的数量
	 * 邮费折扣申请ID，处理1or删除2，处理人员
	 */
	public int getPostageDNumber(int userid, int type);

	/**
	 * ylm
	 * 修改邮费折扣申请的状态
	 * 邮费折扣申请ID，处理1or删除2，处理人员
	 */
	public int upPostageD(int id, int type, String handleman);

	/**
	 * 给客户修改价格
	 * lyb 2016-8-25
	 */
	public int updatePriceById(double price, int id);

	public int updateGoodsCarPrice(double price, String itemId);

	public int updatePayPrice(String itemId, double payprice);
}
