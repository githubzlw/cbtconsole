package com.cbt.website.bean;

import lombok.Data;

@Data
public class LossInventoryRecord {
	/**
	 * 
	 */
	private int id;
	/**
	 * 
	 */
	private int inventorySkuId;
	/**
	 * 
	 */
	private String goodsPid;
	/**
	 * 
	 */
	private String skuid;
	/**
	 * 
	 */
	private String specid;
	/**
	 * 
	 */
	private int changeNumber;
	/**
	 * 
	 */
	private int changeType;
	/**
	 * 
	 */
	private int changeAdm;
	/**
	 * 
	 */
	private String changeTime;
	/**
	 * 
	 */
	private int deleteState;
	/**
	 * 
	 */
	private int deleteAdm;
	/**
	 * 
	 */
	private String deleteTime;

}
