package com.cbt.parse.bean;

/**alibaba商店推荐其他的商品的数据结构
 * @author abc
 *
 */
public class SupplierGoods {
	private String g_name;//商品名称
	private String g_img;//商品图片
	private String g_price;//商品价格
	private String g_min;//商品最小订单
	private String g_url;//商品链接
	
	public String getG_name() {
		return g_name;
	}
	public void setG_name(String g_name) {
		this.g_name = g_name;
	}
	public String getG_img() {
		return g_img;
	}
	public void setG_img(String g_img) {
		this.g_img = g_img;
	}
	public String getG_price() {
		return g_price;
	}
	public void setG_price(String g_price) {
		this.g_price = g_price;
	}
	public String getG_min() {
		return g_min;
	}
	public void setG_min(String g_min) {
		this.g_min = g_min;
	}
	public String getG_url() {
		return g_url;
	}
	public void setG_url(String g_url) {
		this.g_url = g_url;
	}
	@Override
	public String toString() {
		return "g_name=" + g_name + 
				"+# g_img=" + g_img+ 
				"+# g_price=" + g_price + 
				"+# g_min=" + g_min + 
				"+# g_url="+ g_url;
	}

}
