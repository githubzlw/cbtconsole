package com.importExpress.pojo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

@Data
@Builder
@Accessors(chain=true)
public class ProductSkuWeight {
	@Tolerate
	public ProductSkuWeight() {}
	private int id;//      int     自增
	private String pid;//     varchar（50） 产品ID
	private String skuid;//    varchar（50） 产品SKUID
	private int weight;//     int                  产品的克数
	private int v_weight;//    int                 产品的体积重 克数 
	private int adminid;//    int  录入人员
	private String createtime;//     datetime    录入时间
	private int updatestate;//    int              0 需要更新  1 已经更新
	private String updatetime;//    datetime     更新时间

}
