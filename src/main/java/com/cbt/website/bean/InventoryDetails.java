package com.cbt.website.bean;

import lombok.Data;

/**inventory_details_sku
 * @author Administrator
 *
 */
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
	 * 线上PID
	 */
	private String goodsPPid;
	/**
	 * sku
	 */
	private String sku;
	/**
	 * 线上SKUID 无匹配留空
	 */
	private String goodsSkuid;
	/**
	 * 产品货源是亏的
	 */
	private String goodsPSkuid;
	/**
	 * 产品货源价格
	 */
	private String goodsPPrice;
	/**
	 * 产品价格
	 */
	private String goodsPrice;
	/**
	 * 淘宝订单号
	 */
	private String tbOrderid;
	/**
	 * 套播啊运单号
	 */
	private String tbShipno;
	/**
	 * 货源产品链接
	 */
	private String goodsPUrl;
	/**
	 * 产品链接
	 */
	private String goodsUrl;
	/**
	 * 产品名称
	 */
	private String goodsName;
	/**
	 * 货源产品图片
	 */
	private String goodsPImg;
	/**
	 *操作人
	 */
	private int admid;
	/**
	 * '0 入库  1 出库 2 报损',
	 */
	private int type;
	/**
	 * specid
	 */
	private String goodsSpecid;
	private String goodsPSpecid;
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
	 * 创建时间
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
	private int goodsNumber;
	/**
	 * 订单详情id
	 */
	private int odId;
	/**
	 * 网站订单号
	 */
	private String orderno;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 操作人
	 */
	private String adm;

}
