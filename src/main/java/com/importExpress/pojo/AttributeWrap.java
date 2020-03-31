package com.importExpress.pojo;

import java.util.List;

import lombok.Data;

@Data
public class AttributeWrap {
	/**
	 * 属性id
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 值列表
	 */
	private List<Attribute> attrs;
}
