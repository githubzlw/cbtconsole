package com.importExpress.service;

import com.importExpress.pojo.OrderCancelApproval;
import com.importExpress.pojo.OrderCancelApprovalDetails;

import java.util.List;

public interface OrderCancelApprovalService {

    /**
     * 分页查询取消订单申请
     * @param cancelApproval
     * @return
     */
    List<OrderCancelApproval> queryForList(OrderCancelApproval cancelApproval);

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
    List<OrderCancelApprovalDetails> queryForDetailsList(int approvalId);

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

}
