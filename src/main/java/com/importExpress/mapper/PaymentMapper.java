package com.importExpress.mapper;

import com.cbt.website.bean.PayCheckBean;
import com.cbt.website.bean.PaymentDetails;
import com.cbt.website.bean.PaymentStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PaymentMapper {

    /**
     * 根据参数查询支付统计数据
     *
     * @param bean
     * @return
     */
    List<PaymentStatistics> queryPaymentStatistics(PayCheckBean bean);

    /**
     * 根据参数查询支付统计数据数量
     *
     * @param bean
     * @return
     */
    int queryPaymentStatisticsCount(PayCheckBean bean);


    /**
     * 根据订单号查询支付详情数据
     *
     * @param orderNo
     * @return
     */
    List<PaymentDetails> queryPaymentDetails(@Param("orderNo") String orderNo);
    
    /**
     * @param startNum
     * @param limitNum
     * @return
     */
    List<Map<String,Object>> listStripePayment(@Param("startNum") int startNum, @Param("limitNum") int limitNum);
    /**统计
     * @return
     */
    int countStripePayment();
}
