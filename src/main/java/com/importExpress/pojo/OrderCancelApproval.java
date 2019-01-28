package com.importExpress.pojo;

public class OrderCancelApproval {

    private Integer id;
    private Integer userId;
    private String orderNo;
    private double payPrice;
    private Integer type;
    private Integer dealState;// 0 已申请 1销售中 2主管审批 3审批通过 4驳回
    private String createTime;
    private OrderCancelApprovalDetails  approval1;
    private OrderCancelApprovalDetails  approval2;
    private OrderCancelApprovalDetails  approval3;
    private String userEmail;
    private Integer adminId;
    private String adminName;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDealState() {
        return dealState;
    }

    public void setDealState(Integer dealState) {
        this.dealState = dealState;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
