package com.cbt.website.bean;

import lombok.Data;

@Data
public class InventoryWrap {
	/**
	 * inventory_sku id
	 */
	private int inid;
	private int liid;
	/**
	 * 
	 */
	private int lockRemaining;
	/**
	 * 
	 */
	private int odid;
	/**
	 * 
	 */
	private int flag;
	/**
	 * 
	 */
	private int isDelete;
	/**
	 * 
	 */
	private int isUse;
	/**
	 * 
	 */
	private String createtime;
	/**
	 * 
	 */
	private String inBarcode;
	/**
	 * 
	 */
	private String orderBarcode;
	/**
	 * 
	 */
	private int ibid;
	/**
	 * 
	 */
	private int ibState;
	/**
	 * 
	 */
	private String ibRemark;
	/**
	 * 
	 */
	private String iskSCarImg;
	/**
	 * 
	 */
	private String iskSGoodsPid;
	/**
	 * 
	 */
	private String iskGoodsName;
	/**
	 * 
	 */
	private String iskSku;
	/**
	 * 
	 */
	private String iskSkuid;
	/**
	 * 
	 */
	private String iskSpecid;
	/**
	 * 
	 */
	private String goodsPid;
	/**
	 * 
	 */
	private String odCarImg;
	/**
	 * 
	 */
	private String odSku;
	/**
	 * 
	 */
	private String odSpecid;
	/**
	 * 
	 */
	private String odSkuid;
	
	private int odYourOrder;
	
	private int odSeilUnit;
	
	private String orderid;
	private String goodsid;
	
	private String stateContext;

	private int remaining;
	private int canRemaining;
}