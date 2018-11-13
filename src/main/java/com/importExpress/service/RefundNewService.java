package com.importExpress.service;

import com.cbt.bean.OrderBean;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.bean.PaymentDetails;
import com.importExpress.pojo.RefundDetailsBean;
import com.importExpress.pojo.RefundNewBean;

import java.util.List;

public interface RefundNewService {

    /**
     * 分页查询退款信息
     *
     * @param userId
     * @param payPalEmail
     * @param beginTime
     * @param endTime
     * @param type
     * @param state
     * @param adminId
     * @param startNum
     * @param limitNum
     * @return
     */
    List<RefundNewBean> queryForList(int userId, String payPalEmail, String beginTime, String endTime, int type,
                                     int state, int appMoney, int adminId, int startNum, int limitNum);

    /**
     * 统计退款信息总数
     *
     * @param userId
     * @param payPalEmail
     * @param beginTime
     * @param endTime
     * @param type
     * @param state
     * @param adminId
     * @return
     */
    int queryForListCount(int userId, String payPalEmail, String beginTime, String endTime, int type, int state, int adminId, int appMoney);


    /**
     * 根据ID查询退款数据
     *
     * @param refundId
     * @return
     */
    RefundNewBean queryRefundById(int refundId);

    /**
     * 根据退款ID查询退款详情信息
     *
     * @param refundId
     * @return
     */
    List<RefundDetailsBean> queryRefundDetailsByRefundId(int refundId);

    /**
     * 设置退款申请流程状态和备注信息
     *
     * @param detailsBean
     * @return
     */
    boolean setAndRemark(RefundDetailsBean detailsBean);

    /**
     * 查询所有支付订单信息
     *
     * @param userId
     * @param type   0全部 1最近一个月 2最近两个月 3最近三个月
     * @return
     */
    List<PaymentBean> queryPaymentInfoByUserId(int userId, int type);


    /**
     * 根据客户ID查询退款信息
     *
     * @param userId
     * @return
     */
    List<RefundNewBean> queryRefundByUserId(int userId);


    /**
     * 根据userId或者orderNo查询订单信息
     *
     * @param userId
     * @param orderNo
     * @return
     */
    List<OrderBean> queryOrderInfoByOrderNoOrUserId(int userId, String orderNo);


    /**
     * 根据userId或者orderNo查询支付信息
     *
     * @param userId
     * @param orderNo
     * @return
     */
    List<PaymentDetails> queryPaymentInfoByOrderNoOrUserId(int userId, String orderNo);

}
