package com.cbt.processes.service;

import com.cbt.bean.PostageDiscounts;
import com.cbt.bean.Preferential;
import com.cbt.bean.PreferentialWeb;

import java.util.List;
import java.util.Map;

/**
 * @author ylm
 * 优惠申请接口（批量折扣）
 */
public interface IPreferentialServer {

	/**
	 * 保存申请
	 * 
	 * @param user
	 * 	用户信息
	 */
	public int savePreferential(Preferential preferential, String url);

	/**
	 * 保存申请
	 *
	 * @param user
	 * 	用户信息
	 */
	public int savePreferential2(Preferential preferential, String url);

	/**
	 * 查询优惠申请系统已回复的数量
	 *
	 * @param user
	 * 	用户信息
	 */
	public int getPreferentialNum(int userid);

	/**
	 * 获取申请号
	 *
	 * @param user
	 * 	用户信息
	 */
	public List<PreferentialWeb> getPreferentials(int userid, int state, int page);
	//zlw start
	/**
	 * 获取申请号
	 *
	 * @param user
	 * 	用户信息
	 */
	public List<PreferentialWeb> getDiscounts(int userid);
	//zlw end

	/**
	 * 获取申请号数量
	 *
	 * @param user
	 * 	用户信息
	 */
	public int getPreferentials(int userid, int state);

	/**
	 * 保存批量优惠的交互信息并发送邮件
	 *
	 * @param user
	 * 	优惠申请id，商品ID，用户申请价格
	 */
	public int savePai(int id, int gids, double prices);

	/**
	 * 取消批量优惠申请
	 *
	 * @param
	 *
	 */
	public int delPaprestrain(int pid, String cancel_reason);

	/**
	 * 根据链接中的uid获取申请号
	 *
	 * @param user
	 * 	用户信息
	 */
	public List<PreferentialWeb> getPreferentials(String uid, int userid);

	/**
	 * 添加批量优惠申请数据到购物车
	 *
	 * @param uid
	 * 	批量优惠申请id,用户ID,链接进入||个人中心进入
	 */
	public int addPa_Googs_car(String uid, int userid, int type, String uCurrency, Map<String, Double> maphl);

	/**
	 * ylm
	 * 保存邮费折扣申请
	 *
	 */
	public int savePostageD(PostageDiscounts pd);

	/**
	 * ylm
	 * 将该优惠申请改为已处理,单个产品页面申请批量后立刻转入购物车
	 * （批量折扣）
	 */
	public int upPA(int pid);

	/**
	 * ylm
	 * 查询邮费折扣申请
	 *
	 */
	public PostageDiscounts getPostageD(int userid, String sessionid);
	
	/**
	 * ylm
	 * 修改邮费折扣申请
	 * 
	 */
	public int upPostageD(PostageDiscounts pd);
}
