package com.importExpress.pojo;

import lombok.Data;

@Data
public class PurchaseInfoBean {
    private Integer id;
    private Integer odId;
    private String orderNo;
    private Integer goodsId;
    private Integer confirmUserId;
    private String confirmTime;
    private int useCount;
    private int buyCount;
    private int purchaseState;

}
