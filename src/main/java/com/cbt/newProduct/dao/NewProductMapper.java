package com.cbt.newProduct.dao;

import com.cbt.newProduct.pojo.NewProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewProductMapper {

	int addNewProduct(NewProduct bean);

	List<NewProduct> findNewProductData(@Param("cid") String cid, @Param("createtime") String createtime);

	List<NewProduct> showCategoryData(String createtime);

	int checkUrl(@Param("purl") String url);

	int down(String pid);

	List<NewProduct> getAllCategory();

}
