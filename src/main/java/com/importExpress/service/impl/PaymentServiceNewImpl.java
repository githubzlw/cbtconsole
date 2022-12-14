package com.importExpress.service.impl;

import com.cbt.website.bean.PayCheckBean;
import com.cbt.website.bean.PaymentDetails;
import com.cbt.website.bean.PaymentStatistics;
import com.importExpress.mapper.PaymentMapper;
import com.importExpress.service.PaymentServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceNewImpl implements PaymentServiceNew {
    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public List<PaymentStatistics> queryPaymentStatistics(PayCheckBean bean) {
    	if(!"8".equals(bean.getPaytype())) {
    		return paymentMapper.queryPaymentStatistics(bean);
    	}else {
    		return paymentMapper.queryPaymentStatistics_notWebsitData(bean);
    	}
        
    }

    @Override
    public int queryPaymentStatisticsCount(PayCheckBean bean) {
        return paymentMapper.queryPaymentStatisticsCount(bean);
    }

    @Override
    public List<PaymentDetails> queryPaymentDetails(String orderNo) {
        return paymentMapper.queryPaymentDetails(orderNo);
    }

    @Override
    public int checkIsPayPalOrStripePay(int userId, String orderNo) {
        return paymentMapper.checkIsPayPalOrStripePay(userId, orderNo);
    }
}
