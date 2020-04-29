package com.cbt.pojo;

import com.importExpress.pojo.Address;
import com.importExpress.pojo.UserBean;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * *****************************************************************************************
 *
 * @ClassName Buy4MeCusotme
 * @Author: cjc
 * @Descripeion TODO
 * @Date： 2020/4/21 5:28 下午
 * @Version 1.0
 * <p>
 * <p>
 * Version    Date                ModifiedBy                 Content
 * --------   ---------           ----------                -----------------------
 * 1.0.0       5:28 下午2020/4/21     cjc                       初版
 * ******************************************************************************************
 */
@Data
public class Buy4MeCusotme {
    public String userId;
    public String jumpLink;
    public boolean hasMsg;
    public String adm;

    private String sessionId;
    private Double totalPrice = 0D;
    private Double goodsCost = 0D;
    private Integer totalNum = 0;
    private Integer itemNum = 0;
    private Double itemPrice = 0D;
    private Double serviceFee = 0D;
    private String orderNo;
    private int state = -2;
    private String our_order_no;
    private String deliveryTime;
    private String deliveryMethod;
    private String shipFeight;
    private int sampleFlag = 0;
    private String country = "Tourist";
}
