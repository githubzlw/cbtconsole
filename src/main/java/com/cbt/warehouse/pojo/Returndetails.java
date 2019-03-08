package com.cbt.warehouse.pojo;

import java.util.Date;

public class Returndetails {
	private int id;
	private String cusorder;//获取客户订单
	private int purNum;//获取采购订单数量
	private String tborder;//获取采购订单号
	private String purSou;//获取采购订单来源： 
	private String waybill;//获取采购运单号
	private double purManey;//获取采购金额
	private String returnOrder;//获取退货运单号
	private String warehouse;//获取所在仓库
	private int returnNum;//获取退货数量
	private double returnMoney;//获取退货金额
	private String ordeerPeo;//获取订单采购人
	private String orderSale;//获取订单关联销售
	private String returnApply;//获取退货申请人
	private String returnReason;//获取退货原因说明
	private String placeDate;//获取下单时间
	private String deliveryDate;//获取发货时间
	private String returnState;//获取退货跟进状态
	private String returndate;//获取退货时间
	private String operating;
	private String company;//获取快递公司；
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCusorder() {
		return cusorder;
	}
	public void setCusorder(String cusorder) {
		this.cusorder = cusorder;
	}
	public int getPurNum() {
		return purNum;
	}
	public void setPurNum(int purNum) {
		this.purNum = purNum;
	}
	public String getTborder() {
		return tborder;
	}
	public void setTborder(String tborder) {
		this.tborder = tborder;
	}
	public String getPurSou() {
		return purSou;
	}
	public void setPurSou(String purSou) {
		this.purSou = purSou;
	}
	public String getWaybill() {
		return waybill;
	}
	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}
	public double getPurManey() {
		return purManey;
	}
	public void setPurManey(double purManey) {
		this.purManey = purManey;
	}
	public String getReturnOrder() {
		return returnOrder;
	}
	public void setReturnOrder(String returnOrder) {
		this.returnOrder = returnOrder;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public int getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(int returnNum) {
		this.returnNum = returnNum;
	}
	public double getReturnMoney() {
		return returnMoney;
	}
	public void setReturnMoney(double returnMoney) {
		this.returnMoney = returnMoney;
	}
	public String getOrdeerPeo() {
		return ordeerPeo;
	}
	public void setOrdeerPeo(String ordeerPeo) {
		this.ordeerPeo = ordeerPeo;
	}
	public String getOrderSale() {
		return orderSale;
	}
	public void setOrderSale(String orderSale) {
		this.orderSale = orderSale;
	}
	public String getReturnApply() {
		return returnApply;
	}
	public void setReturnApply(String returnApply) {
		this.returnApply = returnApply;
	}
	public String getReturnReason() {
		return returnReason;
	}
	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}
	public String getPlaceDate() {
		return placeDate;
	}
	public void setPlaceDate(String placeDate) {
		this.placeDate = placeDate;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getReturnState() {
		return returnState;
	}
	public void setReturnState(String returnState) {
		this.returnState = returnState;
	}
	public String getReturndate() {
		return returndate;
	}
	public void setReturndate(String returndate) {
		this.returndate = returndate;
	}
	public String getOperating() {
		return operating;
	}
	public void setOperating(String operating) {
		this.operating = operating;
	}
	
	
	
}
