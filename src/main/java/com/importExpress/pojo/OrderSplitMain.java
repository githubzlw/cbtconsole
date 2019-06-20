package com.importExpress.pojo;

import lombok.Data;

@Data
public class OrderSplitMain {
	private int id;
	private String orderid;
	private double weight;
	private double cost;
	private double feight;
	private int country;
	private String modeTransport;
	private String countryName;
}
