package com.importExpress.mapper;

import com.cbt.bean.Category1688Bean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

}
