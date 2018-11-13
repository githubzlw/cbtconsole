package com.cbt.parse.daoimp;

import com.cbt.parse.bean.KeysPageBean;

public interface IKeysPageDao {
	
	public int add(KeysPageBean bean);
	public int updateValid(String rectName, int valid);
	public String querry(String keyword);

}
