package com.cbt.pojo;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.common.pojo
 * @date:2020/3/24
 */

import lombok.Data;

/**
 * 处理后的详情数据
 */
@Data
public class BuyForMeDeal {

    private String order_no;
    private Integer id;
    private Integer bf_id;
    private Integer bf_details_id;
    private String num_iid;
    private String skuid;
    private String price;
    private Integer num;
    private String remark;
    private Integer state;
    private String product_url;
    private String sku;
    private String price_buy;
    private String ship_feight;
    private String price_buy_c;
    private String weight;
    private String unit;

    private int isCheck = 0;
    private int del_flag=0;
}
