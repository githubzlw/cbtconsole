package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.GradeDiscountBean;
import com.cbt.warehouse.pojo.OrderBean;
import com.cbt.warehouse.pojo.UserGradeGrowthBean;
import com.cbt.warehouse.pojo.UserGradeGrowthLogBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * vip等级优惠接口
 * @author Administrator
 *
 */
public interface GradeDiscountService {
	/**获取用户等级折扣
	 * @date 2016年11月9日
	 * @author abc
	 * @return  
	 */
	public Map<String, String> getDisount();
	
	/**
	 * 获取用户user_grade_growth等级积分信息
	 * @param userId
	 * @return
	 */
	public UserGradeGrowthBean getUserGradeGrowth(int userId);
	
	/**
	 * 根据总积分获取grade_discount等级信息
	 * @param growthValue
	 * @return
	 */
	public GradeDiscountBean getGradeDiscountByGrowthValue(int growthValue);
	/**
	 * 根据等级gid获取grade_discount等级信息
	 * @param gid
	 * @return
	 */
	public GradeDiscountBean getGradeDiscountByGid(int gid);
	/**
	 * 添加一条用户等级积分信息
	 * @param bean
	 * @return
	 */
	public int insertUserGradeGrowth(UserGradeGrowthBean bean);
	
	/**
	 * 获取用户的成功订单数
	 * @param userId
	 * @return
	 */
	public int getCompleteOrderCount(int userId);
	/**
	 * 修改一条用户等级积分信息
	 * @param bean
	 * @return
	 */
	public int updateUserGradeGrowth(UserGradeGrowthBean bean);
	/**
	 * 根据orderNo查询支付的订单信息
	 * @param orderNo
	 * @return
	 */
	public OrderBean getCompleteOrder(String orderNo);
	/**
	 * 查询所有vip等级优惠
	 * @return
	 */
	public List<GradeDiscountBean> getAllGradeDiscount();
	/**
	 * 修改用户表中等级
	 * @param grade
	 * @param userId
	 * @return
	 */
	public int updateUserGrade(int grade, int userId);
	/**
	 * 插入一条用户等级信息更改记录日志
	 * @param bean
	 * @return
	 */
	public int insertUserGradeGrowthLog(UserGradeGrowthLogBean bean);
	/**
	 * 依据userid和orderNo取得最新一条日志
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public UserGradeGrowthLogBean getLogByUserIdAndOrderNo(int userId, String orderNo);
	/**
	 * 依据userid取得用户最新一条日志
	 * @param userId
	 * @return
	 */
	public UserGradeGrowthLogBean getLogByUserId(int userId);
	/**
	 * 根据userid和grade查询用户在此等级的优惠剩余次数
	 * @param userId
	 * @param grade
	 * @return
	 */
	public Integer getTimesByUserIdAndGrade(int userId, int grade);
	/**
	 * 查询其他的未付款订单
	 * @param userId
	 * @return
	 */
	public List<OrderBean> getOtherGradeDiscount(int userId);
	/**
	 * 修改其他订单的等级优惠和支付金额
	 * @param grade_discount
	 * @param pay_price
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	public int updateOtherOrderGradeDiscout(double grade_discount, String orderNo, int userId);

	/**
	 * 根据orderNo查询订单数量来判断否是拆单后的订单
	 * @param userId
	 * @param orderNo
	 * @return boolean
	 */
	public boolean isSpitOrder(int userId, String orderNo);

	/**
	 * 根据orderNo查询订单数量来判断否是拆单后的订单
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public int getCountLikeOrderNo(int userId, String orderNo);

	/**
	 * 根据orderNo查询拆单后的订单的取消数量
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public int getDeleteCountLikeOrderNo(int userId, String orderNo);

	/**
	 * 获取除了该订单和同源的拆单以外完成的订单数
	 * @param userId
	 * @return
	 */
	public int getCompleteOrderCountExcludeTOrder(@Param("userId") int userId, @Param("orderNo") String orderNo);

	/**
	 * 获取同源的拆单的未退单的订单的总支付金额
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public Double getTOrderNowTotalPayPrice(@Param("userId") int userId, @Param("orderNo") String orderNo);
}
