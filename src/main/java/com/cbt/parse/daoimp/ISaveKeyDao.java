package com.cbt.parse.daoimp;

import java.util.ArrayList;
import java.util.HashMap;

public interface ISaveKeyDao {
	/*添加一条静态页面保存数据*/
	public  int addData(String keyword, String website, String file, String url, String isexists);

	/*更新一条静态页面保存数据*/
	public int updateData(String keyword, String website, String file, String url, String isexists);

	/*查询一条数据*/
	public ArrayList<HashMap<String, String>> queryData(String keyword, String website);

}
