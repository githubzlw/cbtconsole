package com.importExpress.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailsSku {
	private int id;
	private String skuid;
	private String sku;
	private String url;
	private String price;
	private int num;
	private String priceBuy;
	private String shipFeight;
	private String priceBuyc;
}
