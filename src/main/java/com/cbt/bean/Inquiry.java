package com.cbt.bean;

import java.util.Date;
import java.util.List;

/**
 * @author ylm
 * 询价表
 */
public class Inquiry {

	private int id;
	private String payno;//支付唯一码
	private double server_price;//支付总询价费
	private double pay_price;//已支付费用
	private Date createtime;
	private List<InquiryDetail> details;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPayno() {
		return payno;
	}
	public void setPayno(String payno) {
		this.payno = payno;
	}
	public double getServer_price() {
		return server_price;
	}
	public void setServer_price(double server_price) {
		this.server_price = server_price;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public List<InquiryDetail> getDetails() {
		return details;
	}
	public void setDetails(List<InquiryDetail> details) {
		this.details = details;
	}
	public double getPay_price() {
		return pay_price;
	}
	public void setPay_price(double pay_price) {
		this.pay_price = pay_price;
	}
	 
	

}
