package com.cbt.share.service;

import com.cbt.share.dao.ShareDao;
import com.cbt.share.pojo.SharePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShareServiceImpl implements ShareService {
	@Autowired
	private ShareDao shareDao;
	
	//获得所有ip地址
	@Override
	public List<SharePojo> getAllIpAddress() {
		// TODO Auto-generated method stub
		return shareDao.getAllIpAddress();
	}
}
