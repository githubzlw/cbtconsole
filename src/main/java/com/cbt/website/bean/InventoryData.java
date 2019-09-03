package com.cbt.website.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryData extends InventorySku{
	/**
	 * 规格相关 sku  specid  skuid
	 */
	private String skuContext;
	/**
	 * 类别名称
	 */
	private String categoryName;
	/**
	 * 不可售原因
	 */
	private String unsellableReason;
	
	/**
	 * 上次盘点时间
	 */
	private String checkTime;
	
	/**
	 * 操作按钮
	 */
	private String operation;
	
	/**
	 * 上架 下架
	 */
	private String online;
	
	/**
	 * 历史备注信息
	 */
	private String remarkContext;
	
	/**
	 *临时 盘点库存
	 */
	private int tempRemaining;
	/**
	 *临时 盘点记录表ID
	 */
	private int checkTempId;
	/**
	 * 临时盘点库位
	 */
	private String tempBarcode;
	
}
