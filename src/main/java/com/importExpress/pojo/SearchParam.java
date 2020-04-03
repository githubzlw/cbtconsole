package com.importExpress.pojo;

import lombok.Data;

@Data
public class SearchParam {
	private String keyword;

	private String fKey;

	private String unkey;

	private String reverseKeywords;

	private String minq;

	private String maxq;

	private String sort = "default";

	private String minPrice;

	private String maxPrice;

	private String catid;

	private String attrId;

	private int page = 1;

	private int pageSize = 60;

	private int freeShipping = 1;

	private int site;

	private String uriRequest;

	private Currency currency;

	private boolean isImportExpress = false;

	private int importType = 0;

	private int  userType = 1;
	private boolean isFactCategory = true;

	private boolean isFactPvid = true;

	private boolean boutique = false;

	private int collection;

	private String storied;

	private String newArrivalDate;

	private boolean isMobile = false;

	private boolean isOrder = true;

	private  String prices;

	private String pid;

	private int selectedInterval;

	private boolean isRange;

	private boolean synonym=false;

	private boolean salable = false;
}
