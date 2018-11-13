package com.cbt.bean;

public class ExchangeRateDaily {

    private double eurRate;//美元兑EUR汇率
    private double cadRate;//美元兑CAD汇率
    private double gbpRate;//美元兑GBP汇率
    private double audRate;//美元兑AUD汇率
    private double rmbRate;//美元兑RMB汇率
    private String getTime;//日期
    private String createTime;//创建时间

    public double getEurRate() {
        return eurRate;
    }

    public void setEurRate(double eurRate) {
        this.eurRate = eurRate;
    }

    public double getCadRate() {
        return cadRate;
    }

    public void setCadRate(double cadRate) {
        this.cadRate = cadRate;
    }

    public double getGbpRate() {
        return gbpRate;
    }

    public void setGbpRate(double gbpRate) {
        this.gbpRate = gbpRate;
    }

    public double getAudRate() {
        return audRate;
    }

    public void setAudRate(double audRate) {
        this.audRate = audRate;
    }

    public double getRmbRate() {
        return rmbRate;
    }

    public void setRmbRate(double rmbRate) {
        this.rmbRate = rmbRate;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ExchangeRateDaily{" +
                "eurRate=" + eurRate +
                ", cadRate=" + cadRate +
                ", gbpRate=" + gbpRate +
                ", audRate=" + audRate +
                ", rmbRate=" + rmbRate +
                ", getTime='" + getTime + '\'' +
                '}';
    }
}
