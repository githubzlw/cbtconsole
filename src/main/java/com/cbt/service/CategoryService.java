package com.cbt.service;

import com.cbt.bean.Category1688Bean;
import com.cbt.bean.CategoryAllBean;
import com.cbt.bean.CategoryBean;

import java.util.List;
import java.util.Map;

public interface CategoryService {
	
    /**
     * 获取1688类别列表
     * @date 2018年3月28日
     * @author user4
     * @return  
     */
    List<Category1688Bean> getList(String categoryName, String categoryId, int page);

    /**
     * @date 2018年3月28日
     * @author user4
     * @return
     */
    int getListTotal(String categoryName, String categoryId);


    /**修改类别英文名称
     * @date 2018年3月28日
     * @author user4
     * @param id
     * @param categoryname
     * @return
     */
    int updateCategoryname(int id, String categoryname);
    
    /**获取类别列表
     * @return
     */
    Map<String,String> geCategoryList();
    
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
     * @param category1688Bean
     * @return
     */
    int queryCategoryListCount(CategoryBean category1688Bean);

    /**
     * 根据类别ID获取类别信息
     * @param cid
     * @return
     */
    CategoryBean queryCategoryById(String cid);

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
    List<CategoryAllBean> queryAllCategoryByParam(String cid,int startNum,int limitNum);

    int queryAllCategoryByParamCount(String cid,int startNum,int limitNum);

    /**
     * 更新类别名称
     * @param categoryBean
     * @return
     */
    int updateChangeAllBeanInfo(CategoryAllBean categoryBean);
    
}
