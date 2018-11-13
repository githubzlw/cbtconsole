package com.cbt.parse.check;

public class CheckGoods {
	private String minOrder;
	private String unit;
	private String price;
	private String url;
	private String imgurl;
	
	public String getMinOrder() {
		return minOrder;
	}
	public void setMinOrder(String minOrder) {
		this.minOrder = minOrder;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
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
	@Override
	public String toString() {
		return "GoogsInfo [minOrder=" + minOrder + ", unit=" + unit
				+ ", price=" + price + ", url=" + url + ", imgurl=" + imgurl
				+ "]";
	}

}
