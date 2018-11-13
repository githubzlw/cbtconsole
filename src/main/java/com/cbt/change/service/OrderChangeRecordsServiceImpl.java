package com.cbt.change.service;

import com.cbt.change.bean.OrderChangeRecords;
import com.cbt.change.bean.OrderChangeRecordsQuery;
import com.cbt.change.dao.OrderChangeRecordsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderChangeRecordsServiceImpl implements OrderChangeRecordsService {

	@Resource
	private OrderChangeRecordsMapper orderChangeMapper;

	@Override
	public List<OrderChangeRecords> queryForList(OrderChangeRecordsQuery changeRecordsQuery) {
		return orderChangeMapper.queryForList(changeRecordsQuery);
	}

	@Override
	public int queryForCount(OrderChangeRecordsQuery changeRecordsQuery) {
		return orderChangeMapper.queryForCount(changeRecordsQuery);
	}
	
	@Override
	public void insertRecords(OrderChangeRecords changeRecords) {
		orderChangeMapper.insertRecords(changeRecords);
	}

	

}
