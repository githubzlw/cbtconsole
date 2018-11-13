package com.cbt.service.impl;

import com.cbt.bean.UserGradeBean;
import com.cbt.dao.UserGradeDao;
import com.cbt.dao.impl.UserGradeDaoImpl;
import com.cbt.service.UserGradeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGradeServiceImpl implements UserGradeService {
	private UserGradeDao userGradeDao = new UserGradeDaoImpl();

	@Override
	public List<UserGradeBean> getGrades() {

		return userGradeDao.getGrades();
	}

	@Override
	public int updateUserGrade(int gid, int uid) {
		
		return userGradeDao.updateGrade(gid, uid);
	}

	@Override
	public List<UserGradeBean> getGradeDiscount() {
		
		return userGradeDao.getGradeDiscount();
	}

	@Override
	public int updateGradeDiscount(int gid, double discount, int valid) {
		
		return userGradeDao.updateGradeDiscount(gid, discount, valid);
	}

	@Override
	public int isExsis(int gid) {
		
		return userGradeDao.isExsis(gid);
	}

	@Override
	public int addDicount(int gid, double discount) {
		
		return userGradeDao.addDicount(gid, discount);
	}
	
	

}
