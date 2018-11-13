package com.cbt.systemcode.service;

import com.cbt.bean.SecondaryValidation;
import com.cbt.systemcode.dao.SecondaryValidationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondaryValidationServiceImpl implements SecondaryValidationService {

	@Autowired
	private SecondaryValidationMapper secValiMapper;

	@Override
	public List<SecondaryValidation> queryForList() {
		return secValiMapper.queryForList();
	}

	@Override
	public SecondaryValidation queryByUserId(int userid) {
		return secValiMapper.queryByUserId(userid);
	}

	@Override
	public int existsSecondaryValidation(int userid) {
		return secValiMapper.existsSecondaryValidation(userid);
	}

	@Override
	public int insertSecondaryValidation(SecondaryValidation secVali) {
		return secValiMapper.insertSecondaryValidation(secVali);
	}

	@Override
	public int updateByUserId(SecondaryValidation secVali) {
		return secValiMapper.updateByUserId(secVali);
	}

	@Override
	public int deleteByUserId(int userid) {
		return secValiMapper.deleteByUserId(userid);
	}

	@Override
	public boolean checkExistsPassword(int userid, String password) {
		return secValiMapper.checkExistsPassword(userid, password) > 0;
	}

}
