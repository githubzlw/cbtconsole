package com.cbt.website.bean;

import java.io.Serializable;

public class PrePurchasePojo implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String flag;
	private String orderid;
	private String user_id;
	private String paytime;
	private String domesticTime;
	private String internationalTime;
	private String status;
	private String country;
	private String amounts;
	private String saleer;
	private int countOd;
	private int checked;
	private int problem;
	private String product_cost;
	private String delaytime;
	private String amount_s;
	private String fptime;
	private String remark;
	private String option;
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFptime() {
		return fptime;
	}

	public void setFptime(String fptime) {
		this.fptime = fptime;
	}

	public String getAmount_s() {
		return amount_s;
	}

	public void setAmount_s(String amount_s) {
		this.amount_s = amount_s;
	}

	public String getDelaytime() {
		return delaytime;
	}

	public void setDelaytime(String delaytime) {
		this.delaytime = delaytime;
	}
	public String getProduct_cost() {
		return product_cost;
	}
	public void setProduct_cost(String product_cost) {
		this.product_cost = product_cost;
	}
	public int getProblem() {
		return problem;
	}
	public void setProblem(int problem) {
		this.problem = problem;
	}
	public int getCountOd() {
		return countOd;
	}
	public void setCountOd(int countOd) {
		this.countOd = countOd;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public String getFlag() {
	return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public String getDomesticTime() {
		return domesticTime;
	}
	public void setDomesticTime(String domesticTime) {
		this.domesticTime = domesticTime;
	}
	public String getInternationalTime() {
		return internationalTime;
	}
	public void setInternationalTime(String internationalTime) {
		this.internationalTime = internationalTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAmounts() {
		return amounts;
	}
	public void setAmounts(String amounts) {
		this.amounts = amounts;
	}
	public String getSaleer() {
		return saleer;
	}
	public void setSaleer(String saleer) {
		this.saleer = saleer;
	}
}
