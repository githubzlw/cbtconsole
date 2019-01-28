package com.importExpress.pojo;

public class OrderCancelApprovalDetails {
    private Integer id;
    private Integer approvalId;
    private String orderNo;
    private double payPrice;
    private Integer adminId;
    private String adminName;
    private Integer dealState;// 0 待审批 1销售中 2主管审批 3审批通过 4驳回
    private String dealStateDesc;
    private String createTime;
    private String remark;

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

    public double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(double payPrice) {
        this.payPrice = payPrice;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Integer getDealState() {
        return dealState;
    }

    public void setDealState(Integer dealState) {
        this.dealState = dealState;
        // 0 待审批 1销售中 2主管审批 3审批通过 4驳回
        if (dealState == 0) {
            this.dealStateDesc = "待审批";
        } else if (dealState == 1) {
            this.dealStateDesc = "销售审批";
        } else if (dealState == 2) {
            this.dealStateDesc = "主管审批";
        } else if (dealState == 3) {
            this.dealStateDesc = "完成";
        } else if (dealState == 4) {
            this.dealStateDesc = "驳回";
        }
    }

    public String getDealStateDesc() {
        return dealStateDesc;
    }

    public void setDealStateDesc(String dealStateDesc) {
        this.dealStateDesc = dealStateDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        sb.append(",\"payPrice\":")
                .append(payPrice);
        sb.append(",\"adminId\":")
                .append(adminId);
        sb.append(",\"adminName\":")
                .append(adminName);
        sb.append(",\"dealState\":")
                .append(dealState);
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
