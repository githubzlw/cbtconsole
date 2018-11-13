package com.cbt.dc.dao;

import com.cbt.parse.bean.SearchGoods;

import java.util.ArrayList;


public interface DcDao {
	
	/**
	 * 添加购买商品
	 * @param url
	 * @param price
	 * @param goodssum
	 */
	public void changeBuyGoods(String url, String price, int goodssum);

	/**
	 * 根据传入的关键字查询热销产品
	 * @param keywords
	 * @param catList
	 * @param catid
	 * @return
	 */
	public ArrayList<SearchGoods> getPopProducts(String keywords, String catList, String catid);
}
