package com.cbt.service.impl;

import com.cbt.bean.DiscountBean;
import com.cbt.dao.ClassDiscountDao;
import com.cbt.dao.impl.ClassDiscountDaoImpl;
import com.cbt.service.ClassDiscountSerice;

import java.util.List;

public class ClassDiscountSericeImpl implements ClassDiscountSerice {
	private ClassDiscountDao classDiscountDao = new ClassDiscountDaoImpl();

	@Override
	public List<DiscountBean> getDiscount(String catid,String price,String desopite) {

		return classDiscountDao.getDiscount(catid,price,desopite);
	}

	@Override
	public int add(DiscountBean bean) {
		
		return classDiscountDao.add(bean);
	}

	@Override
	public int delete(int id) {
		
		return classDiscountDao.delete(id);
	}

	@Override
	public int update(DiscountBean bean) {
		
		return classDiscountDao.update(bean);
	}

	@Override
	public int isExsis(String catid) {
		
		return classDiscountDao.isExsis(catid);
	}

}
