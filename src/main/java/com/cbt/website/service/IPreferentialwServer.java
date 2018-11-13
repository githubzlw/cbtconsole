package com.cbt.website.service;

import com.cbt.bean.PostageDiscounts;
import com.cbt.bean.PreferentialWeb;

import java.util.List;

public interface IPreferentialwServer {

	/**
	 * 获取申请号
	 * 
	 * @param user
	 * 	用户信息
	 */
	public List<PreferentialWeb> getPreferentials(int type, int userid, int page, String hdate, String cdate, String email, int adminid);

	/**
	 * 获取申请数量
	 *
	 * @param user
	 * 	用户信息
	 */
	public int getPreferentialsNumber(int type, int userid);

	public double getPreferentialsPayPrice(int id);

	/**
	 * 保存批量优惠的交互信息并发送邮件
	 *
	 * @param user
	 * 	优惠申请id，商品ID，用户申请价格，商品名称，商品原价格
	 */
	public int savePai(String ids, String gids, String prices, String title, String sprices, String number, int userid, String emial, int confirm,
                       String eend, String sessionid, String username, String content, String currency, String imgs, String questions);

	/**
	 * 取消批量优惠申请
	 *
	 * @param
	 *
	 */
	public int delPaprestrain(int pid, String cancel_reason);

	/**
	 * 修改批量优惠申请失效时间
	 *
	 * @param
	 *
	 */
	public int uptimePainteracted(int id, String cancel_reason);

	/**
	 * ylm
	 * 查询邮费折扣申请
	 * 用户名，是否处理
	 */
	public List<PostageDiscounts> getPostageD(int userid, int type, int page, String email);

	/**
	 * ylm
	 * 查询邮费折扣申请
	 * 用户名，是否处理
	 */
	public int getPostageDNumber(int userid, int type);

	/**
	 * ylm
	 * 修改邮费折扣申请的状态
	 * 邮费折扣申请ID，处理1or删除2，处理人员
	 */
	public int upPostageD(int id, int type, String handleman);

	public int updatePriceById(double price, int id);

	//更改优惠购物车价格
	public int updateGoodsCarPrice(double price, String itemId);

	public int updatePayPrice(String itemId, double payprice);
	
}
