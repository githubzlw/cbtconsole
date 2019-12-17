package com.cbt.report.vo;

import lombok.Data;

/**
 * 订单取消(全部或部分)详情
 *
 * @author JXW
 */
@Data
public class OrderCancelBean {

    private String year;// 年份
    private String month;// 月份
    private int userId;// 客户id

    private String ipnOrderNo;
    private String txnId;
    private double payAmount;
    private String payTime;

    private double amount;// 取消商品金额
    private String remarkId;//备注ID一般是订单号
    private String remark;//备注信息
    private int payType = -1;// 退款类型

}
