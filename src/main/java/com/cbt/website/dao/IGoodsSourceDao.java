package com.cbt.website.dao;

import com.cbt.website.bean.GoodsSource;

public interface IGoodsSourceDao {
	public int add(GoodsSource bean);
	public int 	queryExsis(String sourceurl, String url);
	public int 	deleteGoodsSource(String goodsUrl, String goodsPurl);
	public String getGoodsDataImg(String url);
	public int delGoodsDataImg(String url);
	public int updateChaImg(String sourceurl, String url, String pImg);

}
