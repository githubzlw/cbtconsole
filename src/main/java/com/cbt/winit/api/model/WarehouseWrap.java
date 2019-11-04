package com.cbt.winit.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**仓库模型
 * @author Administrator
 *
 */
@Data
@Builder
@Accessors(chain=true)
public class WarehouseWrap {
	/**
	 * 	海外仓仓库ID
	 */
	private String id;
	/**
	 * 海外仓库代码
	 */
	private String code;
	/**
	 * 海外仓仓库名称
	 */
	private String name;
	/**
	 * 海外仓仓库地址
	 */
	private String address;
	
}
