package com.cbt.website.bean;

import lombok.Data;

/**库存盘点 
 * @author Administrator
 *
 */
@Data
public class InventoryCheck {
	
	/**
	 * 
	 */
	private int id;
	/**
	 * 0 表示全部盘点
	 */
	private int checkCat;
	/**
	 * 盘点时间
	 */
	private String checkTime;
	/**
	 * 开始盘点的人员
	 */
	private int checkAdm;
	/**
	 * 撤销人员
	 */
	private int cancelAdm;
	/**
	 * 撤销盘点时间
	 */
	private String cancelTime;
	/**
	 * 撤销说明
	 */
	private String cancelRemark;
	
	/**
	 * 撤销标记
	 */
	private int cancleFlag;

}
