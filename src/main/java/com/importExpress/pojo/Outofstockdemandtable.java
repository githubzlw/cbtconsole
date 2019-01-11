package com.importExpress.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class Outofstockdemandtable {
    private Integer id;

    private String email;

    private String itemid;

    private String goodstype;

    private Date creatime;

    private Date updatetime;

    private Integer flag;

    private String ctime;

    private String etime;
}