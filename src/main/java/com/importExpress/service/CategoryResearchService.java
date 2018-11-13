package com.importExpress.service;

import com.importExpress.pojo.FineCategory;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.pojo.TabSeachPagesDetailBean;

import java.util.List;
import java.util.Map;

public interface CategoryResearchService {
	
	public int insert(TabSeachPageBean bean);
	
	public List<TabSeachPageBean> list(int i);
	
	public int delete(Integer id);
	
	public TabSeachPageBean get(Integer id);
	
	public int update(TabSeachPageBean bean);
	
	
	public int insertDetail(FineCategory bean);
	
	/**
	 * 查询该关键词下的细分类
	 * @Title detailList 
	 * @Description TODO
	 * @param sid
	 * @return
	 * @return List<FineCategory>
	 */
	public List<FineCategory> detailFineCategoryList(int parseInt);
	
	
	public List<TabSeachPagesDetailBean> detailList(Integer sid);
	
	public int updateDetail(TabSeachPagesDetailBean bean);
	
	public int deleteDetail(Integer id);
	
	public TabSeachPagesDetailBean getDetail(Integer id);
	
	public int getWordsCount(String keyword);
	
	public int getWordsCount1(String keyword, Integer id);

	public int getNameCount(String name, Integer sid);

	public int getNameCount1(String name, Integer sid, Integer id);
	/**
	 * 是否启用关键词
	 * @Title updateIsshow
	 * @Description TODO
	 * @param isshow
	 * @param id
	 * @return
	 * @return int
	 */
	public int updateIsshow(Integer isshow, Integer id);
	/**
	 * 查询单个细分类商品
	 * @Title getOneFineCategory 
	 * @Description TODO
	 * @param parseInt
	 * @return
	 * @return FineCategory
	 */
	public FineCategory getOneFineCategory(int parseInt);
	/**
	 * 保存修改后的细分类商品
	 * @Title updateFineCategory 
	 * @Description TODO
	 * @param fineCategory
	 * @return
	 * @return int
	 */
	public int updateFineCategory(FineCategory fineCategory);
	/**
	 * 删除该细分类商品
	 * @Title deleteFineCategory 
	 * @Description TODO
	 * @param parseInt
	 * @return
	 * @return int
	 */
	public int deleteFineCategory(int parseInt);
	/**
	 * 
	 * @Title aliCategory 
	 * @Description TODO
	 * @return
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String,Object>> search1688Category();

	public void deleCate(int parseInt);
	
}
