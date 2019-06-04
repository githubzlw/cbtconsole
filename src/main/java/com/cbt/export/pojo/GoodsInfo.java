package com.cbt.export.pojo;

import lombok.Data;

@Data
public class GoodsInfo {

	/**
	 * 电商商品编号 
	 */
	private String id;
	/**
	 * 商品单价
	 */
	private String price;
	/**
	 * 商品总价 
	 */
	private String cost;
	/**
	 * 商品数量 
	 */
	private String quality;
	/**
	 * 产品类别
	 */
	private String catid;
	/**
	 *商品名称 
	 */
	private String name;
	/**
	 * 单件重量
	 */
	private String weight;
	
}
