package com.cbt.website.bean;

import lombok.Data;

/**
 * inventory_sku实体类
 * @author Administrator
 *
 */
@Data
public class InventorySku {
	/**
	 * 
	 */
	private int id;
	/**
	 * 商品链接
	 */
	private String goodsUrl;
	/**
	 * 库存数量
	 */
	private int remaining;
	/**
	 * 可用库存
	 */
	private int canRemaining;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 产品价格
	 */
	private String goodsPrice;
	/**
	 * 品规格
	 */
	private String sku;
	/**
	 * 库位
	 */
	private String barcode;
	/**
	 * 货源链接
	 */
	private String goodsPUrl;
	/**
	 * 商品类别
	 */
	private String goodsCatid;
	/**
	 * 商品图片
	 */
	private String carImg;
	/**
	 * 采购价
	 */
	private String goodsPPrice;
	/**
	 * 产品id
	 */
	private String goodsPid;
	/**
	 * 库存创建时间
	 */
	private String createtime;
	/**
	 * 最后更新时间
	 */
	private String updatetime;
	/**
	 * 注信息
	 */
	private String remark;
	/**
	 * 
	 */
	private String carUrlMD5;
	/**
	 * 采购产品pid
	 */
	private String goodsPPid;
	/**
	 * 规格唯一skuid
	 */
	private String skuid;
	/**
	 * 规格唯一specid
	 */
	private String specid;
	/**
	 * 库存锁定标识 0:未锁定 1:已锁定
	 */
	private int isLock;
	/**
	 * 0:未盘点 1:已盘点 2 问题库存
	 */
	private int flag;
	/**
	 * order_details表id
	 */
	private int odid;
	/**
	 * 0：电商库存  1：亚马逊库存
	 */
	private int inventoryType;
	/**
	 * li产品是否下架，4：下架，1在线
	 */
	private int onlineFlag;
	/**
	 * 线上网站是否有该产品1：有，0没有
	 */
	private int dbFlag;
	/**
	 * 盘点库存
	 */
	private int checkRemaining;
	
	/**
	 * 盘点表id
	 */
	private int inventoryCheckId;
}
