package com.cbt.website.bean;

import java.io.Serializable;
/**
 * Created by qiqing
 * Date :17/07/25
 */

/**
 * 购物车商品信息实体：添加购物车时，需要变更或者购物车价格计算用到的商品信息
 * */
public class GoodsCarActiveBean implements Serializable, Cloneable {
    /**
     *
     */
    private static final long serialVersionUID = -622507823283220339L;

    private double categoryDiscountRate;//商品类别折扣率
    private double es1;//记录商品添加购物车时，当前商品的9-15天运费，供下次添加购物车使用（非免邮国家无）
    private double jes1;//记录商品添加购物车时，当前商品的5-9天运费，供下次添加购物车使用（非免邮国家及短交期）
    private int firstnumber;//每个商品第一次添加到购物车的数量
    private double firstprice;// 	添加购物车是的免邮价 Use
    private double freeprice;//免邮价格
    private double freight;//
    private double freight_es1;//购物车中计算出来的产品单页中的初始运费
    private String guId;//商品url,type等计算得到的id,可以理解为购物车商品唯一性标志 Use
    private double method_feight;//美国运费
    private String norm_least;// 最小定量
    private double notfreeprice;//非免邮价格 Use
    private int number;//商品在购物车中的数量 Use
    private int oNum;//记录购物车本次添加的数量product number 下一次计算购物车使用  Use 默认0
    private String perWeight;//每个商品的重量(number=1时的重量) Use
    private String price;//购物车最新价格
    private double price1;//根据数量变化的非免邮价格，平摊非免邮价格
    private double price2;//原始一件商品的邮费
    private double price3;//原始工厂价-用来后台修改工厂价，保留原始值的
    private String startBizFactoryPrice;//产品起始工厂价
    private int state;//购物车状态  1-生成订单  9-删除 0-商品还在购物车 Use 默认0
    private double total_price;//商品总价格
    private String total_weight;//商品总重量
    private String bulk_volume;//
    private int isBattery;//商品是否带电，0-不带电，1带电 Use
    private double freightByWeight;//单个产品按照重量计算得到的运费 7.21
    private int goods_class;//商品类别  Use
    private String seilUnit;//重量计算单位
    private String itemId;//商品id,即这个商品信息存在goods_car表中的id Use
    private String goodsUrlMD5;//产品id和数据来源类型生成的唯一值 Use
    private String bizPriceDiscount;// catid path 类别id树
    private String priceListSize;//判断该商品是否有区间价格的9-13 >0 有  Use
    private String spider_Price;//产品单页中添加购物车的价格
    private int spider_changenum;//当前商品改变的数量 2017年11月22日
    private double ts1;//购物车的总运费
    private double old_price;//记录当前商品上次的价格，给下一次价格计算比较使用 Use 默认0
    private String method_day;//记录当前产品的价格的交期时间
    private int old_CountryId;//记录当前商品上次计算的时候的国家id
    private double JTS1;//购物车 短交期或者免邮国家的TS1
    private String priceList;//该商品的价格区间  Use
    private int groupBuyId;//团购编号，也是团购标识，团购商品不执行降价逻辑:0非团购商品;>0团购商品
    private double gbPrice;//加入购物车时团购价格

    private int isFreeShipProduct;//是否是免邮商品；0-无免邮价；1-老客户订单商品（强制要有免邮价）；2-有免邮价商品
    private int isStockFlag;//库存标识
    /**
     * 店铺商品数量
     */
    private int shopCount;

    public int getIsStockFlag() {
        return isStockFlag;
    }

