package com.cbt.bean;

import java.io.Serializable;

public class SkuValBean implements Serializable {

	private static final long serialVersionUID = 1L;
	//	"actSkuCalPrice":"2.54",
	//"actSkuMultiCurrencyCalPrice":"2.54",
	//"actSkuMultiCurrencyDisplayPrice":"2.54",
//	"availQuantity":6350,
	//"inventory":6666,
	//"isActivity":true,
	//"skuCalPrice":"2.99",
	//"skuMultiCurrencyCalPrice":"2.99",
//	"skuMultiCurrencyDisplayPrice":"2.99"
	private String actSkuCalPrice;
	private String actSkuMultiCurrencyCalPrice;
	private String actSkuMultiCurrencyDisplayPrice;
	private int availQuantity;
	private int inventory;
	private boolean isActivity;
	private String skuCalPrice;
	private String skuMultiCurrencyCalPrice;
	private String skuMultiCurrencyDisplayPrice;
	public String getActSkuCalPrice() {
		return actSkuCalPrice;
	}
	public void setActSkuCalPrice(String actSkuCalPrice) {
		this.actSkuCalPrice = actSkuCalPrice;
	}
	public String getActSkuMultiCurrencyCalPrice() {
		return actSkuMultiCurrencyCalPrice;
	}
	public void setActSkuMultiCurrencyCalPrice(String actSkuMultiCurrencyCalPrice) {
		this.actSkuMultiCurrencyCalPrice = actSkuMultiCurrencyCalPrice;
	}
	public String getActSkuMultiCurrencyDisplayPrice() {
		return actSkuMultiCurrencyDisplayPrice;
	}
	public void setActSkuMultiCurrencyDisplayPrice(
			String actSkuMultiCurrencyDisplayPrice) {
		this.actSkuMultiCurrencyDisplayPrice = actSkuMultiCurrencyDisplayPrice;
	}
	public int getAvailQuantity() {
		return availQuantity;
	}
	public void setAvailQuantity(int availQuantity) {
		this.availQuantity = availQuantity;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	public boolean isActivity() {
		return isActivity;
	}
	public void setActivity(boolean isActivity) {
		this.isActivity = isActivity;
	}
	public String getSkuCalPrice() {
		return skuCalPrice;
	}
	public void setSkuCalPrice(String skuCalPrice) {
		this.skuCalPrice = skuCalPrice;
	}
	public String getSkuMultiCurrencyCalPrice() {
		return skuMultiCurrencyCalPrice;
	}
	public void setSkuMultiCurrencyCalPrice(String skuMultiCurrencyCalPrice) {
		this.skuMultiCurrencyCalPrice = skuMultiCurrencyCalPrice;
	}
	public String getSkuMultiCurrencyDisplayPrice() {
		return skuMultiCurrencyDisplayPrice;
	}
	public void setSkuMultiCurrencyDisplayPrice(String skuMultiCurrencyDisplayPrice) {
		this.skuMultiCurrencyDisplayPrice = skuMultiCurrencyDisplayPrice;
	}
	@Override
	public String toString() {
		return String
				.format("{\"actSkuCalPrice\":\"%s\", \"actSkuMultiCurrencyCalPrice\":\"%s\", "
						+ "\"actSkuMultiCurrencyDisplayPrice\":\"%s\", \"availQuantity\":%s, "
						+ "\"inventory\":%s, \"isActivity\":%s, \"skuCalPrice\":\"%s\", "
						+ "\"skuMultiCurrencyCalPrice\":\"%s\", \"skuMultiCurrencyDisplayPrice\":\"%s\"}",
						actSkuCalPrice, actSkuMultiCurrencyCalPrice,
						actSkuMultiCurrencyDisplayPrice, availQuantity,
						inventory, isActivity, skuCalPrice,
						skuMultiCurrencyCalPrice, skuMultiCurrencyDisplayPrice);
	}
	public SkuValBean(String actSkuCalPrice,
			String actSkuMultiCurrencyCalPrice,
			String actSkuMultiCurrencyDisplayPrice, int availQuantity,
			int inventory, boolean isActivity, String skuCalPrice,
			String skuMultiCurrencyCalPrice, String skuMultiCurrencyDisplayPrice) {
		super();
		this.actSkuCalPrice = actSkuCalPrice;
		this.actSkuMultiCurrencyCalPrice = actSkuMultiCurrencyCalPrice;
		this.actSkuMultiCurrencyDisplayPrice = actSkuMultiCurrencyDisplayPrice;
		this.availQuantity = availQuantity;
		this.inventory = inventory;
		this.isActivity = isActivity;
		this.skuCalPrice = skuCalPrice;
		this.skuMultiCurrencyCalPrice = skuMultiCurrencyCalPrice;
		this.skuMultiCurrencyDisplayPrice = skuMultiCurrencyDisplayPrice;
	}
	public SkuValBean() {
		super();
	}
	
	
	
	
	
	
}
