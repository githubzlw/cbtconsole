package com.cbt.bean;

import java.io.Serializable;

/**
 * 购物车信息
 */
public class SpiderBean implements Serializable, Comparable<SpiderBean> {


    private int id;
    private int carId;// 购物车的ID，不存储数据库
    private String guId;// 购物车的ID，不存储数据库
    private String itemId;// 网站中的商品ID
    private String shopId;// 网站中的商店ID
    private int userId;
    private String sessionId;
    private String name;
    private String url;
    private String seller;// 店家名称
    private String price;// 单价
    private int number;// 数量
    private String freight;// 运费
    private String img_url;
    private String color;
    private String size;
    private String types;// 规格
    private String remark;
    private String norm_most;// 最大定量
    private String norm_least;// 最小定量
    private String norm_least1;// 最小定量
    private String delivery_time;// 交期
    private double total_price;// 某一商店的购买总价
    private String pWprice;// 批发价格
    private int cartNumber;// 购物车数量
    private String true_shipping;// 运费备注
    private int freight_free;// 免运费
    private String perWeight;// 单位重量+
    private String seilUnit;// 重量计算单位
    private String goodsUnit;// 购买商品单位
    private String bulk_volume;// 总体积
    private String total_weight;// 总重量
    private String width;// 体积
    private String weight;// 单个产品重量
    private String createtime;// 加入时间
    private String goods_email;
    private String free_shopping_company;// 免邮快递公司
    private String free_sc_days;// 免邮对应快递公司的运输时间
    private int goodsdata_id;// 商品ID
    private int preferential;// 是否优惠申请
    private int deposit_rate;// 折扣率
    private String feeprice;// 运费（被免去的运费）
    private String free_price;// 免邮价格
    private int state;// 购物车状态
    private String currency;// 货币单位
    private int goods_class;// 商品类别
    private String img_type;// 选择的类别折扣
    private double extra_freight;// 额外运费
    private int source_url;// 来源网址,1->1688网址->feeprice免邮转非免邮价格减去该字段,否则计算
    private int preshopping;// 是否优惠商品
    private String goods_catid;// 商品最小类别ID
    private double notfreeprice;// 商品的出厂价格
    private String goods_type;// 商品规格
    private String showname;// 商品类别显示名称
    private Double deposit_rates;// 优惠比例
    private Double rate_price;// 满多少开始折扣
    private Double ceil_money;// 商品价格小计
    private Double class_money;// 按类总计价格
    private double grade_discount;// 等级折扣

    public double getGrade_discount() {
        return grade_discount;
    }

