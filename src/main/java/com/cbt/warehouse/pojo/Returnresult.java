package com.cbt.warehouse.pojo;

import java.util.List;


public class Returnresult {
private String cusorder;//获取客户订单
private String tborder;//淘宝订单号
private String orderSale;//获取订单关联销售
private String warehouse;//获取所在仓库
private String ordeerPeo;//获取订单采购人
public String getTborder() {
	return tborder;
}
public void setTborder(String tborder) {
	this.tborder = tborder;
}
public String getWarehouse() {
	return warehouse;
}
public void setWarehouse(String warehouse) {
	this.warehouse = warehouse;
}
public String getOrdeerPeo() {
	return ordeerPeo;
}
public void setOrdeerPeo(String ordeerPeo) {
	this.ordeerPeo = ordeerPeo;
}
public String getCusorder() {
	return cusorder;
}
public void setCusorder(String cusorder) {
	this.cusorder = cusorder;
}

public String getOrderSale() {
	return orderSale;
}
public void setOrderSale(String orderSale) {
	this.orderSale = orderSale;
}

}
