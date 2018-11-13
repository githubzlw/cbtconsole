package com.cbt.refund.bean;

/**
 * PayPal导入数据bean
 * @author JXW
 *
 */

import java.sql.Timestamp;

public class PayPalImportInfo implements java.io.Serializable {
	/** 版本号 */
	private static final long serialVersionUID = -8926122491476786663L;

	/** 发生日期 */
	private String occurrenceDate;

	/** 发生时间 */
	private String occurrenceTime;

	/** 时区 */
	private String timeZone;

	/** 创建时间 */
	private Timestamp createTime;

	/** 创建人id */
	private Integer createAdminid;

	/** 客户名称 */
	private String customerName;

	/** PayPal类型 */
	private String type;

	/** PayPal状态 */
	private String state;

	/** 币种 */
	private String monetaryUnit;

	/** 总额 */
	private Float totalAmount;

	/** 手续费 */
	private Float serviceCharge;

	/** 净额 */
	private Float netAmount;

	/** 发件人邮箱地址 */
	private String senderEmail;

	/** 收件人邮箱 */
	private String recipientEmail;

	/** 交易号 */
	private String transactionNumber;

	/** 送货地址 */
	private String deliveryAddress;

	/** 地址状态 */
	private String addressState;

	/** 物品名称 */
	private String itemName;

	/** 订单号(物品号) */
	private String orderNo;

	/** 运费和手续费金额 */
	private Float shippingHandling;

	/** 保险金额 */
	private Float insuredAmount;

	/** 营业税 */
	private Float businessTax;

	/** 选项 1 名称 */
	private String option1Name;

	/** 选项 1 值 */
	private String option1Value;

	/** 选项 2 名称 */
	private String option2Name;

	/** 选项 2 值 */
	private String option2Value;

	/** 参考交易号 */
	private String referenceTradeNumber;

	/** 账单号 */
	private String billNumber;

	/** 自定义号码 */
	private String customNumber;

	/** 数量 */
	private Integer quantity;

	/** 收据号 */
	private String receiptNumber;

	/** 余额 */
	private Float balance;

	/** 地址第1行 */
	private String addressLine1;

	/** 地址第2行 */
	private String addressLine2;

	/** 城镇/城市 */
	private String townCity;

	/** 省/市/自治区/直辖市/特别行政区 */
	private String province;

	/** 邮政编码 */
	private String postalCode;

	/** 国家/地区 */
	private String country;

	/** 联系电话号码 */
	private String contactPhoneNo;

	/** 主题 */
	private String theme;

	/** 备注 */
	private String remark;

	/** 国家/地区代码 */
	private String countryCode;

	/** 余额影响 */
	private String influenceBalance;

	/**
	 * 获取发生日期
	 * 
	 * @return 发生日期
	 */
	public String getOccurrenceDate() {
		return this.occurrenceDate;
	}

	/**
	 * 设置发生日期
	 * 
	 * @param occurrenceDate
	 *            发生日期
	 */
	public void setOccurrenceDate(String occurrenceDate) {
		this.occurrenceDate = occurrenceDate;
	}

	/**
	 * 获取发生时间
	 * 
	 * @return 发生时间
	 */
	public String getOccurrenceTime() {
		return this.occurrenceTime;
	}

	/**
	 * 设置发生时间
	 * 
	 * @param occurrenceTime
	 *            发生时间
	 */
	public void setOccurrenceTime(String occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}

	/**
	 * 获取时区
	 * 
	 * @return 时区
	 */
	public String getTimeZone() {
		return this.timeZone;
	}

	/**
	 * 设置时区
	 * 
	 * @param timeZone
	 *            时区
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return 创建时间
	 */
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	/**
	 * 设置创建时间
	 * 
	 * @param createTime
	 *            创建时间
	 */
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取创建人id
	 * 
	 * @return 创建人id
	 */
	public Integer getCreateAdminid() {
		return this.createAdminid;
	}

	/**
	 * 设置创建人id
	 * 
	 * @param createAdminid
	 *            创建人id
	 */
	public void setCreateAdminid(Integer createAdminid) {
		this.createAdminid = createAdminid;
	}

	/**
	 * 获取客户名称
	 * 
	 * @return 客户名称
	 */
	public String getCustomerName() {
		return this.customerName;
	}

	/**
	 * 设置客户名称
	 * 
	 * @param customerName
	 *            客户名称
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * 获取PayPal类型
	 * 
	 * @return PayPal类型
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * 设置PayPal类型
	 * 
	 * @param type
	 *            PayPal类型
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取PayPal状态
	 * 
	 * @return PayPal状态
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * 设置PayPal状态
	 * 
	 * @param state
	 *            PayPal状态
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 获取币种
	 * 
	 * @return 币种
	 */
	public String getMonetaryUnit() {
		return this.monetaryUnit;
	}

	/**
	 * 设置币种
	 * 
	 * @param currency
	 *            币种
	 */
	public void setMonetaryUnit(String monetaryUnit) {
		this.monetaryUnit = monetaryUnit;
	}

