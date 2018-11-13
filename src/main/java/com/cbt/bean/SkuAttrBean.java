package com.cbt.bean;

public class SkuAttrBean {
	private String skuAttr;//
	private String skuPropIds;//
	private SkuValBean skuVal;
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
	public SkuValBean getSkuVal() {
		return skuVal;
	}
	public void setSkuVal(SkuValBean skuVal) {
		this.skuVal = skuVal;
	}
	@Override
	public String toString() {
		return String.format(
				"{\"skuAttr\":\"%s\", \"skuPropIds\":\"%s\", \"skuVal\":%s}", skuAttr,
				skuPropIds, skuVal);
	}
	public SkuAttrBean(String skuAttr, String skuPropIds, SkuValBean skuVal) {
		super();
		this.skuAttr = skuAttr;
		this.skuPropIds = skuPropIds;
		this.skuVal = skuVal;
	}
	public SkuAttrBean() {
		super();
	}
	
	
	
	
	
	
	
	
	

}
