package com.cbt.website.bean;

import lombok.Data;

/**盘点数据列表
 * @author Administrator
 *
 */
@Data
public class InventoryCheckWrap {
	
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
	 * 产品名称
	 */
	private String goodsName;
	/**
	 * 产品sku
	 */
	private String goodsSku;
	/**
	 * 产品sku唯一specid
	 */
	private String goodsSpecid;
	/**
	 * 产品图片
	 */
	private String goodsImg;
	
	/**
	 * 类别名称
	 */
	private String categoryName;
	
	/**
	 * 类别id
	 */
	private String catid;
	/**
	 * 产品sku唯一skuid
	 */
	private String goodsSkuid;
	/**
	 * 产品价格
	 */
	private String goodsPrice;
	/**
	 * 上次盘点数量
	 */
	private int lastCheckRemaining;
	/**
	 * 库存
	 */
	private int remaining;
	
	/**
	 * 可用库存
	 */
	private int canRemaining;
	/**
	 *上次盘点时间
	 */
	private String lastCheckTime;
	/**
	 * 库位
	 */
	private String barcode;
	
	/**
	 * 变更数量
	 */
	private int changeRemaining;
	
	/**
	 * 
	 */
	private String  operation;
	

}
