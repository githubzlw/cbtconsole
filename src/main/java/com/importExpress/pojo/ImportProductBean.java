package com.importExpress.pojo;

import org.apache.commons.lang3.StringUtils;

public class ImportProductBean {
    private Integer id;
    private String pid;//产品id
    private String name;//产品名称
    private String img;//产品图片
    private String price;//产品价格
    private String url;//产品链接
    private String unit;//单位
    private String catid;
    private String weight;
    private String createTime;
    private String aliPid;
    private String remotePath;
    private String rangePrice;
	private String wprice;
	private String feeprice;
	private String showPrice;
	private Integer isSoldFlag;
	private Integer moq;
	private Integer sold;
	private Integer dealState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
        if (StringUtils.isNotBlank(pid)) {
            this.url = "https://detail.1688.com/offer/" + pid + ".html";
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getRangePrice() {
        return rangePrice;
    }

    public void setRangePrice(String rangePrice) {
        this.rangePrice = rangePrice;
    }

    public String getWprice() {
        return wprice;
    }

    public void setWprice(String wprice) {
        this.wprice = wprice;
    }

    public String getFeeprice() {
        return feeprice;
    }

    public void setFeeprice(String feeprice) {
        this.feeprice = feeprice;
    }

    public String getShowPrice() {
        return showPrice;
    }

    public void setShowPrice(String showPrice) {
        this.showPrice = showPrice;
    }

    public Integer getIsSoldFlag() {
        return isSoldFlag;
    }

    public void setIsSoldFlag(Integer isSoldFlag) {
        this.isSoldFlag = isSoldFlag;
    }

    public Integer getMoq() {
        return moq;
    }

    public void setMoq(Integer moq) {
        this.moq = moq;
    }

    public Integer getSold() {
        return sold;
    }

    public void setSold(Integer sold) {
        this.sold = sold;
    }

    public Integer getDealState() {
        return dealState;
    }

    public void setDealState(Integer dealState) {
        this.dealState = dealState;
    }


    @Override
    public String toString() {
        return "ImportProductBean{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", price='" + price + '\'' +
                ", url='" + url + '\'' +
                ", unit='" + unit + '\'' +
                ", catid='" + catid + '\'' +
                ", weight='" + weight + '\'' +
                ", createTime='" + createTime + '\'' +
                ", aliPid='" + aliPid + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", rangePrice='" + rangePrice + '\'' +
                ", wprice='" + wprice + '\'' +
                ", feeprice='" + feeprice + '\'' +
                ", showPrice='" + showPrice + '\'' +
                ", isSoldFlag=" + isSoldFlag +
                ", moq=" + moq +
                ", sold=" + sold +
                ", dealState=" + dealState +
                '}';
    }
}
