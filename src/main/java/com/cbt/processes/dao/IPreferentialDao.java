package com.cbt.processes.dao;

import com.cbt.bean.*;

import java.util.List;

/**
 * @author ylm
 * 优惠申请接口
 */
public interface IPreferentialDao {
	
	/**
	 * 查询是否待审批
	 * （批量折扣）
	 * @param user
	 * 	用户信息
	 */
	public int getPreferential(int userid, String gid);
	
	/**
	 * 保存申请
	 * （批量折扣）
	 * @param user
	 * 	用户信息
	 */
	public int savePreferential(Preferential preferential, String url);

	/**
	 * 查询优惠申请系统已回复的数量
	 * （批量折扣）
	 * @param user
	 * 	用户信息
	 */
	public int getPreferentialNum(int userid);

	/**
	 * 获取申请号
	 * （批量折扣）
	 * @param user
	 * 	用户信息
	 */
	public List<PreferentialWeb> getPreferentials(int userid, int state, int startpage, int page);

	/**
	 * 获取申请号
	 * （批量折扣）
	 * @param user
	 * 	用户信息
	 */
	public List<PreferentialWeb> getDiscounts(int userid);


	/**
	 * 获取申请号数量
	 * （批量折扣）
	 * @param user
	 * 	用户信息
	 */
	public int getPreferentials(int userid, int state);

	/**
	 * 保存批量优惠的交互信息并发送邮件
	 * （批量折扣）
	 * @param user
	 * 	优惠申请id，商品ID，用户申请价格
	 */
	public int savePai(int id, int gids, double prices);

	/**
	 * 取消批量优惠申请
	 * （批量折扣）
	 * @param
	 *
	 */
	public int delPaprestrain(int pid, String cancel_reason);

	/**
	 * 根据链接中的uid获取申请号
	 * （批量折扣）
	 * @param user
	 * 	用户信息
	 */
	public List<PreferentialWeb> getPreferentials(String uid);

	/**（批量折扣）
	 * ylm
	 * 查询优惠申请对应的商品信息
	 * 优惠申请ids
	 */
	public List<SpiderBean> getGoodsdata(String uid, int userid, int type);

	/**（批量折扣）
	 * ylm
	 * 查询该uid是否是该用户下的并且是有效uid
	 *
	 */
	public int getPauid(String uid, int userid);

	/**（批量折扣）
	 * ylm
	 * 将该uid状态设置为已链接
	 *
	 */
	public int upPauid(String uid, int type);

	/**
	 * ylm
	 * 将该优惠申请改为已处理
	 * （批量折扣）
	 */
	public int upPA(String uid, int type);

	/**
	 * ylm
	 * 将该优惠申请改为已处理
	 * （批量折扣）
	 */
	public int upPA(int pid);

	/**
	 * ylm
	 * 保存邮费折扣申请
	 *
	 */
	public int savePostageD(PostageDiscounts pd);

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

	/**
	 *
	 * 返回主键
	 *
	 */
	public int savePreferential2(Preferential preferential, String url);
	
	/**
	 * 
	 * 更新remark
	 * 
	 */
	public int updateRemark(int id, String remark);
	

	/**
	 * 查询混批折扣
	 * 
	 * @param user
	 * 	用户信息
	 */
	public List<ClassDiscount> getClass_discount();
}
