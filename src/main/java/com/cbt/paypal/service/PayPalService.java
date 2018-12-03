/**
 * *****************************************************************************************
 * 类描述：PayPal支付接口类
 *
 * @author: luohao
 * @date： 2018-04-27
 * @version 1.0
 *
 *
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0        2018-04-27          luohao                    初版
 *******************************************************************************************
 */
package com.cbt.paypal.service;


import com.cbt.paypal.config.PayPalPaymentIntentEnum;
import com.cbt.paypal.config.PayPalPaymentMethodEnum;
import com.cbt.website.util.JsonResult;
import com.paypal.api.payments.DetailedRefund;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayPalService {

    /**
     * 创建交易
     *
     * @param total
     * @param cancelUrl
     * @param successUrl
     * @param orderNO
     * @param customMsg
     * @return
     * @throws PayPalRESTException
     */
    Payment createPayment(Double total,
                          String cancelUrl,
                          String successUrl,
                          String orderNO, String customMsg) throws PayPalRESTException;

    /**
     * 创建交易
     *
     * @param total
     * @param currency
     * @param method
     * @param intent
     * @param description
     * @param cancelUrl
     * @param successUrl
     * @param orderNO
     * @param customMsg
     * @return
     * @throws PayPalRESTException
     */
    Payment createPayment(
            Double total,
            String currency,
            PayPalPaymentMethodEnum method,
            PayPalPaymentIntentEnum intent,
            String description,
            String cancelUrl,
            String successUrl,
            String orderNO, String customMsg) throws PayPalRESTException;

    /**
     * 执行交易（回调时候执行）
     *
     * @param paymentId
     * @param payerId
     * @return
     * @throws PayPalRESTException
     */
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    /**
     * 退款处理（一次调用）
     *
     * @param saleId
     * @param amountMoney
     * @param isOld 判断收款账号是否是老账号标识 1老账号 0新账号
     * @return
     * @throws PayPalRESTException
     */
    DetailedRefund reFund(String saleId, String amountMoney,int isOld) throws PayPalRESTException;
    /**
     * @Title: reFundNew
     * @Author: cjc
     * @Despricetion:TODO 发起退款
     * @Date: 2018/7/5 16:25
     * @Param: [orderNo, amountMoney]
     * @Return: java.lang.String
     */
    JsonResult reFundNew(String orderNo, String amountMoney);
}