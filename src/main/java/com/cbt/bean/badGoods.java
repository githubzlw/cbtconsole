package com.cbt.bean;

public class badGoods {
    private String catid1;// 1688类别id
    private String pid;// 产品id
    private String name;// 产品名称-中文
    private String maxPrice;//最高价格

    public badGoods() {
    }

    public badGoods(String catid1, String pid, String name, String maxPrice) {
        this.catid1 = catid1;
        this.pid = pid;
        this.name = name;
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return "badGoods{" +
                "catid1='" + catid1 + '\'' +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", maxPrice='" + maxPrice + '\'' +
                '}';
    }

    public String getCatid1() {
        return catid1;
    }

    public void setCatid1(String catid1) {
        this.catid1 = catid1;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }
}
