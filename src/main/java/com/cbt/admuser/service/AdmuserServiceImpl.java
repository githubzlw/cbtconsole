package com.cbt.admuser.service;

import com.cbt.admuser.dao.AdmuserMapper;
import com.cbt.pojo.Admuser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("AdmuserService")
public class AdmuserServiceImpl implements AdmuserService{

	@Autowired
	private AdmuserMapper admuserDao;

	@Override
	public Admuser selectByPrimaryKey(Integer id) {
		return admuserDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Admuser record) {
		return admuserDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Admuser> selectAdmuser() {
		return admuserDao.selectAdmuser();
	}
	
	


}