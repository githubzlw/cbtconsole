package com.cbt.systemcode.service;

import com.cbt.pojo.SystemCode;
import com.cbt.pojo.SystemCodeExample;
import com.cbt.systemcode.dao.SystemCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemCodeServiceImpl implements SystemCodeService {

	@Autowired
	private SystemCodeMapper SystemCodeDao;
	
	@Override
	public List<SystemCode> selectByExample(SystemCodeExample example) {
		return SystemCodeDao.selectByExample(example);
	}

}