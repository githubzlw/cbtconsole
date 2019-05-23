package com.importExpress.pojo;

public class ShopCarUserStatistic {
    private int userId;//用户id
    private String userEmail;//用户email
    private String createTime;//创建时间
    private String lastLoginTime;//最后访问时间
    private String lastAddCartTime;//最后加入购物车时间
    private String shippingName;//运输方式
    private int countryId;
    private String countryName;
    private int gradeId;
    private double estimateProfit;
    private double totalPrice;//总金额
    private double totalFreight;
    private double totalWhosePrice;
    private double averagePrice;//均价
    private String currency;//用户货币
    private int totalCatid;//类别总数
    private int followAdminId;//跟进人ID
    private String followAdminName;//跟进人
    private String followTime;//跟进时间
    private int isOrder = -1;//是否下单 0未下单 1下单
    private int isFollow= -1;//是否跟进
    private int limitNum;
    private int startNum;
    private double beginMoney;
    private double endMoney;
    private int saleId;//跟进人ID
    private String saleName;//销售
    private double offFreight;// 线下预估运费
    private int num;// 客户浏览商品数
    private String password;// 客户浏览商品数
    private String admname;// 客户浏览商品数

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdmname() {
        return admname;
    }

    public void setAdmname(String admname) {
        this.admname = admname;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public double getEstimateProfit() {
        return estimateProfit;
    }

    public void setEstimateProfit(double estimateProfit) {
        this.estimateProfit = estimateProfit;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalFreight() {
        return totalFreight;
    }

    public void setTotalFreight(double totalFreight) {
        this.totalFreight = totalFreight;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public double getTotalWhosePrice() {
        return totalWhosePrice;
    }

    public void setTotalWhosePrice(double totalWhosePrice) {
        this.totalWhosePrice = totalWhosePrice;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getFollowAdminId() {
        return followAdminId;
    }

    public void setFollowAdminId(int followAdminId) {
        this.followAdminId = followAdminId;
    }

    public String getFollowAdminName() {
        return followAdminName;
    }

    public void setFollowAdminName(String followAdminName) {
        this.followAdminName = followAdminName;
    }

    public String getFollowTime() {
        return followTime;
    }

    public void setFollowTime(String followTime) {
        this.followTime = followTime;
    }

    public int getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(int isOrder) {
        this.isOrder = isOrder;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getTotalCatid() {
        return totalCatid;
    }

    public void setTotalCatid(int totalCatid) {
        this.totalCatid = totalCatid;
    }

    public String getLastAddCartTime() {
        return lastAddCartTime;
    }

    public void setLastAddCartTime(String lastAddCartTime) {
        this.lastAddCartTime = lastAddCartTime;
    }

    public double getBeginMoney() {
        return beginMoney;
    }

    public void setBeginMoney(double beginMoney) {
        this.beginMoney = beginMoney;
    }

    public double getEndMoney() {
        return endMoney;
    }

    public void setEndMoney(double endMoney) {
        this.endMoney = endMoney;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public double getOffFreight() {
        return offFreight;
    }

    public void setOffFreight(double offFreight) {
        this.offFreight = offFreight;
    }

    @Override
    public String toString() {
        return "ShopCarUserStatistic{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", lastAddCartTime='" + lastAddCartTime + '\'' +
                ", shippingName='" + shippingName + '\'' +
                ", countryId=" + countryId +
                ", countryName='" + countryName + '\'' +
                ", gradeId=" + gradeId +
                ", estimateProfit=" + estimateProfit +
                ", totalPrice=" + totalPrice +
                ", totalFreight=" + totalFreight +
                ", totalWhosePrice=" + totalWhosePrice +
                ", averagePrice=" + averagePrice +
                ", currency='" + currency + '\'' +
                ", totalCatid=" + totalCatid +
                ", followAdminId='" + followAdminId + '\'' +
                ", followAdminName='" + followAdminName + '\'' +
                ", followTime='" + followTime + '\'' +
                '}';
    }
}
