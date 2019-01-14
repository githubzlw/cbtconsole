package com.cbt.bean;

import java.util.List;

/**
 * @author ylm ������Ϣ
 */
public class OrderBean implements Cloneable {

    private int id;
    private int userid;
    private String userName;
    private String userEmail;
    private int package_style;
    private String mode_transport;// 运输方式
    private String service_fee;// 服务费
    private String domestic_freight;// 国内运费
    private String foreign_freight;// 运输方式对应的运费
    private String orderNo;// 订单号
    private int deliveryTime;
    private String product_cost;
    private int state;// 订单状态：5-确认价格中,1-购买中,0-等待付款,3-出运 2-到库
    private List<SpiderBean> spider;
    private String createtime;
    private Address address;
    private double actual_allincost;// 实际总费用
    private double pay_price;// 客户付款总金额
    private String pay_price_tow;// 客户已支付运费
    private String transport_time;// 运输时间
    private String actual_ffreight;// 实际国际运费
    private double remaining_price;// 订单所剩金额
    private String actual_weight;// 总重量
    private double actual_weight_estimate;// 预估总重量
    private String actual_volume;// 销售输入的订单长宽高
    private String custom_discuss_other;// 细节描述
    private int cancel_obj;// 取消订单对象
    private String expect_arrive_time;// 预计到货时间
    private int client_update; // 确认价格中 客户更新
    private int server_update; // 价格确认中 服务器更新
    private String arrive_time;// 用户收货时间
    private String ip;// 客户的ip地址
    private double applicable_credit;// 用户表中的运费补贴余额
    private double order_ac;// 订单所用的运费补贴
    private String email;// 订单所用的运费补贴
    private int purchase_number;// 已采购数量
    private int details_number;// 需采购数量
    private String details_pay;// 后台显示付款时间+最长交期
    private String pay_price_three;// 用户余额抵扣金额
    private String adminname;// 订单负责人员---wanyang--20151010
    private String currency;// 货币单位
    private String paystatus;// 支付状态
    private double discount_amount;// 订单折扣金额
    private String expressNo;// 快递号
    private String actual_lwh;// 总体积
    private int count;// 用户下单数----wanyang---20151123
    private String buyuser;// 采购负责人员---sj--20151010
    private double actual_freight_c;// 实际成本国际费用
    private boolean orderNumber;// 是否是第一次的订单，是则免服务费
    private String chaOrderNo;
    private double extra_freight;// 额外运费金额
    private int free_shipping;
    private int order_count;
    private int payFlag;
    private int cancelFlag;
    private int comformFlag;// 8小時内消息切换标识
    private int changenum;// 替换产品数量
    private int packag_number;// 快递运输总包裹数量
    private String maxSplitOrder; // 关联的订单信息
    private int orderFlag = 0;// 项目标识---sj--20160323
    private Integer addressid;
    private String exchange_rate;// 实时汇率
    private int odcount;
    private String businessName;// 客户公司名称
    private float gradeDiscount;// vip等级折扣
    private String dropShipState;// dropship子订单状态
    private String dropShipList;// dropship子订单列表 ,分割
    private String grade;//客户等级
    private String pay_type;//付款方式
    private String firstdiscount;//首单优惠3美元
    private String stateDesc;
    private double actualPay;//实际payPal支付
    private String volumeweight;//订单实际重量
    private String svolume;//实际体积
    private String paytypes;//支付类型
    private String checked;
    private String countOd;
    private String no_checked;
    private String problem;
    private String new_zid;
    private String country;
    private String countryName;
    private String zipcode;
    private String phonenumber;
    private String statename;
    private String address2;
    private String recipients;
    private String addresss;
    private int ordernum;
    private String adminemail;
    private int complain;
    private int purchase;
    private int deliver;
    private double freightFee;
    //会员费
    private double memberFee;
    private String payUserName;
    private String backFlag;
    private double processingfee;
    //黑名单相关
    private int backList;
    private int backAddressCount;
    private int payBackList;
    //优惠券优惠
    private String couponAmount;
    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public int getBackList() {
        return backList;
    }

    public void setBackList(int backList) {
        this.backList = backList;
    }

    public int getBackAddressCount() {
        return backAddressCount;
    }

    public void setBackAddressCount(int backAddressCount) {
        this.backAddressCount = backAddressCount;
    }

    public int getPayBackList() {
        return payBackList;
    }

