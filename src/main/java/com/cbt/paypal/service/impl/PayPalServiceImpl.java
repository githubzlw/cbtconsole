package com.cbt.paypal.service.impl;


import com.cbt.bean.OrderBean;
import com.cbt.paypal.config.PayPalConfig;
import com.cbt.paypal.config.PayPalPaymentIntentEnum;
import com.cbt.paypal.config.PayPalPaymentMethodEnum;
import com.cbt.stripe.StripeService;
import com.cbt.stripe.impl.StripeServiceImpl;
import com.cbt.website.dao.OrderInfoDao;
import com.cbt.website.dao.OrderInfoImpl;
import com.cbt.website.util.JsonResult;
import com.importExpress.mapper.RefundResultInfoMapper;
import com.importExpress.pojo.RefundResultInfo;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("payPalService")
public class PayPalServiceImpl implements com.cbt.paypal.service.PayPalService {

    //private static Logger logger = REFUNDLOGFactory.getREFUNDLOG(PayPalServiceImpl.class);
    private final static org.slf4j.Logger REFUNDLOG = LoggerFactory.getLogger("refund");
    @Autowired
    private RefundResultInfoMapper refundResultInfoMapper;

    @Override
    public Payment createPayment(
            Double total,
            String cancelUrl,
            String successUrl,
            String orderNO, String customMsg
    ) throws PayPalRESTException {

        return createPayment(
                total,
                "USD",
                PayPalPaymentMethodEnum.paypal,
                PayPalPaymentIntentEnum.sale,
                "",
                cancelUrl,
                successUrl,
                orderNO, customMsg);

    }

    @Override
    public Payment createPayment(
            Double total,
            String currency,
            PayPalPaymentMethodEnum method,
            PayPalPaymentIntentEnum intent,
            String description,
            String cancelUrl,
            String successUrl,
            String orderNO,
            String customMsg) throws PayPalRESTException {
        REFUNDLOG.info("createPayment():[{total:"+total+"}],[{currency:"+currency+"}],[{method:"+method+"}],[intent:{"
                    +intent+"}],[{description:"+description+"}],[{cancelUrl:"+cancelUrl+"}],[{successUrl:"+successUrl+"}]");

        APIContext apiContext = getApiContext(0);

        // ###Details
        Details details = new Details();
        details.setShipping("0");
        String strTotal = String.format("%.2f", total);
        details.setSubtotal(strTotal);
        details.setTax("0");

        // ###Amount
        Amount amount = new Amount();
        amount.setCurrency(currency);
        // Total must be equal to sum of shipping, tax and subtotal.
        amount.setTotal(strTotal);
        amount.setDetails(details);

        // ###Transaction
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setCustom(customMsg);
        // ###Transactions
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        // ### Items
        Item item = new Item();
        item.setName(orderNO).setQuantity("1").setCurrency(currency).setPrice(strTotal);
        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        items.add(item);
        itemList.setItems(items);
        transaction.setItemList(itemList);

        // ###Payer
        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        // ###Payment
        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        // ###Redirect URLs
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    @Override
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {

        REFUNDLOG.info("executePayment():[{paymentId:"+paymentId+"}],[{payerId:"+payerId+"}]");

        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);

        return payment.execute(getApiContext(0), paymentExecute);

    }

    private APIContext getApiContext(int isOld) {

        REFUNDLOG.info("getApiContext()");
        Map<String, String> sdkConfig = new HashMap<>(1);
        sdkConfig.put("mode", PayPalConfig.mode);
        if("live".equals(PayPalConfig.mode) && isOld > 0){
            return new APIContext(PayPalConfig.clientIdOld, PayPalConfig.clientSecretOld, PayPalConfig.mode, sdkConfig);
        }else{
            return new APIContext(PayPalConfig.clientId, PayPalConfig.clientSecret, PayPalConfig.mode, sdkConfig);
        }
        /*sdkConfig.put("mode", "sandbox");
        return new APIContext("AaYrawhmyep4w1VQ_Q8r9ubzt09tBJevuCJ2rGbOHUfNRhbQt3rh9Dl1VsTGjKo4pei1uap-dq2j1cJO",
                "EJpfYpI3Ym3iuFgG8yK8JwUVdkhiU0DrxUMcQ8h62dS96pZ8V3_ws8yL4r6QnBCuKQJ_PjUXDh8HE2mp",
                "sandbox",
                sdkConfig);*/
    }

    @Override
    public DetailedRefund reFund(String saleId, String amountMoney,int isOld) throws PayPalRESTException {
        REFUNDLOG.info("reFund():[{saleId:"+saleId+"}],[{amountMoney:"+amountMoney+"}]");

        Sale sale = new Sale();
        sale.setId(saleId);

        RefundRequest refund = new RefundRequest();

        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(amountMoney);
        refund.setAmount(amount);
        return sale.refund(getApiContext(isOld), refund);
    }

