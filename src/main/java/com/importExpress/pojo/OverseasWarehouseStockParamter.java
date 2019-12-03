package com.importExpress.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

/**
 * 海外仓管理页面参数
 * @author Administrator
 *
 */
@Data
@Builder
@Accessors(chain = true)
public class OverseasWarehouseStockParamter {
	@Tolerate
	OverseasWarehouseStockParamter(){}
	/**
	 * 页码
	 */
	private int page;
	/**
	 * 产品ID
	 */
	private String goodsPid;
	/**
	 * skuid
	 */
	private String skuid;
	/**
	 * specid
	 */
	private String specid;
	
	/**
	 * 商品号
	 */
	private String code;
	
	/**
	 * overseas_warehouse_stock 表id
	 */
	private int owsid;
	
	/**
	 * 0-占用 1-释放   -1 全部
	 */
	@Builder.Default
	private int changeType=-1;
	
	/**
	 * 订单odid
	 */
	@Builder.Default
	private int odid = 0;

}
