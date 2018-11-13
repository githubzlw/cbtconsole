package com.cbt.pojo;

/**
 * 付款审批的相关财务实体类
 * @author 王宏杰 2017-08-23
 *
 */
public class ApprovalPaymentPojo {
    private String times;//日期
    
    private String totalSum;//非取消订单总进账(TT+PayPal)
    
    private String titalRefund;//总退款
    
    private String balancePayment;//余额支付
    
    private String balancePrepaid;//余额充值
    
    private String salesAmount;//进账对应销售额
    
    private String cancelBeforeOrderAmount;//取消的非当日订单对应的销售额
    public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}

	public String getTitalRefund() {
		return titalRefund;
	}

	public void setTitalRefund(String titalRefund) {
		this.titalRefund = titalRefund;
	}

	public String getBalancePayment() {
		return balancePayment;
	}

	public void setBalancePayment(String balancePayment) {
		this.balancePayment = balancePayment;
	}

	public String getBalancePrepaid() {
		return balancePrepaid;
	}

	public void setBalancePrepaid(String balancePrepaid) {
		this.balancePrepaid = balancePrepaid;
	}

	public String getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(String salesAmount) {
		this.salesAmount = salesAmount;
	}

	public String getCancelBeforeOrderAmount() {
		return cancelBeforeOrderAmount;
	}

	public void setCancelBeforeOrderAmount(String cancelBeforeOrderAmount) {
		this.cancelBeforeOrderAmount = cancelBeforeOrderAmount;
	}

}
