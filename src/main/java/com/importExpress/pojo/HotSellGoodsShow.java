package com.importExpress.pojo;

public class HotSellGoodsShow {
    private String show_name;// 热卖产品显示名称
    private int hot_id;
    private String goods_import_url;// 本网站的商品链接
    private String goods_img;
    private String goods_pid;
    private String goods_price;// 产品搜索页的价格 源自custom_benchmark_ready表wprice
    private String price_show;// 用于最终在首页页面显示的价格
    private String goods_unit;// 用于最终在首页页面显示的单位
    private String rangePrice;// 产品搜索页的价格 源自custom_benchmark_ready表range_price
    private String price1688;// 源自custom_benchmark_ready表price
    private String moq;// 最小订量
    private String asin_code;// 亚马逊asin码
    private String amazon_price;// 亚马逊价格
    private double profit_margin;

    //折扣信息
    private int discountId;
    private double discountPercentage;
    private String discountBeginTime;
    private String discountEndTime;
    private int discountSort;
    private double discountPrice;


    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public int getHot_id() {
        return hot_id;
    }

    public void setHot_id(int hot_id) {
        this.hot_id = hot_id;
    }

    public String getGoods_import_url() {
        return goods_import_url;
    }

    public void setGoods_import_url(String goods_import_url) {
        this.goods_import_url = goods_import_url;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getGoods_pid() {
        return goods_pid;
    }

    public void setGoods_pid(String goods_pid) {
        this.goods_pid = goods_pid;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getPrice_show() {
        return price_show;
    }

    public void setPrice_show(String price_show) {
        this.price_show = price_show;
    }

    public String getGoods_unit() {
        return goods_unit;
    }

    public void setGoods_unit(String goods_unit) {
        this.goods_unit = goods_unit;
    }

    public String getRangePrice() {
        return rangePrice;
    }

    public void setRangePrice(String rangePrice) {
        this.rangePrice = rangePrice;
    }

    public String getPrice1688() {
        return price1688;
    }

    public void setPrice1688(String price1688) {
        this.price1688 = price1688;
    }

    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public String getAsin_code() {
        return asin_code;
    }

    public void setAsin_code(String asin_code) {
        this.asin_code = asin_code;
    }

    public String getAmazon_price() {
        return amazon_price;
    }

    public void setAmazon_price(String amazon_price) {
        this.amazon_price = amazon_price;
    }

    public double getProfit_margin() {
        return profit_margin;
    }

    public void setProfit_margin(double profit_margin) {
        this.profit_margin = profit_margin;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getDiscountBeginTime() {
        return discountBeginTime;
    }

    public void setDiscountBeginTime(String discountBeginTime) {
        this.discountBeginTime = discountBeginTime;
    }

    public String getDiscountEndTime() {
        return discountEndTime;
    }

    public void setDiscountEndTime(String discountEndTime) {
        this.discountEndTime = discountEndTime;
    }

    public int getDiscountSort() {
        return discountSort;
    }

    public void setDiscountSort(int discountSort) {
        this.discountSort = discountSort;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    @Override
    public String toString() {
        return "HotSellGoodsShow{" +
                "show_name='" + show_name + '\'' +
                ", hot_id=" + hot_id +
                ", goods_import_url='" + goods_import_url + '\'' +
                ", goods_img='" + goods_img + '\'' +
                ", goods_pid='" + goods_pid + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", price_show='" + price_show + '\'' +
                ", goods_unit='" + goods_unit + '\'' +
                ", rangePrice='" + rangePrice + '\'' +
                ", price1688='" + price1688 + '\'' +
                ", moq='" + moq + '\'' +
                ", asin_code='" + asin_code + '\'' +
                ", amazon_price='" + amazon_price + '\'' +
                ", profit_margin=" + profit_margin +
                ", discountId=" + discountId +
                ", discountPercentage=" + discountPercentage +
                ", discountBeginTime='" + discountBeginTime + '\'' +
                ", discountEndTime='" + discountEndTime + '\'' +
                ", discountSort=" + discountSort +
                ", discountPrice=" + discountPrice +
                '}';
    }
}
