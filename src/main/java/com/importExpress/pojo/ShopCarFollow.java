package com.importExpress.pojo;

import lombok.Data;

@Data
public class ShopCarFollow {
    private Integer id;
    private Integer userId;
    private Integer adminId;
    private String content;
    private String adminName;
    private String createTime;
}