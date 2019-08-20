package com.cbt.website.bean;

import lombok.Data;

/**inventory_sku_log
 * @author Administrator
 *
 */
@Data
public class InventoryLog {
	/**
	 * id
	 */
	private int  id;
	/**
	 * 本次变更库存数量
	 */
	private int  remaining;
	/**
	 * 变更前库存数量
	 */
	private int  beforeRemaining;
	/**
	 * 变更后库存数量
	 */
	private int  afterRemaining;
	/**
	 * 0：默认 1：增加  2：减少，3：盘点
	 */
	private int  changeType;
	/**
	 * 库存表 inventory_sku表的id
	 */
	private int  inventorySkuId;
	
	/**
	 * 商品链接
	 */
	private String  goodsUrl;
	/**
	 * 商品名称
	 */
	private String  goodsName;
	/**
	 * 商品规格
	 */
	private String  sku;
	/**
	 * 入库库位
	 */
	private String  barcode;
	/**
	 * 产品id
	 */
	private String  goodsPid;
	/**
	 * 库存更新时间
	 */
	private String  createtime;
	/**
	 * 备注信息
	 */
	private String  remark;
	/**
	 * 采购产品pid
	 */
	private String  goodsPPid;
	/**
	 * 格唯一specid
	 */
	private String  specid;
	/**
	 * 规格唯一skuid
	 */
	private String  skuid;

}
