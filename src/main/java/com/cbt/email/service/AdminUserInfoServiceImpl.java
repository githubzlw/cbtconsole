package com.cbt.email.service;

import com.cbt.email.dao.AdminUserDaoImpl;
import com.cbt.email.dao.IAdminUserDao;


public class AdminUserInfoServiceImpl implements IAdminUserService{
	IAdminUserDao dao = new AdminUserDaoImpl();
	@Override
	public String getemail(String email) {
		
		return dao.getemail(email);
	}
	@Override
	public int getId(String email) {
		
		return dao.getId(email);
	}
	@Override
	public String getname(String email) {
		
		return dao.getname(email);
	}
	@Override
	public int getid(int id) {
		
		return dao.getid(id);
	}

}
