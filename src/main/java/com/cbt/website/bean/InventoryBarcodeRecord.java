package com.cbt.website.bean;

import lombok.Data;

/**库存使用库位变更intentory_barcode_record
 * @author Administrator
 *
 */
@Data
public class InventoryBarcodeRecord {
	/**
	 * 
	 */
	private int  id;
	/**
	 * inventory_sku id
	 */
	private int  inventoryId;
	/**
	 *lock_inventory id 
	 */
	private int  lockId;
	/**
	 * order_details id
	 */
	private int  odId;
	
	/**
	 * inventory_sku_temporary  id
	 */
	private int temId;
	/**
	 * 
	 */
	private int  admid;
	/**
	 * 0:采购使用国旅库存等待仓库实际操作，1 仓库蒋库存移库到客户订单  2.取消了订单，等待仓库还原库位 3.将订单库位移动到库存库位
	 */
	private int state ;
	/**
	 * 库存库位
	 */
	private String  inventoryBarcode;
	/**
	 * 订单库位
	 */
	private String  orderBarcode;
	
	
	/**
	 * 0:采购使用国旅库存等待仓库实际操作，1 仓库蒋库存移库到客户订单  2.取消了订单，等待仓库还原库位 3.将订单库位移动到库存库位
	 */
	private String stateContext;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 移库数量
	 */
	private int changeNum;

}
