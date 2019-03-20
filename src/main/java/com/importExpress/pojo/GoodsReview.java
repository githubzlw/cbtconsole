package com.importExpress.pojo;

import lombok.Data;

@Data
public class GoodsReview {

    private Long id;

    //商品号
    private String goodsPid;

    //评论国家
    private String country;

    //评论人
    private String reviewName;

    //评论分数
    private Integer reviewScore;

    //评论时间
    private String createtime;

    //修改时间
    private String updatetime;

    //评论内容
    private String reviewRemark;

    //0:运营评论 1客户评论
    private Integer type;

    //0显示 1不显示
    private Integer reviewFlag;

}