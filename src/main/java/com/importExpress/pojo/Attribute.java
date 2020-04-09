package com.importExpress.pojo;

import lombok.Data;

@Data
public class Attribute {
	/**
	 * 属性id
	 */
	private String id;
	/**
	 * 属性
	 */
	private String name;
	/**
	 * 属性值
	 */
	private String value;

	/**
	 * 链接
	 */
	private String url;
}
