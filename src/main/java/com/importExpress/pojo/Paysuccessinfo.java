package com.importExpress.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Paysuccessinfo {
    private Integer id;

    private Integer userid;

    private String orderno;

    private String paymentamount;

    private Integer sampleschoice;

    private Integer sharechoice;

    private String info;

    private Date creatime;

    private Integer del;

    private String creatimeStr;

    // 0 默认 1是 2 否 3:客户看不到
    private String sampleschoicestr;

    // 0 默认 1是 2 否 3:客户看不到
    private String sharechoicestr;

}