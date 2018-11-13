package com.cbt.pojo;

public class RechangeRecord {

    private int userId;
    private double price;
    private int type;
    private String typeDesc;
    private String remark;
    private String remarkId;
    private String dataTime;
    private String adminUser;
    private int useSign;
    private String useSignDesc;
    private String currency;
    private double balanceAfter;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkId() {
        return remarkId;
    }

    public void setRemarkId(String remarkId) {
        this.remarkId = remarkId;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(String adminUser) {
        this.adminUser = adminUser;
    }

    public int getUseSign() {
        return useSign;
    }

    public void setUseSign(int useSign) {
        this.useSign = useSign;
        if(useSign == 0){
            this.useSignDesc = "收入";
        }else if(useSign == 1){
            this.useSignDesc = "支出";
        }else if(useSign == 2){
            this.useSignDesc = "余额补偿";
        }
    }

    public String getUseSignDesc() {
        return useSignDesc;
    }

    public void setUseSignDesc(String useSignDesc) {
        this.useSignDesc = useSignDesc;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return "RechangeRecord{" +
                "userId=" + userId +
                ", price=" + price +
                ", type=" + type +
                ", typeDesc='" + typeDesc + '\'' +
                ", remark='" + remark + '\'' +
                ", remarkId='" + remarkId + '\'' +
                ", dataTime='" + dataTime + '\'' +
                ", adminUser='" + adminUser + '\'' +
                ", useSign=" + useSign +
                ", useSignDesc='" + useSignDesc + '\'' +
                ", currency='" + currency + '\'' +
                ", balanceAfter=" + balanceAfter +
                '}';
    }
}
