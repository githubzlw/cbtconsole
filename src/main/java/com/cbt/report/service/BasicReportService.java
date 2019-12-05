package com.cbt.report.service;

import com.cbt.pojo.BasicReport;
import com.cbt.refund.bean.PayPalImportInfo;
import com.cbt.report.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BasicReportService {

	int updateByPrimaryKey(BasicReport record);

	/**
	 * 查询每月的订单财务
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public List<OrderFinancialBean> queryOrderFinancial(String year, String month);

	/**
	 * 插入订单财务信息
	 * 
	 * @param beginDate
	 * @param endDate
	 */
	public void insertOrderFinancial(String beginDate, String endDate);

	/**
	 * 分页查询每月的退款订单的信息
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<RefundInfoBean> queryPaypalRefunds(String beginDate, String endDate, int start, int rows);

	/**
	 * 查询每月的退款订单的信息的总数
	 * 
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryPaypalRefundsCount(String beginDate, String endDateint);

	/**
	 * 分页查询每月的客户余额变更信息
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<CustomerBalanceChangeBean> queryCustomerBalanceChange(String beginDate, String endDate, int start,
                                                                      int rows);

	/**
	 * 查询客户的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryCustomerBalancesChangeCount(String beginDate, String endDateint);

	/**
	 * 分页查询每月的客户余额变更信息全部
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<CustomerBalanceChangeBean> queryBalanceDetailsAll(String beginDate, String endDate, int start,
                                                                      int rows);

	/**
	 * 查询客户的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @return
	 */
	public int queryBalanceDetailsAllCount(String beginDate, String endDateint);

	/**
	 * 分页查询每月的实际订单销售额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<OrderInfoBean> queryOrderSales(String beginDate, String endDate, int start, int rows, int type);

	List<OrderInfoBean> queryOrderTranscriptSales(@Param("beginDate") String beginDate, @Param("endDate") String endDate,
												  @Param("start") int start, @Param("rows") int rows);

    int queryOrderTranscriptSalesCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

	/**
	 *重新生成订单及销售额对账报表
	 * @param map
	 * @return
	 */
	public int onloadOrderFinancialDate(Map<String, String> map);

	/**
	 * 查询每月的订单的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryOrderSalesCount(String beginDate, String endDate);

	/**
	 * 分页查询每月的PayPal总收入
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<PayPalInfoBean> queryPayPalRevenue(String beginDate, String endDate, int start, int rows);
	/**
	 * 分页查询每月的电汇总收入
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<PayPalInfoBean> queryWireRevenue(String beginDate, String endDate, int start, int rows);
	/**
	 * 查询每月的电汇总收入的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryWireRevenueCount(String beginDate, String endDate);
	/**
	 * 查询每月的PayPal总收入的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryPayPalRevenueCount(String beginDate, String endDate);

	/**
	 * 分页查询每月的未启动订单总金额
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<PayPalInfoBean> queryNotStartedOrderAmount(String beginDate, String endDate, int start, int rows);

	/**
	 * 查询每月的未启动订单的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryNotStartedOrderAmountCount(String beginDate, String endDate);

	/**
	 * 分页查询每月的PayPal总退款
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<RefundInfoBean> queryTotalRefund(String beginDate, String endDate, int start, int rows);

	/**
	 * 查询每月的PayPal退款的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryTotalRefundCount(String beginDate, String endDate);

	/**
	 * 分页查询每月的总余额支付
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<PayPalInfoBean> queryBalancePayment(String beginDate, String endDate, int start, int rows);

	/**
	 * 查询每月的余额支付的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryBalancePaymentCount(String beginDate, String endDate);

	/**
	 * 分页查询每月的总余额提现
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<RefundInfoBean> queryBalanceWithdrawal(String beginDate, String endDate, int start, int rows);

	/**
	 * 查询每月的余额提现的总数
	 *
	 * @param beginDate
	 * @param endDateint
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryBalanceWithdrawalCount(String beginDate, String endDate);

	/**
	 * 批量保存从Excel的获取的PayPal信息
	 *
	 * @param infos
	 * @return
	 */
	public void batchSavePayPalInfoByExcel(List<PayPalImportInfo> infos);

	/**
	 * 查询每月的余额补偿详情
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<BalanceCompensation> queryBalanceCompensation(String beginDate, String endDate, int start, int rows);

	/**
	 * 查询每月的余额补偿详情总数
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryBalanceCompensationCount(String beginDate, String endDate);


	/**
	 * 查询每月的订单取消(全部或部分)详情
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public List<OrderCancelBean> queryOrderCancel(String beginDate, String endDate, int start, int rows);

	/**
     * 查询确认取消的订单数据
     * @param beginDate
     * @param start
     * @param rows
     * @return
     */
    List<OrderCancelBean> queryOrderConfirmCancel(String beginDate, int start, int rows);

    int queryOrderConfirmCancelCount(String beginDate);

	/**
	 * 查询每月的订单取消(全部或部分)详情总数
	 *
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	public int queryOrderCancelCount(String beginDate, String endDate);

	/**
	 *
	 * @Title queryEditedProductProfits
	 * @Description 查询人工编辑产品利润
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @param sorting
	 * @return List<EditedProductProfits>
	 */
	public List<EditedProductProfits> queryEditedProductProfits(String beginDate, String endDate, int start, int rows, int sorting, double exchageRate);
	
	/**
	 * 
	 * @Title queryEditedProductProfits 
	 * @Description 查询人工编辑产品利润总数
	 * @param beginDate
	 * @param endDate
	 * @return int
	 */
	public int queryEditedProductProfitsCount(String beginDate, String endDate);

	/**
	 * Stripe支付查询
	 * @param beginDate
	 * @param endDate
	 * @param start
	 * @param rows
	 * @return
	 */
	List<PayPalInfoBean> queryStripPayInfo(String beginDate, String endDate, int start, int rows);

	int queryStripPayInfoCount(String beginDate, String endDate);

}