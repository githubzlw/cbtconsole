package com.cbt.paypal.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.cbt.bean.OrderBean;
import com.cbt.paypal.config.PayPalConfig;
import com.cbt.paypal.config.PayPalPaymentIntentEnum;
import com.cbt.paypal.config.PayPalPaymentMethodEnum;
import com.cbt.stripe.StripeService;
import com.cbt.stripe.impl.StripeServiceImpl;
import com.cbt.util.DateFormatUtil;
import com.cbt.website.dao.OrderInfoDao;
import com.cbt.website.dao.OrderInfoImpl;
import com.cbt.website.util.JsonResult;
import com.cbt.website.util.MD5Util;
import com.importExpress.mapper.RefundResultInfoMapper;
import com.importExpress.pojo.RefundResultInfo;
import com.importExpress.utli.SendMQ;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Currency;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service("payPalService")
public class PayPalServiceImpl implements com.cbt.paypal.service.PayPalService {

    private final static Logger REFUNDLOG = LoggerFactory.getLogger(PayPalServiceImpl.class);
    //private static Logger logger = REFUNDLOGFactory.getREFUNDLOG(PayPalServiceImpl.class);
    // private final static org.slf4j.Logger REFUNDLOG = LoggerFactory.getLogger("refund");
    @Autowired
    private RefundResultInfoMapper refundResultInfoMapper;
    private OrderInfoDao orderInfoDao = new OrderInfoImpl();

    LocalDateTime NEW_STRIPE_PAY_TIME = DateFormatUtil.getTimeWithStr("2019-11-06 00:00:00");

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

                String today = DateFormatUtil.getWithSeconds(new Date());

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
                        refundResultInfo.setCreateTime(today);
                        refundResultInfo.setUpdateTime(today);

                        REFUNDLOG.info("begin refund:" + refundResultInfo.toString());
                        refundResultInfoMapper.insertSelective(refundResultInfo);

                        DetailedRefund detailedRefund;

                        /*DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        LocalDateTime payTime = LocalDateTime.parse(paymentInfo.get("payTime").toString(),df);
                        LocalDateTime changeTime = LocalDateTime.parse("2018-09-20 00:00:00",df);

                        if(payTime.isBefore(changeTime)){
                            detailedRefund = reFund(saleId, amountMoney,1);
                        }else{
                             detailedRefund = reFund(saleId, amountMoney,0);
                        }*/

                        // 新的判断新老账号方法
                        boolean isOld = orderInfoDao.checkIsOldPayPal(orderNo);
                        if(isOld){
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
                            json.setTotal(0L);
                            json.setMessage("交易号[" + detailedRefund.getId() + "]");
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
                        refundResultInfo.setCreateTime(today);
                        refundResultInfo.setUpdateTime(today);

                        REFUNDLOG.info("begin refund:" + refundResultInfo.toString());
                        refundResultInfoMapper.insertSelective(refundResultInfo);

                        Double amountDouble = Double.valueOf(amountMoney) * 100D;
                        StripeService stripeService = new StripeServiceImpl();


                        String payTime = paymentInfo.get("payTime").toString();
                        boolean isNew = false;
                        LocalDateTime payTimeBean = DateFormatUtil.getTimeWithStr(payTime);

                        if(payTimeBean.isAfter(NEW_STRIPE_PAY_TIME)){
                            isNew = true;
                        }

                        com.stripe.model.Refund detailedRefund = stripeService.refund(saleId, amountDouble.longValue(), isNew);

                        if (detailedRefund != null) {
                            refundResultInfo.setRefundid(detailedRefund.getId());
                            refundResultInfo.setAmountTotal(detailedRefund.getAmount().toString());
                            refundResultInfo.setState(detailedRefund.getStatus());
                            refundResultInfo.setSaleId(detailedRefund.getId());
                            refundResultInfo.setInfo(detailedRefund.toString().getBytes());
                            refundResultInfo.setCreateTime(today);
                            refundResultInfo.setUpdateTime(today);
                            REFUNDLOG.info("refund result:" + refundResultInfo.toString());
                            refundResultInfoMapper.insertSelective(refundResultInfo);
                            json.setOk(true);
                            json.setData(detailedRefund);
                            json.setTotal(5L);
                            json.setMessage("交易号[" + detailedRefund.getId() + "]");
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
            REFUNDLOG.error("orderNo:"+orderNo+",amountMoney:" + amountMoney + ",error:",e);
        }
        return json;
    }


