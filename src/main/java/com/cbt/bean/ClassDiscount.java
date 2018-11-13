package com.cbt.bean;


/**
 * @author ylm
 * 混批折扣
 */
public class ClassDiscount {

	/**
	 * 
	 */
	private int id;
	private String showname;
	private String classname;
	private int price ;
	private double deposit_rate;
	private double g_price;//实现的折扣金额
	private double sum_price;//类别总金额
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
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
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
}
