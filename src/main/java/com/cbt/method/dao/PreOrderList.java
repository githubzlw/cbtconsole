package com.cbt.method.dao;

public class PreOrderList {

	int id;
	private String url = "";
	private String firstOption = "";
	private String secondOption = "";
	private String qty = "";
	private String price="";
	private String saleprice="";
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(String saleprice) {
		this.saleprice = saleprice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public void setFirstOption(String firstOption){
		this.firstOption = firstOption;
	}
	
	public String getFirstOption(){
		return this.firstOption;
	}
	
	public void setSecondOption(String secondOption){
		this.secondOption = secondOption;
	}
	
	public String getSecondOption(){
		return this.secondOption;
	}
	
	public void setQty(String qty){
		this.qty = qty;
	}
	
	public String getQty(){
		return this.qty;
	}


}
