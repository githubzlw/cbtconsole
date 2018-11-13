package com.cbt.change.service;

import com.cbt.change.bean.OrderDetailsChangeRecords;
import com.cbt.change.bean.OrderDetailsChangeRecordsQuery;
import com.cbt.change.dao.OrderDetailsChangeRecordsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderDetailsChangeRecordsServiceImpl implements OrderDetailsChangeRecordsService {

	@Resource
	private OrderDetailsChangeRecordsMapper orderDetailsMapper;

	@Override
	public List<OrderDetailsChangeRecords> queryForList(OrderDetailsChangeRecordsQuery changeRecordsQuery) {
		return orderDetailsMapper.queryForList(changeRecordsQuery);
	}

	@Override
	public int queryForCount(OrderDetailsChangeRecordsQuery changeRecordsQuery) {
		return orderDetailsMapper.queryForCount(changeRecordsQuery);
	}
	
	@Override
	public void insertChangeRecords(OrderDetailsChangeRecords changeRecords) {
		orderDetailsMapper.insertChangeRecords(changeRecords);
	}

	

}
