package com.cbt.bean;

public class Eightcatergory {
	private int id;
	private int row;
	private String catergory;
	private int minorder;
	private String unit;
	private float price;
	private String url;
	private String imgurl;
	private String productname;
	private int valid;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCatergory() {
		return catergory;
	}
	public void setCatergory(String catergory) {
		this.catergory = catergory;
	}
	public int getMinorder() {
		return minorder;
	}
	public void setMinorder(int minorder) {
		this.minorder = minorder;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	
	public void setValid(int valid) {
		this.valid = valid;
	}
	public int getValid() {
		return valid;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"catergory\":\"%s\", \"minorder\":\"%s\", \"unit\":\"%s\", \"price\":\"%s\", \"url\":\"%s\", \"imgurl\":\"%s\", \"productname\":\"%s\"}",
						id, catergory, minorder, unit, price, url, imgurl,
						productname);
	}
	
}
