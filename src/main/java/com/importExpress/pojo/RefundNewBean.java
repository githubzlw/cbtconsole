package com.importExpress.pojo;

/**
 * 新的退款Bean
 */
public class RefundNewBean {
    private int id;
    private int userId;//用户id
    private int type;//来源   0-余额提现  1-payPal申诉
    private String typeDesc;//来源描述
    private int state;//退款状态   0-新申请 1-销售审批 2-主管审批 3-财务确认 4-已经完结 9驳回  -1客户取消
    private String stateDesc;//退款状态描述
    private String paymentEmail;//支付账号
    private String userEmail;//客户邮箱
    private double userBalance;//客户账号余额
    private String orderNo;//订单号
    private double appliedAmount;//申请金额
    private String currency;//货币单位
    private String appliedTime;//申请时间
    private String reasonCode;//payPal申诉类型
    private String reasonNote;//payPal申诉内容
    private String endTime;//截止时间
    private String operationTime;//操作时间
    private double agreeAmount;//同意金额
    private int salesId;
    private String salesName;
    private String paymentNo;//交易号
    private RefundDetailsBean salesApproval;//销售审批详情
    private RefundDetailsBean adminApproval;//主管审批详情
    private RefundDetailsBean financialApproval;//财务确认详情
    private RefundDetailsBean finishApproval;//完结详情


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        //0-余额提现  1-payPal申诉
        this.type = type;
        if (type == 0) {
            this.typeDesc = "余额提现 ";
        } else if (type == 1) {
            this.typeDesc = "payPal申诉";
        } else {
            this.typeDesc = "";
        }
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {

        this.state = state;
        //0-新申请 1-销售审批 2-主管审批 3-财务确认 4-已经完结 9驳回  -1客户取消
        if (state == 0) {
            this.stateDesc = "新申请";
        } else if (state == 1) {
            this.stateDesc = "销售审批";
        } else if (state == 2) {
            this.stateDesc = "主管审批";
        } else if (state == 3) {
            this.stateDesc = "财务确认";
        } else if (state == 4) {
            this.stateDesc = "已经完结";
        } else if (state == 9) {
            this.stateDesc = "驳回";
        } else if (state == -1) {
            this.stateDesc = "客户取消";
        } else {
            this.stateDesc = "";
        }
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getPaymentEmail() {
        return paymentEmail;
    }

    public void setPaymentEmail(String paymentEmail) {
        this.paymentEmail = paymentEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public double getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(double userBalance) {
        this.userBalance = userBalance;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(double appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAppliedTime() {
        return appliedTime;
    }

    public void setAppliedTime(String appliedTime) {
        this.appliedTime = appliedTime;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonNote() {
        return reasonNote;
    }

    public void setReasonNote(String reasonNote) {
        this.reasonNote = reasonNote;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public double getAgreeAmount() {
        return agreeAmount;
    }

    public void setAgreeAmount(double agreeAmount) {
        this.agreeAmount = agreeAmount;
    }

    public int getSalesId() {
        return salesId;
    }

    public void setSalesId(int salesId) {
        this.salesId = salesId;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public RefundDetailsBean getSalesApproval() {
        return salesApproval;
    }

    public void setSalesApproval(RefundDetailsBean salesApproval) {
        this.salesApproval = salesApproval;
    }

    public RefundDetailsBean getAdminApproval() {
        return adminApproval;
    }

    public void setAdminApproval(RefundDetailsBean adminApproval) {
        this.adminApproval = adminApproval;
    }

    public RefundDetailsBean getFinancialApproval() {
        return financialApproval;
    }

    public void setFinancialApproval(RefundDetailsBean financialApproval) {
        this.financialApproval = financialApproval;
    }

    public RefundDetailsBean getFinishApproval() {
        return finishApproval;
    }

    public void setFinishApproval(RefundDetailsBean finishApproval) {
        this.finishApproval = finishApproval;
    }
}
