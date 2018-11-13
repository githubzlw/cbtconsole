package com.cbt.newProduct.service.impl;

import com.cbt.newProduct.dao.NewProductMapper;
import com.cbt.newProduct.pojo.NewProduct;
import com.cbt.newProduct.service.NewProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewProductServiceImpl implements NewProductService {

	@Autowired
	private  NewProductMapper  newProductMapper;
	
	@Override
	public int addNewProduct(NewProduct bean) {
		// TODO Auto-generated method stub
		return newProductMapper.addNewProduct(bean);
	}

	@Override
	public List<NewProduct> findNewProductData(String cid, String createtime) {
		// TODO Auto-generated method stub
		return newProductMapper.findNewProductData(cid,createtime);
	}

	@Override
	public List<NewProduct> showCategoryData(String createtime) {
		// TODO Auto-generated method stub
		return newProductMapper.showCategoryData(createtime);
	}

	@Override
	public int checkUrl(String purl) {
		// TODO Auto-generated method stub
		return newProductMapper.checkUrl(purl);
	}

	@Override
	public int down(String pid) {
		// TODO Auto-generated method stub
		return newProductMapper.down(pid);
	}

	@Override
	public List<NewProduct> getAllCategory() {
		// TODO Auto-generated method stub
		return newProductMapper.getAllCategory();
	}
}
