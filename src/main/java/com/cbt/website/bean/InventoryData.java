package com.cbt.website.bean;

import lombok.Data;

@Data
public class InventoryData {
	/**
	 * 库位号
	 */
	private String barcode;
	/**
	 * 库存数量
	 */
	private String inventoryCount;
	/**
	 * 订单号
	 */
	private String orderId; 
	/**
	 * order_details表id
	 */
	private String odId;
	/**
	 * 验货数量
	 */
	private String storage_count;
	/**
	 * 当次验货数量
	 */
	private String when_count;
	/**
	 * 
	 */
	private String admName;
	/**
	 * 单位数量
	 */
	private String unit;
	/**
	 * sku唯一specid
	 */
	private String specid;
	/**
	 * sku唯一skuid
	 */
	private String skuid;
	
	
	
	
	

}
