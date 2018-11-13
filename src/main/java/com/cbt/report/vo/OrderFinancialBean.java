package com.cbt.report.vo;

/**
 * 订单财务统计
 * 
 * @author JXW
 *
 */
public class OrderFinancialBean {

	private int id;
	private String year;// 年份
	private String month;// 月份

	/**
	 * 验证R1=R2
	 */
	private float orderSales;// 本月实际销售额(R1)

	/**
	 * R2 = V + Q - (U - 余额补偿 - 客户充值 - 客户多付)
	 */
	private float financialSales;// 财务销售额(R2)
	private float payPalRevenue;// 系统PayPal总收入(V)
	private float balancePayment;// 余额支付(Q)
	private float stripePay;//stripe支付

	/**
	 * U = 客户充值 + 客户多付 + 余额补偿 + 订单取消
	 */
	private float newAddBalance;// 本月新增加的余额之和(U)
	private float balanceCompensation;// 余额补偿
	private float payForBalance;// 客户充值
	private float overPaymentBalance;// 客户多付
	private float orderCancel;// 订单取消(全部或部分)

	/**
	 * 余额变更 期末-期初 = U - Q - Z
	 */
	private float balanceChanges;// 余额变更 期末-期初
	private float balanceWithdrawal;// 余额提现(Z)

	public float getTt_revenue() {
		return tt_revenue;
	}

	public void setTt_revenue(float tt_revenue) {
		this.tt_revenue = tt_revenue;
	}

	private float tt_revenue;//TT付款
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public float getOrderSales() {
		return orderSales;
	}

	public void setOrderSales(float orderSales) {
		this.orderSales = orderSales;
	}

	public float getFinancialSales() {
		return financialSales;
	}

	public void setFinancialSales(float financialSales) {
		this.financialSales = financialSales;
	}

	public float getPayPalRevenue() {
		return payPalRevenue;
	}

	public void setPayPalRevenue(float payPalRevenue) {
		this.payPalRevenue = payPalRevenue;
	}

	public float getBalancePayment() {
		return balancePayment;
	}

	public void setBalancePayment(float balancePayment) {
		this.balancePayment = balancePayment;
	}

	public float getStripePay() {
		return stripePay;
	}

	public void setStripePay(float stripePay) {
		this.stripePay = stripePay;
	}

	public float getNewAddBalance() {
		return newAddBalance;
	}

	public void setNewAddBalance(float newAddBalance) {
		this.newAddBalance = newAddBalance;
	}

	public float getBalanceCompensation() {
		return balanceCompensation;
	}

	public void setBalanceCompensation(float balanceCompensation) {
		this.balanceCompensation = balanceCompensation;
	}

	public float getPayForBalance() {
		return payForBalance;
	}

	public void setPayForBalance(float payForBalance) {
		this.payForBalance = payForBalance;
	}

	public float getOverPaymentBalance() {
		return overPaymentBalance;
	}

	public void setOverPaymentBalance(float overPaymentBalance) {
		this.overPaymentBalance = overPaymentBalance;
	}

	public float getOrderCancel() {
		return orderCancel;
	}

	public void setOrderCancel(float orderCancel) {
		this.orderCancel = orderCancel;
	}

	public float getBalanceChanges() {
		return balanceChanges;
	}

	public void setBalanceChanges(float balanceChanges) {
		this.balanceChanges = balanceChanges;
	}

	public float getBalanceWithdrawal() {
		return balanceWithdrawal;
	}

	public void setBalanceWithdrawal(float balanceWithdrawal) {
		this.balanceWithdrawal = balanceWithdrawal;
	}

	@Override
	public String toString() {
		return "OrderFinancialBean [id=" + id + ", year=" + year + ", month=" + month + ", orderSales=" + orderSales
				+ ", financialSales=" + financialSales + ", payPalRevenue=" + payPalRevenue + ", balancePayment="
				+ balancePayment + ", newAddBalance=" + newAddBalance + ", balanceCompensation=" + balanceCompensation
				+ ", payForBalance=" + payForBalance + ", overPaymentBalance=" + overPaymentBalance + ", orderCancel="
				+ orderCancel + ", balanceChanges=" + balanceChanges + ", balanceWithdrawal=" + balanceWithdrawal + "]";
	}

}
