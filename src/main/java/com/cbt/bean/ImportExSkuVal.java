package com.cbt.bean;

/**
 * 电商网站Sku的Val
 * 
 * @author JXW
 *
 */
public class ImportExSkuVal {
	private float actSkuCalPrice;
	private float actSkuMultiCurrencyCalPrice;
	private float actSkuMultiCurrencyDisplayPrice;
	private int availQuantity;
	private int inventory;
	private boolean isActivity;
	private float skuCalPrice;
	private float skuMultiCurrencyCalPrice;
	private float skuMultiCurrencyDisplayPrice;
	private String freeSkuPrice;

	public float getActSkuCalPrice() {
		return actSkuCalPrice;
	}

	public void setActSkuCalPrice(float actSkuCalPrice) {
		this.actSkuCalPrice = actSkuCalPrice;
	}

	public float getActSkuMultiCurrencyCalPrice() {
		return actSkuMultiCurrencyCalPrice;
	}

	public void setActSkuMultiCurrencyCalPrice(float actSkuMultiCurrencyCalPrice) {
		this.actSkuMultiCurrencyCalPrice = actSkuMultiCurrencyCalPrice;
	}

	public float getActSkuMultiCurrencyDisplayPrice() {
		return actSkuMultiCurrencyDisplayPrice;
	}

	public void setActSkuMultiCurrencyDisplayPrice(float actSkuMultiCurrencyDisplayPrice) {
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

	public float getSkuCalPrice() {
		return skuCalPrice;
	}

	public void setSkuCalPrice(float skuCalPrice) {
		this.skuCalPrice = skuCalPrice;
	}

	public float getSkuMultiCurrencyCalPrice() {
		return skuMultiCurrencyCalPrice;
	}

	public void setSkuMultiCurrencyCalPrice(float skuMultiCurrencyCalPrice) {
		this.skuMultiCurrencyCalPrice = skuMultiCurrencyCalPrice;
	}

	public float getSkuMultiCurrencyDisplayPrice() {
		return skuMultiCurrencyDisplayPrice;
	}

	public void setSkuMultiCurrencyDisplayPrice(float skuMultiCurrencyDisplayPrice) {
		this.skuMultiCurrencyDisplayPrice = skuMultiCurrencyDisplayPrice;
	}

	public String getFreeSkuPrice() {
		return freeSkuPrice;
	}

	public void setFreeSkuPrice(String freeSkuPrice) {
		this.freeSkuPrice = freeSkuPrice;
	}

	@Override
	public String toString() {
		return "{\"actSkuCalPrice\":\"" + actSkuCalPrice + "\", \"actSkuMultiCurrencyCalPrice\":\""
				+ actSkuMultiCurrencyCalPrice + "\", \"actSkuMultiCurrencyDisplayPrice\":\""
				+ actSkuMultiCurrencyDisplayPrice + "\", \"availQuantity\":\"" + availQuantity + "\", \"inventory\":\""
				+ inventory + "\", \"isActivity\":\"" + isActivity + "\", \"skuCalPrice\":\"" + skuCalPrice
				+ "\", \"skuMultiCurrencyCalPrice\":\"" + skuMultiCurrencyCalPrice
				+ "\", \"skuMultiCurrencyDisplayPrice\":\"" + skuMultiCurrencyDisplayPrice
				+ "\", \"freeSkuPrice\":\"" + freeSkuPrice + "\"}";
	}

}
