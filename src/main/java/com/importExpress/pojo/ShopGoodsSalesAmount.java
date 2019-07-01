package com.importExpress.pojo;

import lombok.Data;

@Data
public class ShopGoodsSalesAmount {

    private Integer id;
    private String shopId;
    private int goodsNum;
    private double totalPrice;
}
