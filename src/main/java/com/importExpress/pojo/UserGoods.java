package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2019/12/24
 */
@Data
public class UserGoods {

    private int userId;
    private String email;
    private String pid;
    private String mainImg;
    private String remotePath;
    private String goodsName;
    private int sellNum;
    private String goodsUrl;
    private Integer valid;

    private int rows;
    private int startNum;

}
