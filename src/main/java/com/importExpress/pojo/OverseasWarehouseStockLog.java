package com.importExpress.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

/**
 * overseas_warehouse_stock_log数据表映射类
 */
@Data
@Builder
@Accessors(chain = true)
public class OverseasWarehouseStockLog {
	@Tolerate
    OverseasWarehouseStockLog() {}
    private int id;
    /**
     *海外仓库存表id
     */
    private int owsid;
    /**
     *订单表id
     */
    private int odid;
    /**
     *订单号
     */
    private String orderno;
    /**
     *库存变更数量
     */
    private int changeStock;
    /**
     *变更类型
     * 0-占用库存； 1-释放库存
     */
    private int changeType;
    /**
     *创建时间
     */
    private String createTime;
    /**
     *备注
     */
    private String remark;
    /**
     * 商品号
     */
    private String code;
    /**
     * 商品号
     */
    private String coden;
}
