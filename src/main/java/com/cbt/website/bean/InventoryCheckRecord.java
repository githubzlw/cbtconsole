package com.cbt.website.bean;

import lombok.Data;

/**
 * 盘点记录inventory-sku_check_record
 * @author Administrator
 *
 */
@Data
public class InventoryCheckRecord {
	private int id;
	/**
	 * 盘点表inventory_sku_check表id
	 */
	private int inventoryCheckId;
	/**
	 * 库存表inventory_sku 的id
	 */
	private int inventorySkuId;
	/**
	 * 产品ID
	 */
	private String goodsPid;
	/**
	 * 产品sku
	 */
	private String goodsSku;
	/**
	 * 产品sku唯一specid
	 */
	private String goodsSpecid;
	/**
	 * 产品sku唯一skuid
	 */
	private String goodsSkuid;
	/**
	 * 产品价格
	 */
	private String goodsPrice;
	/**
	 * 盘点前库存
	 */
	private int inventoryRemaining;
	/**
	 * 盘点后库存
	 */
	private int checkRemaining;
	/**
	 * 时间
	 */
	private String createTime;
	/**
	 * 盘点前库位
	 */
	private String beforeBarcode;
	/**
	 * 盘点后库位
	 */
	private String afterBarcode;
	

}
