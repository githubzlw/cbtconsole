package com.importExpress.mapper;

import com.importExpress.pojo.RecommendCatalog;

public interface RecommendCatalogMapper {

    /**
     * 插入数据
     * @param cataLog
     * @return
     */
    int insertCatalog(RecommendCatalog cataLog);

    int updateCatalogProduct(RecommendCatalog cataLog);

}