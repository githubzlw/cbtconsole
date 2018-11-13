package com.cbt.pojo;

import java.io.Serializable;

public class ZfuDate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String datas;// 日期 2017-04

	private double beginBlance;// 支付宝月初余额

	private double endBlance;// 月末余额

	private double transfer;// 银行转账支付宝
	
	private double payFreight;//本月实际支付的运费

	private double ebayAmount;//亚马逊公司支付宝支出金额
	
	private double materialsAmount;//电商物料
	
	private double zfbFright;//支付宝支出运费
	private double cancelAmount;//当月采购订单退款金额
	public double getCancelAmount() {
		return cancelAmount;
	}

	public void setCancelAmount(double cancelAmount) {
		this.cancelAmount = cancelAmount;
	}


	public double getZfbFright() {
		return zfbFright;
	}

	public void setZfbFright(double zfbFright) {
		this.zfbFright = zfbFright;
	}

	public double getEbayAmount() {
		return ebayAmount;
	}

	public void setEbayAmount(double ebayAmount) {
		this.ebayAmount = ebayAmount;
	}

	public double getMaterialsAmount() {
		return materialsAmount;
	}

	public void setMaterialsAmount(double materialsAmount) {
		this.materialsAmount = materialsAmount;
	}

	public double getPayFreight() {
		return payFreight;
	}

	public void setPayFreight(double payFreight) {
		this.payFreight = payFreight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDatas() {
		return datas;
	}

	public void setDatas(String datas) {
		this.datas = datas;
	}

	public double getBeginBlance() {
		return beginBlance;
	}

	public void setBeginBlance(double beginBlance) {
		this.beginBlance = beginBlance;
	}

	public double getEndBlance() {
		return endBlance;
	}

	public void setEndBlance(double endBlance) {
		this.endBlance = endBlance;
	}

	public double getTransfer() {
		return transfer;
	}

	public void setTransfer(double transfer) {
		this.transfer = transfer;
	}
}
