package com.importExpress.service;

import com.cbt.website.bean.PayCheckBean;
import com.cbt.website.bean.PaymentDetails;
import com.cbt.website.bean.PaymentStatistics;

import java.util.List;


public interface PaymentServiceNew {

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
    List<PaymentDetails> queryPaymentDetails(String orderNo);

}
