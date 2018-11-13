/**
 * 
 */
package com.cbt.service.impl;

import com.cbt.dao.IDiscountTypeDao;
import com.cbt.service.IDiscountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 *
 */
@Service
public class DiscountTypeServiceImpl implements IDiscountTypeService {
	
	@Autowired
	private IDiscountTypeDao discountTypeDao;

	/**
	 * 类别折扣上限查询
	 */
	@Override
	public List<HashMap<String, String>> queryForAliCategory(String cid,String category) {
		return discountTypeDao.queryForAliCategory(cid, category);
	}

	/**
	 * 类别折扣上限修改
	 */
	@Override
	public int updateDiscountTypeByCid(String cid,String category,double maxDiscount) {
		return discountTypeDao.updateDiscountTypeByCid(cid,category,maxDiscount);
	}

}
