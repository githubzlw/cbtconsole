package com.importExpress.pojo;

import lombok.Data;

@Data
public class ProductBatchBean {

    private String pid;
    private String name_en;
    private String main_img;
    private String name_ch;
    private String catid;
    private String catid_name;
    private String shop_id;
    private String sku_id;
    private String sku_name;//SKU英文
    private String sku_name_ch;//SKU中文
    private String weight;// 重量(g)
    private String volume_weight;//体积重量(g)
    private String unit;//单位

    private int p1_moq;//P1 MOQ
    private String p1_free_price;//免邮价
    private String p1_wprice;//非免邮价
    private String p1_1688;//1688价格

    private int p2_moq;
    private String p2_free_price;
    private String p2_wprice;
    private String p2_1688;

    private int p3_moq;
    private String p3_free_price;
    private String p3_wprice;
    private String p3_1688;
    private String old_1688;//1688价格
    private String packing_size;//包装尺寸

}