	/**
	 * 获取总额
	 * 
	 * @return 总额
	 */
	public Float getTotalAmount() {
		return this.totalAmount;
	}

	/**
	 * 设置总额
	 * 
	 * @param totalAmount
	 *            总额
	 */
	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * 获取手续费
	 * 
	 * @return 手续费
	 */
	public Float getServiceCharge() {
		return this.serviceCharge;
	}

	/**
	 * 设置手续费
	 * 
	 * @param serviceCharge
	 *            手续费
	 */
	public void setServiceCharge(Float serviceCharge) {
		this.serviceCharge = serviceCharge;
	}

	/**
	 * 获取净额
	 * 
	 * @return 净额
	 */
	public Float getNetAmount() {
		return this.netAmount;
	}

	/**
	 * 设置净额
	 * 
	 * @param netAmount
	 *            净额
	 */
	public void setNetAmount(Float netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * 获取发件人邮箱地址
	 * 
	 * @return 发件人邮箱地址
	 */
	public String getSenderEmail() {
		return this.senderEmail;
	}

	/**
	 * 设置发件人邮箱地址
	 * 
	 * @param senderEmail
	 *            发件人邮箱地址
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	/**
	 * 获取收件人邮箱
	 * 
	 * @return 收件人邮箱
	 */
	public String getRecipientEmail() {
		return this.recipientEmail;
	}

	/**
	 * 设置收件人邮箱
	 * 
	 * @param recipientEmail
	 *            收件人邮箱
	 */
	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	/**
	 * 获取交易号
	 * 
	 * @return 交易号
	 */
	public String getTransactionNumber() {
		return this.transactionNumber;
	}

	/**
	 * 设置交易号
	 * 
	 * @param transactionNumber
	 *            交易号
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * 获取送货地址
	 * 
	 * @return 送货地址
	 */
	public String getDeliveryAddress() {
		return this.deliveryAddress;
	}

	/**
	 * 设置送货地址
	 * 
	 * @param deliveryAddress
	 *            送货地址
	 */
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	/**
	 * 获取地址状态
	 * 
	 * @return 地址状态
	 */
	public String getAddressState() {
		return this.addressState;
	}

	/**
	 * 设置地址状态
	 * 
	 * @param addressState
	 *            地址状态
	 */
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	/**
	 * 获取物品名称
	 * 
	 * @return 物品名称
	 */
	public String getItemName() {
		return this.itemName;
	}

	/**
	 * 设置物品名称
	 * 
	 * @param itemName
	 *            物品名称
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * 获取订单号(物品号)
	 * 
	 * @return 订单号(物品号)
	 */
	public String getOrderNo() {
		return this.orderNo;
	}

	/**
	 * 设置订单号(物品号)
	 * 
	 * @param orderNo
	 *            订单号(物品号)
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 获取运费和手续费金额
	 * 
	 * @return 运费和手续费金额
	 */
	public Float getShippingHandling() {
		return this.shippingHandling;
	}

	/**
	 * 设置运费和手续费金额
	 * 
	 * @param shippingHandling
	 *            运费和手续费金额
	 */
	public void setShippingHandling(Float shippingHandling) {
		this.shippingHandling = shippingHandling;
	}

	/**
	 * 获取保险金额
	 * 
	 * @return 保险金额
	 */
	public Float getInsuredAmount() {
		return this.insuredAmount;
	}

	/**
	 * 设置保险金额
	 * 
	 * @param insuredAmount
	 *            保险金额
	 */
	public void setInsuredAmount(Float insuredAmount) {
		this.insuredAmount = insuredAmount;
	}

	/**
	 * 获取营业税
	 * 
	 * @return 营业税
	 */
	public Float getBusinessTax() {
		return this.businessTax;
	}

	/**
	 * 设置营业税
	 * 
	 * @param businessTax
	 *            营业税
	 */
	public void setBusinessTax(Float businessTax) {
		this.businessTax = businessTax;
	}

	/**
	 * 获取选项 1 名称
	 * 
	 * @return 选项 1 名称
	 */
	public String getOption1Name() {
		return this.option1Name;
	}

	/**
	 * 设置选项 1 名称
	 * 
	 * @param option1Name
	 *            选项 1 名称
	 */
	public void setOption1Name(String option1Name) {
		this.option1Name = option1Name;
	}

	/**
	 * 获取选项 1 值
	 * 
	 * @return 选项 1 值
	 */
	public String getOption1Value() {
		return this.option1Value;
	}

	/**
	 * 设置选项 1 值
	 * 
	 * @param option1Value
	 *            选项 1 值
	 */
	public void setOption1Value(String option1Value) {
		this.option1Value = option1Value;
	}

	/**
	 * 获取选项 2 名称
	 * 
	 * @return 选项 2 名称
	 */
	public String getOption2Name() {
		return this.option2Name;
	}

	/**
	 * 设置选项 2 名称
	 * 
	 * @param option2Name
	 *            选项 2 名称
	 */
	public void setOption2Name(String option2Name) {
		this.option2Name = option2Name;
	}

	/**
	 * 获取选项 2 值
	 * 
	 * @return 选项 2 值
	 */
	public String getOption2Value() {
		return this.option2Value;
	}

	/**
	 * 设置选项 2 值
	 * 
	 * @param option2Value
	 *            选项 2 值
	 */
	public void setOption2Value(String option2Value) {
		this.option2Value = option2Value;
	}

	/**
	 * 获取参考交易号
	 * 
	 * @return 参考交易号
	 */
	public String getReferenceTradeNumber() {
		return this.referenceTradeNumber;
	}

	/**
	 * 设置参考交易号
	 * 
	 * @param referenceTradeNumber
	 *            参考交易号
	 */
	public void setReferenceTradeNumber(String referenceTradeNumber) {
		this.referenceTradeNumber = referenceTradeNumber;
	}

	/**
	 * 获取账单号
	 * 
	 * @return 账单号
	 */
	public String getBillNumber() {
		return this.billNumber;
	}

	/**
	 * 设置账单号
	 * 
	 * @param billNumber
	 *            账单号
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	/**
	 * 获取自定义号码
	 * 
	 * @return 自定义号码
	 */
	public String getCustomNumber() {
		return this.customNumber;
	}

	/**
	 * 设置自定义号码
	 * 
	 * @param customNumber
	 *            自定义号码
	 */
	public void setCustomNumber(String customNumber) {
		this.customNumber = customNumber;
	}

	/**
	 * 获取数量
	 * 
	 * @return 数量
	 */
	public Integer getQuantity() {
		return this.quantity;
	}

	/**
	 * 设置数量
	 * 
	 * @param quantity
	 *            数量
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * 获取收据号
	 * 
	 * @return 收据号
	 */
	public String getReceiptNumber() {
		return this.receiptNumber;
	}

	/**
	 * 设置收据号
	 * 
	 * @param receiptNumber
	 *            收据号
	 */
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	/**
	 * 获取余额
	 * 
	 * @return 余额
	 */
	public Float getBalance() {
		return this.balance;
	}

	/**
	 * 设置余额
	 * 
	 * @param balance
	 *            余额
	 */
	public void setBalance(Float balance) {
		this.balance = balance;
	}

	/**
	 * 获取地址第1行
	 * 
	 * @return 地址第1行
	 */
	public String getAddressLine1() {
		return this.addressLine1;
	}

	/**
	 * 设置地址第1行
	 * 
	 * @param addressLine1
	 *            地址第1行
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * 获取地址第2行
	 * 
	 * @return 地址第2行
	 */
	public String getAddressLine2() {
		return this.addressLine2;
	}

	/**
	 * 设置地址第2行
	 * 
	 * @param addressLine2
	 *            地址第2行
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * 获取城镇/城市
	 * 
	 * @return 城镇/城市
	 */
	public String getTownCity() {
		return this.townCity;
	}

	/**
	 * 设置城镇/城市
	 * 
	 * @param townCity
	 *            城镇/城市
	 */
	public void setTownCity(String townCity) {
		this.townCity = townCity;
	}

	/**
	 * 获取省/市/自治区/直辖市/特别行政区
	 * 
	 * @return 省/市/自治区/直辖市/特别行政区
	 */
	public String getProvince() {
		return this.province;
	}

	/**
	 * 设置省/市/自治区/直辖市/特别行政区
	 * 
	 * @param province
	 *            省/市/自治区/直辖市/特别行政区
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * 获取邮政编码
	 * 
	 * @return 邮政编码
	 */
	public String getPostalCode() {
		return this.postalCode;
	}

	/**
	 * 设置邮政编码
	 * 
	 * @param postalCode
	 *            邮政编码
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * 获取国家/地区
	 * 
	 * @return 国家/地区
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 * 设置国家/地区
	 * 
	 * @param country
	 *            国家/地区
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * 获取联系电话号码
	 * 
	 * @return 联系电话号码
	 */
	public String getContactPhoneNo() {
		return this.contactPhoneNo;
	}

	/**
	 * 设置联系电话号码
	 * 
	 * @param contactPhoneNo
	 *            联系电话号码
	 */
	public void setContactPhoneNo(String contactPhoneNo) {
		this.contactPhoneNo = contactPhoneNo;
	}

	/**
	 * 获取主题
	 * 
	 * @return 主题
	 */
	public String getTheme() {
		return this.theme;
	}

	/**
	 * 设置主题
	 * 
	 * @param theme
	 *            主题
	 */
	public void setTheme(String theme) {
		this.theme = theme;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemark() {
		return this.remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark
	 *            备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取国家/地区代码
	 * 
	 * @return 国家/地区代码
	 */
	public String getCountryCode() {
		return this.countryCode;
	}

	/**
	 * 设置国家/地区代码
	 * 
	 * @param countryCode
	 *            国家/地区代码
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * 获取余额影响
	 * 
	 * @return 余额影响
	 */
	public String getInfluenceBalance() {
		return this.influenceBalance;
	}

	/**
	 * 设置余额影响
	 * 
	 * @param influenceBalance
	 *            余额影响
	 */
	public void setInfluenceBalance(String influenceBalance) {
		this.influenceBalance = influenceBalance;
	}
}
