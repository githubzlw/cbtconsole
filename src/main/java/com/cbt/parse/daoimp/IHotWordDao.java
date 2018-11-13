package com.cbt.parse.daoimp;

import com.cbt.parse.bean.HotWordBean;

import java.util.ArrayList;

public interface IHotWordDao {
	
	public int add(HotWordBean bean);
	
	
	public int update(HotWordBean bean);
	
	public int updateValid(String url, int valid);

	public ArrayList<HotWordBean> querySearch(String hotwords);
	public HotWordBean queryGoods(String url, String pid);
	
	public int queryExsis(String hotwords);

}
