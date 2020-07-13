package com.importExpress.pojo;

import lombok.Data;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.pojo
 * @date:2020/1/9
 */
@Data
public class OrderPurchase {


    private String orderid;
    private String orderpaytime;
    private int yourorder;
    private double goodsprice;
    private int od_state;
    private int purchase_state;
    private String od_shipno;
    private String od_pid;

    private double exchange_rate;

    private String shipno;
    private String itemid;
    private String itemurl;
    private String itemqty;
    private String itemprice;
    private String totalprice;
    private String orderdate;
    private String tb_orderid;

    private String orderstatus;

    private String year;
    private String month;

    private String beginTime;
    private String endTime;
    private int startNum;
    private int limitNum;
}