    public void setPayBackList(int payBackList) {
        this.payBackList = payBackList;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    private String street;

    public double getProcessingfee() {
        return processingfee;
    }

    public void setProcessingfee(double processingfee) {
        this.processingfee = processingfee;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    private String gradeName;
    public String getBackFlag() {
        return backFlag;
    }

    public void setBackFlag(String backFlag) {
        this.backFlag = backFlag;
    }

    public String getPayUserName() {
        return payUserName;
    }

    public void setPayUserName(String payUserName) {
        this.payUserName = payUserName;
    }

    public double getMemberFee() {
        return memberFee;
    }

    public void setMemberFee(double memberFee) {
        this.memberFee = memberFee;
    }

    public String getEsBuyPrice() {
        return esBuyPrice;
    }

    public void setEsBuyPrice(String esBuyPrice) {
        this.esBuyPrice = esBuyPrice;
    }

    public String getYourorder() {
        return yourorder;
    }

    public void setYourorder(String yourorder) {
        this.yourorder = yourorder;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    public String getBuycount() {
        return buycount;
    }

    public void setBuycount(String buycount) {
        this.buycount = buycount;
    }

    private String esBuyPrice;
    private String yourorder;
    private String buyAmount;
    private String buycount;
    public double getFreightFee() {
        return freightFee;
    }

    public void setFreightFee(double freightFee) {
        this.freightFee = freightFee;
    }

    public int getYhCount() {
        return yhCount;
    }

    public void setYhCount(int yhCount) {
        this.yhCount = yhCount;
    }

    public int getCheckeds() {
        return checkeds;
    }

    public void setCheckeds(int checkeds) {
        this.checkeds = checkeds;
    }

    public int getCg() {
        return cg;
    }

    public void setCg(int cg) {
        this.cg = cg;
    }

    public int getRk() {
        return rk;
    }

    public void setRk(int rk) {
        this.rk = rk;
    }

    private int yhCount;
    private int checkeds;
    private int cg;
    private int rk;
    public String getDropShipList() {
		return dropShipList;
	}

	public void setDropShipList(String dropShipList) {
		this.dropShipList = dropShipList;
	}

	public int getComplain() {
        return complain;
    }

    public void setComplain(int complain) {
        this.complain = complain;
    }

    public int getPurchase() {
        return purchase;
    }

    public void setPurchase(int purchase) {
        this.purchase = purchase;
    }

    public int getDeliver() {
        return deliver;
    }

    public void setDeliver(int deliver) {
        this.deliver = deliver;
    }

    public String getAdminemail() {
        return adminemail;
    }

    public void setAdminemail(String adminemail) {
        this.adminemail = adminemail;
    }

    public int getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(int ordernum) {
        this.ordernum = ordernum;
    }

    public String getAddresss() {
        return addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNew_zid() {
        return new_zid;
    }

    public void setNew_zid(String new_zid) {
        this.new_zid = new_zid;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getCountOd() {
        return countOd;
    }

    public void setCountOd(String countOd) {
        this.countOd = countOd;
    }

    public String getNo_checked() {
        return no_checked;
    }

    public void setNo_checked(String no_checked) {
        this.no_checked = no_checked;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }


    public String getPaytypes() {
        return paytypes;
    }

    public void setPaytypes(String paytypes) {
        this.paytypes = paytypes;
    }

    public String getSvolume() {
        return svolume;
    }

    public void setSvolume(String svolume) {
        this.svolume = svolume;
    }

    public String getVolumeweight() {
        return volumeweight;
    }

    public void setVolumeweight(String volumeweight) {
        this.volumeweight = volumeweight;
    }

    public String getFirstdiscount() {
        return firstdiscount;
    }

    public void setFirstdiscount(String firstdiscount) {
        this.firstdiscount = firstdiscount;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * 双清包税价格
     */
    private double vatBalance;


    public double getVatBalance() {
        return vatBalance;
    }

    public void setVatBalance(double vatBalance) {
        this.vatBalance = vatBalance;
    }

    public float getGradeDiscount() {
        return gradeDiscount;
    }

    public void setGradeDiscount(float gradeDiscount) {
        this.gradeDiscount = gradeDiscount;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public int getOdcount() {
        return odcount;
    }

    public void setOdcount(int odcount) {
        this.odcount = odcount;
    }

    public String getExchange_rate() {
        return exchange_rate;
    }

    public void setExchange_rate(String exchange_rate) {
        this.exchange_rate = exchange_rate;
    }

    public Integer getAddressid() {
        return addressid;
    }

    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }

    private int buyid;// 采购人员ID
    private String storagetime;// 入库时间
    // 到账
    private int dzId;
    private String dzOrderno;
    private String dzConfirmname;
    private String dzConfirmtime;
    private String orderRemark;
    private int adminid;

    private int[] reminded;// 提醒，0-客户投诉，1-入库问题，2，-出库问题

    private int ropType;
    private String oldValue;

    public int getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(int cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public int getComformFlag() {
        return comformFlag;
    }

    public void setComformFlag(int comformFlag) {
        this.comformFlag = comformFlag;
    }

    private int del_state;
    private double cashback;// 减免费用，订单满200美元会有10元的减免，直接在付款里面给减去了，不算优惠里面--cjc-20161104
    // share_discount

    private int isDropshipOrder;// dropship订单标识，如果是dropship订单的话为1，否则0
    private double share_discount;// 分享折扣--2017.1.6
    private double extra_discount; // 手动优惠金额--1.11
    private double coupon_discount;// 返单优惠 --2.20

    /**
     * 国家中文名字
     */
    private String countryNameCN;


    public String getCountryNameCN() {
        return countryNameCN;
    }

    public void setCountryNameCN(String countryNameCN) {
        this.countryNameCN = countryNameCN;
    }

    public double getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(double coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public double getExtra_discount() {
        return extra_discount;
    }

    public void setExtra_discount(double extra_discount) {
        this.extra_discount = extra_discount;
    }

    public double getShare_discount() {
        return share_discount;
    }

    public void setShare_discount(double share_discount) {
        this.share_discount = share_discount;
    }

    public int getIsDropshipOrder() {
        return isDropshipOrder;
    }

    public void setIsDropshipOrder(int isDropshipOrder) {
        this.isDropshipOrder = isDropshipOrder;
    }

    public OrderBean(int userid, String orderNo, int state) {
        super();
        this.userid = userid;
        this.orderNo = orderNo;
        this.state = state;
    }

    public double getCashback() {
        return cashback;
    }

    public void setCashback(double cashback) {
        this.cashback = cashback;
    }

    public int getRopType() {
        return ropType;
    }

    public void setRopType(int ropType) {
        this.ropType = ropType;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public int getDel_state() {
        return del_state;
    }

    public void setDel_state(int del_state) {
        this.del_state = del_state;
    }

    public int getOrderFlag() {
        return orderFlag;
    }

    public void setOrderFlag(int orderFlag) {
        this.orderFlag = orderFlag;
    }

    public int getDzId() {
        return dzId;
    }

    public void setDzId(int dzId) {
        this.dzId = dzId;
    }

    public String getDzOrderno() {
        return dzOrderno;
    }

    public void setDzOrderno(String dzOrderno) {
        this.dzOrderno = dzOrderno;
    }

    public String getDzConfirmname() {
        return dzConfirmname;
    }

    public void setDzConfirmname(String dzConfirmname) {
        this.dzConfirmname = dzConfirmname;
    }

    public String getDzConfirmtime() {
        return dzConfirmtime;
    }

    public void setDzConfirmtime(String dzConfirmtime) {
        this.dzConfirmtime = dzConfirmtime;
    }

    public void setMaxSplitOrder(String maxSplitOrder) {
        this.maxSplitOrder = maxSplitOrder;
    }

    public String getMaxSplitOrder() {
        return maxSplitOrder;
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

    private List<OrderDetailsBean> orderDetail;// 订单详情

    public OrderBean() {
        super();
    }

    public OrderBean(int id, String orderNo, int deliveryTime, String product_cost, int state, List<SpiderBean> spider,
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
        return String.format(
                "{\"id\":\"%s\", \"userid\":\"%s\", \"package_style\":\"%s\", \"mode_transport\":\"%s\", \"service_fee\":\"%s\", \"domestic_freight\":\"%s\", \"foreign_freight\":\"%s\", \"orderNo\":\"%s\", \"deliveryTime\":\"%s\", \"product_cost\":\"%s\", \"state\":\"%s\", \"spider\":\"%s\", \"createtime\":\"%s\",\"ip\":\"%s\"}",
                id, userid, package_style, mode_transport, service_fee, domestic_freight, foreign_freight, orderNo,
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

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
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
        // 订单状态：5-确认价格中,1-购买中,0-等待付款,3-出运 2-到库
        if (state == 5) {
            this.stateDesc = "确认价格中";
        } else if (state == 1) {
            this.stateDesc = "购买中";
        } else if (state == 0) {
            this.stateDesc = "等待付款";
        } else if (state == 4) {
            this.stateDesc = "完结";
        } else if (state == 3) {
            this.stateDesc = "出运中";
        } else if (state == 2) {
            this.stateDesc = "到库";
        } else if (state == -1 || state == 6) {
            this.stateDesc = "订单取消";
        } else {
            this.stateDesc = "获取状态失败";
        }
    }

    public double getActual_allincost() {
        return actual_allincost;
    }

    public void setActual_allincost(double actual_allincost) {
        this.actual_allincost = actual_allincost;
    }

    public List<SpiderBean> getSpider() {
        return spider;
    }

    public void setSpider(List<SpiderBean> spider) {
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

    public String getService_fee() {
        return service_fee;
    }

    public void setService_fee(String service_fee) {
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

    public int getPackag_number() {
        return packag_number;
    }

    public void setPackag_number(int packag_number) {
        this.packag_number = packag_number;
    }

    public void setStoragetime(String storagetime) {
        this.storagetime = storagetime;
    }

    public String getStoragetime() {
        return storagetime;
    }

    public void setReminded(int[] reminded) {
        this.reminded = reminded;
    }

    public int[] getReminded() {
        return reminded;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setBuyid(int buyid) {
        this.buyid = buyid;
    }

    public int getBuyid() {
        return buyid;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public void setAdminid(int adminid) {
        this.adminid = adminid;
    }

    public int getAdminid() {
        return adminid;
    }

    public String getDropShipState() {
        return dropShipState;
    }

    public void setDropShipState(String dropShipState) {
        this.dropShipState = dropShipState;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public double getActualPay() {
        return actualPay;
    }

    public void setActualPay(double actualPay) {
        this.actualPay = actualPay;
    }

    @Override
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
