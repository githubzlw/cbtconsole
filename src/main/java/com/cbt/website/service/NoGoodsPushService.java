package com.cbt.website.service;

import com.cbt.website.bean.NoGoodsPushBean;
import com.cbt.website.dao.INoGoodsPushDao;
import com.cbt.website.dao.NoGoodsPushDao;

import java.util.List;

public class NoGoodsPushService implements INoGoodsPushService {
	
	private INoGoodsPushDao gdao = new NoGoodsPushDao();
	
	@Override
	public List<NoGoodsPushBean> selectNoGoods() {
		return gdao.selectNoGoods();
	}

	@Override
	public int pushGoods(String carUrl) {
		return gdao.pushGoods(carUrl);
	}
}
