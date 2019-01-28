package com.importExpress.pojo;

public class OrderCancelApproval {

    private Integer id;
    private Integer userId;
    private String orderNo;
    private double payPrice;
    private double agreeAmount;
    private Integer type;
    private String typeDesc;
    private Integer dealState;// 0 待审批 1销售中 2主管审批 3审批通过 4驳回
    private String dealStateDesc;
    private String createTime;
    private String updateTime;// 更新时间
    private OrderCancelApprovalDetails approval1;
    private OrderCancelApprovalDetails approval2;
    private OrderCancelApprovalDetails approval3;
    private String userEmail;
    private Integer adminId;
    private String adminName;
    private double userBalance;

    public OrderCancelApprovalDetails getApproval1() {
        return approval1;
    }

    public void setApproval1(OrderCancelApprovalDetails approval1) {
        this.approval1 = approval1;
    }

    public OrderCancelApprovalDetails getApproval2() {
        return approval2;
    }

    public void setApproval2(OrderCancelApprovalDetails approval2) {
        this.approval2 = approval2;
    }

    public OrderCancelApprovalDetails getApproval3() {
        return approval3;
    }

    public void setApproval3(OrderCancelApprovalDetails approval3) {
        this.approval3 = approval3;
    }

    private int startNum;
    private int limitNum;


    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public double getAgreeAmount() {
        return agreeAmount;
    }

    public void setAgreeAmount(double agreeAmount) {
        this.agreeAmount = agreeAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
        if (type == 1) {
            this.typeDesc = "客户申请";
        } else if (type == 2) {
            this.typeDesc = "后台申请";
        }
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(double userBalance) {
        this.userBalance = userBalance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":")
                .append(id);
        sb.append(",\"userId\":")
                .append(userId);
        sb.append(",\"orderNo\":\"")
                .append(orderNo).append('\"');
        sb.append(",\"payPrice\":")
                .append(payPrice);
        sb.append(",\"type\":")
                .append(type);
        sb.append(",\"dealState\":")
                .append(dealState);
        sb.append(",\"createTime\":\"")
                .append(createTime).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
