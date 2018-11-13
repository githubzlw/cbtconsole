package com.cbt.warehouse.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Skuinfo {
    private Integer id;

    private String sku;

    private String skucnname;

    private String skuenname;

    private Integer statu;

    private String productdirectory;

    private Integer skusum;

    private String shipped;

    private String weight;

    private String skuimglink;

    private String shipimglink;

    private String warehouse;

    private BigDecimal unitcost;

    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getSkucnname() {
        return skucnname;
    }

    public void setSkucnname(String skucnname) {
        this.skucnname = skucnname == null ? null : skucnname.trim();
    }

    public String getSkuenname() {
        return skuenname;
    }

    public void setSkuenname(String skuenname) {
        this.skuenname = skuenname == null ? null : skuenname.trim();
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getProductdirectory() {
        return productdirectory;
    }

    public void setProductdirectory(String productdirectory) {
        this.productdirectory = productdirectory == null ? null : productdirectory.trim();
    }

    public Integer getSkusum() {
        return skusum;
    }

    public void setSkusum(Integer skusum) {
        this.skusum = skusum;
    }

    public String getShipped() {
        return shipped;
    }

    public void setShipped(String shipped) {
        this.shipped = shipped == null ? null : shipped.trim();
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight == null ? null : weight.trim();
    }

    public String getSkuimglink() {
        return skuimglink;
    }

    public void setSkuimglink(String skuimglink) {
        this.skuimglink = skuimglink == null ? null : skuimglink.trim();
    }

    public String getShipimglink() {
        return shipimglink;
    }

    public void setShipimglink(String shipimglink) {
        this.shipimglink = shipimglink == null ? null : shipimglink.trim();
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse == null ? null : warehouse.trim();
    }

    public BigDecimal getUnitcost() {
        return unitcost;
    }

    public void setUnitcost(BigDecimal unitcost) {
        this.unitcost = unitcost;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}