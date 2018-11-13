package com.cbt.parse.daoimp;

import com.cbt.parse.bean.PvidBean;

import java.util.ArrayList;

public interface IOnePvidDao {
	
	public int add(PvidBean bean);
	
	public int update(PvidBean bean);
	
    public ArrayList<PvidBean> query(String catid, String keyword);

    public ArrayList<PvidBean> queryExsis(String param, String value);
    
    /**获得指定catid的搜有param
     * @param catid
     * @return
     */
    public ArrayList<PvidBean> getParamGroup(String catid);

}
