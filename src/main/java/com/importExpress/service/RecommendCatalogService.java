package com.importExpress.service;

import java.util.List;

import com.importExpress.pojo.CatalogProduct;
import com.importExpress.pojo.RecommendCatalog;

public interface RecommendCatalogService {
	
	/**
	 * 获取选中商品集合
	 * @param pids
	 * @return
	 */
	CatalogProduct product(String pids,int site);
	
	/**保存目录
	 * @param catalog
	 * @return
	 */
	int  addCatelog(RecommendCatalog catalog);
	
	
	/**目录列表
	 * @param page
	 * @param template
	 * @param catalog_name
	 * @return
	 */
	List<RecommendCatalog> catalogList(int page,int template,String catalogName);
	/**统计目录列表数量
	 * @param template
	 * @param catalog_name
	 * @return
	 */
	int catalogCount(int template,String catalogName);
	/**更新
     * @param cataLog
     * @return
     */
    int deleteCatalog(int id);


}
