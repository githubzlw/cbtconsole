package com.importExpress.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

@Data
@Builder
@Accessors(chain=true)
public class SynonymsCategoryWrap {
	@Tolerate
	public SynonymsCategoryWrap() {}
	private int id;
	/**
	 * 类别名称
	 */
	private String category;
	/**
	 * 类别id
	 */
	private String catid;
	/**
	 * 同义词
	 */
	private String synonymsCategory;
	/**
	 * 
	 */
	private int valid;

}
