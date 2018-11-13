package com.cbt.feedback.service;

import com.cbt.feedback.bean.CustomerFeedback;
import com.cbt.feedback.bean.CustomerInfoCollection;
import com.cbt.feedback.bean.Questionnaire;
import com.cbt.feedback.dao.CustomerInfoCollectionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerInfoCollectionServiceImpl implements CustomerInfoCollectionService {

	@Resource
	private CustomerInfoCollectionMapper customerInfoCollectionMapper;

	@Override
	public List<Questionnaire> queryForList(int type, String sales, String beginDate, String endDate, String comment,
			int pageNo) {
		return customerInfoCollectionMapper.queryForList(type, sales, beginDate, endDate, comment, pageNo);
	}

	@Override
	public Long queryCount(int type, String sales, String beginDate, String endDate, String comment) {
		return customerInfoCollectionMapper.queryCount(type, sales, beginDate, endDate, comment);
	}

	@Override
	public List<CustomerInfoCollection> queryByType(int type) {
		return customerInfoCollectionMapper.queryByType(type);
	}

	@Override
	public void insertCustomerInfo(CustomerInfoCollection customerInfo) {
		customerInfoCollectionMapper.insertCustomerInfo(customerInfo);

	}

	@Override
	public void updateCustomerInfo(CustomerInfoCollection customerInfo) {
		customerInfoCollectionMapper.updateCustomerInfo(customerInfo);
	}

	@Override
	public void deleteCustomerInfo(int id) {
		customerInfoCollectionMapper.deleteCustomerInfo(id);
	}

	@Override
	public List<CustomerFeedback> queryForAllList() {
		return customerInfoCollectionMapper.queryForAllList();
	}

}
