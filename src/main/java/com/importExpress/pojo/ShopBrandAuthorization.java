package com.importExpress.pojo;

import lombok.Data;

/**
 * 店铺品牌授权
 */
@Data
public class ShopBrandAuthorization {

    private Integer id;
    private String shopId;
    private String shopName;
    private String brandName;
    private int authorizeState;
    private String termOfValidity;
    private String certificateFile;
    private String createTime;
    private String localPath;
    private String remotePath;
    private String updateTime;
}
