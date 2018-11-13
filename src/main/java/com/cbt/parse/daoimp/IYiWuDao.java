package com.cbt.parse.daoimp;

import com.cbt.parse.bean.SqlBean;
import com.cbt.parse.bean.YiWuBean;

import java.util.ArrayList;

public interface IYiWuDao {
	
	/**添加商品数据*/
	public int addDate(YiWuBean bean);
	
	/**更新商品数据*/
	public int updateDate(YiWuBean bean);
	
	/**更改商品数据有效性*/
	public int updateValid(String url, int valid);

	/**查询商品信息  通过url或者pid*/
	public ArrayList<YiWuBean> querry(String url, String pid);

	/*商店商品集*/
	public ArrayList<YiWuBean> querryStore(String sid);

	/*搜索商品集(用于整个搜索页面用的包含价格  排序等)*/
	public ArrayList<YiWuBean> querrySQL(ArrayList<SqlBean> sql);
	/*只针对搜索词  以及 对应于aliexpress的类别*/
	public ArrayList<YiWuBean> querryAll(ArrayList<SqlBean> bean, String aid, String pid);

	/*搜索商品集（去除反关键词）*/
	public ArrayList<YiWuBean> querryFilter(ArrayList<SqlBean> bean);

	/*更新类别 （对应aliexpress）*/
	public int updateC(String cnl, String cnm, String cns, ArrayList<SqlBean> bean);
}
