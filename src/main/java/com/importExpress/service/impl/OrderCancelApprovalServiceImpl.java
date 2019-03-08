package com.importExpress.service.impl;

import com.cbt.dao.RefundDaoPlus;
import com.cbt.dao.impl.RefundDaoImpl;
import com.importExpress.mapper.OrderCancelApprovalMapper;
import com.importExpress.pojo.OrderCancelApproval;
import com.importExpress.pojo.OrderCancelApprovalAmount;
import com.importExpress.pojo.OrderCancelApprovalDetails;
import com.importExpress.service.OrderCancelApprovalService;
import com.importExpress.utli.NotifyToCustomerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderCancelApprovalServiceImpl implements OrderCancelApprovalService {
    @Autowired
    private OrderCancelApprovalMapper approvalMapper;

    private RefundDaoPlus refundDao = new RefundDaoImpl();

    @Override
    public List<OrderCancelApproval> queryForList(OrderCancelApproval cancelApproval) {
        return approvalMapper.queryForList(cancelApproval);
    }

    @Override
    public OrderCancelApproval queryForSingle(int id) {
        return approvalMapper.queryForSingle(id);
    }

    @Override
    public int queryForListCount(OrderCancelApproval cancelApproval) {
        return approvalMapper.queryForListCount(cancelApproval);
    }

    @Override
    public List<OrderCancelApprovalDetails> queryForDetailsList(int approvalId) {
        return approvalMapper.queryForDetailsList(approvalId);
    }

    @Override
    public int insertIntoApprovalDetails(OrderCancelApprovalDetails approvalDetails) {
        return approvalMapper.insertIntoApprovalDetails(approvalDetails);
    }

    @Override
    public int updateOrderCancelApprovalState(OrderCancelApproval cancelApproval) {
        return approvalMapper.updateOrderCancelApprovalState(cancelApproval);
    }

    @Override
    public int checkIsExistsApproval(int userId, String orderNo) {
        return refundDao.checkIsExistsApproval(userId, orderNo);
    }

    @Override
    public int insertIntoPaymentByApproval(int userId, String orderNo) {
        return refundDao.insertIntoPaymentByApproval(userId, orderNo);
    }

    @Override
    public int insertIntoOrderCancelApprovalAmount(OrderCancelApprovalAmount approvalAmount) {
        // mq更新
        String sql = "insert into order_cancel_approval_amount(approval_id,order_no,pay_type,pay_amount) " +
                "values(" + approvalAmount.getApprovalId() + ",'" + approvalAmount.getOrderNo() + "',"
                + approvalAmount.getPayType() + "," + approvalAmount.getPayAmount() + ")";
        NotifyToCustomerUtil.sendSqlByMq(sql);
        return approvalMapper.insertIntoOrderCancelApprovalAmount(approvalAmount);
    }
}
