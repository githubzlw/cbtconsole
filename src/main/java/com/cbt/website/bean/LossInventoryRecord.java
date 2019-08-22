package com.cbt.website.bean;

import lombok.Data;

@Data
public class LossInventoryRecord {
	/**
	 * 
	 */
	private int id;
	/**
	 * inventory_sku id
	 */
	private int inventorySkuId;
	/**
	 * 产品id
	 */
	private String goodsPid;
	/**
	 * 规格skuid
	 */
	private String skuid;
	/**
	 * 规格specid
	 */
	private String specid;
	/**
	 * 数量
	 */
	private int changeNumber;
	/**
	 * 类型 0  损坏 1 遗失  3 添加 4 补货  5 漏发 7 其他原因
	 */
	private int changeType;
	/**
	 * 变更人员
	 */
	private int changeAdm;
	/**
	 * 变更时间
	 */
	private String changeTime;
	/**
	 * 删除标志  0 正常 1 删除
	 */
	private int deleteState;
	/**
	 * 删除人员
	 */
	private int deleteAdm;
	/**
	 * 删除时间
	 */
	private String deleteTime;
	
	/**
	 * 备注
	 */
	private String remark;

}
