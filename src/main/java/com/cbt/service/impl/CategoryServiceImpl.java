package com.cbt.service.impl;

import com.cbt.bean.Category1688Bean;
import com.cbt.service.CategoryService;
import com.importExpress.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

}
