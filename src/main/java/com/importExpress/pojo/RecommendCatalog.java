package com.importExpress.pojo;

import lombok.Data;

@Data
public class RecommendCatalog {
    /**
     * 编号
     */
    private int id;
    /**
     *目录名称
     */
    private String catalogName;
    /**
     *模板
     * 1-import 2-kids 4-pets
     */
    private int template;
    /**
     *生成人
     */
    private String createAdmin;
    /**
     *生成时间
     */
    private String createTime;
    /**
     *包含商品数量
     */
    private int productCount;
    /**
     *包含商品id
     */
    private String productIdList;
    /**
     *目录存放位置
     */
    private String catalogFile;

    /**
     * 状态 1-正常 0-删除
     */
    private int status;
}
