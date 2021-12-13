package com.importExpress.pojo;

import lombok.Data;

@Data
public class ProductBatchDiscountParam {
    private String pid;
    private String sku_id;
    private int rows;
    private int page;
    private int startNum;
    private int limitNum;
    private int admin_id;

}