    @Override
    public JsonResult refundByPayNo(String payNo, String payType,String refundAmount, String remark, int adminId) {
        //先查询这个订单是否存在
        JsonResult json = new JsonResult();
        try {
            if ("1".equals(payType)) {
                RefundResultInfo refundResultInfo = new RefundResultInfo();
                refundResultInfo.setTotalRefundedAmount(refundAmount);
                refundResultInfo.setState("readyRefund");

                REFUNDLOG.info("begin refund:" + refundResultInfo.toString());
                refundResultInfoMapper.insertSelective(refundResultInfo);

                DetailedRefund detailedRefund = reFund(payNo, refundAmount, 0);

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
                    json.setData(detailedRefund.getId());
                } else {
                    json.setOk(false);
                    json.setMessage("API无返回信息");
                }
            } else if ("2".equals(payType)) {
                RefundResultInfo refundResultInfo = new RefundResultInfo();
                refundResultInfo.setTotalRefundedAmount(refundAmount);
                refundResultInfo.setState("readyRefund");

                REFUNDLOG.info("begin refund:" + refundResultInfo.toString());
                refundResultInfoMapper.insertSelective(refundResultInfo);

                Double amountDouble = Double.valueOf(refundAmount) * 100D;
                StripeService stripeService = new StripeServiceImpl();

                com.stripe.model.Refund detailedRefund = stripeService.refund(payNo, amountDouble.longValue(), true);

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
                json.setMessage("无支付信息，请确认支付类型");
            }
        } catch (Exception e) {
            REFUNDLOG.error("payNo:"+payNo+",refundAmount:" + refundAmount + ",error:",e);
            e.printStackTrace();
            json.setOk(false);
            json.setMessage("API退款失败，原因：" + e.getMessage());
        }
        return json;
    }

    @Override
    public JsonResult refundByMq(String orderNo, String amountMoney) {
        JsonResult json = new JsonResult();
        try{
            OrderBean orderInfo = orderInfoDao.getOrderInfo(orderNo, null);
            if (orderInfo != null) {
                String today = DateFormatUtil.getWithSeconds(new Date());
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
                        refundResultInfo.setCreateTime(today);
                        refundResultInfo.setUpdateTime(today);

                        REFUNDLOG.info("begin refund:" + refundResultInfo.toString());
                        refundResultInfoMapper.insertSelective(refundResultInfo);
                        DetailedRefund detailedRefund;

                        // 新的判断新老账号方法
                        boolean isOld = orderInfoDao.checkIsOldPayPal(orderNo);
                        if(isOld){
                            detailedRefund = refundByMq(saleId, amountMoney,1, json);
                        }else{
                             detailedRefund = refundByMq(saleId, amountMoney,0, json);
                        }

                        // detailedRefund = refundByLocal(json);

                        if (json.isOk()) {
                            refundResultInfo.setRefundFromTransactionFee(detailedRefund.getRefundFromTransactionFee().getValue());
                            refundResultInfo.setRefundFromReceivedAmount(detailedRefund.getRefundFromReceivedAmount().getValue());
                            refundResultInfo.setTotalRefundedAmount(detailedRefund.getTotalRefundedAmount().getValue());
                            refundResultInfo.setRefundid(detailedRefund.getId());
                            refundResultInfo.setAmountTotal(detailedRefund.getAmount().getTotal());
                            refundResultInfo.setState(detailedRefund.getState());
                            refundResultInfo.setSaleId(detailedRefund.getSaleId());
                            // refundResultInfo.setParentPayment(detailedRefund.getParentPayment());
                            refundResultInfo.setCreateTime(detailedRefund.getCreateTime());
                            refundResultInfo.setUpdateTime(detailedRefund.getUpdateTime());
                            refundResultInfo.setInfo(detailedRefund.toString().getBytes());
                            REFUNDLOG.info("refund result:" + refundResultInfo.toString());
                            refundResultInfoMapper.insertSelective(refundResultInfo);
                            json.setOk(true);
                            json.setData(detailedRefund);
                            json.setTotal(0L);
                            json.setMessage("交易号[" + detailedRefund.getId() + "]");
                        }
                    } else {
                        json.setOk(false);
                        json.setMessage("订单不符合退款条件!");
                    }
                } else if ("stripe".equals(payType)) {
                    json.setOk(false);
                    json.setMessage("无Stripe退款，请退余额!");
                } else {
                    json.setOk(false);
                    json.setMessage("无支付信息，请确认订单号是否错误");
                }
            } else {
                json.setOk(false);
                json.setMessage("没有查询到该订单!");
            }
        }catch (Exception e){
            e.printStackTrace();
            REFUNDLOG.error("payNo:"+orderNo+",refundAmount:" + amountMoney + ",error:",e);
            json.setOk(false);
            json.setMessage("API退款失败，原因：" + e.getMessage());
        }
        return json;
    }


    private DetailedRefund refundByLocal(JsonResult json){
        json.setOk(true);
        DetailedRefund detailedRefund = new DetailedRefund();
        detailedRefund.setTotalRefundedAmount(new Currency("USD", "50.00"));
        detailedRefund.setId("3PE956036M7631605");
        detailedRefund.setAmount(new Amount("USD", "50.00"));
        detailedRefund.setRefundFromTransactionFee(new Currency("USD", "2.20"));
        detailedRefund.setRefundFromReceivedAmount(new Currency("USD", "47.80"));
        detailedRefund.setCreateTime("2019-12-13T03:14:48Z");
        detailedRefund.setUpdateTime("2019-12-13T03:14:48Z");
        detailedRefund.setState("completed");
        detailedRefund.setDescription("parent_payment\": \"PAYID-LWOVFOQ76W04870DD140644J");
        return detailedRefund;
    }

    /**
     * 远程调用RPC执行退款
     * @param saleId
     * @param amountMoney
     * @param isOld
     * @param json
     * @return
     */
    private DetailedRefund refundByMq(String saleId, String amountMoney,int isOld, JsonResult json) {

        // 两次握手
        // 1. {'step':1}
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("step", 1);

        String uuidJson = SendMQ.sendRefundByRPC(jsonParam);
        JSONObject rsJson = JSONObject.parseObject(uuidJson);
        if (rsJson != null && "200".equals(rsJson.getString("code"))
                && StringUtils.isNotBlank(rsJson.getString("data"))) {
            jsonParam.clear();
            String uuid = rsJson.getString("data");
            // 2. {'step':2,'uuid':'%s','saleid':'%s','amount':%s,'md5':'%s'}
            String md5 = MD5Util.generate(uuid + saleId + amountMoney);
            jsonParam.put("step", 2);
            jsonParam.put("uuid", uuid);
            jsonParam.put("saleid", saleId);
            jsonParam.put("amount", amountMoney);
            jsonParam.put("md5", md5);

            rsJson.clear();
            String resultStr = SendMQ.sendRefundByRPC(jsonParam);
            if (StringUtils.isNotBlank(resultStr)) {
                REFUNDLOG.info("saleId:"+saleId+",refundAmount:" + amountMoney + ",RPC result【" + resultStr + "】");
                rsJson = JSONObject.parseObject(resultStr);
                System.err.println(rsJson);
                if (rsJson != null && "200".equals(rsJson.getString("code"))
                        && StringUtils.isNotBlank(rsJson.getString("data"))) {

                    REFUNDLOG.info("saleId:"+saleId+",refundAmount:" + amountMoney + ",RPC result rsJson:" + rsJson);
                    JSONObject refundJson = rsJson.getJSONObject("data");

                    DetailedRefund detailedRefund = JSONObject.parseObject(rsJson.getString("data"),DetailedRefund.class);
                    /*JSONObject tempAmount = refundJson.getJSONObject("amount");
                    DetailedRefund detailedRefund = new DetailedRefund();
                    detailedRefund.setTotalRefundedAmount(new Currency(tempAmount.getString("currency"), tempAmount.getString("total")));
                    detailedRefund.setId(refundJson.getString("id"));
                    tempAmount = refundJson.getJSONObject("refund_from_transaction_fee");
                    detailedRefund.setRefundFromTransactionFee(new Currency(tempAmount.getString("currency"), tempAmount.getString("value")));
                    tempAmount = refundJson.getJSONObject("refund_from_received_amount");
                    detailedRefund.setRefundFromReceivedAmount(new Currency(tempAmount.getString("currency"), tempAmount.getString("value")));
                    detailedRefund.setCreateTime(refundJson.getString("createTime"));
                    detailedRefund.setUpdateTime(refundJson.getString("updateTime"));
                    detailedRefund.setState(refundJson.getString("status"));
                    detailedRefund.setDescription(refundJson.getString("statusDetails"));*/
                    json.setOk(true);
                    return detailedRefund;
                } else {
                    REFUNDLOG.error("saleId:"+saleId+",refundAmount:" + amountMoney + ",RPC result null:" + rsJson);
                    if(rsJson != null){
                        json.setMessage(rsJson.toJSONString());
                    }
                    json.setOk(false);
                    return null;
                }

            } else {
                REFUNDLOG.error("saleId:"+saleId+",refundAmount:" + amountMoney + ",RPC result null:" + rsJson);
                json.setOk(false);
                json.setMessage("saleId:"+saleId+",refundAmount:" + amountMoney + ",RPC result null:" + rsJson);
                return null;
            }
        } else {
            REFUNDLOG.error("saleId:"+saleId+",refundAmount:" + amountMoney + ",rsJson:" + rsJson);
            json.setOk(false);
            json.setMessage("saleId:"+saleId+",refundAmount:" + amountMoney + ",rsJson:" + rsJson);
            return null;
        }
    }


}