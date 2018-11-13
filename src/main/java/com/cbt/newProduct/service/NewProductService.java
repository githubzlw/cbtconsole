package com.cbt.newProduct.service;

import com.cbt.newProduct.pojo.NewProduct;

import java.util.List;

public interface NewProductService {

	int addNewProduct(NewProduct bean);

	List<NewProduct> findNewProductData(String cid, String createtime);

	List<NewProduct> showCategoryData(String createtime);

	int checkUrl(String purl);

	int down(String pid);

	List<NewProduct> getAllCategory();

}
