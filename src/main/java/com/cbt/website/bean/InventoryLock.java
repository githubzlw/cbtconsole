package com.cbt.website.bean;

import lombok.Data;

/**lock_inventory
 * @author Administrator
 *
 */
@Data
public class InventoryLock {
	private int id;
	/**
	 * 锁定的库存
	 */
	private int lockRemaining;
	/**
	 * inventory_sku  id
	 */
	private int inId;
	/**
	 * order_details id
	 */
	private int odId;
	/**
	 * 0:锁定未只用  1:已使用
	 */
	private int flag;
	/**
	 * 是否删除0:不删除1:已删除
	 */
	private int isDelete;
	/**
	 *采购是否确认使用 0 不使用 1使用 
	 */
	private int isUse;
	/**
	 * 锁定的库存金额
	 */
	private String lockInventoryAmount;

}
