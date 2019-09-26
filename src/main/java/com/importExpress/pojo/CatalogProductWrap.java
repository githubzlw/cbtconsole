package com.importExpress.pojo;

import java.util.List;

import lombok.Data;

@Data
public class CatalogProductWrap {
	private String catid;
	private String category;
	List<CatalogProduct> products;
}
