package com.cbt.website.bean;

public class ProductBean {

	private String productname;//品名(英)
	private String producenglishtname;//品名(中)
	private String productnum;//数量
	private String productprice;//单价
	private String productcurreny;//单位
	private String productremark;//配货备注
	
	
	public String getProductcurreny() {
		return productcurreny;
	}
	public void setProductcurreny(String productcurreny) {
		this.productcurreny = productcurreny;
	}
	public String getProductprice() {
		return productprice;
	}
	public String getProductremark() {
		return productremark;
	}
	public void setProductremark(String productremark) {
		this.productremark = productremark;
	}
	public void setProductprice(String productprice) {
		this.productprice = productprice;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getProductnum() {
		return productnum;
	}
	public void setProductnum(String productnum) {
		this.productnum = productnum;
	}
	public String getProducenglishtname() {
		return producenglishtname;
	}
	public void setProducenglishtname(String producenglishtname) {
		this.producenglishtname = producenglishtname;
	}	
	
}
