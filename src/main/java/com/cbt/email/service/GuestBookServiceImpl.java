package com.cbt.email.service;

import com.cbt.email.dao.IGuestBookDaoImpl;
import com.cbt.email.dao.IIGuestBookDao;
import com.cbt.email.entity.GuestBook;


public class GuestBookServiceImpl implements  IGuestBookService{
	IIGuestBookDao dao = (IIGuestBookDao) new IGuestBookDaoImpl();

	@Override
	public GuestBook getall(int id) {
		
		return dao.getall(id);
	}
}
