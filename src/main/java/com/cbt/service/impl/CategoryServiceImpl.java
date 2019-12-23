package com.cbt.service.impl;

import com.cbt.bean.Category1688Bean;
import com.cbt.bean.CategoryAllBean;
import com.cbt.bean.CategoryBean;
import com.cbt.service.CategoryService;
import com.importExpress.mapper.CategoryMapper;

import com.importExpress.utli.GoodsInfoUpdateOnlineUtil;
import com.importExpress.utli.RunSqlModel;
import com.importExpress.utli.SendMQ;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	@Override
	public List<CategoryBean> queryCategoryList(CategoryBean categoryBean) {
		return categoryMapper.queryCategoryList(categoryBean);
	}

	@Override
	public int queryCategoryListCount(CategoryBean categoryBean) {
		return categoryMapper.queryCategoryListCount(categoryBean);
	}

	@Override
	public CategoryBean queryCategoryById(String cid) {
		return categoryMapper.queryCategoryById(cid);
	}

	@Override
	public List<CategoryBean> queryChildCategory(List<String> list) {
		return categoryMapper.queryChildCategory(list);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int batchUpdateCategory(List<CategoryBean> list) {
		return categoryMapper.batchUpdateCategory(list);
	}

    @Override
    public List<CategoryAllBean> queryAllCategoryByParam(String cid,int startNum,int limitNum) {
        return categoryMapper.queryAllCategoryByParam(cid,startNum,limitNum);
    }

	@Override
	public int queryAllCategoryByParamCount(String cid, int startNum, int limitNum) {
		return categoryMapper.queryAllCategoryByParamCount(cid, startNum, limitNum);
	}

	@Override
    public int updateChangeAllBeanInfo(CategoryAllBean categoryBean) {
        return categoryMapper.updateChangeAllBeanInfo(categoryBean);
    }

	@Override
	public int insertIntoCatidInfo(CategoryAllBean categoryBean) {
		CategoryBean oldParentBean = categoryMapper.queryCategoryById(categoryBean.getParentId());
		synchronized (CategoryServiceImpl.class) {
			// 9 + lv+ 数据库ID作为新的类别
			int maxId = categoryMapper.queryMaxIdByCatidInfo();
			String nwCaitd = "9" + oldParentBean.getLv() + maxId;
			categoryBean.setCategoryId(nwCaitd);
			categoryBean.setPath(oldParentBean.getPath() + "," + nwCaitd);
			categoryBean.setLv(oldParentBean.getLv() + 1);
			categoryMapper.insertIntoCatidInfo(categoryBean);
			// MQ更新线上
			String sql = "insert into 1688_category(id,lv,category_id,name,path,parent_id,en_name,description) values(";
			sql += categoryBean.getId() + ",";
			sql += categoryBean.getLv() + ",";
			sql += "'" + categoryBean.getCategoryId() + "',";
			sql += "'" + GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(categoryBean.getName()) + "',";
			sql += "'" + categoryBean.getPath() + "',";
			sql += "'" + categoryBean.getParentId() + "',";
			sql += "'" + GoodsInfoUpdateOnlineUtil.checkAndReplaceQuotes(categoryBean.getEnName()) + "',";
			sql += "'" + categoryBean.getDescription() + "')";
			SendMQ.sendMsg(new RunSqlModel(sql));
		}
		return categoryBean.getId();
	}

}
