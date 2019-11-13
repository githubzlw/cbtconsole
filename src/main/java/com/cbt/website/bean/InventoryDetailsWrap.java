package com.cbt.website.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class InventoryDetailsWrap  extends InventoryDetails{
	/**
	 * sku相关内容
	 */
	private String skuContext;
	/**
	 * 删除操作相关内容
	 */
	private String delContext;
	
	/**
	 * 订单相关内容
	 */
	private String orderContext;
	
	/**
	 * 出入库说明
	 */
	private String typeContext;
	
	/**
	 * 操作人
	 */
	private String adm;

}
