package com.cbt.bean;

import java.util.Date;

/**
 * @author ylm
 * 询价详情表
 */
public class InquiryDetail {

	private int id;
	private int inquiryId;
	private double price;//询到的商品费用
	private int userid;
	private String payno;
	private int pay_state;//询价表的支付状态
	private Date deliverytime;//客服回应时间，询价状态的修改时间
	private int state;//询价状态：0-未询价，1-已询到价格，2-商品无效	 
	private SpiderBean spiderBean;
	private Date createtime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public int getUserid() {
		return userid;
	}
	
	public String getPayno() {
		return payno;
	}
	public void setPayno(String payno) {
		this.payno = payno;
	}
	public Date getDeliverytime() {
		return deliverytime;
	}
	public void setDeliverytime(Date deliverytime) {
		this.deliverytime = deliverytime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public SpiderBean getSpiderBean() {
		return spiderBean;
	}
	public void setSpiderBean(SpiderBean spiderBean) {
		this.spiderBean = spiderBean;
	}
	
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	
	public void setPay_state(int pay_state) {
		this.pay_state = pay_state;
	}
	public int getPay_state() {
		return pay_state;
	}
	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}
	public int getInquiryId() {
		return inquiryId;
	}
}
