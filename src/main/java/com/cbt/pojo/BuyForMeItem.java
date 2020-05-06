package com.cbt.pojo;

import com.importExpress.pojo.CustomerQuestionsAndReplayBean;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importexpress.aliexpress.model
 * @date:2020/3/16
 */
@Data
public class BuyForMeItem implements Serializable {

    private int id;
    private String orderNo;
    private String title;
    private String pic_url;
    private String promotion_price;
    private String price;
    private String orginal_price;
    private String sales;
    private String num_iid;
    private String sample_id;
    private String seller_nick;
    private String post_fee;

    private String star;
    private String area;
    private String detail_url;

    private int num;
    private String remark;
    private double totalPrice = 0;
    /**
     * 0速卖通搜索 1图片搜索
     */
    private int img_search = 0;

    private String remark_replay;

    private int moq=0;

    private Integer state;
    /**
     * 销售回复的列表
     */
    private List<CustomerQuestionsAndReplayBean> remarkReplay;
}
