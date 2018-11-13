package com.cbt.parse.daoimp;

import com.cbt.parse.bean.PvidBean;

import java.util.ArrayList;

public interface IPvidDao {
	public int add(PvidBean bean);
	public int update(PvidBean bean);
	public ArrayList<PvidBean> query(String pvid);
	public int queryExsis(String pvid);
}
