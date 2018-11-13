package com.importExpress.mapper;

import com.importExpress.pojo.FineCategory;
import com.importExpress.pojo.TabSeachPageBean;
import com.importExpress.pojo.TabSeachPagesDetailBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryResearchMapper {
	public int insert(TabSeachPageBean bean);
	
	public List<TabSeachPageBean> list(@Param("id") Integer id);

	public int delete(@Param("id") Integer id);

	public TabSeachPageBean get(@Param("id") Integer id);

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
	public List<FineCategory> detailFineCategoryList(@Param("sid") int sid);

	/**
	 *
	 * @Title detailList
	 * @Description TODO
	 * @param sid
	 * @return
	 * @return List<TabSeachPagesDetailBean>
	 */
	public List<TabSeachPagesDetailBean> detailList(@Param("sid") Integer sid);

	public int updateDetail(TabSeachPagesDetailBean bean);

	public int deleteDetail(@Param("id") Integer id);

	public TabSeachPagesDetailBean getDetail(@Param("id") Integer id);

	public int getWordsCount(@Param("keyword") String keyword);

	public int getWordsCount1(@Param("keyword") String keyword, @Param("id") Integer id);

	public int getNameCount(@Param("name") String name, @Param("sid") Integer sid);

	public int getNameCount1(@Param("name") String name, @Param("sid") Integer sid, @Param("id") Integer id);

	public int updateIsshow(@Param("isshow") Integer isshow, @Param("id") Integer id);
	/**
	 * 查询单个细分类
	 * @Title getOneFineCategory
	 * @Description TODO
	 * @param id
	 * @return
	 * @return FineCategory
	 */
	public FineCategory getOneFineCategory(@Param("id") int id);
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
	 * 删除
	 * @Title deleteFineCategory
	 * @Description TODO
	 * @param id
	 * @return
	 * @return int
	 */
	public int deleteFineCategory(@Param("id") int id);

	public List<Map<String, Object>> search1688Category();


	public List<Map<String, Object>> search1688CategoryLazy(@Param("id") String id);

	public void move(@Param("id") int id);

	
}
