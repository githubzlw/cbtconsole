package com.importExpress.service;

import com.importExpress.pojo.CatalogProduct;

public interface RecommendCatalogService {
	
	/**
	 * 获取选中商品集合
	 * @param pids
	 * @return
	 */
	CatalogProduct product(String pids,int site);

}
