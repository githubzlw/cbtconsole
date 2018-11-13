package com.cbt.website.service;

import com.cbt.bean.Goods;
import com.cbt.bean.SpiderBean;

import java.util.List;

public interface IGoodsServer {
	//获取产品列表
	public List<Goods> getGoodsList();
	
	//获取关键字查询
	public List<Goods> getSearchConditionList(String condition);
	
	//排序查询
	public List<Goods> getSearchOrderList(int order, String condition);

	//价格范围
	public List<Goods> getSearchScopeList(int order, double minprice, double maxprice, String condition);

	/**
	 * 获取用户购物车信息
	 * ylm
	 * @param userId
	 * 		用户ID
	 */
	public List<SpiderBean> getSpiderBeans(int userid, String stremail);


	/**
	 * 用户购物车修改 体积、重量
	*  lyb
	 */
	public int updateVolumeOrWeight(String f, int id, String vw, String total);

}
