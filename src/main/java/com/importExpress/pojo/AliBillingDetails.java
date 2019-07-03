package com.importExpress.pojo;

import lombok.Data;

@Data
public class AliBillingDetails {

    private Integer id;
    private String billTime;
    private String transactionNo;
    private String serialNo;
    private String orderNo;
    private String payType;
    private double income;
    private double expend;
    private double balance;
    private double serviceFee;
    private String paymentChannel;
    private String merchantAccount;
    private String merchantName;
    private String goodsName;
    private String remark;
    private int accountType;
}
