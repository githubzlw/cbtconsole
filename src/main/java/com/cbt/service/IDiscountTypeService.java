/**
 * 
 */
package com.cbt.service;

import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 * 类别折扣上限
 */
public interface IDiscountTypeService {

	/**
	 * 类别折扣上限查询
	 * @param cid
	 * @param category
	 * @return
	 */
	public List<HashMap<String, String>> queryForAliCategory(String cid, String category);

	/**
	 * 类别折扣上限修改
	 * @param cid
	 * @return
	 */
	public int updateDiscountTypeByCid(String cid, String category, double maxDiscount);
	
}
