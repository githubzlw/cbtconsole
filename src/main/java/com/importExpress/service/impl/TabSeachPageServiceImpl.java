package com.importExpress.service.impl;

import com.importExpress.mapper.TabSeachPageMapper;
import com.importExpress.pojo.ShopUrlAuthorizedInfoPO;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.pojo.TabSeachPagesDetailBean;
import com.importExpress.service.TabSeachPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class TabSeachPageServiceImpl implements TabSeachPageService {
	
	@Autowired
	private TabSeachPageMapper tabSeachPageMapper;

	@Transactional
	@Override
	public int insert(TabSeachPageBean bean) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.insert(bean);
	}

	@Override
	public List<TabSeachPageBean> list(int id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.list(id);
	}

	@Override
	public int delete(Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.delete(id);
	}

	@Override
	public TabSeachPageBean get(Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.get(id);
	}

	@Override
	public int update(TabSeachPageBean bean) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.update(bean);
	}

	@Override
	public List<Map<String, Object>> aliCategory() {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.aliCategory();
	}

	@Override
	public int insertDetail(TabSeachPagesDetailBean bean) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.insertDetail(bean);
	}

	@Override
	public List<TabSeachPagesDetailBean> detailList(Integer sid) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.detailList(sid);
	}

	@Override
	public int updateDetail(TabSeachPagesDetailBean bean) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.updateDetail(bean);
	}

	@Override
	public int deleteDetail(Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.deleteDetail(id);
	}

	@Override
	public TabSeachPagesDetailBean getDetail(Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getDetail(id);
	}

	@Override
	public int getWordsCount(String keyword) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getWordsCount(keyword);
	}
	
	@Override
	public int getWordsCount1(String keyword, Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getWordsCount1(keyword, id);
	}

	@Override
	public int getNameCount(String name,Integer sid) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getNameCount(name,sid);
	}

	@Override
	public int getNameCount1(String name, Integer sid, Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getNameCount1(name, sid, id);
	}

	@Override
	public int updateIsshow(Integer isshow, Integer id) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.updateIsshow(isshow, id);
	}

	@Override
	public Integer getCategoryId(String keyword) {
		// TODO Auto-generated method stub
		return tabSeachPageMapper.getCategoryId(keyword);
	}

	@Override
	public void deleCate(int id) {
		//所有的移动到未分类下
		tabSeachPageMapper.move(id);
		//删除该分类节点
		tabSeachPageMapper.delete(id);
		
	}

	@Override
	public long updateAuthorizedInfo(ShopUrlAuthorizedInfoPO bean) {
		return tabSeachPageMapper.updateAuthorizedInfo(bean);
	}

	@Override
	public ShopUrlAuthorizedInfoPO queryAuthorizedInfo(String shopId) {
		return tabSeachPageMapper.queryAuthorizedInfo(shopId);
	}

	@Override
	public List<TabSeachPageBean> queryStaticizeAll() {
		return tabSeachPageMapper.queryStaticizeAll();
	}

	

}
