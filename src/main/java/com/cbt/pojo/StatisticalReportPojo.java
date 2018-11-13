package com.cbt.pojo;

import java.io.Serializable;

public class StatisticalReportPojo implements Serializable{
    private static final long serialVersionUID = -2510561703549299522L;

    //商品分类销售额统计报表实体
    private String en_name;
    private String category_id;
    private String salesAmount;
    private String avgSalesPrice;
    private String buyAmount;
    private String buyCount;
    private String profitLoss;
    private String salesCount;
    private String cateAmount;
    //订单分类统计报表实体类
    private int id;
    private String orderid;
    private String expressno;
    private double avgSalePrice;
    private double inFreight;
    private double actualFreight;
    private double proLossAmount;
    private double estimatedWeight;
    private double inWeight;
    private double actualWeight;
    private double proLoss;
    private String shippingType;
    private String create_time;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getExpressno() {
        return expressno;
    }

    public void setExpressno(String expressno) {
        this.expressno = expressno;
    }

    public double getAvgSalePrice() {
        return avgSalePrice;
    }

    public void setAvgSalePrice(double avgSalePrice) {
        this.avgSalePrice = avgSalePrice;
    }

    public double getInFreight() {
        return inFreight;
    }

    public void setInFreight(double inFreight) {
        this.inFreight = inFreight;
    }

    public double getActualFreight() {
        return actualFreight;
    }

    public void setActualFreight(double actualFreight) {
        this.actualFreight = actualFreight;
    }

    public double getProLossAmount() {
        return proLossAmount;
    }

    public void setProLossAmount(double proLossAmount) {
        this.proLossAmount = proLossAmount;
    }

    public double getEstimatedWeight() {
        return estimatedWeight;
    }

    public void setEstimatedWeight(double estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }

    public double getInWeight() {
        return inWeight;
    }

    public void setInWeight(double inWeight) {
        this.inWeight = inWeight;
    }

    public double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(double actualWeight) {
        this.actualWeight = actualWeight;
    }

    public double getProLoss() {
        return proLoss;
    }

    public void setProLoss(double proLoss) {
        this.proLoss = proLoss;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getCateAmount() {
        return cateAmount;
    }

    public void setCateAmount(String cateAmount) {
        this.cateAmount = cateAmount;
    }

    public String getSalesCount() {
        return salesCount;
    }

    public void setSalesCount(String salesCount) {
        this.salesCount = salesCount;
    }

    public String getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(String profitLoss) {
        this.profitLoss = profitLoss;
    }

    public String getEn_name() {
        return en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(String salesAmount) {
        this.salesAmount = salesAmount;
    }

    public String getAvgSalesPrice() {
        return avgSalesPrice;
    }

    public void setAvgSalesPrice(String avgSalesPrice) {
        this.avgSalesPrice = avgSalesPrice;
    }

    public String getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    public String getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(String buyCount) {
        this.buyCount = buyCount;
    }
}