    public void setIsStockFlag(int isStockFlag) {
        this.isStockFlag = isStockFlag;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public int getIsFreeShipProduct() {
        return isFreeShipProduct;
    }

    public void setIsFreeShipProduct(int isFreeShipProduct) {
        this.isFreeShipProduct = isFreeShipProduct;
    }

    /**
     * 该商品的最大交期
     */
    private String processingTime;

    /**
     * 该商品的最小交期
     */
    private String processingTimeMin;
    /**
     * ali的价格 或者是我们造的假数据
     */
    private String comparePrices;

    /**
     * 店铺的id
     */
    private String shopId;
    /**
     * 拿样费
     */
    private double sampleFee;
    /**
     * 拿样最小moq
     */
    private int sampleMoq;

    public double getSampleFee() {
        return sampleFee;
    }

    public void setSampleFee(double sampleFee) {
        this.sampleFee = sampleFee;
    }

    public int getSampleMoq() {
        return sampleMoq;
    }

    public void setSampleMoq(int sampleMoq) {
        this.sampleMoq = sampleMoq;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getComparePrices() {
        return comparePrices;
    }

    public void setComparePrices(String comparePrices) {
        this.comparePrices = comparePrices;
    }

    public String getProcessingTimeMin() {
        return processingTimeMin;
    }

    public void setProcessingTimeMin(String processingTimeMin) {
        this.processingTimeMin = processingTimeMin;
    }

    public String getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(String processingTime) {
        this.processingTime = processingTime;
    }

    public String getPriceList() {
        return priceList;
    }

    public void setPriceList(String priceList) {
        this.priceList = priceList;
    }

    public double getJTS1() {
        return JTS1;
    }

    public void setJTS1(double jTS1) {
        JTS1 = jTS1;
    }

    public double getOld_price() {
        return old_price;
    }

    public int getOld_CountryId() {
        return old_CountryId;
    }

    public void setOld_CountryId(int old_CountryId) {
        this.old_CountryId = old_CountryId;
    }

    public String getMethod_day() {
        return method_day;
    }

    public void setMethod_day(String method_day) {
        this.method_day = method_day;
    }

    public void setOld_price(double old_price) {
        this.old_price = old_price;
    }

    public double getJes1() {
        return jes1;
    }

    public void setJes1(double jes1) {
        this.jes1 = jes1;
    }

    public double getTs1() {
        return ts1;
    }

    public void setTs1(double ts1) {
        this.ts1 = ts1;
    }

    public int getSpider_changenum() {
        return spider_changenum;
    }

    public void setSpider_changenum(int changeNumber) {
        this.spider_changenum = changeNumber;
    }

    public String getSpider_Price() {
        return spider_Price;
    }

    public void setSpider_Price(String spider_Price) {
        this.spider_Price = spider_Price;
    }

    public String getPriceListSize() {
        return priceListSize;
    }

    public void setPriceListSize(String priceListSize) {
        this.priceListSize = priceListSize;
    }

    public String getBizPriceDiscount() {
        return bizPriceDiscount;
    }

    public void setBizPriceDiscount(String bizPriceDiscount) {
        this.bizPriceDiscount = bizPriceDiscount;
    }

    public String getUrlMD5() {
        return goodsUrlMD5;
    }

    public void setUrlMD5(String goodsUrlMD5) {
        this.goodsUrlMD5 = goodsUrlMD5;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSeilUnit() {
        return seilUnit;
    }

    public void setSeilUnit(String seilUnit) {
        this.seilUnit = seilUnit;
    }

    public int getGoods_class() {
        return goods_class;
    }

    public void setGoods_class(int goods_class) {
        this.goods_class = goods_class;
    }

    public double getFreightByWeight() {
        return freightByWeight;
    }

    public void setFreightByWeight(double freightByWeight) {
        this.freightByWeight = freightByWeight;
    }

    public int getIsBattery() {
        return isBattery;
    }

    public void setIsBattery(int isBattery) {
        this.isBattery = isBattery;
    }

    public String getBulk_volume() {
        return bulk_volume;
    }

    public void setBulk_volume(String bulk_volume) {
        this.bulk_volume = bulk_volume;
    }

    public double getCategoryDiscountRate() {
        return categoryDiscountRate;
    }

    public void setCategoryDiscountRate(double categoryDiscountRate) {
        this.categoryDiscountRate = categoryDiscountRate;
    }

    public double getEs1() {
        return es1;
    }

    public void setEs1(double es1) {
        this.es1 = es1;
    }

    public int getFirstnumber() {
        return firstnumber;
    }

    public void setFirstnumber(int firstnumber) {
        this.firstnumber = firstnumber;
    }

    public double getFirstprice() {
        return firstprice;
    }

    public void setFirstprice(double firstprice) {
        this.firstprice = firstprice;
    }

    public double getFreeprice() {
        return freeprice;
    }

    public void setFreeprice(double freeprice) {
        this.freeprice = freeprice;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getFreight_es1() {
        return freight_es1;
    }

    public void setFreight_es1(double freight_es1) {
        this.freight_es1 = freight_es1;
    }

    public String getGuId() {
        return guId;
    }

    public void setGuId(String guId) {
        this.guId = guId;
    }

    public double getMethod_feight() {
        return method_feight;
    }

    public void setMethod_feight(double method_feight) {
        this.method_feight = method_feight;
    }

    public String getNorm_least() {
        return norm_least;
    }

    public void setNorm_least(String norm_least) {
        this.norm_least = norm_least;
    }

    public double getNotfreeprice() {
        return notfreeprice;
    }

    public void setNotfreeprice(double notfreeprice) {
        this.notfreeprice = notfreeprice;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getoNum() {
        return oNum;
    }

    public void setoNum(int oNum) {
        this.oNum = oNum;
    }

    public String getPerWeight() {
        return perWeight;
    }

    public void setPerWeight(String perWeight) {
        this.perWeight = perWeight;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
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

    public String getStartBizFactoryPrice() {
        return startBizFactoryPrice;
    }

    public void setStartBizFactoryPrice(String startBizFactoryPrice) {
        this.startBizFactoryPrice = startBizFactoryPrice;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(String total_weight) {
        this.total_weight = total_weight;
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"JTS1\":\"");
        builder.append(JTS1);
        builder.append("\", \"bizPriceDiscount\":\"");
        builder.append(bizPriceDiscount);
        builder.append("\", \"bulk_volume\":\"");
        builder.append(bulk_volume);
        builder.append("\", \"categoryDiscountRate\":\"");
        builder.append(categoryDiscountRate);
        builder.append("\", \"comparePrices\":\"");
        builder.append(comparePrices == null ? "" : comparePrices);
        builder.append("\", \"es1\":\"");
        builder.append(es1);
        builder.append("\", \"firstnumber\":\"");
        builder.append(firstnumber);
        builder.append("\", \"firstprice\":\"");
        builder.append(firstprice);
        builder.append("\", \"freeprice\":\"");
        builder.append(freeprice);
        builder.append("\", \"freight\":\"");
        builder.append(freight);
        builder.append("\", \"freightByWeight\":\"");
        builder.append(freightByWeight);
        builder.append("\", \"freight_es1\":\"");
        builder.append(freight_es1);
        builder.append("\", \"gbPrice\":\"");
        builder.append(gbPrice);
        builder.append("\", \"goods_class\":\"");
        builder.append(goods_class);
        builder.append("\", \"groupBuyId\":\"");
        builder.append(groupBuyId);
        builder.append("\", \"guId\":\"");
        builder.append(guId);
        builder.append("\", \"isBattery\":\"");
        builder.append(isBattery);
        builder.append("\", \"isFreeShipProduct\":\"");
        builder.append(isFreeShipProduct);
        builder.append("\", \"itemId\":\"");
        builder.append(itemId);
        builder.append("\", \"jes1\":\"");
        builder.append(jes1);
        builder.append("\", \"method_day\":\"");
        builder.append(method_day);
        builder.append("\", \"method_feight\":\"");
        builder.append(method_feight);
        builder.append("\", \"norm_least\":\"");
        builder.append(norm_least);
        builder.append("\", \"notfreeprice\":\"");
        builder.append(notfreeprice);
        builder.append("\", \"number\":\"");
        builder.append(number);
        builder.append("\", \"oNum\":\"");
        builder.append(oNum);
        builder.append("\", \"old_CountryId\":\"");
        builder.append(old_CountryId);
        builder.append("\", \"old_price\":\"");
        builder.append(old_price);
        builder.append("\", \"perWeight\":\"");
        builder.append(perWeight);
        builder.append("\", \"price\":\"");
        builder.append(price);
        builder.append("\", \"price1\":\"");
        builder.append(price1);
        builder.append("\", \"price2\":\"");
        builder.append(price2);
        builder.append("\", \"price3\":\"");
        builder.append(price3);
        builder.append("\", \"priceList\":\"");
        builder.append(priceList == null ? "" : priceList);
        builder.append("\", \"priceListSize\":\"");
        builder.append(priceListSize);
        builder.append("\", \"processingTime\":\"");
        builder.append(processingTime);
        builder.append("\", \"processingTimeMin\":\"");
        builder.append(processingTimeMin == null ? "" : processingTimeMin);
        builder.append("\", \"sampleFee\":\"");
        builder.append(sampleFee);
        builder.append("\", \"sampleMoq\":\"");
        builder.append(sampleMoq);
        builder.append("\", \"seilUnit\":\"");
        builder.append(seilUnit);
        builder.append("\", \"shopId\":\"");
        builder.append(shopId == null ? "" : shopId);
        builder.append("\", \"spider_Price\":\"");
        builder.append(spider_Price);
        builder.append("\", \"spider_changenum\":\"");
        builder.append(spider_changenum);
        builder.append("\", \"startBizFactoryPrice\":\"");
        builder.append(startBizFactoryPrice == null ? "" : startBizFactoryPrice);
        builder.append("\", \"state\":\"");
        builder.append(state);
        builder.append("\", \"total_price\":\"");
        builder.append(total_price);
        builder.append("\", \"total_weight\":\"");
        builder.append(total_weight);
        builder.append("\", \"ts1\":\"");
        builder.append(ts1);
        builder.append("\", \"urlMD5\":\"");
        builder.append(goodsUrlMD5);
        builder.append("\"}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodsCarActiveBean that = (GoodsCarActiveBean) o;

        if (Double.compare(that.categoryDiscountRate, categoryDiscountRate) != 0) return false;
        if (Double.compare(that.es1, es1) != 0) return false;
        if (Double.compare(that.jes1, jes1) != 0) return false;
        if (firstnumber != that.firstnumber) return false;
        if (Double.compare(that.firstprice, firstprice) != 0) return false;
        if (Double.compare(that.freeprice, freeprice) != 0) return false;
        if (Double.compare(that.freight, freight) != 0) return false;
        if (Double.compare(that.freight_es1, freight_es1) != 0) return false;
        if (Double.compare(that.method_feight, method_feight) != 0) return false;
        if (Double.compare(that.notfreeprice, notfreeprice) != 0) return false;
        if (number != that.number) return false;
        if (oNum != that.oNum) return false;
        if (Double.compare(that.price1, price1) != 0) return false;
        if (Double.compare(that.price2, price2) != 0) return false;
        if (Double.compare(that.price3, price3) != 0) return false;
        if (state != that.state) return false;
        if (Double.compare(that.total_price, total_price) != 0) return false;
        if (isBattery != that.isBattery) return false;
        if (Double.compare(that.freightByWeight, freightByWeight) != 0) return false;
        if (goods_class != that.goods_class) return false;
        if (spider_changenum != that.spider_changenum) return false;
        if (Double.compare(that.ts1, ts1) != 0) return false;
        if (Double.compare(that.old_price, old_price) != 0) return false;
        if (old_CountryId != that.old_CountryId) return false;
        if (Double.compare(that.JTS1, JTS1) != 0) return false;
        if (guId != null ? !guId.equals(that.guId) : that.guId != null) return false;
        if (norm_least != null ? !norm_least.equals(that.norm_least) : that.norm_least != null) return false;
        if (perWeight != null ? !perWeight.equals(that.perWeight) : that.perWeight != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (startBizFactoryPrice != null ? !startBizFactoryPrice.equals(that.startBizFactoryPrice) : that.startBizFactoryPrice != null)
            return false;
        if (total_weight != null ? !total_weight.equals(that.total_weight) : that.total_weight != null) return false;
        if (bulk_volume != null ? !bulk_volume.equals(that.bulk_volume) : that.bulk_volume != null) return false;
        if (seilUnit != null ? !seilUnit.equals(that.seilUnit) : that.seilUnit != null) return false;
        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        if (goodsUrlMD5 != null ? !goodsUrlMD5.equals(that.goodsUrlMD5) : that.goodsUrlMD5 != null) return false;
        if (bizPriceDiscount != null ? !bizPriceDiscount.equals(that.bizPriceDiscount) : that.bizPriceDiscount != null)
            return false;
        if (priceListSize != null ? !priceListSize.equals(that.priceListSize) : that.priceListSize != null)
            return false;
        if (spider_Price != null ? !spider_Price.equals(that.spider_Price) : that.spider_Price != null) return false;
        if (method_day != null ? !method_day.equals(that.method_day) : that.method_day != null) return false;
        if (priceList != null ? !priceList.equals(that.priceList) : that.priceList != null) return false;
        return processingTime != null ? processingTime.equals(that.processingTime) : that.processingTime == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(categoryDiscountRate);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(es1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(jes1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + firstnumber;
        temp = Double.doubleToLongBits(firstprice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(freeprice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(freight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(freight_es1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (guId != null ? guId.hashCode() : 0);
        temp = Double.doubleToLongBits(method_feight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (norm_least != null ? norm_least.hashCode() : 0);
        temp = Double.doubleToLongBits(notfreeprice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + number;
        result = 31 * result + oNum;
        result = 31 * result + (perWeight != null ? perWeight.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        temp = Double.doubleToLongBits(price1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(price2);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(price3);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (startBizFactoryPrice != null ? startBizFactoryPrice.hashCode() : 0);
        result = 31 * result + state;
        temp = Double.doubleToLongBits(total_price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (total_weight != null ? total_weight.hashCode() : 0);
        result = 31 * result + (bulk_volume != null ? bulk_volume.hashCode() : 0);
        result = 31 * result + isBattery;
        temp = Double.doubleToLongBits(freightByWeight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + goods_class;
        result = 31 * result + (seilUnit != null ? seilUnit.hashCode() : 0);
        result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
        result = 31 * result + (goodsUrlMD5 != null ? goodsUrlMD5.hashCode() : 0);
        result = 31 * result + (bizPriceDiscount != null ? bizPriceDiscount.hashCode() : 0);
        result = 31 * result + (priceListSize != null ? priceListSize.hashCode() : 0);
        result = 31 * result + (spider_Price != null ? spider_Price.hashCode() : 0);
        result = 31 * result + spider_changenum;
        temp = Double.doubleToLongBits(ts1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(old_price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (method_day != null ? method_day.hashCode() : 0);
        result = 31 * result + old_CountryId;
        temp = Double.doubleToLongBits(JTS1);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (priceList != null ? priceList.hashCode() : 0);
        result = 31 * result + (processingTime != null ? processingTime.hashCode() : 0);
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        GoodsCarActiveBean goodsCarActiveBean = new GoodsCarActiveBean();
        goodsCarActiveBean.setIsFreeShipProduct(this.isFreeShipProduct);
        goodsCarActiveBean.setSampleFee(this.sampleFee);
        goodsCarActiveBean.setSampleMoq(this.sampleMoq);
        goodsCarActiveBean.setShopId(this.shopId);
        goodsCarActiveBean.setComparePrices(this.comparePrices);
        goodsCarActiveBean.setProcessingTimeMin(this.processingTimeMin);
        goodsCarActiveBean.setProcessingTime(this.processingTime);
        goodsCarActiveBean.setPriceList(this.priceList);
        goodsCarActiveBean.setJTS1(JTS1);
        goodsCarActiveBean.setOld_CountryId(this.old_CountryId);
        goodsCarActiveBean.setMethod_day(this.method_day);
        goodsCarActiveBean.setOld_price(old_price);
        goodsCarActiveBean.setJes1(this.jes1);
        goodsCarActiveBean.setTs1(this.ts1);
        goodsCarActiveBean.setSpider_changenum(this.spider_changenum);
        goodsCarActiveBean.setSpider_Price(this.spider_Price);
        goodsCarActiveBean.setPriceListSize(this.priceListSize);
        goodsCarActiveBean.setBizPriceDiscount(this.bizPriceDiscount);
        goodsCarActiveBean.setUrlMD5(this.goodsUrlMD5);
        goodsCarActiveBean.setItemId(this.itemId);
        goodsCarActiveBean.setSeilUnit(this.seilUnit);
        goodsCarActiveBean.setGoods_class(this.goods_class);
        goodsCarActiveBean.setFreightByWeight(this.freightByWeight);
        goodsCarActiveBean.setIsBattery(this.isBattery);
        goodsCarActiveBean.setBulk_volume(this.bulk_volume);
        goodsCarActiveBean.setCategoryDiscountRate(this.categoryDiscountRate);
        goodsCarActiveBean.setEs1(this.es1);
        goodsCarActiveBean.setFirstnumber(this.firstnumber);
        goodsCarActiveBean.setFirstprice(this.firstprice);
        goodsCarActiveBean.setFreeprice(this.freeprice);
        goodsCarActiveBean.setFreight(this.freight);
        goodsCarActiveBean.setFreight_es1(this.freight_es1);
        goodsCarActiveBean.setGuId(this.guId);
        goodsCarActiveBean.setMethod_feight(this.method_feight);
        goodsCarActiveBean.setNorm_least(this.norm_least);
        goodsCarActiveBean.setNotfreeprice(this.notfreeprice);
        goodsCarActiveBean.setNumber(this.number);
        goodsCarActiveBean.setoNum(this.oNum);
        goodsCarActiveBean.setPerWeight(this.perWeight);
        goodsCarActiveBean.setPrice(this.price);
        goodsCarActiveBean.setPrice1(this.price1);
        goodsCarActiveBean.setPrice2(this.price2);
        goodsCarActiveBean.setPrice3(this.price3);
        goodsCarActiveBean.setStartBizFactoryPrice(this.startBizFactoryPrice);
        goodsCarActiveBean.setState(this.state);
        goodsCarActiveBean.setTotal_price(this.total_price);
        goodsCarActiveBean.setTotal_weight(this.total_weight);
        goodsCarActiveBean.setGroupBuyId(this.groupBuyId);
        goodsCarActiveBean.setGbPrice(this.gbPrice);
        return goodsCarActiveBean;
    }
}
