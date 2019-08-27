package com.importExpress.pojo;

public class OrderCancelApprovalAmount {

    private Integer id;
    private Integer approvalId;
    private String orderNo;
    private Double payAmount;
    private Integer payType;
    private String createTime;
    /**
     * 1 PayPal或者Stripe 2 余额
     */
    private int refundMethod;
    private String refundMethodDesc;

    public String getRefundMethodDesc() {
        return refundMethodDesc;
    }

    public void setRefundMethodDesc(String refundMethodDesc) {
        this.refundMethodDesc = refundMethodDesc;

    }

    public int getRefundMethod() {
        return refundMethod;
    }

    public void setRefundMethod(int refundMethod) {
        this.refundMethod = refundMethod;
        if(refundMethod == 1){
            refundMethodDesc = "PayPal者Stripe";
        } else if(refundMethod == 2){
            refundMethodDesc = "余额";
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Double payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"approvalId\":")
                .append(approvalId);
        sb.append(",\"orderNo\":\"")
                .append(orderNo).append('\"');
        sb.append(",\"payAmount\":")
                .append(payAmount);
        sb.append(",\"payType\":")
                .append(payType);
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
