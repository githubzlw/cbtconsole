package com.importExpress.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * query_user_list.html 页面对应查询用户数据
 */
@Data
public class UserXlsBean implements Serializable {

	private static final long serialVersionUID = -358454566351860319L;

	private int id; //用户id
    private String name;//用户名
    private String email;//用户邮箱
    private String currency;//国家
    private String admName;//用户对应销售
    private String carNum = "0";//购物车商品数量

    private String orderid;//订单号
    private String state;//订单状态
    private String orderdesc;//支付日志
    private String product_cost;//订单价格
    private String paystatus;//支付状态

    private String whatsapp;//用户whatsApp

    private Integer userType;//未下单用户类型
    private Date createtime;//对应数据时间

}
