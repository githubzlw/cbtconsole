package com.importExpress.pojo;

import lombok.Data;

@Data
public class CatalogProduct {
	
	/**
	 * 产品ID
	 */
	private String pid;
	/**
	 * 产品名称
	 */
	private String name;
	/**
	 * 图片
	 */
	private String img;
	/**
	 * 销量
	 */
	private int sold;
	/**
	 * 单位
	 */
	private String unit;
	/**
	 * 链接
	 */
	private String url;
	/**
	 * 类别ID
	 */
	private String catid;
	/**
	 * 价格
	 */
	private String price;

}
