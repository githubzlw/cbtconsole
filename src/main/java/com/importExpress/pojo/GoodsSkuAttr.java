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
	 * 错误代码 101 sku数据格式错误;102产品表查询不到数据;103无sku内容; 104 无匹配规格
	 */
	private int errorCode;

}
