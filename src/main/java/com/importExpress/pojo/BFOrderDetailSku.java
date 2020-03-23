package com.importExpress.pojo;

import lombok.Data;

@Data
public class BFOrderDetailSku {
	private int id;
	private int bfId;
	private int bfDetailsId;
	private String numIid;
	private String skuid;
	private String sku;
	private int num;
	private int state;
	private String remark;
	private String price;
	private String productUrl;
	private String priceBuy;
	private String priceBuyc;
	private String shipFeight;
}
