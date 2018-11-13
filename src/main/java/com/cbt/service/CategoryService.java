package com.cbt.service;

import com.cbt.bean.Category1688Bean;

import java.util.List;

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
    
    
    
}
