package com.cbt.warehouse.pojo;

public class OrderFeePojo {
	private String userid;
	private String orderno;
	private String trans_method;  //出货方式
	private String exporttime;   //出货时间
	private String acture_fee;   //实际运费
	private String acture_fee_RMB;   //实际运费
	private String acture_fee_USD;   //实际运费
	private String isBadMoney;   
	private String currency; //货币单位
	private String express_no;// 快递单号
	
	
	
	public String getExpress_no() {
		return express_no;
	}
	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTrans_method() {
		return trans_method;
	}
	public void setTrans_method(String trans_method) {
		this.trans_method = trans_method;
	}
	public String getExporttime() {
		return exporttime;
	}
	public void setExporttime(String exporttime) {
		this.exporttime = exporttime;
	}
	public String getActure_fee() {
		return acture_fee;
	}
	public void setActure_fee(String acture_fee) {
		this.acture_fee = acture_fee;
	}
	public String getIsBadMoney() {
		return isBadMoney;
	}
	public void setIsBadMoney(String isBadMoney) {
		this.isBadMoney = isBadMoney;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getActure_fee_RMB() {
		return acture_fee_RMB;
	}
	public void setActure_fee_RMB(String acture_fee_RMB) {
		this.acture_fee_RMB = acture_fee_RMB;
	}
	public String getActure_fee_USD() {
		return acture_fee_USD;
	}
	public void setActure_fee_USD(String acture_fee_USD) {
		this.acture_fee_USD = acture_fee_USD;
	}
	
	

}
