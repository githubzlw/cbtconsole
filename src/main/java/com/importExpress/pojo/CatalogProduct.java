package com.importExpress.pojo;

import lombok.Data;

@Data
public class CatalogProduct {
	
	private String pid;
	private String name;
	private String img;
	private int sold;
	private String unit;
	private String url;
	private String catid;

}
