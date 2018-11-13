package com.cbt.pojo;

import java.io.Serializable;

/**
 * 支付失败订单实体类
 * @ClassName PayFailureOrderPojo 
 * @Description TODO
 * @author 王宏杰
 * @date 2018年4月8日 上午10:34:00
 */
public class PayFailureOrderPojo implements Serializable{

	/**
	 * @fieldName serialVersionUID
	 * @fieldType long
	 * @Description TODO
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String orderid;
	private String paySID;
	private String per_payprice;
	private String description;
	private String useBalance;
	private String createtime;
	private String payment_method_nonce;
	private String message;
	private String payerId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getPaySID() {
		return paySID;
	}
	public void setPaySID(String paySID) {
		this.paySID = paySID;
	}
	public String getPer_payprice() {
		return per_payprice;
	}
	public void setPer_payprice(String per_payprice) {
		this.per_payprice = per_payprice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUseBalance() {
		return useBalance;
	}
	public void setUseBalance(String useBalance) {
		this.useBalance = useBalance;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getPayment_method_nonce() {
		return payment_method_nonce;
	}
	public void setPayment_method_nonce(String payment_method_nonce) {
		this.payment_method_nonce = payment_method_nonce;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPayerId() {
		return payerId;
	}
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

}
