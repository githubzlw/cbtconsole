package com.importExpress.pojo;

import java.util.List;

import lombok.Data;

@Data
public class CatalogProductWrap {
	/**
	 * 类别ID
	 */
	private String catid;
	/**
	 * 类别名称
	 */
	private String category;
	/**
	 * 产品集合
	 */
	List<CatalogProduct> products;
	/**
	 * 产品数量
	 */
	private int productCount;
}
