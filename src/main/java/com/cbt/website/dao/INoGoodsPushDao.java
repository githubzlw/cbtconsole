package com.cbt.website.dao;

import com.cbt.website.bean.NoGoodsPushBean;

import java.util.List;

public interface INoGoodsPushDao {

	public List<NoGoodsPushBean> selectNoGoods();
	
	public int pushGoods(String carUrl);
}
