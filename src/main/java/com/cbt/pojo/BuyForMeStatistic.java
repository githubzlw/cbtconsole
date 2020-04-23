package com.cbt.pojo;

import com.importExpress.pojo.Address;
import com.importExpress.pojo.UserBean;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: JiangXW
 * @version: v1.0
 * @description: com.importExpress.common.pojo
 * @date:2020/3/19
 */
@Data
public class BuyForMeStatistic {

    private String sessionId;
    private Integer userId;
    private Double totalPrice = 0D;
    private Double goodsCost = 0D;
    private Integer totalNum = 0;
    private Integer itemNum = 0;
    private Double itemPrice = 0D;
    private Double serviceFee = 0D;
    private List<BuyForMeItem> itemList;

    private String orderNo;
    private int state = -2;

    private Map<String, List<BuyForMeDeal>> dealMap;

    private Address address;
    private String our_order_no;

    private String deliveryTime;
    private String deliveryMethod;
    private String shipFeight;
    private int sampleFlag = 0;
    private UserBean userBean;
}
