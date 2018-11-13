package com.cbt.website.service;

import com.cbt.bean.Eightcatergory;
import com.cbt.website.dao.CatergorywDao;
import com.cbt.website.dao.ICatergorywDao;

import java.util.List;

public class CatergorywService implements ICatergorywService {

	ICatergorywDao dao = new CatergorywDao();
	@Override
	public List<Eightcatergory> getCatergory(String id,String catergory,Integer type) {
		return dao.getCatergory(id,catergory,type);
	}

	@Override
	public int upCatergory(Eightcatergory catergory) {
		return dao.upCatergory(catergory);
	}

	@Override
	public int addCatergory(Eightcatergory catergory) {
		return dao.addCatergory(catergory);
	}

	@Override
	public List<Object> getCategoryList() {
		return dao.getCategoryList();
	}

	@Override
	public int deleteCatergory(Integer row) {
		return dao.deleteCatergory(row);
	}

	
}
