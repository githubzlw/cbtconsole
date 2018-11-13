package com.cbt.warehouse.pojo;

import java.io.Serializable;

/**
 * @author ylm
 * 收藏商品类
 */
public class CollectionBean implements Serializable {

	private static final long serialVersionUID = 2538880758048400106L;
	
	private int id;
	private int userid;
	private String titile;
	private String moq;
	private String url;
	private String price;
	private String img;
	private String createtime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public void setTitile(String titile) {
		if(titile!=null){
			titile = titile.replaceAll("\\s{2}","");
		}
		this.titile = titile;
	}
	public String getTitile() {
		return titile;
	}
	public void setMoq(String moq) {
		this.moq = moq;
	}
	public String getMoq() {
		return moq;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	
}
