package com.importExpress.service.impl;

import com.cbt.warehouse.util.Utility;
import com.importExpress.mapper.OrderSplitMapper;
import com.importExpress.mapper.UserNewMapper;
import com.importExpress.pojo.*;
import com.importExpress.service.OrderSplitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("orderSplitServiceImpl")
public class OrderSplitServiceImpl implements OrderSplitService {

    @Autowired
    private OrderSplitMapper orderSplitMapper;

    @Autowired
    private UserNewMapper userMapper;

    @Override
    public int splitOrder(String orderNo, String odids) {
        //查询订单和订单详情
        String[] orders = {orderNo};
        List<OrderBean> orderBeans = orderSplitMapper.getOrders(orders);
        if (orderBeans.size() == 0) {
            return 0;
        }
        OrderBean orderBean = orderBeans.get(0);
        //记录拆分日志
        String info_log = "product_cost:" + orderBean.getProduct_cost() + ",pay_price=" + orderBean.getPay_price() + ",pay_price_tow=" + orderBean.getPay_price_tow() + ",pay_price_three=" + orderBean.getPay_price_three() + ",actual_ffreight=" + orderBean.getActual_ffreight() + ",service_fee=" + orderBean.getService_fee() + ",order_ac=" + orderBean.getOrder_ac() + ",discount_amount=" + orderBean.getDiscount_amount();
        orderSplitMapper.addOrderInfoLog(orderBean.getOrderNo(), info_log);
        List<Map<String, Object>> orderDetailsList = orderSplitMapper.getOrdersDetails_split(orderNo);
        List<Object[]> orderDetails = new ArrayList<Object[]>();
        for (Map<String, Object> map : orderDetailsList) {
            Object[] objects = {(Integer) map.get("od.yourorder"), (String) map.get("od.goodsprice"), (String) map.get("total_weight"), (String) map.get("bulk_volume"), (Integer) map.get("od.id"), (Integer) map.get("purchase_state")};
            orderDetails.add(objects);
        }
        //List<Object[]> orderDetails = dao.getOrdersDetails_split(orderNo);
        double price_split = 0;//拆分产品价格
        double price = 0;//未拆分产品价格
        double width = 0;
        double width_split = 0;
        String splitId = "";
        int details_number = 0;//需采购数量
        String[] odIds_ = odids.split("@");//未勾选的商品，先出货
        int purchase_state_o = orderBean.getPurchase_number();
        //遍历订单详情，获取产品金额拆分支付费用，运费拆分，50 credit拆分，混批折扣拆分，服务费拆分
        int isSplit = 0;
        int purchase_state = 0;//原订单已采购数量
        for (int i = 0; i < orderDetails.size(); i++) {
            Object[] obj = orderDetails.get(i);
            //有采购地址，需拆分为采购中的订单
            double width_ = Double.parseDouble(obj[3].toString()) / 50;
            double weight = Double.parseDouble(obj[2].toString());
            for (int j = 0; j < odIds_.length; j++) {
                if (odIds_[j].equals(obj[4].toString())) {
                    isSplit = 1;
                    break;
                }
            }
            if (isSplit == 1) {
                //未拆分出去的，老订单，先出货商品
                price_split += Integer.parseInt(obj[0].toString()) * Double.parseDouble(obj[1].toString());
                width_split += width_ > weight ? width_ : weight;
                purchase_state += Integer.parseInt(obj[5].toString());
            } else {
                //拆分出去的，后出货商品
                splitId += obj[4] + ",";
                price += Integer.parseInt(obj[0].toString()) * Double.parseDouble(obj[1].toString());
                width += width_ > weight ? width_ : weight;
                details_number++;
            }
            isSplit = 0;
        }
        if (splitId.equals("")) {
            return 0;
        }
        double split = price / Double.parseDouble(orderBean.getProduct_cost());
        //订单运费
        double freight = width / (width + width_split) * Double.parseDouble(orderBean.getForeign_freight());
        //credit
        double credit = 0;
        if (orderBean.getOrder_ac() != 0) {
            credit = width / (width + width_split) * orderBean.getOrder_ac();
        }
        //混批折扣
        double discount_amount = split * orderBean.getDiscount_amount();
        //服务费
        double service = 0;
        if (Utility.getIsDouble(orderBean.getService_fee())) {
            service = split * orderBean.getService_fee();
        }
        //pay_price_tow已支付运费，
        double pay_price_tow = 0;
        if (Utility.getStringIsNull(orderBean.getPay_price_tow()) && !"0".equals(orderBean.getPay_price_tow())) {
            pay_price_tow = freight;
        }
        //现订单支付费用
        double pay_price = price + freight + service - discount_amount - credit;
        //pay_price_three余额抵扣
        double pay_price_three = Double.parseDouble((Utility.getStringIsNull(orderBean.getPay_price_three()) ? orderBean.getPay_price_three() : "0"));
        double pay_price_three_split = 0;
        if (pay_price_three != 0) {
            if (pay_price < pay_price_three) {
                pay_price_three_split = pay_price_three - pay_price;
                pay_price_three = pay_price;
            }
        }

        //生成另一个采购中订单
        //修改已有货源订单详情的订单号
        String orderNew = orderNo + "_1";
        OrderBean orderBean2 = new OrderBean();
        orderBean2.setMode_transport(orderBean.getMode_transport());
        orderBean2.setIp(orderBean.getIp());
        orderBean2.setUserid(orderBean.getUserid());
        orderBean2.setDeliveryTime(orderBean.getDeliveryTime());
        orderBean2.setCurrency(orderBean.getCurrency());
        orderBean2.setOrderNo(orderNew);
        orderBean2.setProduct_cost(new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        orderBean2.setPay_price(new BigDecimal(pay_price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        String foreign = new BigDecimal(Double.parseDouble(orderBean.getForeign_freight()) - freight).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        orderBean2.setForeign_freight(new BigDecimal(freight).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        orderBean2.setActual_ffreight(new BigDecimal(freight).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        orderBean2.setOrder_ac(new BigDecimal(credit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        orderBean2.setDiscount_amount(new BigDecimal(discount_amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        orderBean2.setService_fee(new BigDecimal(service).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        orderBean2.setDetails_number(details_number);
        orderBean2.setServer_update(orderBean.getServer_update());
        orderBean2.setClient_update(orderBean.getClient_update());
        if (Utility.getIsDouble(orderBean.getPay_price_tow())) {
            orderBean2.setPay_price_tow(new BigDecimal(pay_price_tow).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        orderBean2.setState(orderBean.getState());
        orderBean2.setPay_price_three(new BigDecimal(pay_price_three_split).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        orderBean2.setPurchase_number(purchase_state_o - purchase_state);
        //修改当前订单表
        orderBean.setProduct_cost(new BigDecimal(price_split).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        orderBean.setPay_price(new BigDecimal(orderBean.getPay_price() - pay_price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        orderBean.setForeign_freight(foreign);
        orderBean.setOrder_ac(new BigDecimal(orderBean.getOrder_ac() - credit).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        orderBean.setDiscount_amount(new BigDecimal(orderBean.getDiscount_amount() - discount_amount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        if (Utility.getIsDouble(orderBean.getService_fee())) {
            orderBean.setService_fee(new BigDecimal(orderBean.getService_fee() - service).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        orderBean.setDetails_number(orderBean.getDetails_number() - details_number);
        orderBean.setPurchase_number(0);
        orderBean.setActual_ffreight(foreign);
        orderBean.setPay_price_three(new BigDecimal(pay_price_three).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        if (Utility.getIsDouble(orderBean.getPay_price_tow())) {
            orderBean.setPay_price_tow(new BigDecimal(Double.parseDouble(orderBean.getPay_price_tow()) - pay_price_tow).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        }
        orderBean.setPurchase_number(purchase_state);

        int res = orderSplitMapper.upOrder(orderBean);
        if (res < 0) {
            //throw new DataAccessException();
            res = -1;
        } else {
            //修改拆分后的订单详情
            res = orderSplitMapper.upOrderDetails(orderNew, splitId.substring(0, splitId.length() - 1));
            if (res < 0) {
                //throw new DataAccessException();
                res = 0;
            }
        }

        //复制订单对应的地址到新订单
        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setOrderno(orderNo);
        orderAddress.setOrderno_new(orderNew);
        int addressid = orderSplitMapper.cpOrder_address(orderAddress);
        if (addressid != 1) {
            //throw new DataAccessException();
            addressid = 0;
        } else {
            addressid = orderAddress.getId();
        }

        //复制订单对应的确认人员到新订单
        PaymentConfirm paymentConfirm = new PaymentConfirm();
        paymentConfirm.setOrderno(orderNo);
        paymentConfirm.setOrderno_new(orderNew);
        int payconfirm = orderSplitMapper.cpOrder_Paymentconfirm(paymentConfirm);
        if (payconfirm != 1) {
            //throw new DataAccessException();
            payconfirm = 0;
        } else {
            payconfirm = paymentConfirm.getId();
        }

        //新增拆分订单
        if (res > 0) {
            orderBean2.setAddressid(addressid);
            res = orderSplitMapper.addOrderInfo(orderBean2);
            if (res < 0) {
                //throw new DataAccessException();
                res = 0;
            }
        } else {
            res = -2;
        }

        //order_change表修改
        orderSplitMapper.upOrder_change(orderNew);
        //payment表拆分
        Payment pay = new Payment();
        pay.setUserid(orderBean.getUserid());// 添加用户id
        pay.setOrderid(orderNew);// 添加订单id
        pay.setOrderdesc("order split");// 添加订单描述
        pay.setPaystatus(1);// 添加付款状态
        pay.setPaymentid("");// 添加付款流水号（paypal返回的）
        pay.setPayment_amount((float) orderBean2.getPay_price());// 添加付款金额（paypal返回的）
        pay.setPayment_cc(orderBean.getCurrency());// 添加付款币种（paypal返回的）
        pay.setPaySID("");
        pay.setPayflag("O");
        pay.setPaytype("3");
        orderSplitMapper.addPayment(pay);
        //复制采购人员到拆分订单
        OrderBuy orderBuy = new OrderBuy();
        orderBuy.setOrderid(orderNo);
        orderBuy.setOrderno_new(orderNew);
        orderSplitMapper.cpOrder_Buy(orderBuy);

        //复制订单对应的备注到新订单
        orderSplitMapper.upOrder_remarkByGooddata(orderNew);
        orderSplitMapper.upOrder_remarkByProduct(orderNew);
		
		/*1.所有 有 采购链接的 商品 就 直接 转入一个 新订单，并且转状态到 采购中
		2.已支付的产品金额按产品直接拆分
		3.客户是以前付过 运费的，我们就按照 体积重量，直接拆分 这 两个订单
		 体积重量 = 长* 宽*高 (厘米)/5000  和 实际重量对比 取 大值
		4.客户如果取消商品，而该订单 是 -split 的，就不再计算批量折扣
		5.原订单的批量折扣金额， 按照价格比例分开
		6.50美元的 运费credit的拆分*/
        return res;
    }

    @Override
    public List<OrderBean> getSplitOrder(String orderNo) {
        String[] orderNos = {orderNo, orderNo + "_1"};
        return orderSplitMapper.getOrders(orderNos);
    }

    @Override
    public List<Object[]> getSplitOrderDetails(String orderNo) {
        List<Map<String, Object>> listMap = orderSplitMapper.getOrdersDetails(orderNo);
        List<Object[]> list = new ArrayList<Object[]>();
        DecimalFormat df = new DecimalFormat("######0.00");
        for (Map<String, Object> map : listMap) {
            String price = (String) map.get("od.goodsprice");
            int number = (Integer) map.get("od.yourorder");
            double sprice = 0;
            if (Utility.getStringIsNull(price)) {
                sprice = Double.parseDouble(price);
            }
            Object[] objects = {(String) map.get("od.orderid"), (Integer) map.get("od.id"), number, df.format(number * sprice), (String) map.get("goodsname"), ((String) map.get("googs_img")).replace("50x50", "200x200"), (String) map.get("goods_type"), (String) map.get("goods_typeimg")};
            list.add(objects);
        }
        return list;
    }

    @Override
    public String getUserEmailByUserName(int userId) {
        return userMapper.getEmailById(userId);
    }

    @Override
    public List<Object[]> getMessage_error(String time, String endtime, int page) {
        List<MessageError> list = orderSplitMapper.getMessage_errorByPage(time, endtime, (page - 1) * 30, 30);
        List<Object[]> res = new ArrayList<Object[]>();
        for (MessageError messageError : list) {
            Object[] objects = {messageError.getId(), messageError.getEmail(), messageError.getError(), messageError.getId(), messageError.getCreatetime()};
            res.add(objects);
        }
        return res;
    }

    @Override
    public int getMessage_error(String time, String endtime) {
        return orderSplitMapper.getMessage_error(time, endtime);
    }

    @Override
    public int addMessage_error(String email, String error, String info) {
        MessageError messageError = new MessageError();
        messageError.setEmail(email);
        messageError.setError(error);
        messageError.setInfo(info);
        int affectedRowCount = orderSplitMapper.addMessage_error(messageError);
        if (affectedRowCount != 1) {
            //throw new DataAccessException();
            affectedRowCount = 0;
        }
        return messageError.getId();
    }

}
