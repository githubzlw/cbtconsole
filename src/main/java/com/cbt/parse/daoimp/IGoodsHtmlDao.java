package com.cbt.parse.daoimp;

import java.util.ArrayList;
import java.util.HashMap;

public interface IGoodsHtmlDao {
	public int add(String key, String file, String url);

	public int update(String key, String file, String url);
	
	public ArrayList<HashMap<String, String>> query(String key);

}
