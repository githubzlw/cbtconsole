package com.cbt.report.dao;

import com.cbt.pojo.BasicReport;
import com.cbt.pojo.BasicReportExample;
import com.cbt.refund.bean.PayPalImportInfo;
import com.cbt.refund.bean.RefundBean;
import com.cbt.report.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BasicReportMapper {
    int countByExample(BasicReportExample example);

    int deleteByExample(BasicReportExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BasicReport record);

    int insertSelective(BasicReport record);

    List<BasicReport> selectByExample(BasicReportExample example);

    BasicReport selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BasicReport record, @Param("example") BasicReportExample example);

    int updateByExample(@Param("record") BasicReport record, @Param("example") BasicReportExample example);

    int updateByPrimaryKeySelective(BasicReport record);

    int updateByPrimaryKey(BasicReport record);

    /**
     * 查询每月的订单财务
     *
     * @param year
     * @param month
     * @return
     */
    List<OrderFinancialBean> queryOrderFinancial(@Param("year") String year, @Param("month") String month);

    /**
     * 插入订单财务信息
     *
     * @param beginDate
     * @param endDate
     */
    void insertOrderFinancial(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 分页查询每月的退款订单的信息
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public List<RefundInfoBean> queryPaypalRefunds(@Param("beginDate") String beginDate,
                                                   @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询每月的退款订单的信息的总数
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public int queryPaypalRefundsCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 分页查询每月的客户余额变更信息
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<CustomerBalanceChangeBean> queryCustomerBalanceChange(@Param("beginDate") String beginDate,
                                                                      @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询客户的总数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public int queryCustomerBalancesChangeCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);


    /**
     * 分页查询每月的客户余额变更信息
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    List<CustomerBalanceChangeBean> queryBalanceDetailsAll(@Param("beginDate") String beginDate,
                                                                      @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询客户的总数
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    int queryBalanceDetailsAllCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);


    /**
     * 重新生成订单及销售额对账报表
     *
     * @param map
     * @return
     */
    public int onloadOrderFinancialDate(Map<String, String> map);

    /**
     * 插入退款信息
     *
     * @param rfb
     * @return
     */
    public int insertRefund(RefundBean rfb);

    /**
     * 分页查询每月的实际订单销售额
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<OrderInfoBean> queryOrderSales(@Param("beginDate") String beginDate, @Param("endDate") String endDate,
                                               @Param("start") int start, @Param("rows") int rows);

    List<OrderInfoBean> queryOrderTranscriptSales(@Param("beginDate") String beginDate, @Param("endDate") String endDate,
                                               @Param("start") int start, @Param("rows") int rows);

    int queryOrderTranscriptSalesCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 查询每月的订单的总数
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public int queryOrderSalesCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 分页查询每月的PayPal总收入
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<PayPalInfoBean> queryPayPalRevenue(@Param("beginDate") String beginDate,
                                                   @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询每月的PayPal总收入的总数
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public int queryPayPalRevenueCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 分页查询每月的电汇总收入
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<PayPalInfoBean> queryWireRevenue(@Param("beginDate") String beginDate,
                                                 @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询每月的电汇总收入的总数
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public int queryWireRevenueCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 分页查询每月的未启动订单总金额
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<PayPalInfoBean> queryNotStartedOrderAmount(@Param("beginDate") String beginDate,
                                                           @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询每月的未启动订单的总数
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public int queryNotStartedOrderAmountCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 分页查询每月的PayPal总退款
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<RefundInfoBean> queryTotalRefund(@Param("beginDate") String beginDate, @Param("endDate") String endDate,
                                                 @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询每月的PayPal退款的总数
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public int queryTotalRefundCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 分页查询每月的总余额支付
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<PayPalInfoBean> queryBalancePayment(@Param("beginDate") String beginDate,
                                                    @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询每月的余额支付的总数
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public int queryBalancePaymentCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 分页查询每月的总余额提现
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<RefundInfoBean> queryBalanceWithdrawal(@Param("beginDate") String beginDate,
                                                       @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询每月的余额提现的总数
     *
     * @param beginDate
     * @param endDateint
     * @param start
     * @param rows
     * @return
     */
    public int queryBalanceWithdrawalCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

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
    public List<BalanceCompensation> queryBalanceCompensation(@Param("beginDate") String beginDate,
                                                              @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询每月的余额补偿详情总数
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public int queryBalanceCompensationCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 查询每月的订单取消(全部或部分)详情
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public List<OrderCancelBean> queryOrderCancel(@Param("beginDate") String beginDate,
                                                  @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    /**
     * 查询确认取消的订单数据
     * @param beginDate
     * @param start
     * @param rows
     * @return
     */
    List<OrderCancelBean> queryOrderConfirmCancel(@Param("beginDate") String beginDate, @Param("start") int start, @Param("rows") int rows);

    int queryOrderConfirmCancelCount(@Param("beginDate") String beginDate);

    /**
     * 查询每月的订单取消(全部或部分)详情总数
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    public int queryOrderCancelCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @param sorting
     * @return List<EditedProductProfits>
     * @Title queryEditedProductProfits
     * @Description 查询人工编辑产品利润
     */
    public List<EditedProductProfits> queryEditedProductProfits(@Param("beginDate") String beginDate,
                                                                @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows,
                                                                @Param("sorting") int sorting, @Param("exchageRate") double exchageRate);

    /**
     * @param beginDate
     * @param endDate
     * @return int
     * @Title queryEditedProductProfits
     * @Description 查询人工编辑产品利润总数
     */
    public int queryEditedProductProfitsCount(@Param("beginDate") String beginDate,
                                              @Param("endDate") String endDate);

    /**
     * Stripe支付查询
     *
     * @param beginDate
     * @param endDate
     * @param start
     * @param rows
     * @return
     */
    List<PayPalInfoBean> queryStripPayInfo(@Param("beginDate") String beginDate,
                                           @Param("endDate") String endDate, @Param("start") int start, @Param("rows") int rows);

    int queryStripPayInfoCount(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

}