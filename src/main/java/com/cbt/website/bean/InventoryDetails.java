package com.cbt.website.bean;

import lombok.Data;

@Data
public class InventoryDetails {
	private int id;
	/**
	 * inventory_sku表ID
	 */
	private int inventoryId;
	/**
	 * 线上PID
	 */
	private String goodsPid;
	/**
	 * sku
	 */
	private String sku;
	/**
	 * 线上SKUID 无匹配留空
	 */
	private String goodsSkuid;
	/**
	 * '0 入库  1 出库 2 报损',
	 */
	private int type;
	/**
	 * specid
	 */
	private String goodsSpecid;
	/**
	 * 删除备注
	 */
	private String delRemark;
	
	/**
	 * 删除人员ID
	 */
	private int delAdm;
	/**
	 * 删除时间
	 */
	private String delDatetime;
	/**
	 * 
	 */
	private String createtime;
	/**
	 * 0 正常 1 删除   默认0 '
	 */
	private int del;
	/**
	 * 品图片
	 */
	private String goodsImg;
	/**
	 * 入库数量
	 */
	private String goodsNumber;
	/**
	 * 订单详情id
	 */
	private int odId;
	/**
	 * 网站订单号
	 */
	private String orderno;

}
