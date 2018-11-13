package com.importExpress.service.impl;

import com.importExpress.mapper.DropShippApplyMapper;
import com.importExpress.pojo.DShippUser;
import com.importExpress.service.DropShippApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropShippApplyServiceImpl implements DropShippApplyService {

	@Autowired
	private DropShippApplyMapper dropShippApplyMapper;

	@Override
	public List<DShippUser> findAllDropShip(String userCategory,int start,
			int end) {
		return dropShippApplyMapper.findAllDropShip(userCategory,start,end);
	}

	@Override
	public int dropShiptotal(String userCategory,int start, int end) {
		return dropShippApplyMapper.dropShiptotal(userCategory,start, end);
	}

	
}
