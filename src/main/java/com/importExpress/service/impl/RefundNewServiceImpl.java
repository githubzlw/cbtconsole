package com.importExpress.service.impl;

import com.cbt.bean.OrderBean;
import com.cbt.website.bean.PaymentBean;
import com.cbt.website.bean.PaymentDetails;
import com.importExpress.mapper.RefundMapper;
import com.importExpress.pojo.RefundDetailsBean;
import com.importExpress.pojo.RefundNewBean;
import com.importExpress.pojo.RefundResultInfo;
import com.importExpress.service.RefundNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RefundNewServiceImpl implements RefundNewService {

    @Autowired
    private RefundMapper refundMapper;


    @Override
    public List<RefundNewBean> queryForList(int userId, String payPalEmail, String beginTime, String endTime,
                                            int type, int state, int appMoney, int adminId, int startNum, int limitNum) {
        return refundMapper.queryForList(userId, payPalEmail, beginTime, endTime, type, state, appMoney, adminId, startNum, limitNum);
    }

    @Override
    public int queryForListCount(int userId, String payPalEmail, String beginTime, String endTime, int type, int state, int adminId, int appMoney) {
        return refundMapper.queryForListCount(userId, payPalEmail, beginTime, endTime, type, state, adminId, appMoney);
    }

    @Override
    public RefundNewBean queryRefundById(int refundId) {
        return refundMapper.queryRefundById(refundId);
    }

    @Override
    public List<RefundDetailsBean> queryRefundDetailsByRefundId(int refundId) {
        return refundMapper.queryRefundDetailsByRefundId(refundId);
    }

    @Override
    @Transactional
    public boolean setAndRemark(RefundDetailsBean detailsBean) {
        refundMapper.insertIntoRefundDetails(detailsBean);
        return refundMapper.updateRefundState(detailsBean) > 0;
    }

    @Override
    public List<PaymentBean> queryPaymentInfoByUserId(int userId, int type) {
        return refundMapper.queryPaymentInfoByUserId(userId, type);
    }

    @Override
    public List<RefundNewBean> queryRefundByUserId(int userId) {
        return refundMapper.queryRefundByUserId(userId);
    }

    @Override
    public List<OrderBean> queryOrderInfoByOrderNoOrUserId(int userId, String orderNo) {
        return refundMapper.queryOrderInfoByOrderNoOrUserId(userId, orderNo);
    }

    @Override
    public List<PaymentDetails> queryPaymentInfoByOrderNoOrUserId(int userId, String orderNo) {
        return refundMapper.queryPaymentInfoByOrderNoOrUserId(userId, orderNo);
    }

    @Override
    public List<RefundResultInfo> queryForRefundResultList(RefundResultInfo resultInfo) {
        return refundMapper.queryForRefundResultList(resultInfo);
    }

    @Override
    public int queryForRefundResultListCount(RefundResultInfo resultInfo) {
        return refundMapper.queryForRefundResultListCount(resultInfo);
    }

}
