package com.importExpress.mapper;

import com.importExpress.pojo.OrderCancelApproval;
import com.importExpress.pojo.OrderCancelApprovalAmount;
import com.importExpress.pojo.OrderCancelApprovalDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderCancelApprovalMapper {

    /**
     * 分页查询取消订单申请
     * @param cancelApproval
     * @return
     */
    List<OrderCancelApproval> queryForList(OrderCancelApproval cancelApproval);

    /**
     * 单个获取
     * @param id
     * @return
     */
    OrderCancelApproval queryForSingle(@Param("id") int id);

    /**
     * 取消订单申请总数
     * @param cancelApproval
     * @return
     */
    int queryForListCount(OrderCancelApproval cancelApproval);

    /**
     * 取消订单申请审批详情
     * @param approvalId
     * @return
     */
    List<OrderCancelApprovalDetails> queryForDetailsList(@Param("approvalId") int approvalId);

    /**
     * 插入取消订单申请审批详情
     * @param approvalDetails
     * @return
     */
    int insertIntoApprovalDetails(OrderCancelApprovalDetails approvalDetails);

    /**
     * 更新取消订单申请审批状态
     * @param cancelApproval
     * @return
     */
    int updateOrderCancelApprovalState(OrderCancelApproval cancelApproval);

    /**
     * 查询整单申请退款完结后的退款金额
     * @param approvalId
     * @return
     */
    List<OrderCancelApprovalAmount> queryApprovalAmountByApprovalId(@Param("approvalId") int approvalId);

    /**
     * 插入审批完成后该订单的退款情况
     * @param approvalAmount
     * @return
     */
    int insertIntoOrderCancelApprovalAmount(OrderCancelApprovalAmount approvalAmount);
}
