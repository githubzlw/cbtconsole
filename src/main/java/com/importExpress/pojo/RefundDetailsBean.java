package com.importExpress.pojo;

/**
 * 退款详情Bean
 */
public class RefundDetailsBean {

    private int id;
    private int refundId;
    private int userId;
    private String orderNo;
    private double refundAmount;
    private int refundState;//退款状态   0-新申请 1-销售审批 2-主管审批 3-财务确认 4-已经完结 9驳回  -1客户取消
    private String refundStateDesc;
    private int reasonType;
    private String reasonTypeDesc;
    private int adminId;
    private String adminName;
    private String remark;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRefundId() {
        return refundId;
    }

    public void setRefundId(int refundId) {
        this.refundId = refundId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
        //0-新申请 1-销售审批 2-主管审批 3-财务确认 4-已经完结 9驳回  -1客户取消
        if (refundState == 0) {
            this.refundStateDesc = "新申请";
        } else if (refundState == 1) {
            this.refundStateDesc = "销售审批";
        } else if (refundState == 2) {
            this.refundStateDesc = "主管审批";
        } else if (refundState == 3) {
            this.refundStateDesc = "财务确认";
        } else if (refundState == 4) {
            this.refundStateDesc = "已经完结";
        } else if (refundState == 9) {
            this.refundStateDesc = "驳回";
        } else if (refundState == -1) {
            this.refundStateDesc = "客户取消";
        } else {
            this.refundStateDesc = "";
        }
    }

    public String getRefundStateDesc() {
        return refundStateDesc;
    }

    public void setRefundStateDesc(String refundStateDesc) {
        this.refundStateDesc = refundStateDesc;
    }

    public int getReasonType() {
        return reasonType;
    }

    public void setReasonType(int reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonTypeDesc() {
        return reasonTypeDesc;
    }

    public void setReasonTypeDesc(String reasonTypeDesc) {
        this.reasonTypeDesc = reasonTypeDesc;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RefundDetailsBean{" +
                "id=" + id +
                ", refundId=" + refundId +
                ", userId=" + userId +
                ", orderNo=" + orderNo +
                ", refundAmount=" + refundAmount +
                ", refundState=" + refundState +
                ", refundStateDesc='" + refundStateDesc + '\'' +
                ", reasonType=" + reasonType +
                ", reasonTypeDesc='" + reasonTypeDesc + '\'' +
                ", adminId=" + adminId +
                ", remark=" + remark +
                ", createTime=" + createTime +
                '}';
    }
}
