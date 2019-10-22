package com.importExpress.pojo;

import lombok.Data;

@Data
public class GoodsSkuAttr {
	private int id;
	private String pid;
	private String specid;
	private String skuid;
	private String skuattr;
	private String type;
	
	/**
	 * 默认200
	 * 错误代码    100-sku数据格式错误; 101-产品表查询不到数据;  102-无匹配规格; 103-无sku内容
	 */
	private int errorCode;

}
