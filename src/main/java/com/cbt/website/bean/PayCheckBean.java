package com.cbt.website.bean;

public class PayCheckBean {
	private int userid;
	private String email;
	private String payEmail;
	private String paytype;
	private String dataStart;
	private String dataEnd;
	private int page;
	private int ordersum;
	private int rows;
	private String payId;//交易号

	public int getOrdersum() {
		return ordersum;
	}

	public void setOrdersum(int ordersum) {
		this.ordersum = ordersum;
	}

	public String getDataStart() {
		return dataStart;
	}

	public void setDataStart(String dataStart) {
		this.dataStart = dataStart;
	}

	public String getDataEnd() {
		return dataEnd;
	}

	public void setDataEnd(String dataEnd) {
		this.dataEnd = dataEnd;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPayEmail() {
		return payEmail;
	}

	public void setPayEmail(String payEmail) {
		this.payEmail = payEmail;
	}

	public String getPaytype() {
		return paytype;
	}

	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	@Override
	public String toString() {
		return "PayCheckBean [userid=" + userid + ", email=" + email + ", payEmail=" + payEmail + ", paytype=" + paytype
				+ ", dataStart=" + dataStart + ", dataEnd=" + dataEnd + ", page=" + page + ", ordersum=" + ordersum
				+ ", rows=" + rows + "]";
	}

}
