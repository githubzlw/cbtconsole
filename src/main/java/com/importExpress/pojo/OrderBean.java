package com.importExpress.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author ylm
 * ������Ϣ
 */
public class OrderBean implements Serializable {

    private static long serialVersionUID = -6185999685428808260L;

    private int id;
    private int userid;
    private String userName;
    private int package_style;
    private String mode_transport;//运输方式
    private double service_fee;//服务费
    private String domestic_freight;//国内运费
    private String foreign_freight;//运输方式对应的运费
    private String orderNo;
    private String deliveryTime;
    private String product_cost;
    private int state;
    private List<SpiderNewBean> spider;
    private String createtime;
    private Address address;
    private double actual_allincost;//实际总费用
    private double pay_price;//客户付款总金额
    private String pay_price_tow;//客户已支付运费
    private String transport_time;//运输时间
    private String actual_ffreight;//实际国际运费
    private double remaining_price;//订单所剩金额
    private String actual_weight;//总重量
    private double actual_weight_estimate;//预估总重量
    private String actual_volume;//销售输入的订单长宽高
    private String custom_discuss_other;//细节描述
    private int cancel_obj;//取消订单对象
    private String expect_arrive_time;//预计到货时间
    private int client_update; //确认价格中  客户更新
    private int server_update; //价格确认中   服务器更新
    private String arrive_time;//用户收货时间
    private String ip;//客户的ip地址
    private double applicable_credit;//用户表中的运费补贴余额
    private double order_ac;//订单所用的运费补贴
    private double coupon_discount;//优惠券折扣
    private double grade_discount;//vip等级折扣
    private String email;//订单所用的运费补贴
    private int purchase_number;//已采购数量
    private int details_number;//需采购数量
    private String details_pay;//后台显示付款时间+最长交期
    private String pay_price_three;//用户余额抵扣金额
    private String adminname;//订单负责人员---wanyang--20151010
    private String currency;//货币单位
    private String paystatus;//支付状态
    private double discount_amount;//订单折扣金额
    private String expressNo;//快递号
    private String actual_lwh;//总体积
    private int count;//用户下单数----wanyang---20151123
    private String buyuser;//采购负责人员---sj--20151010
    private double actual_freight_c;//小于50美元运费
    private boolean orderNumber;//是否是第一次的订单，是则免服务费
    private String chaOrderNo;
    private double extra_freight;//额外运费金额
    private double extra_discount;//额外優惠
    private int free_shipping;
    private int order_count;
    private int payFlag;
    private int cancelFlag;
    private int comformFlag;//24小時内消息切换标识
    private int arrivalflag;//ling确认到账标识
    private int changenum;//替换产品数量
    private int isDropshipOrder;//是否是drop ship订单
    private int isBalancePayment;//是否可以余额支付
    private int ordertype;//订单类型  1-正常订单；2-buyforme订单
    private String exchange_rate;//实时汇率  private int order_count;

    private double memberFee;//用户下单时，充值的会员费

    private Integer addressid;
    //订单修改记录数(用于查询显示)
    private int ocC;
    private String address2;
    private String country;
    private String phoneNumber;
    private String zipCode;
    private String street;
    private String addressOne;
    private String statename;
    private Date minCreatetime;
    private String countryName;
    private double cashback;
    private double firstdiscount;//yyl

    private int package_number;//包裹数量
    private String orderRemark;//5.30  添加订单备注
    protected String order_img;

    private int odcount;

    private int houseCount;//到库数量

    private int preferential;//是否批量优惠订单 0.否 1.是


    public double getMemberFee() {
        return memberFee;
    }

    public void setMemberFee(double memberFee) {
        this.memberFee = memberFee;
    }

    public int getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(int ordertype) {
        this.ordertype = ordertype;
    }

    /**
     * 双清包税差额
     */
    private double vatBalance;

    public double getFirstdiscount() {
        return firstdiscount;
    }

    public void setFirstdiscount(double firstdiscount) {
        this.firstdiscount = firstdiscount;
    }

    public double getVatBalance() {
        return vatBalance;
    }

