package com.cbt.website.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class LossInventoryWrap extends LossInventoryRecord {
	/**
	 * 图片
	 */
	private String img;
	/**
	 * 链接
	 */
	private String url;
	/**
	 * 报损类型说明
	 */
	private String changeContext;

}