    @Override
    public JsonResult reFundNew(String orderNo, String amountMoney) {
        OrderInfoDao orderInfoDao = new OrderInfoImpl();

        //先查询这个订单是否存在
        JsonResult json = new JsonResult();
        try {
            OrderBean orderInfo = orderInfoDao.getOrderInfo(orderNo, null);
            if (orderInfo != null) {
//                String[] payMentInfo = orderInfoDao.getPayMentInfo(orderNo);
//                if(payMentInfo == null){
//                    json.setOk(false);
//                    json.setMessage("没有获取到正确的付款信息!");
//                    return json;
//                }

                Map<String, Object> paymentInfo = orderInfoDao.queryPaymentInfoByOrderNo(orderNo);
                if (paymentInfo == null || paymentInfo.size() == 0) {
                    json.setOk(false);
                    json.setMessage("没有获取到正确的付款信息!");
                    return json;
                }

                String payType = paymentInfo.get("payType").toString();
                //获取saleid
                String saleId = paymentInfo.get("saleId").toString();
                //获取付款金额
                String total = paymentInfo.get("payAmount").toString();

                if ("paypal".equals(payType)) {
                    //如果退款金额小于付款金额
                    if (Double.parseDouble(amountMoney) <= Double.parseDouble(total)) {
                        RefundResultInfo refundResultInfo = new RefundResultInfo();
                        refundResultInfo.setOrderno(orderNo);
                        refundResultInfo.setUserid(String.valueOf(orderInfo.getUserid()));
                        refundResultInfo.setPayprice(String.valueOf(orderInfo.getPay_price()));
                        refundResultInfo.setTotalRefundedAmount(amountMoney);
                        refundResultInfo.setState("readyRefund");

                        REFUNDLOG.info("begin refund:" + refundResultInfo.toString());
                        refundResultInfoMapper.insertSelective(refundResultInfo);

                        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime payTime = LocalDateTime.parse(paymentInfo.get("payAmount").toString(),df);
                        LocalDateTime changeTime = LocalDateTime.parse("2018-10-10",df);
                        DetailedRefund detailedRefund;
                        if(payTime.isBefore(changeTime)){
                            detailedRefund = reFund(saleId, amountMoney,1);
                        }else{
                             detailedRefund = reFund(saleId, amountMoney,0);
                        }

                        if (detailedRefund != null) {
                            refundResultInfo.setRefundFromTransactionFee(detailedRefund.getRefundFromTransactionFee().getValue());
                            refundResultInfo.setRefundFromReceivedAmount(detailedRefund.getRefundFromReceivedAmount().getValue());
                            refundResultInfo.setTotalRefundedAmount(detailedRefund.getTotalRefundedAmount().getValue());
                            refundResultInfo.setRefundid(detailedRefund.getId());
                            refundResultInfo.setAmountTotal(detailedRefund.getAmount().getTotal());
                            refundResultInfo.setState(detailedRefund.getState());
                            refundResultInfo.setSaleId(detailedRefund.getSaleId());
                            refundResultInfo.setParentPayment(detailedRefund.getParentPayment());
                            refundResultInfo.setCreateTime(detailedRefund.getCreateTime());
                            refundResultInfo.setUpdateTime(detailedRefund.getUpdateTime());
                            refundResultInfo.setInfo(detailedRefund.toString().getBytes());
                            REFUNDLOG.info("refund result:" + refundResultInfo.toString());
                            refundResultInfoMapper.insertSelective(refundResultInfo);
                            json.setOk(true);
                            json.setData(detailedRefund);
                            json.setMessage(detailedRefund.getId());
                        } else {
                            json.setOk(false);
                            json.setMessage("API无返回信息");
                        }
                    } else {
                        json.setOk(false);
                        json.setMessage("订单不符合退款条件!");
                    }
                } else if ("stripe".equals(payType)) {
                    //如果退款金额小于付款金额
                    if (Double.parseDouble(amountMoney) <= Double.parseDouble(total)) {
                        RefundResultInfo refundResultInfo = new RefundResultInfo();
                        refundResultInfo.setOrderno(orderNo);
                        refundResultInfo.setUserid(String.valueOf(orderInfo.getUserid()));
                        refundResultInfo.setPayprice(String.valueOf(orderInfo.getPay_price()));
                        refundResultInfo.setTotalRefundedAmount(amountMoney);
                        refundResultInfo.setState("readyRefund");

                        REFUNDLOG.info("begin refund:" + refundResultInfo.toString());
                        refundResultInfoMapper.insertSelective(refundResultInfo);

                        Double amountDouble = Double.valueOf(amountMoney) * 100D;
                        StripeService stripeService = new StripeServiceImpl();
                        com.stripe.model.Refund detailedRefund = stripeService.refund(saleId, amountDouble.longValue());

                        if (detailedRefund != null) {
                            refundResultInfo.setRefundid(detailedRefund.getId());
                            refundResultInfo.setAmountTotal(detailedRefund.getAmount().toString());
                            refundResultInfo.setState(detailedRefund.getStatus());
                            refundResultInfo.setSaleId(detailedRefund.getId());
                            refundResultInfo.setInfo(detailedRefund.toString().getBytes());
                            REFUNDLOG.info("refund result:" + refundResultInfo.toString());
                            refundResultInfoMapper.insertSelective(refundResultInfo);
                            json.setOk(true);
                            json.setData(detailedRefund);
                            json.setMessage(detailedRefund.getId());
                        } else {
                            json.setOk(false);
                            json.setMessage("API无返回信息");
                        }
                    } else {
                        json.setOk(false);
                        json.setMessage("订单不符合退款条件!");
                    }
                } else {
                    json.setOk(false);
                    json.setMessage("无支付信息，请确认订单号是否错误");
                }
            } else {
                json.setOk(false);
                json.setMessage("没有查询到该订单!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("API退款失败，原因：" + e.getMessage());
        }
        return json;
    }

}