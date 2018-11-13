package com.cbt.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ylm
 * 后台展示批量优惠信息
 */
public class PreferentialWeb {
	private int total;//sj  返回数据总数
	private Preferential preferential;
	private String url;
	private String img;
	private String name;//商品名称
	private String username;
	private String mOrder;//商品原最小定量
	private double price;//系统给出价格
	private List<PaInteracted> paInteracteds;
	private int paid;
	public int getPaid() {
		return paid;
	}
	public void setPaid(int paid) {
		this.paid = paid;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Preferential getPreferential() {
		return preferential;
	}
	public void setPreferential(Preferential preferential) {
		this.preferential = preferential;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public List<PaInteracted> getPaInteracteds() {
		return paInteracteds;
	}
	public void setPaInteracteds(List<PaInteracted> paInteracteds) {
		this.paInteracteds = paInteracteds;
	}
	public String getmOrder() {
		return mOrder;
	}
	public void setmOrder(String mOrder) {
		this.mOrder = mOrder;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getPrice() {
		return price;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	public void addPaInteracteds(PaInteracted paInteracteds) {
		if(this.paInteracteds==null)
		{
			this.paInteracteds=new  ArrayList<PaInteracted>();
		}
		this.paInteracteds.add(paInteracteds);
	}
}
