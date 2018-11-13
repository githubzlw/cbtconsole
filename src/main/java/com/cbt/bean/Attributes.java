package com.cbt.bean;

import net.sf.json.JSONArray;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Attributes implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Map<String,String> specAttributes;
	
	private int amountOnSale;
	
	private String price;//

	public Map<String,String>  getSpecAttributes() {
		return specAttributes;
	}

	public void setSpecAttributes(Map<String,String>  specAttributes) {
		this.specAttributes = specAttributes;
	}

	public int getAmountOnSale() {
		return amountOnSale;
	}

	public void setAmountOnSale(int amountOnSale) {
		this.amountOnSale = amountOnSale;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	
	
	
	

	@Override
	public String toString() {
		return String.format(
				"{\"specAttributes\":%s,\"amountOnSale\":%s,\"price\":\"%s\"}",
				specAttributes, amountOnSale, price);
	}

	public static void main(String[] args) {
		String  type = "[{\"specAttributes\":{\"450\":\"XL\"},\"amountOnSale\":97},"
				+ "{\"specAttributes\":{\"450\":\"M\"},\"amountOnSale\":91},"
				+ "{\"specAttributes\":{\"450\":\"L\"},\"amountOnSale\":93}]";
		JSONArray fromObject = JSONArray.fromObject(type);
		List<Attributes> list = fromObject.toList(fromObject, Attributes.class);
		
		System.err.println(list);
		
		
	}
	
}