    public void setVatBalance(double vatBalance) {
        this.vatBalance = vatBalance;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public void setHouseCount(int houseCount) {
        this.houseCount = houseCount;
    }

    private int tolCount;//订单商品总数量

    public int getTolCount() {
        return tolCount;
    }

    public void setTolCount(int tolCount) {
        this.tolCount = tolCount;
    }

    public int getOdcount() {
        return odcount;
    }

    public String getExchange_rate() {
        return exchange_rate;
    }


    public void setExchange_rate(String exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public void setOdcount(int odcount) {
        this.odcount = odcount;
    }

    //社交分享 折扣
    private double share_discount;

    public int getComformFlag() {
        return comformFlag;
    }


    public void setComformFlag(int comformFlag) {
        this.comformFlag = comformFlag;
    }

    public int getArrivalflag() {
        return arrivalflag;
    }

    public void setArrivalflag(int arrivalflag) {
        this.arrivalflag = arrivalflag;
    }

    public int getCancelFlag() {
        return cancelFlag;
    }

    public double getExtra_discount() {
        return extra_discount;
    }

    public double getCoupon_discount() {
        return coupon_discount;
    }


    public void setCoupon_discount(double coupon_discount) {
        this.coupon_discount = coupon_discount;
    }


    public void setExtra_discount(double extra_discount) {
        this.extra_discount = extra_discount;
    }

    public void setCancelFlag(int cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    /**
     * @return the payFlag
     */
    public int getPayFlag() {
        return payFlag;
    }


    /**
     * @param payFlag the payFlag to set
     */
    public void setPayFlag(int payFlag) {
        this.payFlag = payFlag;
    }


    public int getOrder_count() {
        return order_count;
    }


    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }


    /**
     * @return the chaOrderNo
     */
    public String getChaOrderNo() {
        return chaOrderNo;
    }


    /**
     * @return the free_shipping
     */
    public int getFree_shipping() {
        return free_shipping;
    }


    /**
     * @param free_shipping the free_shipping to set
     */
    public void setFree_shipping(int free_shipping) {
        this.free_shipping = free_shipping;
    }


    /**
     * @param chaOrderNo the chaOrderNo to set
     */
    public void setChaOrderNo(String chaOrderNo) {
        this.chaOrderNo = chaOrderNo;
    }

    private int total;

    /**
     * @return the expressNo
     */
    public String getExpressNo() {
        return expressNo;
    }


    /**
     * @param expressNo the expressNo to set
     */
    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    private List<OrderDetailsBean> orderDetail;//订单详情

    public OrderBean() {
        super();
    }


    public OrderBean(int id, String orderNo, String deliveryTime,
                     String product_cost, int state, List<SpiderNewBean> spider,
                     String createtime) {
        super();
        this.id = id;
        this.orderNo = orderNo;
        this.deliveryTime = deliveryTime;
        this.product_cost = product_cost;
        this.state = state;
        this.spider = spider;
        this.createtime = createtime;
    }


    @Override
    public String toString() {
        return String
                .format("{\"id\":\"%s\", \"userid\":\"%s\", \"package_style\":\"%s\", \"mode_transport\":\"%s\", \"service_fee\":\"%s\", \"domestic_freight\":\"%s\", \"foreign_freight\":\"%s\", \"orderNo\":\"%s\", \"deliveryTime\":\"%s\", \"product_cost\":\"%s\", \"state\":\"%s\", \"spider\":\"%s\", \"createtime\":\"%s\",\"ip\":\"%s\"}",
                        id, userid, package_style, mode_transport, service_fee,
                        domestic_freight, foreign_freight, orderNo,
                        deliveryTime, product_cost, state, spider, createtime, ip);
    }


    public String getBuyuser() {
        return buyuser;
    }


    public void setBuyuser(String buyuser) {
        this.buyuser = buyuser;
    }


    public int getTotal() {
        return total;
    }


    public void setTotal(int total) {
        this.total = total;
    }


    public int getChangenum() {
        return changenum;
    }


    public void setChangenum(int changenum) {
        this.changenum = changenum;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getOrderNo() {
        return orderNo;
    }


    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }


    public String getDeliveryTime() {
        return deliveryTime;
    }


    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }


    public String getProduct_cost() {
        return product_cost;
    }


    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
    }

