package com.importExpress.mapper;

import java.util.List;
import java.util.Map;

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

}
