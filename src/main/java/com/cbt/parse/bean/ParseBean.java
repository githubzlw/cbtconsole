package com.cbt.parse.bean;

/**类别页面生成数据
 * @author abc
 *
 */
public class ParseBean {
	private String url;
	private String name;
	private String price;
	private String img;
	private String morder;
	
	
	public String getMorder() {
		return morder;
	}
	public void setMorder(String morder) {
		this.morder = morder;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	@Override
	public String toString() {
		return String.format("url=%s, name=%s, price=%s, img=%s, morder=%s",
				url, name, price, img, morder);
	}
	
	
	
}
