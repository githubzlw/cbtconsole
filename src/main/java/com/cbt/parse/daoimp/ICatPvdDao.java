package com.cbt.parse.daoimp;

import com.cbt.parse.bean.CatPvdBean;

import java.util.ArrayList;

public interface ICatPvdDao {
	public int add(CatPvdBean bean);
	
	public int update(CatPvdBean bean);
	//更新类别list
	public int updateCList(CatPvdBean bean);
	//更新规格list
	public int updatePList(CatPvdBean bean);
	
	public ArrayList<CatPvdBean> query(String keywords, String catid);

}
