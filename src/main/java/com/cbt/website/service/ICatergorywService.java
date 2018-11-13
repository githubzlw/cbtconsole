package com.cbt.website.service;

import com.cbt.bean.Eightcatergory;

import java.util.List;

public interface ICatergorywService {

	/**
	 * 获取八大类商品
	 * ylm
	 * @param userId
	 * 		用户ID
	 */
	public List<Eightcatergory> getCatergory(String id, String catergory, Integer type);
	
	/**
	 * 修改八大类商品
	 * ylm
	 * @param userId
	 * 		用户ID
	 */
	public int upCatergory(Eightcatergory catergory);
	
	/**
	 * 添加八大类商品
	 * ylm
	 * @param userId
	 * 		用户ID
	 */
	public int addCatergory(Eightcatergory catergory);
	
	/**
	 * 获取所有类别
	 * @return
	 */
	public List<Object> getCategoryList();
	
	/**
	 * 删除某个商品
	 * @param row
	 * @return
	 */
	public int deleteCatergory(Integer row);
}
