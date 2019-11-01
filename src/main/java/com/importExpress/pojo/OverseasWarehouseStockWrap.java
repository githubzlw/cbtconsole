package com.importExpress.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class OverseasWarehouseStockWrap extends OverseasWarehouseStockLog{
	/**
	 * 产品id
	 */
	private String goodsPid;
	/**
	 *产品名称 
	 */
	private String goodsName;
	/**
	 * 产品sku
	 */
	private String sku;
	/**
	 * skuid
	 */
	private String skuid;
	/**
	 * 产品sku的specid
	 */
	private String specid;
	/**
	 * 海外仓产品备注
	 */
	private String stockRemark;
    
}
