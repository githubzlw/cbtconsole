package com.importExpress.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.importExpress.mapper.CustomComplainMapper;
import com.importExpress.service.CustomComplainService;
@Service
public class CustomComplainServiceImpl implements CustomComplainService {
	@Autowired
	private CustomComplainMapper customComplainMapper;

	@Override
	public Integer insertPidList(List<String> pidList) {
		// TODO Auto-generated method stub
		return customComplainMapper.insertPidList(pidList);
	}

	@Override
	public Integer updateComplainCount(List<String> pidList) {
		// TODO Auto-generated method stub
		return customComplainMapper.updateComplainCount(pidList);
	}

	@Override
	public List<String> selectByPidList(List<String> pidList) {
		return customComplainMapper.selectByPidList(pidList);
	}

}
