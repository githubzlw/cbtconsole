package com.cbt.website.bean;

import lombok.Data;

@Data
public class UserInfo {

    private int userid;
    private String userName;
    private String email;
    private String creattime;//创建时间
    private int isfacebook;//是否是facebook登录
    private String zone;//国家
    private String address;//地址
    private String address2;
    private String phone;
    private String statename;
    private String zip_code;
    private Float available;
    private Float goodsPrice;//购物车总额
    private Float orderPriceu;//未付款订单总额
    private Float orderPrice;//已付款订单总额
    private Float payment;//已支付总额
    private String adminname;
    private String currency;//用户货币单位
    private int gid;//等级id
    private String grade;//等级名称
    private String loginStyle;//登录方式
    private String otherphone;//手机号码
    private String pass;
    private String bind_google;
    private String userLogin;
    private String userManager;
    private String operation;
    private String admuser;
    private String goodsPriceUrl;
    private String businessName;
    private String car_info;
    /**
     * 网站标识
     */
    private int site;
    private String webSite;

    private String businessinfo;
    private String productone;
    private String producttwo;
    private String requirementone;
    private String requirementtwo;
    /**
     * 申请时间
     */
    private String applicationTime;

    /**
     * 授权标识 0未授权 1授权
     */
    private int authFlag;

    /**
     * 历史成交价格
     */
    private double historyAmount;

    /**
     * 购物车金额
     */
    private double shopCarAmount;
    private Integer countryId;

    private int limitNum;
    private int startNum;

    private int adminId;

}
