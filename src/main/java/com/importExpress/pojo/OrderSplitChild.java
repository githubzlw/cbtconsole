package com.importExpress.pojo;

import lombok.Data;

@Data
public class OrderSplitChild {
	private int mainid;
	private String orderid;
	private double weight;
	private double cost;
	private double feight;
	private String childOrderid;
	private int recommend;
	private int country;
	private String modeTransport;

}
