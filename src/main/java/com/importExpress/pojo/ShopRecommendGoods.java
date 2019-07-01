package com.importExpress.pojo;

import lombok.Data;

@Data
public class ShopRecommendGoods {
    private Integer id;
    private String shopId;
    private Integer recoId;
    private String pid;
    private int isOn;
    private String createTime;
    private int createAdminId;
    private String updateTime;
    private int updateAdminId;
}