    public void setGrade_discount(double grade_discount) {
        this.grade_discount = grade_discount;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public String getShowname() {
        return showname;
    }

    public void setShowname(String showname) {
        this.showname = showname;
    }

    public Double getDeposit_rates() {
        return deposit_rates;
    }

    public void setDeposit_rates(Double deposit_rates) {
        this.deposit_rates = deposit_rates;
    }

    public Double getRate_price() {
        return rate_price;
    }

    public void setRate_price(Double rate_price) {
        this.rate_price = rate_price;
    }

    public Double getCeil_money() {
        return ceil_money;
    }

    public void setCeil_money(Double ceil_money) {
        this.ceil_money = ceil_money;
    }

    public Double getClass_money() {
        return class_money;
    }

    public void setClass_money(Double class_money) {
        this.class_money = class_money;
    }

    public String getPerWeight() {
        return perWeight;
    }

    public void setPerWeight(String perWeight) {
        this.perWeight = perWeight;
    }

    public String getSeilUnit() {
        return seilUnit;
    }

    public void setSeilUnit(String seilUnit) {
        this.seilUnit = seilUnit;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getBulk_volume() {
        return bulk_volume;
    }

    public void setBulk_volume(String bulk_volume) {
        this.bulk_volume = bulk_volume;
    }

    public SpiderBean() {
        super();
    }

    public SpiderBean(int id, int carId, String itemId, String shopId,
                      int userId, String name, String url, String seller, String price,
                      int number, String freight, String img_url, String color,
                      String size, String remark, String norm_most, String norm_least,
                      String delivery_time, String true_shipping) {
        super();
        this.id = id;
        this.carId = carId;
        this.itemId = itemId;
        this.shopId = shopId;
        this.userId = userId;
        this.name = name;
        this.url = url;
        this.seller = seller;
        this.price = price;
        this.number = number;
        this.freight = freight;
        this.img_url = img_url;
        this.color = color;
        this.size = size;
        this.remark = remark;
        this.norm_most = norm_most;
        this.norm_least = norm_least;
        this.delivery_time = delivery_time;
        this.true_shipping = true_shipping;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        return String
                .format("{\"id\":\"%s\", \"carId\":\"%s\", \"itemId\":\"%s\", \"shopId\":\"%s\", \"userId\":\"%s\", \"name\":\"%s\", \"url\":\"%s\", \"seller\":\"%s\", \"price\":\"%s\", \"number\":\"%s\", \"freight\":\"%s\", \"img_url\":\"%s\", \"color\":\"%s\", \"size\":\"%s\", \"remark\":\"%s\", \"norm_most\":\"%s\", \"norm_least\":\"%s\", \"delivery_time\":\"%s\", \"total_price\":\"%s\", \"pWprice\":\"%s\",\"freight_free\":%d}",
                        id, carId, itemId, shopId, userId, name, url, seller,
                        price, number, freight, img_url, color, size, remark,
                        norm_most, norm_least, delivery_time, total_price,
                        pWprice, freight_free);
    }

    public int getCarId() {
        return carId;
    }

    public void setTrue_shipping(String true_shipping) {
        this.true_shipping = true_shipping;
    }

    public String getTrue_shipping() {
        return true_shipping;
    }

    public String getNorm_most() {
        return norm_most;
    }

    public void setCartNumber(int cartNumber) {
        this.cartNumber = cartNumber;
    }

    public int getCartNumber() {
        return cartNumber;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setNorm_most(String norm_most) {
        this.norm_most = norm_most;
    }

    public String getNorm_least() {
        return norm_least;
    }

    public void setNorm_least(String norm_least) {
        this.norm_least = norm_least;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopId() {
        return shopId;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getTotal_price() {
        if (total_price == 0) {
            if (price != null && !"".equals(price)) {
                price = price.replaceAll(",", "");
            } else {
                price = "0";
            }
            return Double.parseDouble(price);
        }
        return total_price;
    }

    public void setpWprice(String pWprice) {
        this.pWprice = pWprice;
    }

    public String getpWprice() {
        return pWprice;
    }

    public int getFreight_free() {
        return freight_free;
    }

    public void setFreight_free(int freight_free) {
        this.freight_free = freight_free;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(String total_weight) {
        this.total_weight = total_weight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setNorm_least1(String norm_least1) {
        this.norm_least1 = norm_least1;
    }

    public String getNorm_least1() {
        return norm_least1;
    }

    public void setGoods_email(String goods_email) {
        this.goods_email = goods_email;
    }

    public String getGoods_email() {
        return goods_email;
    }

    public void setFree_shopping_company(String free_shopping_company) {
        this.free_shopping_company = free_shopping_company;
    }

    public String getFree_shopping_company() {
        return free_shopping_company;
    }

    public int getGoodsdata_id() {
        return goodsdata_id;
    }

    public void setGoodsdata_id(int goodsdata_id) {
        this.goodsdata_id = goodsdata_id;
    }

    public void setFree_sc_days(String free_sc_days) {
        this.free_sc_days = free_sc_days;
    }

    public String getFree_sc_days() {
        return free_sc_days;
    }

    public int getPreferential() {
        return preferential;
    }

    public void setPreferential(int preferential) {
        this.preferential = preferential;
    }

    @Override
    public int compareTo(SpiderBean o) {
        if (this.shopId != null) {
            if (this.shopId.compareTo(o.shopId) == 0) {
                return Integer.valueOf(id).compareTo(Integer.valueOf(o.id));
            } else {
                return this.shopId.compareTo(o.shopId);
            }
        }
        return 0;
    }

    public int getDeposit_rate() {
        return deposit_rate;
    }

    public void setDeposit_rate(int deposit_rate) {
        this.deposit_rate = deposit_rate;
    }

    public void setGuId(String guId) {
        this.guId = guId;
    }

    public String getGuId() {
        return guId;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getTypes() {
        return types;
    }

    public void setFeeprice(String feeprice) {
        this.feeprice = feeprice;
    }

    public String getFeeprice() {
        return feeprice;
    }

    public void setFree_price(String free_price) {
        this.free_price = free_price;
    }

    public String getFree_price() {
        return free_price;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public int getGoods_class() {
        return goods_class;
    }

    public void setGoods_class(int goods_class) {
        this.goods_class = goods_class;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }

    public String getImg_type() {
        return img_type;
    }

    public double getExtra_freight() {
        return extra_freight;
    }

    public void setExtra_freight(double extra_freight) {
        this.extra_freight = extra_freight;
    }

    public void setSource_url(int source_url) {
        this.source_url = source_url;
    }

    public int getSource_url() {
        return source_url;
    }

    public void setPreshopping(int preshopping) {
        this.preshopping = preshopping;
    }

    public int getPreshopping() {
        return preshopping;
    }

    public void setGoods_catid(String goods_catid) {
        this.goods_catid = goods_catid;
    }

    public String getGoods_catid() {
        return goods_catid;
    }

    public double getNotfreeprice() {
        return notfreeprice;
    }

    public void setNotfreeprice(double d) {
        this.notfreeprice = d;
    }

}
