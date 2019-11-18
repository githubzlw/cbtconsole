package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2019/11/15
 */
@Data
public class GoodsWeightChange {

    private Integer id;
    private String pid;
    private String goodsType;
    private String weight;
    private String volumeWeight;
    private String createTime;
    private String adminName;
    private Integer adminId;
    private Integer syncFlag;
    private String updateTime;
    private String updateAdminName;
    private Integer updateAdminId;
    private int startNum;
    private int limitNum;
}
