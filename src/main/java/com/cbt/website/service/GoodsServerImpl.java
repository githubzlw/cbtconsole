package com.cbt.website.service;

import com.cbt.bean.Goods;
import com.cbt.bean.SpiderBean;
import com.cbt.website.dao.GoodsDaoImpl;
import com.cbt.website.dao.IGoodsDao;

import java.util.List;

public class GoodsServerImpl implements IGoodsServer {
	private IGoodsDao goodsDao=new GoodsDaoImpl();
	@Override
	public List<Goods> getGoodsList() {
		List<Goods> goodsList = goodsDao.getGoodsList();
		return goodsList;
	}
	@Override
	public List<Goods> getSearchConditionList(String condition) {
		List<Goods> conditionList = goodsDao.getConditionList(condition);
		return conditionList;
	}	
	@Override
	public List<Goods> getSearchOrderList(int order, String condition) {
		List<Goods> orderList = goodsDao.getOrderList(order, condition);
		return orderList;
	}
	@Override
	public List<Goods> getSearchScopeList(int order, double minprice,
			double maxprice, String condition) {
		List<Goods> orderList = goodsDao.getSearchScopeList(order, minprice, maxprice, condition);
		return orderList;
	}
	@Override
	public List<SpiderBean> getSpiderBeans(int userid, String stremail) {
		return goodsDao.getSpiderBeans(userid,stremail);
	}
	
	@Override
	public int updateVolumeOrWeight(String f,int id,String vw,String total) {
		return goodsDao.updateVolumeOrWeight(f, id, vw, total);
	}

}
