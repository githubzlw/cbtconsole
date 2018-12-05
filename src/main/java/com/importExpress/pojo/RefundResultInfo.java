package com.importExpress.pojo;

import java.util.Arrays;

public class RefundResultInfo {
    private Integer id;

    private String orderno;

    private String userid;

    private String payprice;

    private String refundFromTransactionFee;

    private String refundFromReceivedAmount;

    private String totalRefundedAmount;

    private String refundid;

    private String amountTotal;

    private String state;

    private String saleId;

    private String parentPayment;

    private String createTime;

    private String updateTime;

    private byte[] info;

    private int limitNum;
    private int startNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getPayprice() {
        return payprice;
    }

    public void setPayprice(String payprice) {
        this.payprice = payprice == null ? null : payprice.trim();
    }

    public String getRefundFromTransactionFee() {
        return refundFromTransactionFee;
    }

    public void setRefundFromTransactionFee(String refundFromTransactionFee) {
        this.refundFromTransactionFee = refundFromTransactionFee == null ? null : refundFromTransactionFee.trim();
    }

    public String getRefundFromReceivedAmount() {
        return refundFromReceivedAmount;
    }

    public void setRefundFromReceivedAmount(String refundFromReceivedAmount) {
        this.refundFromReceivedAmount = refundFromReceivedAmount == null ? null : refundFromReceivedAmount.trim();
    }

    public String getTotalRefundedAmount() {
        return totalRefundedAmount;
    }

    public void setTotalRefundedAmount(String totalRefundedAmount) {
        this.totalRefundedAmount = totalRefundedAmount == null ? null : totalRefundedAmount.trim();
    }

    public String getRefundid() {
        return refundid;
    }

    public void setRefundid(String refundid) {
        this.refundid = refundid == null ? null : refundid.trim();
    }

    public String getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(String amountTotal) {
        this.amountTotal = amountTotal == null ? null : amountTotal.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId == null ? null : saleId.trim();
    }

    public String getParentPayment() {
        return parentPayment;
    }

    public void setParentPayment(String parentPayment) {
        this.parentPayment = parentPayment == null ? null : parentPayment.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

    public byte[] getInfo() {
        return info;
    }

    public void setInfo(byte[] info) {
        this.info = info;
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

    @Override
    public String toString() {
        return "RefundResultInfo{" +
                "id=" + id +
                ", orderno='" + orderno + '\'' +
                ", userid='" + userid + '\'' +
                ", payprice='" + payprice + '\'' +
                ", refundFromTransactionFee='" + refundFromTransactionFee + '\'' +
                ", refundFromReceivedAmount='" + refundFromReceivedAmount + '\'' +
                ", totalRefundedAmount='" + totalRefundedAmount + '\'' +
                ", refundid='" + refundid + '\'' +
                ", amountTotal='" + amountTotal + '\'' +
                ", state='" + state + '\'' +
                ", saleId='" + saleId + '\'' +
                ", parentPayment='" + parentPayment + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", info=" + Arrays.toString(info) +
                '}';
    }
}