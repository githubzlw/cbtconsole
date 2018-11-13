package com.cbt.website.service;

import com.cbt.website.bean.NoGoodsPushBean;

import java.util.List;

public interface INoGoodsPushService {

	public List<NoGoodsPushBean> selectNoGoods();
	
	public int pushGoods(String carUrl);
}