    public int getState() {
        return state;
    }


    public void setState(int state) {
        this.state = state;
    }

    public double getActual_allincost() {
        return actual_allincost;
    }

    public void setActual_allincost(double actual_allincost) {
        this.actual_allincost = actual_allincost;
    }

    public List<SpiderNewBean> getSpider() {
        return spider;
    }


    public void setSpider(List<SpiderNewBean> spider) {
        this.spider = spider;
    }


    public String getCreatetime() {
        return createtime;
    }


    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }


    public int getUserid() {
        return userid;
    }


    public void setUserid(int userid) {
        this.userid = userid;
    }


    public int getPackage_style() {
        return package_style;
    }


    public void setPackage_style(int package_style) {
        this.package_style = package_style;
    }


    public String getMode_transport() {
        return mode_transport;
    }


    public void setMode_transport(String mode_transport) {
        this.mode_transport = mode_transport;
    }


    public double getService_fee() {
        return service_fee;
    }


    public void setService_fee(double service_fee) {
        this.service_fee = service_fee;
    }


    public String getDomestic_freight() {
        return domestic_freight;
    }


    public void setDomestic_freight(String domestic_freight) {
        this.domestic_freight = domestic_freight;
    }


    public String getForeign_freight() {
        return foreign_freight;
    }


    public void setForeign_freight(String foreign_freight) {
        this.foreign_freight = foreign_freight;
    }

    public void setOrderDetail(List<OrderDetailsBean> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public List<OrderDetailsBean> getOrderDetail() {
        return orderDetail;
    }

    public void setPay_price(double pay_price) {
        this.pay_price = pay_price;
    }

    public double getPay_price() {
        return pay_price;
    }


    public String getTransport_time() {
        return transport_time;
    }


    public void setTransport_time(String transport_time) {
        this.transport_time = transport_time;
    }


    public String getActual_ffreight() {
        return actual_ffreight;
    }


    public void setActual_ffreight(String actual_ffreight) {
        this.actual_ffreight = actual_ffreight;
    }


    public double getRemaining_price() {
        return remaining_price;
    }


    public void setRemaining_price(double remaining_price) {
        this.remaining_price = remaining_price;
    }


    public String getActual_weight() {
        return actual_weight;
    }


    public void setActual_weight(String actual_weight) {
        this.actual_weight = actual_weight;
    }


    public String getCustom_discuss_other() {
        return custom_discuss_other;
    }


    public void setCustom_discuss_other(String custom_discuss_other) {
        this.custom_discuss_other = custom_discuss_other;
    }

    public void setActual_volume(String actual_volume) {
        this.actual_volume = actual_volume;
    }

    public String getActual_volume() {
        return actual_volume;
    }

    public void setCancel_obj(int cancel_obj) {
        this.cancel_obj = cancel_obj;
    }

    public int getCancel_obj() {
        return cancel_obj;
    }

    public void setExpect_arrive_time(String expect_arrive_time) {
        this.expect_arrive_time = expect_arrive_time;
    }

    public String getExpect_arrive_time() {
        return expect_arrive_time;
    }

    public int getClient_update() {
        return client_update;
    }

    public void setClient_update(int client_update) {
        this.client_update = client_update;
    }

    public int getServer_update() {
        return server_update;
    }

    public void setServer_update(int server_update) {
        this.server_update = server_update;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setApplicable_credit(double applicable_credit) {
        this.applicable_credit = applicable_credit;
    }

    public double getApplicable_credit() {
        return applicable_credit;
    }

    public void setOrder_ac(double order_ac) {
        this.order_ac = order_ac;
    }

    public double getOrder_ac() {
        return order_ac;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


    public int getPurchase_number() {
        return purchase_number;
    }


    public void setPurchase_number(int purchase_number) {
        this.purchase_number = purchase_number;
    }


    public int getDetails_number() {
        return details_number;
    }


    public void setDetails_number(int details_number) {
        this.details_number = details_number;
    }

    public void setDetails_pay(String details_pay) {
        this.details_pay = details_pay;
    }

    public String getDetails_pay() {
        return details_pay;
    }

    public void setPay_price_three(String pay_price_three) {
        this.pay_price_three = pay_price_three;
    }

    public String getPay_price_three() {
        return pay_price_three;
    }

    public void setPay_price_tow(String pay_price_tow) {
        this.pay_price_tow = pay_price_tow;
    }

    public String getPay_price_tow() {
        return pay_price_tow;
    }


    public String getAdminname() {
        return adminname;
    }


    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setPaystatus(String paystatus) {
        this.paystatus = paystatus;
    }

    public String getPaystatus() {
        return paystatus;
    }

    public void setDiscount_amount(double discount_amount) {
        this.discount_amount = discount_amount;
    }

    public double getDiscount_amount() {
        return discount_amount;
    }

    public boolean getOrderNumber() {
        return orderNumber;
    }


    public void setActual_lwh(String actual_lwh) {
        this.actual_lwh = actual_lwh;
    }

    public String getActual_lwh() {
        return actual_lwh;
    }

    public void setActual_weight_estimate(double actual_weight_estimate) {
        this.actual_weight_estimate = actual_weight_estimate;
    }

    public double getActual_weight_estimate() {
        return actual_weight_estimate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getActual_freight_c() {
        return actual_freight_c;
    }

    public void setActual_freight_c(double actual_freight_c) {
        this.actual_freight_c = actual_freight_c;
    }

    public void setOrderNumber(boolean orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setExtra_freight(double extra_freight) {
        this.extra_freight = extra_freight;
    }

    public double getExtra_freight() {
        return extra_freight;
    }


    public Integer getAddressid() {
        return addressid;
    }


    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }


    public int getOcC() {
        return ocC;
    }


    public void setOcC(int ocC) {
        this.ocC = ocC;
    }


    public String getAddress2() {
        return address2;
    }


    public void setAddress2(String address2) {
        this.address2 = address2;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getZipCode() {
        return zipCode;
    }


    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    public String getStreet() {
        return street;
    }


    public void setStreet(String street) {
        this.street = street;
    }


    public String getAddressOne() {
        return addressOne;
    }


    public void setAddressOne(String addressOne) {
        this.addressOne = addressOne;
    }


    public String getStatename() {
        return statename;
    }


    public void setStatename(String statename) {
        this.statename = statename;
    }


    public Date getMinCreatetime() {
        return minCreatetime;
    }


    public void setMinCreatetime(Date minCreatetime) {
        this.minCreatetime = minCreatetime;
    }


    public String getCountryName() {
        return countryName;
    }


    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    public int getPackage_number() {
        return package_number;
    }


    public void setPackage_number(int package_number) {
        this.package_number = package_number;
    }


    public String getOrderRemark() {
        return orderRemark;
    }


    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }


    public String getOrder_img() {
        return order_img;
    }


    public void setOrder_img(String order_img) {
        this.order_img = order_img;
    }


    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setCashback(double cashback) {
        this.cashback = cashback;
    }

    public double getCashback() {
        return cashback;
    }

    public int getIsDropshipOrder() {
        return isDropshipOrder;
    }

    public void setIsDropshipOrder(int isDropshipOrder) {
        this.isDropshipOrder = isDropshipOrder;
    }

    public int getIsBalancePayment() {
        return isBalancePayment;
    }

    public void setIsBalancePayment(int isBalancePayment) {
        this.isBalancePayment = isBalancePayment;
    }


    public double getShare_discount() {
        return share_discount;
    }


    public void setShare_discount(double share_discount) {
        this.share_discount = share_discount;
    }

    public double getGrade_discount() {
        return grade_discount;
    }

    public void setGrade_discount(double grade_discount) {
        this.grade_discount = grade_discount;
    }

    public int getPreferential() {
        return preferential;
    }

    public void setPreferential(int preferential) {
        this.preferential = preferential;
    }


}
	

