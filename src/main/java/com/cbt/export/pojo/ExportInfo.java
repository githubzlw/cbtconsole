package com.cbt.export.pojo;

import lombok.Data;

@Data
public class ExportInfo {
	private int id;
	/**
	 * 电商企业代码
	 */
	private String companyCode;
	/**
	 * 物流企业代码
	 */
	private String logisticscode;
	/**
	 * 电商订单号
	 */
	private String number;
	/**
	 * 总运单号
	 */
	private String shipmentNumber;
	/**
	 *  分运单号 
	 */
	private String subShipmentNumber;
	/**
	 * 订单总重量
	 */
	private String weight;
	/**
	 * 订单总净重
	 */
	private String netWeight;
	/**
	 * 币种
	 */
	private String currency;
	/**
	 * 到帐时间
	 */
	private String receivingTime;
	
	/**
	 * 支付企业代码
	 */
	private String enterpriseCode;
	
	/**
	 * 支付企业名称
	 */
	private String enterpriseName;
	
	/**
	 * 支付总金额
	 */
	private String totalAmount;
	
	/**
	 * 支付交易流水号
	 */
	private String transactionNumber;
	/**
	 * 名称
	*/
	private String postName;
	/**
	 * 地址
	 */
	private String postAddress;
	/**
	 *国家 
	 */
	private String postContry;
	/**
	 * 电话
	 */
	private String postNumber;
	/**
	 * 名称
	*/
	private String receivingName;
	/**
	 * 地址
	 */
	private String receivingAddress;
	/**
	 *国家 
	 */
	private String receivingContry;
	/**
	 * 电话
	 */
	private String receivingNumber;
	
	/**
	 * 海关备案编号
	 */
	private String customsRecordNumber;
	
	
	/**
	 * HS 编码
	 */
	private String hsNumber;
	
	
	
	/**
	 * 保费
	 */
	private String premium;
	
	/**
	 * 集包号
	 */
	private String packageCode;
	/**
	 * 小包件数
	 */
	private String  packageNumber;
	
	
	/**
	 * 商品总价值
	 */
	private String cost;
	/**
	 * 运杂费
	 */
	private String otherCost;
	/**
	 * 电商商品编号 
	 */
	private String pid;
	/**
	 * 商品单价
	 */
	private String goodsPrice;
	/**
	 * 商品总价 
	 */
	private String goodsCost;
	/**
	 * 商品数量 
	 */
	private String quality;
	/**
	 * 产品类别
	 */
	private String catid;
	/**
	 *商品名称 
	 */
	private String goodsName;
	/**
	 * 单件重量
	 */
	private String goodsWeight;
	
}
