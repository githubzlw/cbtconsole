package com.cbt.website.bean;

import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class InventoryWrap {
	/**
	 * inventory_sku id
	 */
	private int inid;
	/**
	 * lock_inventory id
	 */
	private int liid;
	/**
	 * lock_inventory 锁定库存
	 */
	private int lockRemaining;
	/**
	 * order_detials id
	 */
	private int odid;
	/**
	 * 
	 */
	private int flag;
	/**
	 * 是否删除
	 */
	private int isDelete;
	/**
	 * 是否使用库存
	 */
	private int isUse;
	/**
	 * 创建时间
	 */
	private String createtime;
	/**
	 * 库存库位
	 */
	private String inBarcode;
	/**
	 * 订单库位
	 */
	private String orderBarcode;
	/**
	 * inventory_barcode_record id
	 */
	private int ibid;
	/**
	 * 0：采购使用库存,等待仓库移出库存 1：订单取消，等待仓库移入库存 2：已完成移出库存 3：已完成移入库存 4：仓库取消了出库请求 5：仓库取消了入库请求',
	 */
	private int ibState;
	/**
	 * 移库备注
	 */
	private String ibRemark;
	/**
	 * inventory_sku 产品图片
	 */
	private String iskSCarImg;
	/**
	 * inventory_sku 产品id
	 */
	private String iskSGoodsPid;
	/**
	 * inventory_sku 产品名称
	 */
	private String iskGoodsName;
	/**
	 * inventory_sku 产品sku
	 */
	private String iskSku;
	/**
	 * inventory_sku 产品skuid
	 */
	private String iskSkuid;
	/**
	 * inventory_sku 产品sku 的specid
	 */
	private String iskSpecid;
	/**
	 * 订单表 产品id
	 */
	private String goodsPid;
	/**
	 * 订单表 产品图片
	 */
	private String odCarImg;
	/**
	 * 订单表 产品sku
	 */
	private String odSku;
	/**
	 * 订单表 产品sku的specid
	 */
	private String odSpecid;
	/**
	 * 订单表 产品skuid
	 */
	private String odSkuid;
	
	/**
	 * 订单表 产品数量
	 */
	private int odYourOrder;
	
	/**
	 * 订单表 产品单位
	 */
	private int odSeilUnit;
	
	/**
	 * 订单表 订单id
	 */
	private String orderid;
	/**
	 * 
	 */
	private String goodsid;
	
	/**
	 * 状态说明
	 */
	private String stateContext;

	/**
	 * 库存
	 */
	private int remaining;
	/**
	 * 可用库存
	 */
	private int canRemaining;
	
	/**
	 * 变更数量
	 */
	private int changeNum;
	
	
	/**
	 * 退货
	 */
	private int returnOrderNum;
}
