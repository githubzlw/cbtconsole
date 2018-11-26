package com.importExpress.service;

import com.importExpress.pojo.ShopUrlAuthorizedInfoPO;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.pojo.TabSeachPagesDetailBean;

import java.util.List;
import java.util.Map;

public interface TabSeachPageService {
	
	public int insert(TabSeachPageBean bean);
	
	public List<TabSeachPageBean> list(int i);
	
	public int delete(Integer id);
	
	public TabSeachPageBean get(Integer id);
	
	public int update(TabSeachPageBean bean);
	
	public List<Map<String,Object>> aliCategory();
	
	public int insertDetail(TabSeachPagesDetailBean bean);
	
	public List<TabSeachPagesDetailBean> detailList(Integer sid);
	
	public int updateDetail(TabSeachPagesDetailBean bean);
	
	public int deleteDetail(Integer id);
	
	public TabSeachPagesDetailBean getDetail(Integer id);
	
	public int getWordsCount(String keyword);
	
	public int getWordsCount1(String keyword, Integer id);

	public int getNameCount(String name, Integer sid);

	public int getNameCount1(String name, Integer sid, Integer id);

	public int updateIsshow(Integer isshow, Integer id);

	public Integer getCategoryId(String keyword);

	public void deleCate(int parseInt);

	public long updateAuthorizedInfo(ShopUrlAuthorizedInfoPO bean);

	public ShopUrlAuthorizedInfoPO queryAuthorizedInfo(String shopId);

	public List<TabSeachPageBean> queryStaticizeAll();

    boolean updateTitleAndKey(int sid);
}
