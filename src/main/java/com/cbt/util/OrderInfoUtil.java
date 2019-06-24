package com.cbt.util;

import com.cbt.bean.OrderBean;
import com.cbt.bean.OrderDetailsBean;
import com.cbt.website.dao.IOrderSplitDao;
import com.cbt.website.dao.OrderSplitDaoImpl;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单拆分相关的工具类
 */
public class OrderInfoUtil {

    /**
     * 生成新的订单数据
     *
     * @param orderBean         : 原订单bean
     * @param orderBeanTemp     : 临时订单bean
     * @param splitRatio        ： 价格拆分比例,拆分比例=拆单商品的总价/原订单所有商品的总价
     * @param nwOrderNo         : 新的订单号
     * @param totalGoodsCostOld : 原商品总价
     * @param nwOrderDetails    : 新的订单详情数据
     * @return
     */
    public static OrderBean genNewOrderInfo(OrderBean orderBean, OrderBean orderBeanTemp, double splitRatio, String nwOrderNo,
                                            double totalGoodsCostOld, List<OrderDetailsBean> nwOrderDetails) {

        // 支付金额拆分逻辑：拆分比例=拆单商品的总价/原订单所有商品的总价
        // 所有的折扣金额都按照计算的拆分比例计算，新生成的订单支付金额也是按照比例分割

        double totalPayPriceOld = orderBean.getPay_price();
        // 新的订单支付金额
        double totalPayPriceNew = totalPayPriceOld * splitRatio;
        // 新商品总价
        double totalGoodsCostNew = totalGoodsCostOld * splitRatio;
        // coupon优惠
        double coupon_discount_old = orderBean.getCoupon_discount();
        // 新订单coupon优惠
        double coupon_discount_new = coupon_discount_old * splitRatio;
        // 其他优惠
        double extra_discount_old = orderBean.getExtra_discount();
        // 新订单其他优惠
        double extra_discount_new = extra_discount_old * splitRatio;
        // 等级优惠
        double grade_discount_old = orderBean.getGradeDiscount();
        // 新订单等级优惠
        double grade_discount_new = grade_discount_old * splitRatio;
        // 分享优惠
        double share_discount_old = orderBean.getShare_discount();
        // 新订单分享优惠
        double share_discount_new = share_discount_old * splitRatio;
        // 优惠金额（之前BIz等）
        double discount_amount_old = orderBean.getDiscount_amount();
        // 新订单优惠金额（之前BIz等）
        double discount_amount_new = discount_amount_old * splitRatio;
        // cash_back折扣
        double cash_back_old = orderBean.getCashback();
        // 新订单cash_back折扣
        double cash_back_new = cash_back_old * splitRatio;
        // 额外运费
        double extra_freight_old = orderBean.getExtra_freight();
        //新订单额外运费
        double extra_freight_new = extra_freight_old * splitRatio;

        //双清包税价格
        double vatBalanceOld = orderBean.getVatBalance();
        //新双清包税价格
        double vatBalanceNew = vatBalanceOld * splitRatio;

        // 店铺处理费
        double proces_singfee_old = orderBean.getProcessingfee();
        double proces_singfee_new = proces_singfee_old * splitRatio;

        // 质检费
        double actual_lwh_old = BigDecimalUtil.truncateDouble(Double.parseDouble(orderBean.getActual_lwh() == null ? "0.00" : orderBean.getActual_lwh()), 2);
        double actual_lwh_new = actual_lwh_old * splitRatio;

        // 新生成订单信息
        OrderBean odbeanNew = new OrderBean();
        odbeanNew.setVatBalance(vatBalanceNew);
        odbeanNew.setUserid(orderBean.getUserid());
        odbeanNew.setOrderNo(nwOrderNo);
        odbeanNew.setExchange_rate(orderBean.getExchange_rate());
        odbeanNew.setCoupon_discount(BigDecimalUtil.truncateDouble(coupon_discount_new, 2));
        odbeanNew.setExtra_discount(BigDecimalUtil.truncateDouble(extra_discount_new, 2));
        odbeanNew.setGradeDiscount(
                Float.valueOf(String.valueOf(BigDecimalUtil.truncateDouble(grade_discount_new, 2))));
        odbeanNew.setShare_discount(BigDecimalUtil.truncateDouble(share_discount_new, 2));
        odbeanNew.setDiscount_amount(BigDecimalUtil.truncateDouble(discount_amount_new, 2));
        odbeanNew.setProduct_cost(String.valueOf(BigDecimalUtil.truncateDouble(totalGoodsCostNew, 2)));
        odbeanNew.setCashback(cash_back_new);
        odbeanNew.setExtra_freight(extra_freight_new);
        odbeanNew.setOrderDetail(nwOrderDetails);

        odbeanNew.setProcessingfee(proces_singfee_new);
        odbeanNew.setActual_lwh(new BigDecimal(actual_lwh_new).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
        // 计算新订单支付金额
        totalPayPriceNew = new BigDecimal(odbeanNew.getProduct_cost())
                .subtract(new BigDecimal(odbeanNew.getCoupon_discount()))
                .subtract(new BigDecimal(odbeanNew.getShare_discount()))
                .subtract(new BigDecimal(odbeanNew.getDiscount_amount()))
                .subtract(new BigDecimal(odbeanNew.getGradeDiscount()))
                .subtract(new BigDecimal(odbeanNew.getCashback()))
                .subtract(new BigDecimal(odbeanNew.getExtra_discount()))
                .add(new BigDecimal(odbeanNew.getExtra_freight()))
                .add(new BigDecimal(odbeanNew.getProcessingfee()))
                .add(new BigDecimal(odbeanNew.getActual_lwh())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        odbeanNew.setPay_price(totalPayPriceNew);

        // 原始订单减去拆分比例后的值
        orderBeanTemp.setVatBalance(vatBalanceOld - vatBalanceNew);
        orderBeanTemp.setCoupon_discount(BigDecimalUtil.truncateDouble(coupon_discount_old - coupon_discount_new, 2));
        orderBeanTemp.setExtra_discount(BigDecimalUtil.truncateDouble(extra_discount_old - extra_discount_new, 2));
        orderBeanTemp.setGradeDiscount(Float
                .valueOf(String.valueOf(BigDecimalUtil.truncateDouble(grade_discount_old - grade_discount_new, 2))));
        orderBeanTemp.setShare_discount(BigDecimalUtil.truncateDouble(share_discount_old - share_discount_new, 2));
        orderBeanTemp.setDiscount_amount(BigDecimalUtil.truncateDouble(discount_amount_old - discount_amount_new, 2));
        orderBeanTemp.setProduct_cost(
                String.valueOf(BigDecimalUtil.truncateDouble(totalGoodsCostOld - totalGoodsCostNew, 2)));
        orderBeanTemp.setCashback(BigDecimalUtil.truncateDouble(cash_back_old - cash_back_new, 2));
        orderBeanTemp.setExtra_freight(BigDecimalUtil.truncateDouble(extra_freight_old - extra_freight_new, 2));

        orderBeanTemp.setProcessingfee(BigDecimalUtil.truncateDouble(proces_singfee_old - proces_singfee_new, 2));
        orderBeanTemp.setActual_lwh(String.valueOf(BigDecimalUtil.truncateDouble(actual_lwh_old - actual_lwh_new, 2)));

        //拆单前订单payprice = 订单1payprice + 订单2 payprice
        orderBeanTemp.setPay_price(BigDecimalUtil.truncateDouble(totalPayPriceOld - totalPayPriceNew, 2));
        //理论上payprice
        BigDecimal needPay = new BigDecimal(orderBeanTemp.getProduct_cost())
                .subtract(new BigDecimal(orderBeanTemp.getCoupon_discount()))
                .subtract(new BigDecimal(orderBeanTemp.getShare_discount()))
                .subtract(new BigDecimal(orderBeanTemp.getDiscount_amount()))
                .subtract(new BigDecimal(orderBeanTemp.getGradeDiscount()))
                .subtract(new BigDecimal(orderBeanTemp.getCashback()))
                .subtract(new BigDecimal(orderBeanTemp.getExtra_discount()))
                .add(new BigDecimal(orderBeanTemp.getExtra_freight()))
                .add(new BigDecimal(orderBeanTemp.getProcessingfee()))
                .add(new BigDecimal(orderBeanTemp.getActual_lwh()));
        // 排除计算误差
        orderBeanTemp.setExtra_freight(new BigDecimal(orderBeanTemp.getExtra_freight())
                .add(new BigDecimal(orderBeanTemp.getPay_price()).subtract(needPay))
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return odbeanNew;
    }


    /**
     * 生成新的订单号
     *
     * @param orderNo    : 原订单号
     * @param orderBean  : 订单信息Bean
     * @param isSplitNum ： 是否数量拆单标识  0不是 1是
     * @return
     */
    public static String getNewOrderNo(String orderNo, OrderBean orderBean, int isSplitNum) {
        IOrderSplitDao splitDao = new OrderSplitDaoImpl();
        String nwOrderNo = null;
        // 拆单新生成的订单号
        String orderNo1 = null;
        if (orderNo.length() > 17) {
            OrderBean orderBean1 = null;
            if (orderNo.contains("_")) {
                String[] n = orderNo.split("_");
                String orderNo_ = n[0];
                orderBean1 = splitDao.getOrders(orderNo_);
            }
            String maxSplitOrderNo = orderBean1.getMaxSplitOrder();
            if (maxSplitOrderNo.contains("_")) {
                int splitIndex = Integer.parseInt(maxSplitOrderNo.split("_")[1]);
                String[] n = orderNo.split("_");
                orderNo1 = n[0];
                if (isSplitNum > 0) {
                    nwOrderNo = orderNo1 + "N_" + (splitIndex + 1);
                } else {
                    nwOrderNo = orderNo1 + "_" + (splitIndex + 1);
                }
            }
        } else {
            if (isSplitNum > 0) {
                nwOrderNo = orderNo + "N_1";
            } else {
                nwOrderNo = orderNo + "_1";
            }
            String maxSplitOrderNo = orderBean.getMaxSplitOrder();
            if (maxSplitOrderNo.contains("_")) {
                int splitIndex = Integer.parseInt(maxSplitOrderNo.split("_")[1]);
                if (isSplitNum > 0) {
                    nwOrderNo = orderNo + "N_" + (splitIndex + 1);
                } else {
                    nwOrderNo = orderNo + "_" + (splitIndex + 1);
                }
            }
        }
        return nwOrderNo;
    }
}
