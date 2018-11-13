package com.importExpress.service.impl;

import com.importExpress.mapper.CategoryResearchMapper;
import com.importExpress.pojo.FineCategory;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.pojo.TabSeachPagesDetailBean;
import com.importExpress.service.CategoryResearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CategoryResearchServiceImpl implements CategoryResearchService {
	
	@Autowired
	private CategoryResearchMapper categoryResearchMapper;

	@Transactional
	@Override
	public int insert(TabSeachPageBean bean) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.insert(bean);
	}

	@Override
	public List<TabSeachPageBean> list(int pid) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.list(pid);
	}

	@Override
	public int delete(Integer id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.delete(id);
	}

	@Override
	public TabSeachPageBean get(Integer id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.get(id);
	}

	@Override
	public int update(TabSeachPageBean bean) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.update(bean);
	}

	@Override
	public int insertDetail(FineCategory bean) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.insertDetail(bean);
	}

	@Override
	public List<TabSeachPagesDetailBean> detailList(Integer sid) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.detailList(sid);
	}

	@Override
	public int updateDetail(TabSeachPagesDetailBean bean) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.updateDetail(bean);
	}

	@Override
	public int deleteDetail(Integer id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.deleteDetail(id);
	}

	@Override
	public TabSeachPagesDetailBean getDetail(Integer id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.getDetail(id);
	}

	@Override
	public int getWordsCount(String keyword) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.getWordsCount(keyword);
	}
	
	@Override
	public int getWordsCount1(String keyword, Integer id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.getWordsCount1(keyword, id);
	}

	@Override
	public int getNameCount(String name,Integer sid) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.getNameCount(name,sid);
	}

	@Override
	public int getNameCount1(String name, Integer sid, Integer id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.getNameCount1(name, sid, id);
	}

	@Override
	public int updateIsshow(Integer isshow, Integer id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.updateIsshow(isshow, id);
	}

	@Override
	public List<FineCategory> detailFineCategoryList(int sid) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.detailFineCategoryList(sid);
	}
	
	@Override
	public FineCategory getOneFineCategory(int id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.getOneFineCategory(id);
	}

	@Override
	public int updateFineCategory(FineCategory fineCategory) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.updateFineCategory(fineCategory);
	}

	@Override
	public int deleteFineCategory(int id) {
		// TODO Auto-generated method stub
		return categoryResearchMapper.deleteFineCategory(id);
	}

	@Override
	public List<Map<String, Object>> search1688Category() {
		// TODO Auto-generated method stub
		return categoryResearchMapper.search1688Category();
	}

	@Override
	public void deleCate(int id) {
		//所有的移动到未分类下
		categoryResearchMapper.move(id);
				//删除该分类节点
		categoryResearchMapper.delete(id);
		
	}

	

}
