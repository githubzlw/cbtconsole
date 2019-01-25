package com.importExpress.pojo;

import java.io.Serializable;

public class GoodsCarActiveSimplBean implements Serializable {

    /**
     * @fieldName serialVersionUID
     * @fieldType long
     * @Description TODO
     */
    private static final long serialVersionUID = -8701597635121563713L;

    private double categoryDiscountRate;//商品类别折扣率
    private int number;//商品在购物车中的数量 Use
    private String price;//购物车最新价格
    private String price1;//根据数量变化的非免邮价格，平摊非免邮价格
    private double price2;//添加购物车上一次的价格
    private double price3;//price*number
    private String itemId;//产品pid
    private String urlMD5;//产品id和数据来源类型生成的唯一值 Use
    private String bizPriceDiscount;// catid path 类别id树
    private String priceListSize;//判断该商品是否有区间价格的9-13 >0 有  Use
    private String priceList;//该商品的价格区间  Use
    private int groupBuyId;//团购编号，也是团购标识，团购商品不执行降价逻辑:0非团购商品;>0团购商品
    private double gbPrice;//加入购物车时团购价格
    private String comparePrices;//ali的价格 或者是我们造的假数据
    private int shopCount;//店铺商品数量
    private String sessionId;//针对未登录用户，添加购物车时，存入goods_car表，关联sessionId，这块还保留的原因：登陆前后，购物车商品合并时需要用到
    private String types;//商品规格
    private double price4;//购物车划掉的价格，产品单页中的start price
    private String remark;//客户填写的购物车商品备注
    private String skuid_1688;//1688那边的规格id，用来与sku表做关联，从而能校验购物车数据
    private String spec_id;//1688那边的  规格唯一标识，用来做自动下单的

    public double getCategoryDiscountRate() {
        return categoryDiscountRate;
    }

    public void setCategoryDiscountRate(double categoryDiscountRate) {
        this.categoryDiscountRate = categoryDiscountRate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public double getPrice2() {
        return price2;
    }

    public void setPrice2(double price2) {
        this.price2 = price2;
    }

    public double getPrice3() {
        return price3;
    }

    public void setPrice3(double price3) {
        this.price3 = price3;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getUrlMD5() {
        return urlMD5;
    }

    public void setUrlMD5(String urlMD5) {
        this.urlMD5 = urlMD5;
    }

    public String getBizPriceDiscount() {
        return bizPriceDiscount;
    }

    public void setBizPriceDiscount(String bizPriceDiscount) {
        this.bizPriceDiscount = bizPriceDiscount;
    }

    public String getPriceListSize() {
        return priceListSize;
    }

    public void setPriceListSize(String priceListSize) {
        this.priceListSize = priceListSize;
    }

    public String getPriceList() {
        return priceList;
    }

    public void setPriceList(String priceList) {
        this.priceList = priceList;
    }

    public int getGroupBuyId() {
        return groupBuyId;
    }

    public void setGroupBuyId(int groupBuyId) {
        this.groupBuyId = groupBuyId;
    }

    public double getGbPrice() {
        return gbPrice;
    }

    public void setGbPrice(double gbPrice) {
        this.gbPrice = gbPrice;
    }

    public String getComparePrices() {
        return comparePrices;
    }

    public void setComparePrices(String comparePrices) {
        this.comparePrices = comparePrices;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public double getPrice4() {
        return price4;
    }

    public void setPrice4(double price4) {
        this.price4 = price4;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSkuid_1688() {
        return skuid_1688;
    }

    public void setSkuid_1688(String skuid_1688) {
        this.skuid_1688 = skuid_1688;
    }

    public String getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id = spec_id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"categoryDiscountRate\":")
                .append(categoryDiscountRate);
        sb.append(",\"number\":")
                .append(number);
        sb.append(",\"price\":\"")
                .append(price).append('\"');
        sb.append(",\"price1\":\"")
                .append(price1).append('\"');
        sb.append(",\"price2\":")
                .append(price2);
        sb.append(",\"price3\":")
                .append(price3);
        sb.append(",\"itemId\":\"")
                .append(itemId).append('\"');
        sb.append(",\"urlMD5\":\"")
                .append(urlMD5).append('\"');
        sb.append(",\"bizPriceDiscount\":\"")
                .append(bizPriceDiscount).append('\"');
        sb.append(",\"priceListSize\":\"")
                .append(priceListSize).append('\"');
        sb.append(",\"priceList\":\"")
                .append(priceList).append('\"');
        sb.append(",\"groupBuyId\":")
                .append(groupBuyId);
        sb.append(",\"gbPrice\":")
                .append(gbPrice);
        sb.append(",\"comparePrices\":\"")
                .append(comparePrices).append('\"');
        sb.append(",\"shopCount\":")
                .append(shopCount);
        sb.append(",\"sessionId\":\"")
                .append(sessionId).append('\"');
        sb.append(",\"types\":\"")
                .append(types).append('\"');
        sb.append(",\"price4\":")
                .append(price4);
        sb.append(",\"remark\":\"")
                .append(remark).append('\"');
        sb.append(",\"skuid_1688\":\"")
                .append(skuid_1688).append('\"');
        sb.append(",\"spec_id\":\"")
                .append(spec_id).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
