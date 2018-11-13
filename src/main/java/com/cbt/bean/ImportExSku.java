package com.cbt.bean;

/**
 * 电商网站Sku
 * 
 * @author JXW
 *
 */
public class ImportExSku {
	private String skuAttr;
	private String skuPropIds;
	private ImportExSkuVal skuVal;

	public String getSkuAttr() {
		return skuAttr;
	}

	public void setSkuAttr(String skuAttr) {
		this.skuAttr = skuAttr;
	}

	public String getSkuPropIds() {
		return skuPropIds;
	}

	public void setSkuPropIds(String skuPropIds) {
		this.skuPropIds = skuPropIds;
	}

	public ImportExSkuVal getSkuVal() {
		return skuVal;
	}

	public void setSkuVal(ImportExSkuVal skuVal) {
		this.skuVal = skuVal;
	}

	@Override
	public String toString() {
		return "{\"skuAttr\":\"" + skuAttr + "\", \"skuPropIds\":\"" + skuPropIds + "\", \"skuVal\":" + skuVal
				+ "}";
	}

}
