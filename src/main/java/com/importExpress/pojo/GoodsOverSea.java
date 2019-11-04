package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: 海外仓
 * @date:2019/10/17
 */
@Data
public class GoodsOverSea {


    private Integer id;
    private String pid;
    private Integer countryId;
    private String countryName;
    private String createTime;
    private Integer adminId;
    private Integer isSupport;
    private String adminName;



}
