package com.cbt.bean;

import java.util.List;

public class AdvanceOrderBean {

	private int id;
	private String orderNo;
	private String questions;
	private String answer;
	private String freight;
	private String tariffs;
	private String createtime;
	private List<SpiderBean> spider;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getQuestions() {
		return questions;
	}
	public void setQuestions(String questions) {
		this.questions = questions;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getTariffs() {
		return tariffs;
	}
	public void setTariffs(String tariffs) {
		this.tariffs = tariffs;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	public  List<SpiderBean> getSpiderBean() {
		return spider;
	}
	public void setSpiderBean( List<SpiderBean> spider) {
		this.spider = spider;
	}
	
	
}
