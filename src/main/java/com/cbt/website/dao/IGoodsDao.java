package com.cbt.website.dao;

import com.cbt.bean.Goods;
import com.cbt.bean.SpiderBean;

import java.util.List;

public interface IGoodsDao {
	public List<Goods> getGoodsList();
	
	
	public List<Goods> getConditionList(String condition);
	
	
	public List<Goods> getOrderList(int order, String condition);

	public List<Goods> getSearchScopeList(int order, double minprice, double maxprice, String condition);

	/**
	 * 获取用户购物车信息
	 * ylm
	 * @param userId
	 * 		用户ID
	 */
	public List<SpiderBean> getSpiderBeans(int userid, String email);

	/**
	 * 用户购物车 修改体积或重量 lyb
	 */
	public int updateVolumeOrWeight(String f, int id, String vw, String total);

}


