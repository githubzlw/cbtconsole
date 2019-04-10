package com.cbt.warehouse.service;


import com.cbt.bean.Subscribe;
import com.cbt.warehouse.dao.SubScribeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubScribeServiceImpl implements SubScribeService{
	@Autowired
	private SubScribeMapper subScribeMapper;

	public List<Subscribe> queryAllSubscribe(Subscribe sbean){
		return subScribeMapper.queryAllSubscribe(sbean);
	}
	public int queryAllSubscribeConut(Subscribe sbean){
		return subScribeMapper.queryAllSubscribeConut(sbean);
	};
}