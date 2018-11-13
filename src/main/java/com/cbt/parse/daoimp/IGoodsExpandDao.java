package com.cbt.parse.daoimp;

import com.cbt.parse.bean.GoodsExpandBean;
import com.cbt.parse.bean.SqlBean;

import java.util.ArrayList;
import java.util.HashMap;

public interface IGoodsExpandDao {
	
	public int addAll(GoodsExpandBean bean);
	
	public int updateAll(GoodsExpandBean bean);
	
	
	public int updateValid(String url, int valid);


	public int addNote(String noteurl, String url);

	public int updateNote(String noteurl, String url);
	
	public ArrayList<HashMap<String, String>> queryNote(String url);
	
	public ArrayList<GoodsExpandBean> queryExist(String url);
	public ArrayList<GoodsExpandBean> querySearch(ArrayList<SqlBean> bean);
	public ArrayList<GoodsExpandBean> queryDate(String url);
	
}
