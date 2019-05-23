package com.importExpress.pojo;

import lombok.Data;

@Data
public class UserOtherInfoBean {

    private Integer id;
    private Integer userId;
    private String userName;
    private String userEmail;
    private String webFacebookUrl;
    /**
     * What kind of business services are beneficial to you:
     * [ 1] Combine Shipping (ocean freight, cheapest shipping rate)
     * [ 2] Combine Shipping (air freight, better price than shipping individually)
     * [3 ] Quality Control
     * [ 4] Custom Packaging
     */
    private String userType;
    private String userTypeDesc;

    /**
     * tell us what you want
     */
    private String remarks;

    private String createTime;
    private String followTime;

    private Integer adminId;

    private String beginTime;
    private String endTime;
    private int limitNum;
    private int startNum;


}
