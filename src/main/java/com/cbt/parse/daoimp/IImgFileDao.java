package com.cbt.parse.daoimp;

import java.util.ArrayList;

public interface IImgFileDao {
	public int add(String imgurl, String file, String url, int flag);

	public int queryExsis(String imgurl, String file, String url);

	public ArrayList<String> query(String imgurl, String url);
	

}
