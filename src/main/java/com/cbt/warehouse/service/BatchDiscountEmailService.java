package com.cbt.warehouse.service;

import com.cbt.warehouse.pojo.BatchDiscountEmail;
import com.cbt.warehouse.pojo.BatchDiscountEmailDetails;
import com.cbt.warehouse.pojo.BatchDiscountPurchasPrice;

import java.util.List;

public interface BatchDiscountEmailService {
	/**
	 * 根据条件查询批量优惠邮件列表
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @param adminId
	 *            : 销售id
	 * @param userId
	 *            : 用户id
	 * @param flag
	 *            : 邮件发送状态标识
	 * @param beginDate
	 *            : 开始时间
	 * @param endDate
	 *            : 结束时间
	 * @param stateNum
	 *            : 开始位置
	 * @param showNum
	 *            : 显示数量
	 * @return
	 */
	public List<BatchDiscountEmail> queryEmailList(String orderNo, int adminId, int userId, String flag,
                                                   String beginDate, String endDate, int stateNum, int showNum);

	/**
	 * 根据条件查询批量优惠邮件列表数量
	 *
	 * @param orderNo
	 *            : 订单号
	 * @param adminId
	 *            : 销售id
	 * @param userId
	 *            : 用户id
	 * @param flag
	 *            : 邮件发送状态标识
	 * @param beginDate
	 *            : 开始时间
	 * @param endDate
	 *            : 结束时间
	 * @return
	 */
	public int queryEmailListCount(String orderNo, int adminId, int userId, String flag, String beginDate,
                                   String endDate);

	/**
	 * 根据订单号查询批量优惠邮件信息
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @return
	 */
	public BatchDiscountEmail queryEmailByOrderNo(String orderNo);

	/**
	 * 根据orderNo查询邮件下的所有详情
	 * 
	 * @param orderNo
	 * @return
	 */
	public List<BatchDiscountEmailDetails> queryDetailsList(String orderNo);

	/**
	 * 根据商品的goods_car id查询对应的批量价格
	 * 
	 * @param goodsId
	 * @return
	 */
	public List<BatchDiscountPurchasPrice> queryPurchasPriceList(int goodsId);

	/**
	 * 更新商品的批量价格
	 * 
	 * @param purchasPrice
	 * @return
	 */
	public int updatePurchasPrice(BatchDiscountPurchasPrice purchasPrice);

	/**
	 * 插入批量价格邮件
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @return
	 */
	public int insertEmail(String orderNo);

	/**
	 * 插入批量价格邮件详情
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @return
	 */
	public int insertEmailDetails(String orderNo);

	/**
	 * 根据orderNo更新表关联关系batch_discount的email和email_details
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @return
	 */
	public int buildRelationshipsByOrderNo(String orderNo);

	/**
	 * 插入商品的批量价格
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @return
	 */
	public int insertPurchasPrice(String orderNo);

	/**
	 * 更新商品的默认折扣价格=采购价格*1.2
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @return
	 */
	public int updateDefaultPurchasPrice(String orderNo);

	/**
	 * 查询订单是否含有批量价格
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @return
	 */
	public boolean checkIsPurchasPrice(String orderNo);

	/**
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @param goodsId
	 *            : 商品id
	 * @param freeShippingPrice
	 *            : 免邮价格
	 * @return
	 */
	public int updateEmailDetailsFreeShippingPrice(String orderNo, int goodsId, float freeShippingPrice);

	/**
	 * 更新商品批量价格有效
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @param goodsId
	 *            : 商品id
	 * @param minQuantify
	 *            : 最小定量
	 * @param maxQuantify
	 *            : 最多定量
	 * @return
	 */
	public void updateValidByGoodsId(String orderNo, int goodsId, int minQuantify, int maxQuantify);

	/**
	 * 批量更新邮件详情的商品有效
	 * 
	 * @param uedLst
	 * @param orderNo
	 * @return
	 */
	public int batchUpdateEmailDetailsValid(List<Integer> uedLst, String orderNo);

	/**
	 * 批量更新修改后的商品批量价格数据
	 * 
	 * @param uppLst
	 * @return
	 */
	public int batchUpdatePurchasPrice(List<BatchDiscountPurchasPrice> uppLst);

	/**
	 * 更新批量邮件的推送状态
	 * 
	 * @param orderNo
	 *            : 订单号
	 * @param flag
	 *            : 状态:-3废弃不发;-2:未满足发送条件;-1:发送失败;0:待发送;1:已发送(已读);2:已发送(未读)
	 * @return
	 */
	public int updateEmailFlagByOrderNo(String orderNo, String flag);

}
