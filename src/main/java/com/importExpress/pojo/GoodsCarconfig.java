package com.importExpress.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class GoodsCarconfig {
    private Integer id;

    private Integer userid;

    private String shippingname;

    private Double shippingcost;

    private String shippingdays;

    private Integer changeexpress;

    private Double saveFreight;

    private Integer addflag;

    private String usercookieid;

    private Date updatetime;

    private Double totalFreight;

    private String sumTypePrice;

    private Double fastshipbalance;

    private Integer flag;

    private Integer usermark;

    private Integer needcheck;
}