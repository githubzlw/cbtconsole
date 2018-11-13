package com.cbt.bean;

import com.cbt.util.StrUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jxw
 * @ClassName GoodsOfferBean
 * @Description 产品原始表数据bean
 * @date 2018年4月10日
 */
public class GoodsOfferBean {
    private String pid;
    private String shopId;
    private String catid;
    private String weightStr;
    private double weight;
    private String priceStr;
    private double price;
    private String goodsName;
    private String imgUrl;
    private double setWeight;

    private double avgWeightfreight;
    private double goodsWeightfreight;
    /**
     * 重量异常标识 0正常 1特例 2重量在 “类别+关键词”平均重量 正负 30%的以内 3异常太轻 4异常太重 5重量为0
     */
    private int weightFlag;
    private String weightFlagDescribe;
    /**
     * 重量异常处理，0未处理 1已处理
     */
    private int weightDeal;

    private String detail;
    private int detailNum;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getWeightStr() {
        return weightStr;
    }

    public void setWeightStr(String weightStr) {
        this.weightStr = weightStr;
        // 读取重量数据
        if (!(weightStr == null || "".equals(weightStr))) {
            String weight1688 = StrUtils.object2Str(weightStr);
            weight1688 = StrUtils.matchStr(weight1688, "(\\d+(\\.\\d+){0,1})");
            double weightFinal = Double.valueOf((weight1688 == null || "".equals(weight1688)) ? "0" : weight1688);
            this.weight = weightFinal;
        }
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
        // 读取价格数据
        if (!(priceStr == null || "".equals(priceStr))) {
            // 计算价格
            List<String> priceStrList = new ArrayList<String>();
            String price = priceStr.replaceAll("\\n", "");
            String[] priceStrs = price.split("起批量");
            priceStrList = StrUtils.matchStrList("(\\d+\\.\\d{1,2})", priceStrs[0]);
            if (!priceStrList.isEmpty()) {
                String price_1688 = priceStrList.get(0);
                double factoryPrice = Double.valueOf(price_1688.split("-")[0]) / StrUtils.EXCHANGE_RATE;
                if (factoryPrice < 0.01) {
                    factoryPrice = Double.valueOf(price_1688.split("-")[0]);
                }
                this.price = factoryPrice;
            }
            priceStrList.clear();
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getSetWeight() {
        return setWeight;
    }

    public void setSetWeight(double setWeight) {
        this.setWeight = setWeight;
    }

    public double getAvgWeightfreight() {
        return avgWeightfreight;
    }

    public void setAvgWeightfreight(double avgWeightfreight) {
        this.avgWeightfreight = avgWeightfreight;
    }

    public double getGoodsWeightfreight() {
        return goodsWeightfreight;
    }

    public void setGoodsWeightfreight(double goodsWeightfreight) {
        this.goodsWeightfreight = goodsWeightfreight;
    }

    public int getWeightFlag() {
        return weightFlag;
    }

    public void setWeightFlag(int weightFlag) {
        this.weightFlag = weightFlag;
        // 重量异常标识 0正常 1特例 2重量在 “类别+关键词”平均重量 正负 30%的以内 3异常太轻 4异常太重 5重量为0
        if (weightFlag == 0) {
            this.weightFlagDescribe = "正常";
        } else if (weightFlag == 1) {
            this.weightFlagDescribe = "特例";
        } else if (weightFlag == 2) {
            this.weightFlagDescribe = "平均重量范围内";
        } else if (weightFlag == 3) {
            this.weightFlagDescribe = "太轻";
        } else if (weightFlag == 4) {
            this.weightFlagDescribe = "太重";
        } else if (weightFlag == 5) {
            this.weightFlagDescribe = "重量为0";
        }
    }

    public String getWeightFlagDescribe() {
        return weightFlagDescribe;
    }

    public void setWeightFlagDescribe(String weightFlagDescribe) {
        this.weightFlagDescribe = weightFlagDescribe;
    }

    public int getWeightDeal() {
        return weightDeal;
    }

    public void setWeightDeal(int weightDeal) {
        this.weightDeal = weightDeal;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
        if (StringUtils.isNotBlank(detail)) {
            if (detail.length() < 15) {
                this.detailNum = 0;
            } else {
                this.detailNum = detail.length();
            }
        }
    }

    public int getDetailNum() {
        return detailNum;
    }

    public void setDetailNum(int detailNum) {
        this.detailNum = detailNum;
    }

    @Override
    public String toString() {
        return "{\"pid\":\"" + pid + "\", \"shopId\":\"" + shopId + "\", \"catid\":\"" + catid + "\", \"weight\":\""
                + weight + "\", \"price\":\"" + price + "\", \"goodsName\":\"" + goodsName + "\", \"imgUrl\":\""
                + imgUrl + "\", \"weightFlag\":\"" + weightFlag + "\", \"weightFlagDescribe\":\"" + weightFlagDescribe
                + "\", \"weightDeal\":\"" + weightDeal + "\"}";
    }

}
