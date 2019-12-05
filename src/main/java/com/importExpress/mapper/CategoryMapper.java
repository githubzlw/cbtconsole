package com.importExpress.mapper;

import java.util.List;
import java.util.Map;

import com.cbt.bean.CategoryAllBean;
import com.cbt.bean.CategoryBean;
import org.apache.ibatis.annotations.Param;

import com.cbt.bean.Category1688Bean;

public interface CategoryMapper {
	 /**
     * 获取1688类别列表
     * @date 2018年3月28日
     * @author user4
     * @return  
     */
    List<Category1688Bean> getList(@Param("categoryName") String categoryName, @Param("categoryId") String categoryId, @Param("page") int page);
    /**
     * 获取1688类别列表
     * @date 2018年3月28日
     * @author user4
     * @return
     */
    int getListTotal(@Param("categoryName") String categoryName, @Param("categoryId") String categoryId);
    /**修改类别英文名称
     * @date 2018年3月28日
     * @author user4
     * @param id
     * @param categoryname
     * @return
     */
    int updateCategoryname(@Param("id") int id, @Param("categoryname") String categoryname);
    
    /**获取类别列表
     * @return
     */
    List<Map<String,String>> geCategoryList();
    
    /**获取类别名称
     * @param catid
     * @return
     */
    String getCategoryByCatid(String catid);

    /**
     * 根据参数获取类别信息
     * @param categoryBean
     * @return
     */
    List<CategoryBean> queryCategoryList(CategoryBean categoryBean);

    /**
     * 搜索结果总数
     * @param categoryBean
     * @return
     */
    int queryCategoryListCount(CategoryBean categoryBean);

    /**
     * 根据类别ID获取类别信息
     * @param cid
     * @return
     */
    CategoryBean queryCategoryById(@Param("cid") String cid);

    /**
     *根据ID的list获取类别bean
     * @param list
     * @return
     */
    List<CategoryBean> queryChildCategory(List<String> list);

    /**
     * 批量更新改动数据
     * @param list
     * @return
     */
    int batchUpdateCategory(List<CategoryBean> list);

    /**
     * 获取全部bean数据
     * @param cid
     * @return
     */
    List<CategoryAllBean> queryAllCategoryByParam(@Param("cid") String cid, @Param("startNum") int startNum, @Param("limitNum") int limitNum);

    int queryAllCategoryByParamCount(@Param("cid") String cid, @Param("startNum") int startNum, @Param("limitNum") int limitNum);

    /**
     * 更新类别名称
     * @param categoryBean
     * @return
     */
    int updateChangeAllBeanInfo(CategoryAllBean categoryBean);

}
