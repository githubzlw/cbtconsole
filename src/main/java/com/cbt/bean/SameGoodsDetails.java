package com.cbt.bean;

import lombok.Data;

@Data
public class SameGoodsDetails {
	
	private String pid;
	private String shopId;
	private String shopName;
	private String jsonContent;
	private String bookedCnt;
	private Boolean salesShop = false;  // 是否是 核心供应商 默认false 不是

}
