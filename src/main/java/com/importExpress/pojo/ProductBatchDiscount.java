package com.importExpress.pojo;

import lombok.Data;

@Data
public class ProductBatchDiscount {

    private int id;
    private String pid;
    private String sku_id;
    private int p1_num;
    private double p1_discount;
    private int p2_num;
    private double p2_discount;
    private int admin_id;
    private String admin_name;
    private String update_time;
}
