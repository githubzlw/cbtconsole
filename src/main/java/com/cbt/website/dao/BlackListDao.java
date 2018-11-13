package com.cbt.website.dao;

import com.cbt.bean.BlackList;
import com.cbt.bean.SpiderBean;

import java.util.List;
import java.util.Set;

public interface BlackListDao {
	public int[] addBlackList(BlackList blackList, Set<String> ipSet);
	
	public boolean getBlackListCount(BlackList blackList);
	
	public int getBlackListPageCount(BlackList blackList);
	
	public List<BlackList> getBlackListPage(BlackList blackList, int pagenum, int pagesize);
	
	public int delBlackList(String ids);
	
	public BlackList getBlackList(BlackList blackList);
	
	public int modifyBlackList(BlackList blackList);
	
	public List<SpiderBean> getBlackgoods();
	
	public void addBlackgoods(String url);
}
