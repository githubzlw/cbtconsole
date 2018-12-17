package com.importExpress.mapper;

import com.cbt.bean.OrderBean;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.bean.PaymentDetails;
import com.importExpress.pojo.RefundDetailsBean;
import com.importExpress.pojo.RefundNewBean;
import com.importExpress.pojo.RefundResultInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RefundMapper {

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
    List<RefundNewBean> queryForList(@Param("userId") int userId, @Param("payPalEmail") String payPalEmail, @Param("beginTime") String beginTime,
                                     @Param("endTime") String endTime, @Param("type") int type, @Param("state") int state,
                                     @Param("appMoney") int appMoney, @Param("adminId") int adminId, @Param("startNum") int startNum, @Param("limitNum") int limitNum);

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
    int queryForListCount(@Param("userId") int userId, @Param("payPalEmail") String payPalEmail, @Param("beginTime") String beginTime,
                          @Param("endTime") String endTime, @Param("type") int type, @Param("state") int state, @Param("appMoney") int appMoney, @Param("adminId") int adminId);


    /**
     * 根据ID查询退款数据
     * @param refundId
     * @return
     */
    RefundNewBean queryRefundById(@Param("refundId") int refundId);


    /**
     * 根据退款ID查询退款详情信息
     *
     * @param refundId
     * @return
     */
    List<RefundDetailsBean> queryRefundDetailsByRefundId(@Param("refundId") int refundId);

    /**
     * 更新退款信息
     *
     * @param detailsBean
     * @return
     */
    int updateRefundState(RefundDetailsBean detailsBean);

    /**
     * 插入退款详情信息
     *
     * @param detailsBean
     * @return
     */
    int insertIntoRefundDetails(RefundDetailsBean detailsBean);


    /**
     * 查询所有支付订单信息
     *
     * @param userId
     * @param type   0全部 1最近一个月 2最近两个月 3最近三个月
     * @return
     */
    List<PaymentBean> queryPaymentInfoByUserId(@Param("userId") int userId, @Param("type") int type);


    /**
     * 根据客户ID查询退款信息
     *
     * @param userId
     * @return
     */
    List<RefundNewBean> queryRefundByUserId(@Param("userId") int userId);


    /**
     * 根据userId或者orderNo查询订单信息
     * @param userId
     * @param orderNo
     * @return
     */
    List<OrderBean> queryOrderInfoByOrderNoOrUserId(@Param("userId") int userId, @Param("orderNo") String orderNo);


    /**
     * 根据userId或者orderNo查询支付信息
     * @param userId
     * @param orderNo
     * @return
     */
    List<PaymentDetails> queryPaymentInfoByOrderNoOrUserId(@Param("userId") int userId, @Param("orderNo") String orderNo);


    /**
     * 分页查询退款结果数据
     * @param resultInfo
     * @return
     */
    List<RefundResultInfo> queryForRefundResultList(RefundResultInfo resultInfo);

    /**
     * 查询款结果数据总数
     * @param resultInfo
     * @return
     */
    int queryForRefundResultListCount(RefundResultInfo resultInfo);

}
