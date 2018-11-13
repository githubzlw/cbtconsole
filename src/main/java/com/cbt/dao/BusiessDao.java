package com.cbt.dao;

import com.cbt.bean.BusiessBean;

import java.util.List;
import java.util.Map;

public interface BusiessDao {

	public int saveBusiess(Map<String, Object> params);
	
	public int getBusiessCountPage(BusiessBean busiessBean);
	
	public List<BusiessBean> getBusiessPage(BusiessBean busiessBean, int pagenum);
	
	public BusiessBean getById(int id);
	
	public int deleteBusiess(int id);
	
	public int queryByChoiceCountPage(BusiessBean busiessBean, int type);

	public List<BusiessBean> queryByChoice(BusiessBean busiessBean, int pagenum, int type);
}
