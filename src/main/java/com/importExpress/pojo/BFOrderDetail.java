package com.importExpress.pojo;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class BFOrderDetail {
	private int id;
	/**
	 * buyforme_orderinfo 表ID
	 */
	private int bfId;
	private String title;
	private String picUrl;
	private String price;
	private String detailUrl;
	private String state;
	private String createTime;
	private String remark;
	private String remarkReplay;
	private String orderNo;
	private String weight;
	/**
	 * 产品ID
	 */
	private String numIid;
	private int num;
	
	private List<DetailsSku> skus = Lists.newArrayList();
	private int skuCount;

}
