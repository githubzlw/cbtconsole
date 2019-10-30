package com.cbt.service;

import com.cbt.bean.Category1688Bean;

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
    
}
