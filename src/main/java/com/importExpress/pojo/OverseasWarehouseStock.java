package com.importExpress.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;
/**
 * overseas_warehouse_stock数据表映射类
 */
@Data
@Builder
@Accessors(chain = true)
public class OverseasWarehouseStock {
	@Tolerate
	OverseasWarehouseStock(){}
	private int id;
    /**
     *海外仓库存
     */
    private int owStock;
    /**
     *订单占用库存
     */
    private int orderStock;
    /**
     *可用库存
     */
    private int availableStock;
    /**
     *产品id
     */
    protected String goodsPid;
    /**
     *海外仓商品编码
     */
    private String code;
    /**
     *海外仓商品编码(去除规格组合差异)
     */
    private String coden;
    /**
     *产品名称
     */
    private String goodsName;
    /**
     *产品规格
     */
    private String sku;
    /**
     *产品规格skuid
     */
    private String skuid;
    /**
     *产品规格specid
     */
    private String specid;
    /**
     *备注
     */
    private String remark;
    /**
     *创建时间
     */
    private String createTime;
    /**
     *更新时间
     */
    private String updateTime;
}
