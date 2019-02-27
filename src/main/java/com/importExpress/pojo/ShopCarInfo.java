package com.importExpress.pojo;

public class ShopCarInfo {
    private int id;
    private String pid;
    private double cartGoodsPrice;
    private int cartGoodsNum;
    private double cartOldPrice;
    private String cartGoodsImg;
    private String mainImg;
    private String remotePath;
    private String showImg;
    private String wprice;
    private int isBenchmark;
    private int bmFlag;
    private String wholesalePrice;
    private double finalWeight;
    private double weight1688;
    private double cartWeight;
    private String aliPid;
    private String aliImg;
    private String aliPrice;
    private double priceRate;
    private String catid1;
    private String goodsTitle;
    private String goodsType;
    private int moq;
    private String onlineUrl;
    private String aliName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public double getCartGoodsPrice() {
        return cartGoodsPrice;
    }

    public void setCartGoodsPrice(double cartGoodsPrice) {
        this.cartGoodsPrice = cartGoodsPrice;
    }

    public int getCartGoodsNum() {
        return cartGoodsNum;
    }

    public void setCartGoodsNum(int cartGoodsNum) {
        this.cartGoodsNum = cartGoodsNum;
    }

    public double getCartOldPrice() {
        return cartOldPrice;
    }

    public void setCartOldPrice(double cartOldPrice) {
        this.cartOldPrice = cartOldPrice;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public String getWprice() {
        return wprice;
    }

    public void setWprice(String wprice) {
        this.wprice = wprice;
    }

    public int getIsBenchmark() {
        return isBenchmark;
    }

    public void setIsBenchmark(int isBenchmark) {
        this.isBenchmark = isBenchmark;
    }

    public int getBmFlag() {
        return bmFlag;
    }

    public void setBmFlag(int bmFlag) {
        this.bmFlag = bmFlag;
    }

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public double getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(double finalWeight) {
        this.finalWeight = finalWeight;
    }

    public double getWeight1688() {
        return weight1688;
    }

    public void setWeight1688(double weight1688) {
        this.weight1688 = weight1688;
    }

    public double getCartWeight() {
        return cartWeight;
    }

    public void setCartWeight(double cartWeight) {
        this.cartWeight = cartWeight;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    public String getAliImg() {
        return aliImg;
    }

    public void setAliImg(String aliImg) {
        this.aliImg = aliImg;
    }

    public String getAliPrice() {
        return aliPrice;
    }

    public void setAliPrice(String aliPrice) {
        this.aliPrice = aliPrice;
    }

    public double getPriceRate() {
        return priceRate;
    }

    public void setPriceRate(double priceRate) {
        this.priceRate = priceRate;
    }

    public String getCatid1() {
        return catid1;
    }

    public void setCatid1(String catid1) {
        this.catid1 = catid1;
    }

    public String getShowImg() {
        return showImg;
    }

    public void setShowImg(String showImg) {
        this.showImg = showImg;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public int getMoq() {
        return moq;
    }

    public void setMoq(int moq) {
        this.moq = moq;
    }

    public String getOnlineUrl() {
        return onlineUrl;
    }

    public void setOnlineUrl(String onlineUrl) {
        this.onlineUrl = onlineUrl;
    }

    public String getCartGoodsImg() {
        return cartGoodsImg;
    }

    public void setCartGoodsImg(String cartGoodsImg) {
        this.cartGoodsImg = cartGoodsImg;
    }

    public String getAliName() {
        return aliName;
    }

    public void setAliName(String aliName) {
        this.aliName = aliName;
    }

    @Override
    public String toString() {
        return "ShopCarInfo{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", cartGoodsPrice=" + cartGoodsPrice +
                ", cartGoodsNum=" + cartGoodsNum +
                ", cartOldPrice=" + cartOldPrice +
                ", mainImg='" + mainImg + '\'' +
                ", remotePath='" + remotePath + '\'' +
                ", wprice='" + wprice + '\'' +
                ", isBenchmark=" + isBenchmark +
                ", bmFlag=" + bmFlag +
                ", wholesalePrice='" + wholesalePrice + '\'' +
                ", finalWeight=" + finalWeight +
                ", weight1688=" + weight1688 +
                ", cartWeight=" + cartWeight +
                ", aliPid='" + aliPid + '\'' +
                ", aliImg='" + aliImg + '\'' +
                ", aliPrice=" + aliPrice +
                ", priceRate=" + priceRate +
                ", catid1='" + catid1 + '\'' +
                '}';
    }
}
