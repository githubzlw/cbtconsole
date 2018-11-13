package com.cbt.bean;

import java.io.Serializable;


public class DataQueryBean implements Serializable {
	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	private String new_user;// 新增注册用户
	private String old_user_order;// 老客户订单数量
	private String new_user_order;// 新客户订单数量
	private String new_user_order_cost;// 新客户订单总金额
	private String new_user_address;// 新用户 录入 地址的数量
	private String new_user_goods_car;// 新用户 购物车中 平均商品数量
	private String new_user_count;// 购物车中新用户数量
	public String getNew_user_count() {
		return new_user_count;
	}

	public void setNew_user_count(String new_user_count) {
		this.new_user_count = new_user_count;
	}

	public String getNew_user() {
		return new_user;
	}

	public void setNew_user(String new_user) {
		this.new_user = new_user;
	}

	public String getOld_user_order() {
		return old_user_order;
	}

	public void setOld_user_order(String old_user_order) {
		this.old_user_order = old_user_order;
	}

	public String getNew_user_order() {
		return new_user_order;
	}

	public void setNew_user_order(String new_user_order) {
		this.new_user_order = new_user_order;
	}

	public String getNew_user_order_cost() {
		return new_user_order_cost;
	}

	public void setNew_user_order_cost(String new_user_order_cost) {
		this.new_user_order_cost = new_user_order_cost;
	}

	public String getNew_user_address() {
		return new_user_address;
	}

	public void setNew_user_address(String new_user_address) {
		this.new_user_address = new_user_address;
	}

	public String getNew_user_goods_car() {
		return new_user_goods_car;
	}

	public void setNew_user_goods_car(String new_user_goods_car) {
		this.new_user_goods_car = new_user_goods_car;
	}
}
