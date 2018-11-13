package com.cbt.warehouse.pojo;

import java.io.Serializable;


/**
 * @author ylm
 * 混批折扣
 */
public class ClassDiscount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3520933492683405582L;
	/**
	 * 
	 */
	private int id;
	private String showname;
	private String classname;
	private double price ;
	private double deposit_rate;
	private double g_price;//实现的折扣金额
	private double sum_price;//类别总金额
	private String cid;//类别ID
	private int classtype;//折扣分类
	
	
	public int getClasstype() {
		return classtype;
	}
	public void setClasstype(int classtype) {
		this.classtype = classtype;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClassname() {
		return classname;
	}
	public void setClassname(String classname) {
		this.classname = classname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getDeposit_rate() {
		return deposit_rate;
	}
	public void setDeposit_rate(double deposit_rate) {
		this.deposit_rate = deposit_rate;
	}
	public void setG_price(double g_price) {
		this.g_price = g_price;
	}
	public double getG_price() {
		return g_price;
	}
	public void setSum_price(double sum_price) {
		this.sum_price = sum_price;
	}
	public double getSum_price() {
		return sum_price;
	}
	public void setShowname(String showname) {
		this.showname = showname;
	}
	public String getShowname() {
		return showname;
	}
	public void setCid(String  cid) {
		this.cid = cid;
	}
	public String getCid() {
		return cid;
	}
	@Override
	public String toString() {
		return String
				.format("ClassDiscount [id=%s, showname=%s, classname=%s, price=%s, deposit_rate=%s, g_price=%s, sum_price=%s, cid=%s]",
						id, showname, classname, price, deposit_rate, g_price,
						sum_price, cid);
	}
	
	
}
