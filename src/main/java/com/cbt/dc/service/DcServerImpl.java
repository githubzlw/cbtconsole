package com.cbt.dc.service;

import com.cbt.dc.dao.DcDao;
import com.cbt.dc.dao.DcDaoImpl;
import com.cbt.parse.bean.SearchGoods;

import java.util.ArrayList;

public class DcServerImpl implements DcServer {
	
	private DcDao dcDao = new DcDaoImpl();
	
	@Override
	public void changeBuyGoods(String url, String price, int goodssum) {
		// TODO Auto-generated method stub
		dcDao.changeBuyGoods(url, price, goodssum);
	}

	@Override
	public ArrayList<SearchGoods> getPopProducts(String keywords, String catList,String catid) {
		// TODO Auto-generated method stub
		ArrayList<SearchGoods> bgbList=dcDao.getPopProducts(keywords, catList, catid);
		return bgbList;
	}

}
