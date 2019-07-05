package com.importExpress.pojo;

import lombok.Data;

@Data
public class AliPayInfo {

    private Integer id;
    private String orderCreateTime;
    private String orderNo;
    private String goodsName;
    private double orderAmount;
    private String payState;
    private String merchantAccount;
    private String merchantName;
    private String transactionNo;
    private String tradeMark;
    private double discounts;
    private double refundAmount;
    private double serviceFee;
    private String paymentChannel;
    private String createTime;
}
