package com.cbt.website.bean;

public class CustomerFinancialBean {
    private int userId;//客户ID

    private double totalOrderAmount;//下订单总金额
    private double actualOrderAmount;//实际完成订单金额
    private double cancelOrderAmount;//被取消的订单金额

    private double actualPayAmount;//实际到账金额
    private double payPalPay;//PayPal支付
    private double wireTransferPay;//wireTransfer支付
    private double balancePay;//余额支付
    private double stripePay;//stripe支付
    private double payForBalance;//充值

    private double hasRefundAmount;//已经退款或提现金额
    private double apiRefundAmount;//系统API退款金额
    private double manualRefundAmount;//手动退款金额

    private double compensateAmount;//额外奖励或者补偿金额
    private double dealRefundAmount;//退款或提现处理中金额
    private double cancelRefundAmount;//取消的退款或提现金额
    private double disputeRefundAmount;//根据争议而PayPal上退款的金额

    private double dueBalance;//应有余额(算法一)
    private double dueBalance2;//应有余额(算法一)
    private double currentBalance;//当前余额
    private double availableBalance;//可用余额
    private double blockedBalance;//冻结余额
    private int warnFlag;//警告标识
    private String warnMsg;//警告说明

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public double getActualOrderAmount() {
        return actualOrderAmount;
    }

    public void setActualOrderAmount(double actualOrderAmount) {
        this.actualOrderAmount = actualOrderAmount;
    }

    public double getCancelOrderAmount() {
        return cancelOrderAmount;
    }

    public void setCancelOrderAmount(double cancelOrderAmount) {
        this.cancelOrderAmount = cancelOrderAmount;
    }

    public double getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(double actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public double getPayPalPay() {
        return payPalPay;
    }

    public void setPayPalPay(double paypalPay) {
        this.payPalPay = paypalPay;
    }

    public double getWireTransferPay() {
        return wireTransferPay;
    }

    public void setWireTransferPay(double wireTransferPay) {
        this.wireTransferPay = wireTransferPay;
    }

    public double getBalancePay() {
        return balancePay;
    }

    public void setBalancePay(double balancePay) {
        this.balancePay = balancePay;
    }

    public double getStripePay() {
        return stripePay;
    }

    public void setStripePay(double stripePay) {
        this.stripePay = stripePay;
    }

    public double getPayForBalance() {
        return payForBalance;
    }

    public void setPayForBalance(double payForBalance) {
        this.payForBalance = payForBalance;
    }

    public double getHasRefundAmount() {
        return hasRefundAmount;
    }

    public void setHasRefundAmount(double hasRefundAmount) {
        this.hasRefundAmount = hasRefundAmount;
    }

    public double getApiRefundAmount() {
        return apiRefundAmount;
    }

    public void setApiRefundAmount(double apiRefundAmount) {
        this.apiRefundAmount = apiRefundAmount;
    }

    public double getManualRefundAmount() {
        return manualRefundAmount;
    }

    public void setManualRefundAmount(double manualRefundAmount) {
        this.manualRefundAmount = manualRefundAmount;
    }

    public double getCompensateAmount() {
        return compensateAmount;
    }

    public void setCompensateAmount(double compensateAmount) {
        this.compensateAmount = compensateAmount;
    }

    public double getDealRefundAmount() {
        return dealRefundAmount;
    }

    public void setDealRefundAmount(double dealRefundAmount) {
        this.dealRefundAmount = dealRefundAmount;
    }

    public double getCancelRefundAmount() {
        return cancelRefundAmount;
    }

    public void setCancelRefundAmount(double cancelRefundAmount) {
        this.cancelRefundAmount = cancelRefundAmount;
    }

    public double getDisputeRefundAmount() {
        return disputeRefundAmount;
    }

    public void setDisputeRefundAmount(double disputeRefundAmount) {
        this.disputeRefundAmount = disputeRefundAmount;
    }

    public double getDueBalance() {
        return dueBalance;
    }

    public void setDueBalance(double dueBalance) {
        this.dueBalance = dueBalance;
    }

    public double getDueBalance2() {
        return dueBalance2;
    }

    public void setDueBalance2(double dueBalance2) {
        this.dueBalance2 = dueBalance2;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public double getBlockedBalance() {
        return blockedBalance;
    }

    public void setBlockedBalance(double blockedBalance) {
        this.blockedBalance = blockedBalance;
    }

    public int getWarnFlag() {
        return warnFlag;
    }

    public void setWarnFlag(int warnFlag) {
        this.warnFlag = warnFlag;
    }

    public String getWarnMsg() {
        return warnMsg;
    }

    public void setWarnMsg(String warnMsg) {
        this.warnMsg = warnMsg;
    }


    @Override
    public String toString() {
        return "CustomerFinancialBean{" +
                "userId=" + userId +
                ", totalOrderAmount=" + totalOrderAmount +
                ", actualOrderAmount=" + actualOrderAmount +
                ", cancelOrderAmount=" + cancelOrderAmount +
                ", actualPayAmount=" + actualPayAmount +
                ", payPalPay=" + payPalPay +
                ", wireTransferPay=" + wireTransferPay +
                ", stripePay=" + stripePay +
                ", payForBalance=" + payForBalance +
                ", balancePay=" + balancePay +
                ", hasRefundAmount=" + hasRefundAmount +
                ", apiRefundAmount=" + apiRefundAmount +
                ", manualRefundAmount=" + manualRefundAmount +
                ", compensateAmount=" + compensateAmount +
                ", dealRefundAmount=" + dealRefundAmount +
                ", cancelRefundAmount=" + cancelRefundAmount +
                ", disputeRefundAmount=" + disputeRefundAmount +
                ", dueBalance=" + dueBalance +
                ", dueBalance2=" + dueBalance2 +
                ", currentBalance=" + currentBalance +
                ", availableBalance=" + availableBalance +
                ", blockedBalance=" + blockedBalance +
                ", warnFlag=" + warnFlag +
                ", warnMsg='" + warnMsg + '\'' +
                '}';
    }
}
