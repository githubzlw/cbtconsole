package com.cbt.service.impl;

import com.cbt.bean.Category1688Bean;
import com.cbt.service.CategoryService;
import com.importExpress.mapper.CategoryMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;

	public CategoryServiceImpl() {
	}

	@Override
	public List<Category1688Bean> getList(String categoryName, String categoryId, int page) {

		return categoryMapper.getList(categoryName, categoryId,page);
	}

	@Override
	public int getListTotal(String categoryName, String categoryId) {
		
		return categoryMapper.getListTotal(categoryName, categoryId);
	}

	@Override
	public int updateCategoryname(int id, String categoryname) {
		
		return categoryMapper.updateCategoryname(id, categoryname);
	}

	@Override
	public Map<String, String> geCategoryList() {
		List<Map<String, String>> geCategoryList = categoryMapper.geCategoryList();
		Map<String, String> result = new HashMap<String, String>();
		for(Map<String, String> m : geCategoryList) {
			result.put(m.get("category_id"), m.get("en_name"));
		}
		return result;
	}

	@Override
	public String getCategoryByCatid(String catid) {
		if(StringUtils.isBlank(catid)) {
			return "";
		}
		return categoryMapper.getCategoryByCatid(catid);
	}

}
